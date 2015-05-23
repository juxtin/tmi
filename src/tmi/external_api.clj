(ns tmi.external-api
  (:require [cheshire.core :as json]
            [environ.core :refer [env]]))

(def trimet-id (or (env :trimet-id)
                   (throw (IllegalArgumentException.
                           "No :trimet-id found in environment!"))))

(def wunderground-id (or (env :wunderground-id)
                         (throw (IllegalArgumentException.
                                 "No :wunderground-id found in environment!"))))

(defn ->int
  [x]
  (if (integer? x)
    x
    (try (Integer/parseInt x)
      (catch Exception e
        (throw (IllegalArgumentException. (str x " is not a valid ID.")))))))

(defn get-stop*
  ;; COPYPASTA
  "Queries the Trimet API using the supplied appId."
  [app-id stop]
  (let [query-head (str "http://developer.trimet.org/ws/V1/arrivals?appId=" app-id "&json=true&locIDs=")]
    (let [query (str query-head stop)]
      (-> query
        (slurp)
        (json/decode keyword)))))

(defn next-arrivals* [response route]
  ;; COPYPASTA
  (->> (get-in response [:resultSet :arrival])
       (filter #(= route (get % :route)))
       (map (juxt #(get % :fullSign) #(get % :estimated)))))

(defn get-arrivals*
  ;; COPYPASTA
  "Gets the next arrivals for the given stop and bus."
  [id stop bus]
  (-> (get-stop* id stop)
    (next-arrivals* bus)))

(defn get-arrivals
  [stop bus]
  (->> (get-arrivals* trimet-id
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
