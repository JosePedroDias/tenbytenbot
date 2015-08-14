(ns tenbytenbot.pieces)

(load "matrix")
(refer 'tenbytenbot.matrix)



(def pieces
  [
    ; 1: 1x1 square
    (mcreate [1 1] '([0 0]))

    ; 2: 2x1 I
    (mcreate [1 2] '([0 0] [0 1]))
    (mcreate [2 1] '([0 0] [1 0]))

    ; 3: 3x1 I
    (mcreate [3 1] '([0 0] [1 0] [2 0]))
    (mcreate [1 3] '([0 0] [0 1] [0 2]))

    ; 4: 2x2 L
    (mcreate [2 2] '([0 0] [0 1] [1 0]))
    (mcreate [2 2] '([0 0] [1 0] [1 1]))
    (mcreate [2 2] '([0 1] [1 0] [1 1]))
    (mcreate [2 2] '([0 0] [0 1] [1 1]))

    ; 5: 4x1 I
    (mcreate [1 4] '([0 0] [0 1] [0 2] [0 3]))
    (mcreate [4 1] '([0 0] [1 0] [2 0] [3 0]))

    ; 6: 2x2 square
    (mcreate [2 2] '([0 0] [0 1] [1 0] [1 1]))

    ; 7: 5x1 I
    (mcreate [1 5] '([0 0] [0 1] [0 2] [0 3] [0 4]))
    (mcreate [5 1] '([0 0] [1 0] [2 0] [3 0] [4 0]))

    ; 8: 3x3 square
    (mcreate [3 3] '([0 0] [0 1] [0 2] [1 0] [1 1] [1 2] [2 0] [2 1] [2 2]))

    ; 9: 3x3 L
    (mcreate [3 3] '([2 0] [1 0] [0 0] [0 1] [0 2]))
    (mcreate [3 3] '([0 0] [1 0] [2 0] [2 1] [2 2]))
    (mcreate [3 3] '([0 0] [0 1] [0 2] [1 2] [2 2]))
    (mcreate [3 3] '([2 0] [2 1] [2 2] [1 2] [0 2]))

  ])



;(get pieces 1)

;(count pieces)

;(doseq [p pieces]
;  (print "\n")
;  (mprint p))
