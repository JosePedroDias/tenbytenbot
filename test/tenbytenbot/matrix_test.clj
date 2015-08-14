(ns tenbytenbot.matrix-test
  (:require [clojure.test :refer :all]
            [tenbytenbot.matrix :as m]))



(deftest mcreate-test
  (testing "simple creation"
    (is (=
         (m/mcreate [3 2])
         {:dims [3 2] :values [false false false false false false]}))))
