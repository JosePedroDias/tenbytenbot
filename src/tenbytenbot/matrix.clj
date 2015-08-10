(ns tenbytenbot.matrix)


(defn -create [n]
  "auxiliary"
  (into
     (vector-of :boolean)
     (repeat n false)))


(defn -linearize [dims pos]
  (+ (get pos 0) (* (get pos 1) (get dims 0))))


;;-------------------


(defn mcreate
  [dims]
  "creates a matrix of m by n dims, with false values"
  {:dims dims :values (-create (reduce * dims))})


(defn mget [matrix pos]
  "gets element at position pos"
  (let [values (get matrix :values) dims (get matrix :dims)]
    (get values (-linearize dims pos))))


(defn mset [matrix pos val]
  "sets element at position pos with value val"
  (let [values (get matrix :values) dims (get matrix :dims)]
    (assoc
      matrix
      :values
      (assoc values (-linearize dims pos) val))))


(defn msets [matrix positions val]
  "same as mset but accepts sequence of elements"
  (if (empty? positions)
    matrix
    (msets (mset matrix (first positions) val) (next positions) val)))


(defn mprint [matrix]
  "prints matrix"
  (let [values (get matrix :values) dims (get matrix :dims)]
    (doseq [y (range (get dims 1)) x (range (get dims 0))]
      (let [v (mget matrix [x y])]
        (print (if v "1" "0"))
        (when (= x (- (get dims 0) 1))
          (print "\n"))))))


;(mcreate [3 2])

;(mset (mcreate [3 2]) [0 0] true)

(msets (mcreate [3 2]) '() true)
(msets (mcreate [3 2]) '([0 0]) true)
(msets (mcreate [3 2]) '([0 0] [0 1]) true)

;(msets (mcreate [3 2]) '([0 0] [1 1]) true)

;(mprint (mcreate [3 2]))

;(mset (mcreate [3 2]) [0 0] true)

;(mprint (mcreate [3 2] (list [0 0])))

;(mset(mcreate [3 2])
