(ns relationships.t-set
  (:use midje.sweet)
  (:use [relationships.set]))

(facts "about the `FamilyTreeSet` protocol implementation."
  (fact "can create relationships"
        (create-family-tree nil) => {:relationships {}}
        (create-family-tree []) => {:relationships {}}))

