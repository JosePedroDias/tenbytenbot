(ns tenbytenbot.server-io
  (:require
    ;#?(:clj  [clj-http.client   :as client]
    ;   :cljs [ajax.client       :as client])
    [ajax.client       :as client])
    ;#?(:clj [clojure.data.json :as j]))
)


(def GAME_HOST "http://stage.sl.pt:3000")



(defn- io [endpoint]
  "auxiliary function to perform actions on the server"
  (js->clj
    (.parse js/JSON
      (client/sync (str GAME_HOST endpoint)))
    :keywordize-keys true))


(defn new-game []
  "creates new game session, returning it"
  (let [r (io "/new-game")]
    (println "type" (type r))
    (println ":id" (:id r))
    (println "id" (get r "id"))
    r))
  ;(io "/new-game"))


(defn play [session-id step slot-index x y]
  "attempts to play the given command and returns updated state"
  (io (str "/play/" session-id "/" step "/" slot-index "/" x "/" y)))


(defn highscore [session-id email name]
  "converts an ended state into a high score (email is to use gravatar avatar)"
  (io (str "/highscore/" session-id "/" email "/" name)))

;; -----


(defn stats []
  "returns server stats"
  (io "/stats"))
