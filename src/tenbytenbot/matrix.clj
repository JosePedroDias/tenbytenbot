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


(defn filled-positions [m]
  "returns sequence of filled positions in a given matrix"
  (let [[w h] (get m :dims)]
    (for [
      y (range h)
      x (range w)
      :when (mget m [x y])]
        [x y])))


(defn filled-positions-shifted [m pos]
  "filled position shifted by given pos"
  (let [[px py] pos]
    (map (fn [p] [(+ (get p 0) px) (+ (get p 1) py)]) (filled-positions m))))


(defn collides-with [m1 m2 pos]
  "returns true if m2 positioned at pos collides with m1"
  (loop [positions (filled-positions-shifted m2 pos)]
    (if (empty? positions)
      false
      (let [pos2 (first positions)]
        (if (or (not (in-bounds m1 pos2))
                (mget m1 pos2))
          true
          (recur (next positions)))))))


(defn glue [m1 m2 pos]
  "returns result of applying m2 positioned at pos onto m1
  assumes no collision ocurrs"
  (loop [m1 m1
         positions (filled-positions-shifted m2 pos)]
    (if (empty? positions)
      m1
      (recur (mset m1 (first positions) true) (next positions)))))


(defn mprint [matrix]
  "prints matrix"
  (let [values (get matrix :values) [w h] (get matrix :dims)]
    (doseq [y (range h) x (range w)]
      (let [v (mget matrix [x y])]
        (print (if v "#" "."))
        (when (= x (- w 1))
          (print "\n"))))))
