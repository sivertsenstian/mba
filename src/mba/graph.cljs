(ns mba.graph (:require [re-frame.core :as re-frame]
                        [reagent.core  :as r]
                        [cljsjs.c3]))

(defn humidity [box-id]
  (let [timeseries (re-frame/subscribe [:timeseries box-id])]
    (r/create-class
     {:component-will-update
      (fn []
        (let [chart-data {:bindto (str "#timeseries-" box-id "-humidity")
                          :padding {:right 30 :top 0 :bottom 0}
                          :size {:height 100 :width 250}
                          :color {:pattern ["#2185D0"]}
                          :legend {:show false}
                          :data   {:x "date"
                                   :empty {:label "no data.."}
                                   :columns [(into ["date"] (mapv #(-> % :TimeStamp (js/Date.)) @timeseries))
                                             (into ["humidity"] (mapv #(-> % :Humidity int) @timeseries))]
                                   :type "area"}
                          :axis {:x {:type "timeseries"
                                     :localtime true
                                     :tick {:count 3
                                            :format  "%H:%M"}
                                     :label {:text "Last 12 hours"
                                             :position "outer-center"}}
                                 :y {:label {:text "%"
                                             :position "inner-middle"}
                                     :tick {:count 2}
                                     :fit true}}
                          :zoom {:enabled false}
                          :grid {:x {:show false}
                                 :y {:show false}}}]
          (js/c3.generate (clj->js chart-data))))
      :reagent-render
      (fn [box-id]
        @timeseries
        [:svg {:id (str "timeseries-" box-id "-humidity")
               :width "100%"
               :height 100}])})))

(defn temperature [box-id]
  (let [timeseries (re-frame/subscribe [:timeseries box-id])]
    (r/create-class
     {:component-will-update
      (fn []
        (let [chart-data {:bindto (str "#timeseries-" box-id "-temperature")
                          :padding {:right 30 :top 0 :bottom 0}
                          :size {:height 100 :width 250}
                          :color {:pattern ["#F2711C"]}
                          :legend {:show false}
                          :data   {:x "date"
                                   :empty {:label "no data.."}
                                   :columns [(into ["date"] (mapv #(-> % :TimeStamp (js/Date.)) @timeseries))
                                             (into ["temperature"] (mapv #(-> % :Temperature int) @timeseries))]
                                   :type "area"}
                          :axis {:x {:type "timeseries"
                                     :localtime true
                                     :tick {:count 3
                                            :format  "%H:%M"}
                                     :label {:text "Last 12 hours"
                                             :position "outer-center"}}
                                 :y {:label {:text "°C"
                                             :position "inner-middle"}
                                     :tick {:count 2}
                                     :fit true}}
                          :zoom {:enabled false}
                          :grid {:x {:show false}
                                 :y {:show false}}}]
          (js/c3.generate (clj->js chart-data))))
      :reagent-render
      (fn [box-id]
        @timeseries
        [:svg {:id (str "timeseries-" box-id "-temperature")
               :width "100%"
               :height 100}])})))
