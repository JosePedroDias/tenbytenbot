(ns tenbytenbot.matrix-test)

(require '[clojure.test])
(refer 'clojure.test)

(load "matrix")
(refer 'tenbytenbot.matrix)



(deftest mcreate-test
  (testing "simple creation"
    (is (=
         (mcreate [3 2])
         {:dims [3 2] :values [false false false false false false]}))))
