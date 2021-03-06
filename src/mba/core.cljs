(ns mba.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [mba.events]
            [mba.subs]
            [mba.routes :as routes]
            [mba.views :as views]
            [mba.config :as config]
            [cljsjs.react]
            [cljsjs.react.dom]
            [cljsjs.create-react-class]))

(defn dev-setup []
  (when config/dev?
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (routes/app-routes)
  (re-frame/dispatch-sync [:initialize])
  (dev-setup)
  (mount-root))
