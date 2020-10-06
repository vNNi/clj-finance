(ns finance.db_test
    (:require [midje.sweet :refer :all]
              [finance.db :refer :all]))

(facts "store a transaction in atom"
    (against-background [(before :facts (clear))
                         (after :facts (clear))])
    (fact "the transaction initiate empty" 
        (count (transactions)) => 0)

    (fact "the transaction is the first register" 
        (register! {:value 7 :type "revenue"}) => {:id 1 :value 7 :type "revenue"}
            (count (transactions)) => 1))

(facts "calculate the score based on transactions"
    (against-background [(before :facts (clear))])
        (fact "score is positive when just has revenue"
            (register! {:value 10 :type "revenue"})
            (register! {:value 10 :type "revenue"})
            (score) => 20)
        (fact "score is negative when just has expense"
            (register! {:value 10 :type "expense"})
            (register! {:value 10 :type "expense"})
            (score) => -20)
        (fact "score is the sum of revenues minus expenses"
            (register! {:value 20 :type "revenue"})
            (register! {:value 10 :type "expense"})
            (score) => 10))