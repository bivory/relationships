(ns relationships.t-core
  (:use midje.sweet)
  (:use [relationships.core]))

(facts "about `relationship` core functions"
  (fact "can parse simple relationships"
        (parse-relation "John,Sue\n") => {:parent "John" :child "Sue"}
        (parse-relation "Mary,Fred\n") => {:parent "Mary" :child "Fred"})
  (fact "can read in files"))

