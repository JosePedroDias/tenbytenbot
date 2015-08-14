(ns tenbytenbot.core
  (:gen-class))

(require 'tenbytenbot.server-io)



(defn -main
  "main fn"
  [& args]
  (println "starting")
  (println (tenbytenbot.server-io/new-game)))
