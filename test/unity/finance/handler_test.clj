(ns finance.handler-test
  (:require [midje.sweet :refer :all]
            [ring.mock.request :as mock]
            [finance.handler :refer :all]
            [cheshire.core :as json]))

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
  (let [response (app (mock/request :get "/score"))]
    (fact "return 200 status code"
      (:status response) => 200)

    (fact "return zero from score" 
      (json/parse-string (:body response) true) => { :score "0" })))