(defproject tenbytenbot "0.1.0-SNAPSHOT"
  :description "tenbytenbot is a bot for playing the tenbyten game via HTTP"
  :url "http://github.com/JosePedroDias/tenbytenbot"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [
    [org.clojure/clojure "1.7.0"]
    [clj-http "2.0.0"]
    [org.clojure/data.json "0.2.6"]
    [org.clojure/clojurescript "1.7.48" :exclusions [org.apache.ant/ant]]
  ]
  :plugins [
    [lein-cljsbuild "1.0.6"]
  ]
  :main ^:skip-aot tenbytenbot.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}}
  :cljsbuild {
    :builds [
      {:id "dev"
       :source-paths ["src"]
       :compiler {:output-to "dist/tenbytenbot.js"
                  :optimizations :whitespace
                  :pretty-print true}}
      {:id "min"
       :source-paths ["src"]
       :compiler {:output-to "dist/tenbytenbot.min.js"
                  :optimizations :advanced
                  :pretty-print false}}
]})
