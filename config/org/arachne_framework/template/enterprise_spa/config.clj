(ns ^:config org.arachne-framework.template.enterprise-spa.config
  (:require
   [arachne.core.dsl :as a]
   [arachne.http.dsl :as h]
   [arachne.pedestal.dsl :as p]
   [arachne.assets.dsl :as aa]
   [arachne.pedestal-assets.dsl :as pa]
   [arachne.cljs.dsl :as cljs]
   [arachne.figwheel.dsl :as fig]))

;; Make things shorter to type...
(alias 'app 'org.arachne-framework.template.enterprise-spa)

;; Always in dev mode, for now
(def dev? (constantly true))

(a/enable-debug!)
(a/disable-debug!)

;; Runtime setup
(a/id ::app/runtime (a/runtime [::app/server]))

;; HTTP Server setup
(a/id ::app/server
  (p/server 8080

    (a/id ::app/asset-interceptor (pa/interceptor :index? true))

    (h/endpoint :get "/healthcheck"
      (h/handler 'org.arachne-framework.template.enterprise-spa.web/healthcheck))

    ))

;; Asset Pipeline setup
(a/id ::app/public-dir (aa/input-dir "public" :classpath? true :watch? (dev?)))

(aa/pipeline [::app/public-dir ::app/asset-interceptor])

(def cljs-opts {:main 'org.arachne-framework.template.enterprise-spa
                :optimizations (if (dev?) :none :advanced)
                :asset-path "js/out"
                :output-to "js/app.js"
                :output-dir "js/out"
                :source-map-timestamp true})

(a/id ::app/src-dir (aa/input-dir "src" :watch? (dev?)))

;; For prod mode, use a standard CLJS build pipeline
(a/id ::app/cljs (cljs/build cljs-opts))
(aa/pipeline [::app/src-dir ::app/cljs])

;; Figwheel ClojureScript setup (dynamic CLJS development)
(a/id ::app/figwheel (fig/server cljs-opts :port 8888))

(aa/pipeline
  [::app/src-dir ::app/figwheel #{:src}]
  [::app/public-dir ::app/figwheel #{:public}])

;; Always use Figwheel for builds in dev
(aa/pipeline [(if (dev?) ::app/figwheel ::app/cljs)
              ::app/asset-interceptor])


