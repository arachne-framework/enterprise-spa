(ns org.arachne-framework.application-template-1.web)

(defn healthcheck
  [req]
  {:status 200
   :body "OK"})
