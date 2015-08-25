(ns tenbytenbot.tactics
  (:require
    [tenbytenbot.server-io :as io]
    [tenbytenbot.matrix    :as m]
    [tenbytenbot.pieces    :as p]))



(defn compute-valid-positions-for-piece [board piece]
  "returns the sequence of positions a piece can occupy on the given board"
  (let [[bw bh] (:dims board)
        [pw ph] (:dims piece)]
    (filter
      (fn [pos] (not (m/collides-with? board piece pos)))
      (for [x (range (inc (- bw pw)))
            y (range (inc (- bh ph)))]
        [x y]))))



(defn compute-valid-moves [board slotnums]
  "returns a sequence valid moves with keys for :slot-index and :pos"
  (apply concat
    (map
      (fn [[index slotnum]]
        (let [piece (get p/pieces slotnum)
              positions (compute-valid-positions-for-piece board piece)]
          (map
            (fn [pos] {:slot-index index :pos pos})
            positions)))
      (filter
        (fn [[index slotnum]] slotnum)
        (map vector (iterate inc 0) slotnums)))))


; HEURISTIC HELPER FUNCTIONS


(defn empty-spots [board]
  "returns number of cells which are empty"
  (reduce (fn [prev this] (+ prev (if this 0 1))) 0 (:values board)))

;(empty-spots (m/mcreate [3 3] '([0 0] [1 0])))



(defn- neighbours-4 [pos filled yet-to-visit]
  "aux"
  (let [[x y] pos
        neig (conj (filter
                      (partial contains? filled)
                      [[(dec x) y]
                       [(inc x) y]
                       [x (dec y)]
                       [x (inc y)]]) pos)]
    (loop [changed false
           neig neig
           yet-to-visit yet-to-visit]
      (if (empty? neig)
        {:changed changed :yet-to-visit yet-to-visit}
        (let [f (first neig)
              n (next neig)]
          (if (contains? yet-to-visit f)
            (recur true    n (disj yet-to-visit f))
            (recur changed n       yet-to-visit)))))))




(defn number-of-islands [board]
;;   "TODO http://prismoskills.appspot.com/lessons/Arrays/Count_blobs_in_matrix.jsp"
   (let [filled (set (m/filled-positions board))]
     (loop [yet-to-visit filled
            result 0]
       (if (empty? yet-to-visit)
         result
         (let [r (neighbours-4 (first yet-to-visit) filled yet-to-visit)]
           (recur (:yet-to-visit r) (if (:changed r) (inc result) result)))))))


(number-of-islands (m/from-string "##.#
##..
..##"))



;;       (if (and (m/mget board [x y])
;;                (not (m/get [
;;   0)



(defn play [board slot-nums]
  "given a board and an array of pieces (indices)"
  (let [valid-moves (compute-valid-moves board slot-nums)]
    (rand-nth valid-moves)))



(defn go []
  "play a tenbyten game"
  (let [verbose true
        debug true]

    (loop [board (m/mcreate [10 10])
           state (io/new-game)]

      (when debug
        (println "server state:")
        (println state))

      (when verbose
        (println)
        (println (m/to-string board))
        (println))

      (if (:ended state)
        (println "GAME OVER")
        (let [move (play board (:slots state))
              piece (get p/pieces (get (:slots state) (:slot-index move)))
              [x y] (:pos move)]

          (when verbose
            (println "About to play slot #" (:slot-index move) "on pos" x "," y )

            (when piece
              (println)
              (println (m/to-string board))
              (println)))

          (let [new-board (let [temp-board (m/glue board piece (:pos move))
                                status (m/list-filled-rows-and-columns temp-board)
                                rs-and-cs (m/count-filled-rows-or-columns temp-board status)]
                            (if (= rs-and-cs 0)
                              temp-board
                              (do
                                (println "DESTROYED " rs-and-cs " ROWS AND COLS!")
                                (m/wipe-filled-rows-and-columns temp-board status))))]

            (if (nil? new-board)
              nil
              (let [new-state (io/play (:id state) (:step state) (:slot-index move) x y)]
                (if (:err new-state)
                  (println (:err new-state))
                  (recur new-board new-state))))))))))
