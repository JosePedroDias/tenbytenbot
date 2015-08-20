# tenbytenbot

A bot to play the [tenbyten](https://github.com/JosePedroDias/tenbyten) game via its HTTP interface.
I'm making this project as a way of learning Clojure.



## Usage

1. via clojure

    $ lein run

2. via clojurescript

    $ python -m SimpleHTTPServer 5566 &
    $ lein cljsbuild once

visit <http://127.0.0.1:5566/test-cljs.html> (open the console)

3. now, on your browser

visit <http://rawgit.com/JosePedroDias/tenbytenbot/master/test-cljs.html> (open the console)



## Roadmap

* [x] porting whole tenbyten operations
* [x] make dumbest playing bot (random from valid moves)
* [x] compute board after lines have been removed
* [x] make bot run on the browser via `cljsbuild`
* [ ] **make bot elect moves based on 1 play heuristic (> number of empty cells; < number of islands)**
* [ ] make bot elect moves based on 3 plays heuristic
* [ ] make the bot enter highscores
