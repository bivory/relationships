(ns relationships.t-core
  (:use midje.sweet)
  (:use [relationships.core]))

(facts "about `relationship` core functions"

  (fact "can parse simple relationships"
        (parse-relation "John,Sue") => {:parent "John" :child "Sue"}
        (parse-relation "Mary,Fred") => {:parent "Mary" :child "Fred"})

  (fact "can parse multi-line relationships"
        (parse-relations "John,Sue\nMary,Fred\n") => [{:parent "John" :child "Sue"}
                                                      {:parent "Mary" :child "Fred"}])
  (fact "can read in files"))

