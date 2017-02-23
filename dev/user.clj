(ns user
  (:require [reloaded.repl :refer [set-init! system init start stop go reset reset-all]]
            [arachne.core :as a]
            [arachne.figwheel :as fig]
            [org.arachne-framework.template.enterprise-spa :as app]))

(defn init-arachne
  "Create an Arachne runtime"
  [runtime]
  (a/runtime (a/config :org.arachne-framework.template/enterprise-spa)
             runtime))

(set-init! #(init-arachne ::app/runtime))

(defn cljs-repl
  "Launch a CLJS repl for a Figwheel in the currently running Arachne system."
  []
  (fig/repl system))

(defn config
  "Returns the config for the currently running Arachne system."
  []
  (:config system))
