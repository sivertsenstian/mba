(ns mba.graph (:require [re-frame.core :as re-frame]
                        [reagent.core  :as r]
                        [mba.config    :as config]
                        [cljsjs.chartjs]))

(defn timeseries []
  (let [timeseries (re-frame/subscribe [:timeseries])
        chart (atom nil)
        draw
        #(let [element (r/dom-node %)
               ctx (.getContext element "2d")
               chart-data
               (clj->js
                {:type "line"
                 :data {:datasets []}
                 :options
                 {:responsive true
                  :title {:display true
                          :text "Timeseries"}
                  :scales {:xAxes [{:type "time"
                                    :time {:displayFormats {:hour "DD/MM HH:mm"}}}]}}})]
           (reset! chart (js/Chart. ctx chart-data)))
        update-data
        #(let [data (map (fn [[id series]]
                           [id
                            {:temperature
                             (mapv (fn [{:keys [Temperature TimeStamp]}]
                                     {:x (js/Date. TimeStamp)
                                      :y Temperature})
                                   series)
                             :humidity
                             (mapv (fn [{:keys [Humidity TimeStamp]}]
                                     {:x (js/Date. TimeStamp)
                                      :y Humidity})
                                   series)}])
                         @timeseries)
               cdata
               (reduce
                (fn [d [[id name] {:keys [temperature humidity]}]]
                  (let [colors (get-in config/boxes [id :colors])]
                    (into d
                          [{:label (str name " - Temperature")
                            :fill false
                            :backgroundColor (colors :temperature)
                            :borderColor (colors :temperature)
                            :data temperature}
                           {:label (str name " - Humidity")
                            :fill false
                            :backgroundColor (colors :humidity)
                            :borderColor (colors :humidity)
                            :data humidity}])))
                []
                data)]
           (when @chart
             (-> @chart .-config .-data .-datasets (set! (clj->js cdata)))
             (.update @chart)))]
    (r/create-class
     {:component-did-mount draw
      :component-did-update update-data
      :reagent-render
      (fn []
        @timeseries
        [:canvas])})))
