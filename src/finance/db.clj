(ns finance.db)

(def registers
    (atom []))

(defn transactions
    []
    @registers)

(defn clear 
    []
    (reset! registers []))

(defn register!
    [transaction]
    (let [new-transaction    (swap! registers conj transaction)]
        (merge transaction {:id (count new-transaction)})))

(defn- expense?
    [transaction]
    (= (:type transaction) "expense"))

(defn- calculate
    [acc transaction]
    (let [value (:value transaction)]
    (if (expense? transaction)
        (- acc value)
        (+ acc value))))
(defn score []
    (reduce calculate 0 @registers))