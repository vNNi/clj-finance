(ns finance.score_acceptance_test
    (:require
        [midje.sweet :refer :all]
        [finance.handler :refer [app]]
        [ring.adapter.jetty :refer [run-jetty]]
        [clj-http.client :as http]
        [cheshire.core :as json]))

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

(against-background [(before :facts (start-server d-port))
                     (after :facts (stop-server))]

                (fact "return 0 as score" :acceptance
                    (json/parse-string (content "/score") true)) => {:score "0"}
                    
                (fact "return score as 10" :acceptance
                    (http/post (build-route "/transaction")
                        {:body (json/generate-string {:value 10 :type "revenue"})})
                    (json/parse-string (content "/score") true) => {:value 10}))

