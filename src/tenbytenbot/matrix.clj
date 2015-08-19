(ns tenbytenbot.matrix)


(defn- create [n]
  "aux: creates vector of booleans of given size"
  (into
     (vector-of :boolean)
     (repeat n false)))


(defn- linearize [dims pos]
  "aux: 2D coords to linear vector"
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


(defn collides-with? [m1 m2 pos]
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


(defn- column-positions [matrix x]
  "returns positions that make up the given column"
  (map
    (fn [y] [x y])
    (range (get (:dims matrix) 1))))


(defn- row-positions [matrix y]
  "returns positions that make up the given row"
  (map
    (fn [x] [x y])
    (range (get (:dims matrix) 0))))


(defn is-column-filled? [matrix x]
  "true iif matrix has its cells on column x filled"
  (every?
    (fn [pos] (mget matrix pos))
    (column-positions matrix x)))


(defn is-row-filled? [matrix y]
  "true iif matrix has its cells on row y filled"
  (every?
    (fn [pos] (mget matrix pos))
    (row-positions matrix y)))


(defn set-column [matrix x val]
  "set given column to given val"
  (msets matrix (column-positions matrix x) val))


(defn set-row [matrix y val]
  "set given row to given val"
  (msets matrix (row-positions matrix y) val))


(defn list-filled-rows-and-columns [matrix]
  "returns hash with indices for filled rows and columns"
  (let [[w h] (:dims matrix)]
    {:rows (filter (fn [y] (is-row-filled? matrix y)) (range h))
     :cols (filter (fn [x] (is-column-filled? matrix x)) (range w))}))


(defn count-filled-rows-or-columns
  "returns number of filled rows and columns in given matrix
  supports 2nd argument with precomputed hash"
  ([matrix] (count-filled-rows-or-columns matrix (list-filled-rows-and-columns matrix)))
  ([matrix status]
    (let [{rows :rows cols :cols} status]
      (+ (count rows) (count cols)))))


(defn wipe-filled-rows-and-columns
  "returns a new board with filled rows and columns removed
  supports 2nd argument with precomputed hash"
  ([matrix] (wipe-filled-rows-and-columns matrix (list-filled-rows-and-columns matrix)))
  ([matrix status]
    (let [{rows :rows cols :cols} status]
      (loop [m0 (loop [m matrix
                      cols cols]
                 (if (empty? cols)
                   m
                   (recur (set-column m (first cols) false) (next cols))))
             rows rows]
        (if (empty? rows)
          m0
          (recur (set-row m0 (first rows) false) (next rows)))))))


(defn mprint [matrix]
  "prints matrix"
  (let [values (get matrix :values) [w h] (get matrix :dims)]
    (doseq [y (range h) x (range w)]
      (let [v (mget matrix [x y])]
        (print (if v "#" "."))
        (when (= x (- w 1))
          (print "\n"))))))

