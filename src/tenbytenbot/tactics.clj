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
      (fn [pos] (not (m/collides-with board piece pos)))
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


;(compute-valid-moves (m/mcreate [5 5]) [3 nil 6])


(defn play [board slot-nums step]
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
        (m/mprint board)
        (println))

      (if (:ended state)
        (println "GAME OVER")
        (let [move (play board (:slots state) (:step state))
              piece (get p/pieces (get (:slots state) (:slot-index move)))
              [x y] (:pos move)]

          (when verbose
            (println (str "About to play slot #" (:slot-index move) " on pos " x ", " y ))

            (when piece
              (println)
              (m/mprint piece)
              (println)))

          (let [new-board (try
                            (m/glue board piece (:pos move))
                            ;; TODO: remove lines!
                            (catch Exception ex
                              ;(println ex)
                              (println "invalid move!")
                              nil))]

            (if (nil? new-board)
              nil
              (let [new-state (io/play (:id state) (:step state) (:slot-index move) x y)]
                (if (:err new-state)
                  (println (:err new-state))
                  (recur new-board new-state))))))))))
