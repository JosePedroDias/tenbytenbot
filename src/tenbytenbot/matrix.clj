(ns tenbytenbot.matrix)


(defn -create [n]
  "auxiliary"
  (into
     (vector-of :boolean)
     (repeat n false)))


(defn -linearize [dims pos]
  (+ (get pos 0) (* (get pos 1) (get dims 0))))


(defn mcreate [dims positions]
  "creates a matrix of m by n dims, with false values"
  (let [matrix {:dims dims :values (-create (reduce * dims))}]
    matrix))


(defn mget [matrix pos]
  (let [values (get matrix :values) dims (get matrix :dims)]
    (get values (-linearize dims pos))))


(defn mset [matrix pos val]
  (let [values (get matrix :values) dims (get matrix :dims)]
    (assoc
      matrix
      (assoc values (-linearize dims pos) val)
      :values)))


(defn mprint [matrix]
  (let [values (get matrix :values) dims (get matrix :dims)]
    (doseq [x (range (get dims 0)) y (range (get dims 1))]
      (print (str x "," y "\n")))))


(mprint (mcreate [2 3] []))
