(ns finance.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [cheshire.core :as json]))

(defroutes app-routes
  (GET "/" [] "Hello World")
  (GET "/score" [] { :headers { "Content-Type" "application/json; charset=utf8" }
                    :body (json/generate-string { :score "0" }) })
  (POST "/transaction" [] {})
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes api-defaults))
