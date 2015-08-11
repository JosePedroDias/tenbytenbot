(ns tenbytenbot.matrix-test
  (:require
    [clojure.test       :refer :all]
    [tenbytenbot.matrix :refer :all]))

(deftest mcreate
  (testing "simple creation"
    (is (= (mcreate [3 2] {:dims [3 2] :values [false false false false false false]})))))
