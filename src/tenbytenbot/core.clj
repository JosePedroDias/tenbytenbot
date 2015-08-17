(ns tenbytenbot.core
  (:gen-class)
  (:require
    [tenbytenbot.tactics :as t]))




(defn -main
  "main fn"
  [& args]

  (println "starting")

  (t/go))
