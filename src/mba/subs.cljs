(ns mba.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub :active-panel
 (fn [state _]
   (:active-panel state)))

(re-frame/reg-sub :box
 (fn [state [_ id]]
  (get-in state [:boxes id])))

(re-frame/reg-sub :boxes
 (fn [state _]
  (:boxes state)))

(re-frame/reg-sub :timeseries
 (fn [state [_ id]]
  (get-in state [:timeseries id])))
