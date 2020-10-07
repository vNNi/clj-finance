(ns finance.transactions_test
    (:require [midje.sweet :refer :all]
              [finance.transactions :refer :all]))

(fact "A transaction without value is not valid"
    (valid? {:type "revenue"}) => false)

(fact "A transaction with negative value is not valid"
    (valid? {:value -10 :type "revenue"}) => false)

(fact "A transaction with unkown type is not valid"
    (valid? {:value 10 :type "cdi"}) => false)

(fact "A transaction with not numerico value is not valid"
    (valid? {:value "1203" :type "revenye"}) => false)

(fact "A transaction with know type and positive number as value is valid", 
    (valid? {:value 10 :type "revenue"}) => true)