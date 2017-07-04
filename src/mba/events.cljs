(ns mba.events
  (:require [re-frame.core :as re-frame]
            [mba.state :as state]
            [mba.rest  :as rest]))

(re-frame/reg-event-db
 :initialize
 (fn  [_ _] state/initial-state))

(re-frame/reg-event-db
 :set-active-panel
 (fn [state [_ active-panel]]
   (assoc state :active-panel active-panel)))

(re-frame/reg-event-db
 :request-box
 (fn  [state [_ id]]
   (rest/get-box id
    {:handler #(re-frame/dispatch [:receive-box id (rest/unpack %)])})
  state))

(re-frame/reg-event-db
 :receive-box
 (fn  [state [_ id data]]
  (assoc-in state [:boxes id] data)))

(re-frame/reg-event-db
 :request-boxes
 (fn  [state _]
   (rest/get-boxes
    {:handler #(re-frame/dispatch [:receive-boxes (rest/unpack %)])})
  state))

(re-frame/reg-event-db
 :receive-boxes
 (fn  [state [_ data]]
   (let [boxes (map #(vector (:BoxId %) %) data)]
    (assoc state :boxes (into (sorted-map) boxes)))))

(re-frame/reg-event-db
 :request-timeseries
 (fn  [state _]
   (rest/get-timeseries
    {:handler #(re-frame/dispatch [:receive-timeseries (rest/unpack %)])})
  state))

(re-frame/reg-event-db
 :receive-timeseries
 (fn  [state [_ data]]
   (let [ts (map #(vector (:BoxId %) (:BoxData %)) data)]
    (assoc state :timeseries (into (sorted-map) ts)))))
