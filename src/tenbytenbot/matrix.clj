(ns tenbytenbot.matrix)


(defn- create [n]
  "auxiliary"
  (into
     (vector-of :boolean)
     (repeat n false)))


(defn- linearize [dims pos]
  (+ (get pos 0) (* (get pos 1) (get dims 0))))


;;-------------------


(defn mset [matrix pos val]
  "sets element at position pos with value val"
  (let [values (get matrix :values) dims (get matrix :dims)]
    (assoc
      matrix
      :values
      (assoc values (linearize dims pos) val))))


(defn msets [matrix positions val]
  "same as mset but accepts sequence of elements"
  (if (empty? positions)
    matrix
    (msets (mset matrix (first positions) val) (next positions) val)))


(defn mcreate
  "creates a matrix of m by n dims, with false values"
  ([dims]
    {:dims dims :values (create (reduce * dims))})
  ([dims positions]
    (msets (mcreate dims) positions true)))


(defn mget [matrix pos]
  "gets element at position pos"
  (let [values (get matrix :values) dims (get matrix :dims)]
    (get values (linearize dims pos))))


(defn in-bounds [matrix pos]
  "returns true iif position inside matrix's bounds"
  (let [
    [w h] (get matrix :dims)
    [x y] pos]
    (and
      (>= x 0)
      (>= y 0)
      (< x w)
      (< y h))))


;; (defn collides-with [m1 m2 pos]
;;   "returns true if m2 positioned at pos collides with m1"
;;   (let [
;;     [px py] pos
;;     [w h] (get m2 :dims)]
;;     (doseq [y (range h) x (range w)]
;;       (if (and (mget m2 [x y])
;;                (or (not (in-bounds m1 [(+ px x) (+ py y)]))
;;                    (mget m1 [(+ px x) (+ py y)])))))))


(defn mprint [matrix]
  "prints matrix"
  (let [values (get matrix :values) [w h] (get matrix :dims)]
    (doseq [y (range h) x (range w)]
      (let [v (mget matrix [x y])]
        (print (if v "1" "0"))
        (when (= x (- w 1))
          (print "\n"))))))


;(mcreate [3 2])

;(mset (mcreate [3 2]) [0 0] true)

;(msets (mcreate [3 2]) '() true)
;(msets (mcreate [3 2]) '([0 0]) true)
;(msets (mcreate [3 2]) '([0 0] [0 1]) true)

;(mcreate [3 2] '([0 0] [1 0]))

;(mget (mcreate [3 2]) [0 0])
;(mget (mset (mcreate [3 2]) [0 0] true) [0 0])

;(mprint (mcreate [3 2]))
;(mprint (msets (mcreate [3 2]) '([0 0] [0 1]) true))
