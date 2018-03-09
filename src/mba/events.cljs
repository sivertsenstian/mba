(ns mba.events
  (:require [re-frame.core :as re-frame :refer [reg-event-db dispatch]]
            [mba.state :as state]
            [mba.rest  :as rest]))

(reg-event-db
 :initialize
 (fn  [_ _] state/initial-state))

(reg-event-db
 :set-active-panel
 (fn [state [_ active-panel]]
   (assoc state :active-panel active-panel)))

(reg-event-db
 :request-box
 (fn  [state [_ id]]
   (rest/get-box id
                 {:handler #(dispatch [:receive-box id (rest/unpack %)])})
   state))

(reg-event-db
 :receive-box
 (fn  [state [_ id data]]
   (assoc-in state [:boxes id] data)))

(reg-event-db
 :request-boxes
 (fn  [state _]
   (rest/get-boxes
    {:handler #(dispatch [:receive-boxes (rest/unpack %)])})
   state))

(reg-event-db
 :receive-boxes
 (fn  [state [_ data]]
   (let [boxes (map #(vector (:BoxId %) %) data)]
     (assoc state :boxes (into (sorted-map) boxes)))))

(reg-event-db
 :request-timeseries
 (fn  [state _]
   (rest/get-timeseries
    {:handler #(dispatch [:receive-timeseries (rest/unpack %)])})
   state))

(reg-event-db
 :receive-timeseries
 (fn  [state [_ data]]
   (let [ts (map #(vector (:Name %) (:BoxData %)) data)]
     (assoc state :timeseries (into (sorted-map) ts)))))
