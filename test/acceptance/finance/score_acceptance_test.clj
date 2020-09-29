(ns finance.score_acceptance_test
    (:require
        [midje.sweet :refer :all]
        [finance.handler :refer [app]]
        [ring.adapter.jetty :refer [run-jetty]]
        [clj-http.client :as http]))

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
                        (fact "return 0 as limit" :acceptance
                            (content "/limit")) => "0")
