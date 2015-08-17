(ns tenbytenbot.tactics
  (:require
    [tenbytenbot.server-io :as io]
    [tenbytenbot.matrix    :as m]
    [tenbytenbot.pieces    :as p]))



(defn play [board slot-nums]
  ""
  (let [slots (map (fn [num] (get p/pieces num)) slot-nums)]

    ;(println slots)
    (println "----")
    (doseq [[i slot] (map-indexed vector slots)]
      (println (str "\n" i ":"))
      (m/mprint slot))

    (let [pos [0 0]
          slot-index 0
          slot (get slots slot-index)]
      (println (str "\nabout to play " slot-index " on " pos "..."))
      {:slot-index slot-index :pos pos}
      ;(io/play )
      ;(m/mprint (m/glue board slot pos))
      )))



(defn go []
  "play a tenbyten game"
  (let [board (m/mcreate [10 10])
        ng (io/new-game)]
    (println ng)

    (let [p (play board (:slots ng))
          [x y] (:pos p)]

      (let [r (io/play (:id ng) (:step ng) (:slot-index p) x y)]
        (println r)))))
