(ns mba.rest
 (:require [ajax.core    :refer [GET POST]]
           [clojure.walk :as w]
           [mba.config   :as config]))

(def service-url
  "https://milesbox.azurewebsites.net/api"
  #_(if config/dev?
     "http://milesbox.azurewebsites.net/api"
     "/api"))

(defn unpack [v]
  (-> v js->clj w/keywordize-keys))

(defn get-box [id options]
 (GET (str service-url "/currentvalue/" id)
      options))

(defn get-boxes [options]
 (GET (str service-url "/currentvalue")
      options))

(defn get-timeseries [options]
 (GET (str service-url "/timeseries")
      options))
