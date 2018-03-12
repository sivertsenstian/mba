(ns mba.config
  (:require [clojure.string :as s]))

(def dev?
  ^boolean goog.DEBUG)

(def boxes {1835 {:img "stian.jpg"
                  :colors {:temperature "#f2711c"
                           :humidity "#2185d0"}}
            2470 {:img "jp.jpg"
                  :colors {:temperature "#CE6018"
                           :humidity "#1C71B1"}}
            816 {:img "marcel.jpg"
                 :colors {:temperature "#F4863E"
                           :humidity "#4297D7"}}
            1234 {:img "kjetil.jpg"
                  :colors {:temperature "#A94F14"
                           :humidity "#175D92"}}})

(defn path
  ([]
   (let [p (-> js/document .-location .-pathname)]
     (path p)))
  ([p] (if (s/ends-with? p ".html")
         (s/join "/"
                 (-> p
                     (s/split "/")
                     pop
                     (conj "")))
         p)))

(def dummy-boxes
  [[:1 {:BoxId 1835 :Name "Test 1" :Owner "stian" :Location "IRIS"
        :BoxData [{:Temperature 20
                   :Humidity 50
                   :TimeStamp (js/Date.)}]}]
   [:2 {:BoxId 2470 :Name "Test 2" :Owner "jp" :Location "NORLED"
        :BoxData [{:Temperature 30
                   :Humidity 40
                   :TimeStamp (js/Date.)}]}]
   [:3 {:BoxId 816 :Name "Test 3" :Owner "marcel" :Location "MILES"
        :BoxData [{:Temperature 12
                   :Humidity 73
                   :TimeStamp (js/Date.)}]}]
   [:4 {:BoxId 1234 :Name "Test 4" :Owner "kjetil" :Location "TELIA"
        :BoxData [{:Temperature 18
                   :Humidity 33
                   :TimeStamp (js/Date.)}]}]])
