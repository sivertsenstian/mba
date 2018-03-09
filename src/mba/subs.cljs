(ns mba.subs
  (:require [re-frame.core :as re-frame
             :refer [reg-sub]]))

(reg-sub
 :active-panel
 (fn [state _]
   (:active-panel state)))

(reg-sub
 :box
 (fn [state [_ id]]
   (get-in state [:boxes id])))

(reg-sub
 :boxes
 (fn [state _]
   (:boxes state)))

(reg-sub
 :timeseries
 (fn [state [_ id]]
   (get state :timeseries)))
