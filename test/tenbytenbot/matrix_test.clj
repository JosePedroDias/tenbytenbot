(ns tenbytenbot.matrix-test
  (:require [clojure.test :refer :all]
            [tenbytenbot.matrix :as m]))



(deftest mcreate-test
  (testing "simple creation"
    (is (=
         (m/mcreate [3 2])
         {:dims [3 2] :values [false false false false false false]})))

  (testing "creation with values"
    (is (=
         (m/mcreate [3 2] '([0 0] [2 1]))
         {:dims [3 2] :values [true false false false false true]}))))


(deftest mget-test
  (testing "get true"
    (is (=
         (m/mget (m/mcreate [3 2] '([0 0])) [0 0])
         true)))

  (testing "get false"
    (is (=
         (m/mget (m/mcreate [3 2] '([0 0])) [1 0])
         false))))


(deftest mset-test
  (testing "set true"
    (is (=
         (m/mget (m/mset (m/mcreate [3 2]) [0 0] true) [0 0])
         true))))


(deftest in-bounds-test
  (testing "x < 0"   (is (= (m/in-bounds (m/mcreate [3 2]) [-1  0]) false)))
  (testing "y < 0"   (is (= (m/in-bounds (m/mcreate [3 2]) [ 0 -1]) false)))
  (testing "x > w-1" (is (= (m/in-bounds (m/mcreate [3 2]) [ 3  0]) false)))
  (testing "y > h-1" (is (= (m/in-bounds (m/mcreate [3 2]) [ 0  2]) false)))
  (testing "ok"      (is (= (m/in-bounds (m/mcreate [3 2]) [ 0  0]) true))))


(deftest collides-with?-test
  (testing "inside, empty board = no"
    (is (=
          (m/collides-with?
            (m/mcreate [6 6])
            (m/mcreate [2 2] '([0 0] [0 1] [1 0]))
            [0 0])
         false)))
  (testing "inside, not empty board = yes"
    (is (=
          (m/collides-with?
            (m/mcreate [6 6] '([0 0]))
            (m/mcreate [2 2] '([0 0] [0 1] [1 0]))
            [0 0])
         true)))
  (testing "outside, empty board = yes"
    (is (=
          (m/collides-with?
            (m/mcreate [6 6])
            (m/mcreate [2 2] '([0 0] [0 1] [1 0]))
            [-1 0])
         true)))
  (testing "outside, empty board = yes"
    (is (=
          (m/collides-with?
            (m/mcreate [6 6])
            (m/mcreate [2 2] '([0 0] [0 1] [1 0]))
            [5 0])
         true)))
  )


(deftest glue-test
  (testing "I2 onto empty 3x3 board"
    (is (=
         (m/glue
           (m/mcreate [3 3])
           (m/mcreate [2 2] '([0 0] [1 0]))
           [0 0])
         {:dims [3 3] :values [true true false false false false false false false]}))))


(deftest is-column-filled?-test
    (testing "filled x=1 T"
      (is (=
           (m/is-column-filled? (m/mcreate [10 10] (map (fn [y] [1 y]) (range 10))) 1)
           true)))
    (testing "filled x=1 F #1"
      (is (=
           (m/is-column-filled? (m/mcreate [10 10] (map (fn [y] [2 y]) (range 10))) 1)
           false)))
    (testing "filled x=1 F #2"
      (is (=
           (m/is-column-filled? (m/mcreate [10 10] (map (fn [y] [1 y]) (range 9))) 1)
           false))))


(deftest is-row-filled?-test
    (testing "filled y=1 T"
      (is (=
           (m/is-row-filled? (m/mcreate [10 10] (map (fn [x] [x 1]) (range 10))) 1)
           true)))
    (testing "filled y=1 F #1"
      (is (=
           (m/is-row-filled? (m/mcreate [10 10] (map (fn [x] [x 2]) (range 10))) 1)
           false)))
    (testing "filled y=1 F #2"
      (is (=
           (m/is-row-filled? (m/mcreate [10 10] (map (fn [x] [x 1]) (range 9))) 1)
           false))))


(deftest list-filled-rows-and-columns-test
  (testing "empty"
    (is (=
          (m/list-filled-rows-and-columns (m/mcreate [2 2]))
          {:rows '() :cols '()})))
  (testing "none"
    (is (=
          (m/list-filled-rows-and-columns (m/mcreate [2 2] '([0 0] [1 1])))
          {:rows '() :cols '()})))
  (testing "rows=0"
    (is (=
          (m/list-filled-rows-and-columns (m/mcreate [2 2] '([0 0] [1 0])))
          {:rows '(0) :cols '()})))
  (testing "cols=0"
    (is (=
          (m/list-filled-rows-and-columns (m/mcreate [2 2] '([0 0] [0 1])))
          {:rows '() :cols '(0)})))
  (testing "rows=0, cols=1"
    (is (=
          (m/list-filled-rows-and-columns (m/mcreate [2 2] '([0 0] [1 0] [1 1])))
          {:rows '(0) :cols '(1)}))))


(deftest count-filled-rows-or-columns-test
  (testing "empty"
    (is (=
          (m/count-filled-rows-or-columns (m/mcreate [2 2]))
          0)))
  (testing "none"
    (is (=
          (m/count-filled-rows-or-columns (m/mcreate [2 2] '([0 0] [1 1])))
          0)))
  (testing "rows=0"
    (is (=
          (m/count-filled-rows-or-columns (m/mcreate [2 2] '([0 0] [1 0])))
          1)))
  (testing "cols=0"
    (is (=
          (m/count-filled-rows-or-columns (m/mcreate [2 2] '([0 0] [0 1])))
          1)))
  (testing "rows=0, cols=1"
    (is (=
          (m/count-filled-rows-or-columns (m/mcreate [2 2] '([0 0] [1 0] [1 1])))
          2))))



(deftest wipe-filled-rows-and-columns-test
  (testing "empty"
    (is (=
          (m/wipe-filled-rows-and-columns (m/mcreate [2 2]))
          (m/mcreate [2 2]))))
  (testing "none"
    (is (=
          (m/wipe-filled-rows-and-columns (m/mcreate [2 2] '([0 0] [1 1])))
          (m/mcreate [2 2] '([0 0] [1 1])))))
  (testing "rows=0"
    (is (=
          (m/wipe-filled-rows-and-columns (m/mcreate [2 2] '([0 0] [1 0])))
          (m/mcreate [2 2]))))
  (testing "cols=0"
    (is (=
          (m/wipe-filled-rows-and-columns (m/mcreate [2 2] '([0 0] [0 1])))
          (m/mcreate [2 2]))))
  (testing "rows=0, cols=1"
    (is (=
          (m/wipe-filled-rows-and-columns (m/mcreate [2 2] '([0 0] [1 0] [1 1])))
          (m/mcreate [2 2])))))
