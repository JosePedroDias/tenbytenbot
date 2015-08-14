(ns tenbytenbot.core
  (:gen-class)
  (:require [tenbytenbot.server-io :as io]))



(defn -main
  "main fn"
  [& args]
  (println "starting")
  (println (io/new-game)))
