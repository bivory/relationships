(ns relationships.t-set
  (:require [relationships.t-core :as t-core])
  (:use midje.sweet)
  (:use [relationships.core])
  (:use [relationships.set]))

(facts "about the `FamilyTreeSet` protocol implementation."
       (fact "can create relationships"
             (create-family-tree nil) => {:relationships {}}
             (create-family-tree []) => {:relationships {}})

       (let [relation [{:parent "John" :child "Sue"} {:parent "Mary" :child "Fred"}]
             fts (create-family-tree relation)]
         (fact "can query relations about isParent?"
               (isParent? fts "John" "Sue") => true))

       (against-background [(before :facts (t-core/create-relationship-file))
                            (after :facts (t-core/delete-relationship-file))]
                           (fact "can create relationships from a file"
                                 (create-family-tree-from-file nil) => {:relationships {}}
                                 (create-family-tree-from-file "") => {:relationships {}})))

