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


(deftest collides-with-test
  (testing "inside, empty board = no"
    (is (=
          (m/collides-with
            (m/mcreate [6 6])
            (m/mcreate [2 2] '([0 0] [0 1] [1 0]))
            [0 0])
         false)))
  (testing "inside, not empty board = yes"
    (is (=
          (m/collides-with
            (m/mcreate [6 6] '([0 0]))
            (m/mcreate [2 2] '([0 0] [0 1] [1 0]))
            [0 0])
         true)))
  (testing "outside, empty board = yes"
    (is (=
          (m/collides-with
            (m/mcreate [6 6])
            (m/mcreate [2 2] '([0 0] [0 1] [1 0]))
            [-1 0])
         true)))
  (testing "outside, empty board = yes"
    (is (=
          (m/collides-with
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
