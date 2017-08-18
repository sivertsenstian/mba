(ns mba.config
 (:require [clojure.string :as s]))

(def dev?
  ^boolean goog.DEBUG)

(def boxes {1835 {:img "stian.jpg"}
            2470 {:img "jp.jpg"}
            816 {:img "marcel.jpg"}
            1234 {:img "kjetil.jpg"}})

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
