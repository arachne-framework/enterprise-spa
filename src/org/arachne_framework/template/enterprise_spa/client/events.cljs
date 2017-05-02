(ns org.arachne-framework.template.enterprise-spa.client.events
  "Tools for dealing with browser state and events."
  (:require [cljs.core.async :as a])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(defmulti handle
  "Handle an event, dispatching on the event type (:app.event/type). Takes the
   current app state as an argument, and returns a (possibly updated) app
   state."
  (fn [app-state send evt] (:app.event/type evt))
  :default ::default)

(defmethod handle ::default [app-state send evt]
  (.log js/console "unknown event type" (str (:app.event/type evt)))
  app-state)

(defn send-fn
  "Given an app state atom, return a dispatch function. The function may be
   called with a message to dispatch the message to the application-wide
   message processing system."
  [app-atom]
  (let [chan (a/chan)
        send (fn [msg]
               (go (a/>! chan msg)))]
    (go (loop []
          (when-let [msg (a/<! chan)]
            (swap! app-atom handle send msg)
            (recur))))
    send))
