(ns finance.handler-test
  (:require [midje.sweet :refer :all]
            [ring.mock.request :as mock]
            [finance.handler :refer :all]
            [cheshire.core :as json]
            [finance.db :as db]))

(facts "Tests to root route"
  (let [response (app (mock/request :get "/"))]
  (fact "return 200 status code"
    (:status response) => 200)
    
  (fact "retorna mensagem de Hello World"
      (:body response) => "Hello World")))

(facts "Tests for invalid routes" 
  (let [response (app (mock/request :get "/invalid"))]
  (fact "return 404 status code for unknow paths"
      (:status response) => 404)))

(facts "Tests score route"
  ; doing a mock in json/generate-string :P
  (against-background (json/generate-string { :score "0" }) => "xablau")
  (let [response (app (mock/request :get "/score"))]
    (fact "return 200 status code"
      (:status response) => 200)

    (fact "return zero from score" 
      (:body response) => "xablau")
    
    (fact "has application/json in header"
      (get-in response [:headers "Content-Type"]) => "application/json; charset=utf8")))

(facts "Tests transaction route"
  (against-background (db/register {:value 10 :type "revenue"}) => 
    {:id 1 :score 10 :type "revenue"})
  
    (let [response (app (-> (mock/request :post "/transaction")
                            (mock/json-body {:value 10 :type "revenue"})))]
    (fact "return 201 status code" :unity
      (:status response) => 201)
    
    (fact "return a json body"
      (:body response) => (json/generate-string {:id 1 :score 10 :type "revenue"}))))