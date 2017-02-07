(ns org.arachne-framework.application-template-1
  (:require [rum.core :as rum]))

(rum/defc hello [text]
  [:div {:class "hello"} text])

(rum/mount (hello "Hello, world!")
  (.getElementById js/document "app"))
