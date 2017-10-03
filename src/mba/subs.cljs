(ns mba.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :as re-frame :refer [reg-sub]]))

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
   (get-in state [:timeseries id])))

(defn jensen2k []
  (+ 12 2))

(1 2 3)

