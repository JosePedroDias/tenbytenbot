# tenbytenbot

trying to do a bot to play the [tenbyten](https://github.com/JosePedroDias/tenbyten) game via its HTTP interface

**work in progress**



## Installation

fetch the standalone jar from the `dist` folder



## Usage

from source:

    $ lein run

or:

    $ lein uberjar
    $ java -jar tenbytenbot-0.1.0-standalone.jar



## Roadmap

* [x] porting whole tenbyten operations
* [x] make dumbest playing bot (random from valid moves)
* [x] compute board after lines have been removed
* [ ] **make bot elect moves based on 1 play heuristic (> number of empty cells; < number of islands)**
* [ ] make bot elect moves based on 3 plays heuristic
