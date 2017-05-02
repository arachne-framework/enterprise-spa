(ns org.arachne-framework.template.enterprise-spa.client
  (:require [rum.core :as rum]
            [org.arachne-framework.template.enterprise-spa.client.events :as e]
            [org.arachne-framework.template.enterprise-spa.client.nav :as nav]))

(enable-console-print!)

(defn- nav-link
  [app-state send label page]
  (let [current? (= (:app/page app-state) page)]
    [:li {:class (when current? "active")}
     [:a {:href (nav/path page)
          :on-click (when-not current? (nav/handler send page))}
      label]]))

(rum/defc NavBar < rum/static
  [app-state send version]
  [:nav.navbar.navbar-default.navbar-fixed-top
   [:div.navbar-header
    [:img {:src "/img/arachne.svg"
           :height "50"
           :width "80"}]]
   [:div.container-fluid
    [:ul.nav.navbar-nav
     (nav-link app-state send "Page 1" "/page-1")
     (nav-link app-state send "Page 2" "/page-2")
     (nav-link app-state send "Page 3" "/page-3")]]])

(defmethod nav/page "/page-1"
  [app-state send version]
  [:div.container [:h1 "Page One"]])

(defmethod nav/page "/page-2"
  [app-state send version]
  [:div.container [:h1 "Page Two"]])

(defmethod nav/page "/page-3"
  [app-state send version]
  [:div.container [:h1 "Page Three"]])

(rum/defc Layout < rum/static
  "The top-level application layout"
  [app-state send version]
  [:div
   (NavBar app-state send version)
   (nav/page app-state send version)])

;; The version is incremented whenever Figwheel reloads the page, and is
;; passed as an argument to every component to ensure that components re-render
;; when figwheel loads new code.
(defonce version (atom 0))

(defn ^:export main
  "Initialize the app and start rendering"
  []
  (let [app-atom (atom {})
        dom-root (.getElementById js/document "app")
        send (e/send-fn app-atom)
        render (fn []
                 (js/requestAnimationFrame
                   #(rum/mount (Layout @app-atom send @version) dom-root)))]
    (nav/sync-locations app-atom)
    (render)
    (add-watch app-atom :ui-render render)
    (add-watch version :ui-render render)))

(defn on-jsload
  "Invoked when the system's Javascript is reloaded via Figwheel"
  []
  (swap! version inc))