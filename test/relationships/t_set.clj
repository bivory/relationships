(ns relationships.t-set
  (:require [relationships.t-core :as t-core])
  (:use midje.sweet)
  (:use [relationships.core])
  (:use [relationships.set]))

(facts "about the `FamilyTreeSet` protocol implementation."
       (fact "can create relationships"
             (create-family-tree nil) => (->FamilyTreeSet nil)
             (create-family-tree []) => (->FamilyTreeSet nil))

       (let [relation [{:parent "John" :child "Sue"}
                       {:parent "John" :child "Fred"}
                       {:parent "Mary" :child "Fred"}]
             fts (create-family-tree relation)]
         (fact "can query relations about isParent?"
               (isParent? fts nil nil) => false
               (isParent? fts "Does not exist" "Sue") => false
               (isParent? fts "Mary" "Does not exist") => false
               (isParent? fts "John" "Sue") => true
               (isParent? fts "Mary" "Fred") => true
               (isParent? fts "Sue" "John") => false)

         (fact "can query relations about isSibling?"
               (isSibling? fts nil nil) => false
               (isSibling? fts "Does not exist" "Sue") => false
               (isSibling? fts "Fred" "Does not exist") => false
               (isSibling? fts "John" "Sue") => false
               (isSibling? fts "Fred" "Sue") => true))

       (against-background [(before :facts (t-core/create-relationship-file))
                            (after :facts (t-core/delete-relationship-file))]
                           (fact "can create relationships from a file"
                                 (create-family-tree-from-file nil) => (->FamilyTreeSet nil)
                                 (create-family-tree-from-file "") => (->FamilyTreeSet nil)
                                 (create-family-tree-from-file t-core/test-path)
                                 => (->FamilyTreeSet {"Fred" #{"Mary"}, "Sam" #{"Sue"}, "Sue" #{"John" "Mary"}})))

       (against-background [(before :facts (t-core/create-large-relationship-file))
                            (after :facts (t-core/delete-large-relationship-file))]

                           (fact "can create relationships from the large file"
                                 (create-family-tree-from-file t-core/test-path)
                                 => (->FamilyTreeSet {"Chris" #{"John"}
                                                      "Fred" #{"Pat"}
                                                      "George" #{"Pat"}
                                                      "John" #{"George" "Mary" "Sue"}
                                                      "Mary" #{"George"}
                                                      "Sam" #{"Peggy"}}))))

