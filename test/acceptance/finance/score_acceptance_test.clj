(ns finance.score_acceptance_test
    (:require
        [midje.sweet :refer :all]
        [finance.handler :refer [app]]
        [ring.adapter.jetty :refer [run-jetty]]
        [clj-http.client :as http]
        [cheshire.core :as json]
        [finance.helpers :refer :all]
        [finance.db :as db]))

(def server (atom nil))

(defn start-server [port]
    (swap! server
        (fn [_] (run-jetty app { :port port :join? false }))))

(defn stop-server [] (.stop @server))

(def d-port 3001)

(defn build-route  [route] (str "http://localhost:" d-port route))

(defn request-get [route] (comp (http/get (build-route route))))

(defn content [route] (:body (request-get route)))

(fact "star and stop server" (start-server 3000) (stop-server))

(against-background [(before :facts [(start-server d-port)
                                     (db/clear)])
                     (after :facts (stop-server))]

                (fact "return 0 as score" :acceptance
                    (json/parse-string (content "/score") true) => {:score 0})
                    
                (fact "return score as 10" :acceptance
                    (http/post (build-route "/transaction")
                        (content-as-json (revenue 10)))

                    (json/parse-string (content "/score") true) => {:score 10})

                (fact "the score is 1000 when create two revenues with 2000 and a expense with 3000" :acceptance
                    (http/post (build-route "/transaction") 
                        (content-as-json (revenue 2000)))

                        (http/post (build-route "/transaction") 
                            (content-as-json (revenue 2000)))
                        (http/post (build-route "/transaction") 
                            (content-as-json (expense 3000)))
                        (json/parse-string (content "/score") true) => {:score 1000})

                (fact "reject transaction whithout value" :acceptance
                    (let [response (http/post (build-route "/transaction") (content-as-json {:type "revenue"}))]
                        (:status response ) => 422 ))
                
                (fact "reject transaction with negative value" :acceptance
                    (let [response (http/post (build-route "/transaction") (content-as-json (revenue -1000)))]
                        (:status response ) => 422 ))
                
                (fact "reject transaction whithout type" :acceptance
                    (let [response (http/post (build-route "/transaction") (content-as-json {:value 1000}))]
                        (:status response ) => 422 ))

                (fact "reject transaction with unknow type" :acceptance
                        (let [response (http/post (build-route "/transaction") (content-as-json {:type "cdi" :value 100}))]
                            (:status response ) => 422 )))
