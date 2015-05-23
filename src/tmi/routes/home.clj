(ns tmi.routes.home
  (:require [compojure.core :refer :all]
            [tmi.views.layout :as layout]
            [cheshire.core :as json]
            [tmi.external-api :as api]))

(defroutes home-routes
  (GET "/arrivals" [stop bus] (json/encode (api/get-arrivals stop bus)))
  (GET "/advice" [stop bus] (json/encode (api/get-advice stop bus))))
