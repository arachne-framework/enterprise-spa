(ns ^:config org.arachne-framework.template.enterprise-spa
  (:require
   [arachne.core.dsl :as a]
   [arachne.http.dsl :as h]
   [arachne.pedestal.dsl :as p]
   [arachne.assets.dsl :as aa]
   [arachne.pedestal-assets.dsl :as pa]
   [arachne.cljs.dsl :as cljs]
   [arachne.figwheel.dsl :as fig]))

;; Always in dev mode, for now
(def dev? (constantly true))

(a/id ::runtime (a/runtime [::server]))

;; HTTP Server setup
(a/id ::server
  (p/server 8080

    (a/id ::asset-interceptor (pa/interceptor :index? true))

    (h/endpoint :get "/healthcheck"
      (h/handler 'org.arachne-framework.template.enterprise-spa.web/healthcheck))

    (h/endpoint :get "/app"
      (h/handler 'org.arachne-framework.template.enterprise-spa.web/app))
    (h/endpoint :get "/app/*app"
      (h/handler 'org.arachne-framework.template.enterprise-spa.web/app)
      :name ::app-catch-all)

    ))

;; Asset Pipeline setup
(a/id ::public-dir (aa/input-dir "public" :classpath? true :watch? (dev?)))

(aa/pipeline [::public-dir ::asset-interceptor])

(def cljs-opts {:main 'org.arachne-framework.template.enterprise-spa.client
                :optimizations (if (dev?) :none :advanced)
                :asset-path "/js/out"
                :output-to "js/app.js"
                :output-dir "js/out"
                :source-map-timestamp true})

(a/id ::src-dir (aa/input-dir "src" :watch? (dev?)))

;; For prod mode, use a standard CLJS build pipeline
(a/id ::cljs (cljs/build cljs-opts))
(aa/pipeline [::src-dir ::cljs])

;; Figwheel ClojureScript setup (dynamic CLJS development)
(a/id ::figwheel (fig/server cljs-opts :port 8888))

(aa/pipeline
  [::src-dir ::figwheel #{:src}]
  [::public-dir ::figwheel #{:public}])

;; Always use Figwheel for builds in dev
(aa/pipeline [(if (dev?) ::figwheel ::cljs)
              ::asset-interceptor])


