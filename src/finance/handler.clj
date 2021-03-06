(ns finance.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.json :refer [wrap-json-body]]
            [cheshire.core :as json]
            [finance.db :as db]
            [finance.transactions :as transactions]))


(defn common-response [content & [status]]
  {:status (or status 200)
  :headers { "Content-Type" "application/json; charset=utf8" }
  :body (json/generate-string content)})
            
(defroutes app-routes
  (GET "/" [] "Hello World")
  (GET "/score" [] (common-response { :score (db/score) }))
  (POST "/transaction" request (if (transactions/valid? (:body request))
                                  (-> (db/register! (:body request))
                                      (common-response 201))
                                  (common-response {:message "Body is not valid"} 422)))
  (route/not-found "Not Found"))

(def app
  (-> (wrap-defaults app-routes api-defaults)
      (wrap-json-body {:keywords? true :bigdecimails? true})))
