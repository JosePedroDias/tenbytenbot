(ns tenbytenbot.tactics
  (:require
    [tenbytenbot.server-io :as io]
    [tenbytenbot.matrix    :as m]
    [tenbytenbot.pieces    :as p]))



(defn play [board slot-nums step]
  "elects a move to perform"
  (let [slots (vec (map (fn [num] (get p/pieces num)) slot-nums))]

    (let [pos [(* step 3) 0]
          slot-index step
          slot (get slots slot-index)]

      { :slot slot
        :slot-index slot-index
        :pos pos})))



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

      (let [move (play board (:slots state) (:step state))
            [x y] (:pos move)]

        (when verbose
          (println (str "About to play slot #" (:slot-index move) " on pos " x ", " y ))

          (when (:slot move)
            (println)
            (m/mprint (:slot move))
            (println)))

        (let [new-board (try
                          (m/glue board (:slot move) (:pos move))
                          (catch Exception ex
                            ;(println ex)
                            (println "invalid move!")
                            nil))]

          (if (nil? new-board)
            nil
            (let [new-state (io/play (:id state) (:step state) (:slot-index move) x y)]
              (if (:err new-state)
                (println (:err new-state))
                (recur new-board new-state)))))))))
