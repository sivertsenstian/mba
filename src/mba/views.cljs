(ns mba.views
  (:require [re-frame.core     :as re-frame]
            [mba.graph         :as graph]
            [mba.config        :as config]))

(defn chiev [{:keys [name value description winner]}]
  [:div.item
   [:img.ui.avatar.image {:src (str (config/path) "Content/"
                                    (or (get-in config/boxes [winner :img])
                                        "placeholder.jpg"))}]
   [:div.content
    [:a.header (str name ": " value)]
    [:div.description description]]])

(defn achievments []
  (let [timeseries (re-frame/subscribe [:timeseries])]
    (fn []
      (let [values (remove #(= (:BoxId %) 0) (mapcat vec (vals @timeseries)))
            hottest (apply max-key :Temperature values)
            coldest (apply min-key :Temperature values)
            wettest (apply max-key :Humidity values)
            driest (apply min-key :Humidity values)
            chievs [{:name "Beddoen"
                     :description (-> hottest :TimeStamp js/Date. (js/moment.) (.format "MMMM Do YYYY, HH:mm"))
                     :value (str (-> hottest :Temperature Math/round) " degrees celsius")
                     :winner (:BoxId hottest)}
                    {:name "Ice ice baby"
                     :description (-> coldest :TimeStamp js/Date. (js/moment.) (.format "MMMM Do YYYY, HH:mm"))
                     :value (str (-> coldest :Temperature Math/round) " degrees celsius")
                     :winner (:BoxId coldest)}
                    {:name "Ordfører i vågå"
                     :description(-> wettest :TimeStamp js/Date. (js/moment.) (.format "MMMM Do YYYY, HH:mm"))
                     :value (str (-> wettest :Humidity Math/round) "% humidity")
                     :winner (:BoxId wettest)}
                    {:name "Tørrpinnen"
                     :description(-> driest :TimeStamp js/Date. (js/moment.) (.format "MMMM Do YYYY, HH:mm"))
                     :value (str (-> driest :Humidity Math/round) "% humidity")
                     :winner (:BoxId driest)}]]
        (if (seq values)
          (into
           [:div.ui.tiny.divided.very.relaxed.list]
           (map chiev chievs))
          [:div.ui.content.aligned.center "No data :("])))))


(defn render-box [[_
                   {:keys [BoxId Name Owner Location]
                    [{:keys [Temperature Humidity TimeStamp]} _] :BoxData
                    :as data}]]
  (let [header (fn []
                 [:div.content
                  [:img.right.floated.mini.ui.image
                   {:src (str (config/path) "Content/"
                              (or (get-in config/boxes [BoxId :img])
                                  "placeholder.jpg"))}]
                  [:div.header Owner]
                  [:div.meta BoxId]])]
    ^{:key BoxId}
    [:div.column
     (if data
       [:div.ui.centered.card
        {:title (str Name " // " BoxId)}
        [header]
        (let [{{{:keys [temperature humidity]} :colors} BoxId} config/boxes]
          [:div.content
           [:div.ui.one.small.statistics
            [:div.ui.statistic
             [:div.value
              {:style {:color temperature}}
              Temperature " "
              [:span {:dangerouslySetInnerHTML {:__html "&deg;"}}]
              [:span "C"]]
             [:div.label "Temperature"]]]
           [:div.ui.one.small.statistics.mba-padding-top-xs
            [:div.ui.statistic
             [:div.value
              {:style {:color humidity}}
              Humidity " %"]
             [:div.label "Humidity"]]]])
        [:div.content
         [:span.right.floated
          [:span.add.to.calendar.icon Location]]
         [:span.header "Last updated"]
         [:span.meta
          (-> TimeStamp js/Date. .toLocaleDateString)
          " - "
          (-> TimeStamp js/Date. .toLocaleTimeString)]]]

       [:div.ui.card
        {:title (str Name " // " BoxId)}
        [header]
        [:div.content.center.aligned
         [:i.spinner.icon]
         "Waiting for data"]])]))
;; home
(defn home-panel []
  (let [boxes (re-frame/subscribe [:boxes])]
    (js/setInterval #(do
                       (re-frame/dispatch [:request-boxes])
                       (re-frame/dispatch [:request-timeseries])) 30000)
    (fn []
      [:div.ui.stackable.grid
       (if-let [boxes @boxes]
         (into [:div.four.column.centered.row]
               (map render-box boxes))
         [:div.ui.centered.row
          [:h2 "No boxes found :("]])])))

;; main
(defn- panels [panel-name]
  (case panel-name
    :home-panel [home-panel]
    [:div]))

(defn show-panel [panel-name]
  [panels panel-name])

(defn main-panel []
  (let [active-panel (re-frame/subscribe [:active-panel])]
    (fn []
      [:div
       [:div.ui.container.right.aligned
        [:img {:height 75 :src (str (config/path) "Content/logo.png")}]]
       [:div.ui.container
        [:div.ui.grid
         [:div.twelve.wide.column
          [graph/timeseries]]
         [:div.four.wide.column
          [:div.ui.header "Achievments"]
          [achievments]]]
        [:h4.ui.horizontal.divider.header
         [:i.user.icon]]
        [show-panel @active-panel]]])))
