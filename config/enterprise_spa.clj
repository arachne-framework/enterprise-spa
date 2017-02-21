(require '[arachne.core.dsl :as a])
(require '[arachne.http.dsl :as h])
(require '[arachne.pedestal.dsl :as p])
(require '[arachne.assets.dsl :as aa])
(require '[arachne.pedestal-assets.dsl :as pa])
(require '[arachne.cljs.dsl :as cljs])
(require '[arachne.figwheel.dsl :as fig])

;; Make things shorter to type...
(alias 'app 'org.arachne-framework.template.enterprise-spa)

;; Always in dev mode, for now
(def dev? (constantly true))

;; Runtime setup
(if (dev?)
  (a/runtime ::app/runtime [::app/figwheel ::app/server])
  (a/runtime ::app/runtime [::app/server]))

;; HTTP Server setup
(p/server ::app/server 8080

  (pa/interceptor ::app/asset-interceptor :index? true)

  (h/endpoint :get "/healthcheck"
    (h/handler 'org.arachne-framework.template.enterprise-spa.web/healthcheck))

  )

;; Asset Pipeline setup
(aa/input-dir ::app/public-dir "public" :classpath? true :watch? (dev?))

(aa/pipeline [::app/public-dir ::app/asset-interceptor])

(def cljs-opts {:main 'org.arachne-framework.template.enterprise-spa
                :optimizations (if (dev?) :none :advanced)
                :asset-path "js/out"
                :output-to "js/app.js"
                :output-dir "js/out"
                :source-map-timestamp true})

(aa/input-dir ::app/src-dir "src" :watch? (dev?))

;; For prod mode, use a standard CLJS build pipeline
(cljs/build ::app/cljs cljs-opts)
(aa/pipeline [::app/src-dir ::app/cljs])

;; Figwheel ClojureScript setup (dynamic CLJS development)
(fig/server ::app/figwheel cljs-opts :port 8888)

(aa/pipeline
  [::app/src-dir ::app/figwheel #{:src}]
  [::app/public-dir ::app/figwheel #{:public}])

;; Always use Figwheel for builds in dev
(if (dev?)
  (aa/pipeline [::app/figwheel ::app/asset-interceptor])
  (aa/pipeline [::app/cljs ::app/asset-interceptor]))


