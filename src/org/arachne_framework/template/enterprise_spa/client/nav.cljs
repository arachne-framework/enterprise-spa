(ns org.arachne-framework.template.enterprise-spa.client.nav
  "Tools for dealing with browser navigation and history"
  (:require [clojure.string :as str]
            [org.arachne-framework.template.enterprise-spa.client.events :as e]))

(def path-prefix "/app")
(def default-page "/page-1")

(defn path
  "Generate a clean URL path for the given page"
  [page]
  (let [p (str/replace page #"/$" "")]
    (if (str/blank? p)
      path-prefix
      (str path-prefix p))))

(defmulti page
  "Given the application state, render the current page based on the state's
   `:app/page` key"
  (fn [app-state send version]
    (:app/page app-state)))

(defmethod page :default
  [app-state send version]
  [:div.alert.alert-danger {:role "alert"}
   "Page " [:span.missing-page (:app/page app-state)] " not found. "
   [:a.alert-link {:href (path "/")} "Click here"]
   " to return to the Portal home page."])

(defn handler
  "Return a handler function that will navigate to the requested page."
  [send page]
  (fn [evt]
    (send {:app.event/type ::nav
           :app/page page})
    (.preventDefault evt)
    false))

(defmethod e/handle ::nav
  [app-state send evt]
  (let [page (:app/page evt)]
    (.pushState js/history "" (.-title js/document) (path page))
    (assoc app-state :app/page page)))

(defn sync-locations
  "Synchronize the application's :app/page to match the actual browser URL.
   Also updates the :app/page if the page URL changes (e.g, the user used the
   back or forward arrows)"
  [app-atom]
  (let [current-page #(let [path (.-pathname (.-location js/window))
                            path (str/replace path path-prefix "")]
                        (if (str/blank? path)
                          default-page
                          path))
        sync #(swap! app-atom assoc :app/page (current-page))]
    (sync)
    (.addEventListener js/window "popstate" sync)))