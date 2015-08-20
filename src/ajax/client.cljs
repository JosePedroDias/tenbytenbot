(ns ajax.client)


(defn ^:export sync [url]
  "synchronous AJAX request"
  (let [xhr (js/XMLHttpRequest.)]
    (.open xhr "GET" url false)
    (.send xhr nil)
    (.-responseText xhr)))


;(js/alert (sync "http://stage.sl.pt:3000/active-sessions"))
