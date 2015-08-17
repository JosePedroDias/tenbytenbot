(ns tenbytenbot.tactics
  (:require
    [tenbytenbot.server-io :as io]
    [tenbytenbot.matrix    :as m]
    [tenbytenbot.pieces    :as p]))



(defn play [board slot-nums debug]
  "elects a move to perform"
  (let [slots (vec (map (fn [num] (get p/pieces num)) slot-nums))]

    (when debug
      (println "----")
      (doseq [[i slot] (map-indexed vector slots)]
        (println (str "\n" i ":"))
        (m/mprint slot)))

    (let [pos [0 0]
          slot-index 0
          slot (get slots slot-index)]

      (when debug
        (println (str "\nabout to play " slot-index " on " pos "...")))

      { :slot slot
        :slot-index slot-index
        :pos pos})))



(defn go []
  "play a tenbyten game"
  (let [debug true
        board (m/mcreate [10 10])
        ng (io/new-game)]

    (when debug
      (println ng))

    (let [p (play board (:slots ng) debug)
          [x y] (:pos p)]

      (let [r (io/play (:id ng) (:step ng) (:slot-index p) x y)
            board2 (m/glue board (:slot p) (:pos p))]
        (when debug
          (println r)
          (println)
          (m/mprint (:slot p))
          (println)
          (m/mprint board2))

        ))))
