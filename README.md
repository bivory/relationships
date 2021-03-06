# relationships

Relationships allows you to model unrealistic family trees, just like you always wanted to! It uses [Midje](https://github.com/marick/Midje/) and was built to try out TDD.

## Cloning the repository

Run `git clone https://github.com/bivory/relationships.git` to clone the repository. Follow the installation steps in the following sections to setup [Clojure](http://clojure.org/), [Leiningen](http://leiningen.org/) and the dependencies.

## Ubuntu Installation

`wget https://raw.github.com/technomancy/leiningen/stable/bin/lein`
`chmod +x lein`
`./lein` will install Leiningen and Clojure.
`./lein deps` will install the dependencies.

`./lein midje` will run all tests.

## OS X Installation

`brew install leiningen` using [Homebrew](http://brew.sh/) or follow the Ubuntu steps for a standalone installation.

## How to run the tests

`lein midje` will run all tests.

`lein midje namespace.*` will run only tests beginning with "namespace.".

`lein midje :autotest` will run all the tests indefinitely. It sets up a
watcher on the code files. If they change, only the relevant tests will be
run again.

## License

Copyright © 2013 Bryan Ivory

Distributed under the Eclipse Public License, the same as Clojure.
