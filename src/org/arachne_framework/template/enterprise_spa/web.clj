(ns org.arachne-framework.template.enterprise-spa.web
  (:require [rum.core :as rum]))

(defn healthcheck
  "Handler for healthcheck service"
  [req]
  {:status 200
   :body "OK"})

(defn- app-html
  "Generate the HTML markup for the application main page using server-side Rum"
  []
  (rum/render-static-markup
    [:html {:lang "en"}
     [:head
      [:title "My Application"]
      [:link {:rel "stylesheet" :type "text/css"
              :href "/bootstrap/3.3.7-1/css/bootstrap.min.css"}]
      [:link {:rel "stylesheet" :type "text/css"
              :href "/css/app.css"}]]
     [:body
      [:div#app]
      [:script {:type "text/javascript"
                :src "/js/app.js"}]
      [:script {:type "text/javascript"}
       "org.arachne_framework.template.enterprise_spa.client.main()"]]]))

(defn app
  "Handler for serving the application main page"
  [req]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (app-html)})