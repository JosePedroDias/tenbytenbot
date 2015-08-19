(ns tenbytenbot.server-io
  (:require
    #?(:clj  [clj-http.client   :as client]
       :cljs [cljs-http.client  :as client])
    [clojure.data.json :as json]))



(def GAME_HOST "http://stage.sl.pt:3000")


(defn- io [endpoint]
  "auxiliary function to perform actions on the server"
  (json/read-str
    (get
      (client/get
         (str GAME_HOST endpoint)
         {:accept :json})
      :body) :key-fn keyword))


(defn new-game []
  "creates new game session, returning it"
  (io "/new-game"))


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
