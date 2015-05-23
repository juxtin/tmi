(ns tmi.external-api
  (:require [trimet.core :as tm]
            [cheshire.core :as json]
            [environ.core :refer [env]]))

(def trimet-id (or (:trimet-id env)
                   (throw (IllegalArgumentException.
                           "No :trimet-id found in environment!"))))

(def wunderground-id (or (:wunderground-id env)
                         (throw (IllegalArgumentException.
                                 "No :wunderground-id found in environment!"))))

(defn ->int
  [x]
  (if (integer? x)
    x
    (try (Integer/parseInt x)
      (catch Exception e
        (throw (IllegalArgumentException. (str x " is not a valid ID.")))))))

(defn get-arrivals
  [stop bus]
  (->> (tm/get-arrivals trimet-id
                        (->int stop)
                        (->int bus))
    (map second)
    (keep identity)))

(defn hourly-weather
  []
  (let [resp (-> (str "http://api.wunderground.com/api/"
                      wunderground-id "/forecast/geolookup"
                      "/conditions/q/OR/Portland.json")
               slurp
               (json/decode keyword))]
    (get-in resp [:forecast :txt_forecast :forecastday])))

(defn current-conditions
  []
  (get-in (hourly-weather) [0 :icon]))

(defn get-advice
  [stop bus]
  {:weather (current-conditions)
   :arrivals (get-arrivals stop bus)})
