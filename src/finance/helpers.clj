(ns finance.helpers
    (:require [cheshire.core :as json]))

(defn content-as-json 
    [transaction]
    {:content-type :json
     :body (json/generate-string transaction)
     :throw-exceptions false})

(defn revenue
    [value]
    {:value value :type "revenue"})

(defn expense 
    [value]
    {:value value :type "expense"})