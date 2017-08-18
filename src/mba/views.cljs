(ns mba.views
  (:require [re-frame.core :as re-frame]
            [reagent.core  :as r]
            [mba.config    :as config]
            [mba.graph     :as graph]))

(defn render-box [[_
                   {:keys [BoxId Name Owner Location]
                    [{:keys [Temperature Humidity TimeStamp]} _] :BoxData
                    :as data}]]
  (let [header (fn []
                [:div.content
                 [:div.right.floated.meta BoxId]
                 [:img.ui.avatar.image
                  {:src (str (config/path) "Content/" (or (get-in config/boxes [BoxId :img])
                                                       "placeholder.jpg"))}]
                 Owner])]
    ^{:key BoxId}
    [:div.four.wide.column
     (if data
       [:div.ui.card
        {:title (str Name " // " BoxId)}
        [header]
        [:div.content
         [:div.ui.one.small.statistics
          [:div.ui.orange.statistic
           [:div.value Temperature " "
            [:span {:dangerouslySetInnerHTML {:__html "&deg;"}}]
            [:span "C"]]
           [:div.label "Temperature"]]]
         [:div.ui.one.small.statistics.mba-padding-top-xs
          [:div.ui.blue.statistic
           [:div.value Humidity " %"]
           [:div.label "Humidity"]]]]
        [:div.content
         [graph/temperature BoxId]
         [graph/humidity BoxId]]
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
   (if @boxes
    [:div.ui.row
     (map render-box @boxes)]
    [:div.ui.row [:h2 "No boxes found :("]]))))

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
       [:div.ui.fixed.top.menu
        [:div.item [:img {:src "https://instagram.fsvg1-1.fna.fbcdn.net/t51.2885-19/s320x320/14073142_328109660855320_1629482734_a.jpg"}]]
        [:a.item {:href "#/"} "Dashboard"]]
       [:div.ui.grid.container.mba-view
        [show-panel @active-panel]]])))
