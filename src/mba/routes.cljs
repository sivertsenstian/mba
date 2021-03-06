(ns mba.routes
  (:import goog.History)
  (:require [secretary.core :as secretary
             :refer [defroute]]
            [goog.events :as events]
            [goog.history.EventType :as EventType]
            [re-frame.core :as re-frame]))

(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
     EventType/NAVIGATE
     (fn [event]
       (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

(defn app-routes []
  (secretary/set-config! :prefix "#")
  ;; --------------------
  ;; define routes here
  (defroute "/" []
    (re-frame/dispatch [:request-boxes])
    (re-frame/dispatch [:request-timeseries])
    (re-frame/dispatch [:set-active-panel :home-panel]))
  ;; --------------------
  (hook-browser-navigation!))
