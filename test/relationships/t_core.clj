(ns relationships.t-core
  (:require [clojure.java.io :as io])
  (:use midje.sweet)
  (:use [relationships.core]))

(def test-path "test/relationships.txt")

(defn create-relationship-file [] (spit test-path "John,Sue\nMary,Fred\nMary,Sue\nSue,Sam"))
(defn delete-relationship-file [] (io/delete-file test-path))
(def relationship-file-output [{:parent "John" :child "Sue"}
                               {:parent "Mary" :child "Fred"}
                               {:parent "Mary" :child "Sue"}
                               {:parent "Sue" :child "Sam"}])

(defn create-large-relationship-file [] (spit test-path (str "Mary,John\n"
                                                             "George,Mary\n"
                                                             "George,John\n"
                                                             "Sue,John\n"
                                                             "John,Chris\n"
                                                             "Pat,George\n"
                                                             "Pat,Fred\n"
                                                             "Peggy,Sam")))
(defn delete-large-relationship-file [] (io/delete-file test-path))
(def large-relationship-file-output [{:child "John", :parent "Mary"}
                                     {:child "Mary", :parent "George"}
                                     {:child "John", :parent "George"}
                                     {:child "John", :parent "Sue"}
                                     {:child "Chris", :parent "John"}
                                     {:child "George", :parent "Pat"}
                                     {:child "Fred", :parent "Pat"}
                                     {:child "Sam", :parent "Peggy"}])


(facts "about `relationship` core functions"

       (fact "can parse simple relationships"
             (parse-relation nil) => nil
             (parse-relation "") => nil
             (parse-relation "John,Sue") => {:parent "John" :child "Sue"}
             (parse-relation "Mary,Fred") => {:parent "Mary" :child "Fred"})

       (fact "can parse multi-line relationships"
             (parse-relations nil) => []
             (parse-relations "") => []
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
                                 (parse-relations-file test-path) => relationship-file-output))

       (against-background [(before :facts (create-large-relationship-file))
                            (after :facts (delete-large-relationship-file))]
                           (fact "can read in large files"
                                 (parse-relations-file test-path) => large-relationship-file-output)))
