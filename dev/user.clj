 (ns user
   (:require [reloaded.repl :refer [set-init! system init start stop go reset reset-all]]
             [arachne.core :as a]
             [arachne.figwheel :as fig]
             [org.arachne-framework.template.enterprise-spa :as app])
   )

(def ^{:dynamic true
       :doc "Bound to the config for the current application"}
  *cfg*
  nil)

(defn init-arachne
  "Create an Arachne runtime"
  [runtime]
  (let [cfg (a/config :org.arachne-framework.template/enterprise-spa)
        rt (a/runtime cfg runtime)]
    (alter-var-root #'*cfg* (constantly cfg))
    rt))

(set-init! #(init-arachne ::app/runtime))

(defn cljs-repl
  "Launch a CLJS repl for a Figwheel in the currently running Arachne system"
  []
  (fig/repl system))
