(ns relationships.t-core
  (:require [clojure.java.io :as io])
  (:use midje.sweet)
  (:use [relationships.core]))

(def test-path "test/relationships.txt")

(defn create-relationship-file [] (spit test-path "John,Sue\nMary,Fred\nMary,Sue\nSue,Sam"))
(defn delete-relationship-file [] (io/delete-file test-path))


(facts "about `relationship` core functions"

       (fact "can parse simple relationships"
             (parse-relation nil) => nil
             (parse-relation "") => nil
             (parse-relation "John,Sue") => {:parent "John" :child "Sue"}
             (parse-relation "Mary,Fred") => {:parent "Mary" :child "Fred"})

       (fact "can parse multi-line relationships"
             (parse-relations nil) => []
             (parse-relations "John,Sue\nMary,Fred\n") => [{:parent "John" :child "Sue"}
                                                           {:parent "Mary" :child "Fred"}]
             (parse-relations "John,Sue\nMary,Fred\nMary,Sue\nSue,Sam")
             => [{:parent "John" :child "Sue"}
                 {:parent "Mary" :child "Fred"}
                 {:parent "Mary" :child "Sue"}
                 {:parent "Sue" :child "Sam"}])

       (against-background [(before :facts (create-relationship-file))
                            (after :facts (delete-relationship-file))]
                           (fact "can read in files"
                                 (parse-relations-file nil) => []
                                 (parse-relations-file "file-doesnt-exist") => []
                                 (parse-relations-file test-path)
                                 => [{:parent "John" :child "Sue"}
                                     {:parent "Mary" :child "Fred"}
                                     {:parent "Mary" :child "Sue"}
                                     {:parent "Sue" :child "Sam"}])))
