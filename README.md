# relationships

Relationships allows you to model unrealistic family trees, just like you always wanted to! It uses [Midje](https://github.com/marick/Midje/) and was built to try out TDD.

## How to run the tests

`lein midje` will run all tests.

`lein midje namespace.*` will run only tests beginning with "namespace.".

`lein midje :autotest` will run all the tests indefinitely. It sets up a
watcher on the code files. If they change, only the relevant tests will be
run again.

## License

Copyright © 2013 Bryan Ivory

Distributed under the Eclipse Public License, the same as Clojure.
