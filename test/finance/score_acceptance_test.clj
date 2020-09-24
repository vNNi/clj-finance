(ns finance.score_acceptance_test
    (:require
        [midje.sweet :refer :all]
        [finance.handler :refer [app]]
        [ring.adapter.jetty :refer [run-jetty]]))

(def server (atom nil))

(defn start-server [port]
    (swap! server
        (fn [_] (run-jetty app { :port port :join? false }))))

(defn stop-server [] (.stop @server))

(fact "star and stop server" (start-server 3000) (stop-server))