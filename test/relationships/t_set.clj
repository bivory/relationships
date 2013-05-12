(ns relationships.t-set
  (:require [relationships.t-core :as t-core])
  (:use midje.sweet)
  (:use [relationships.core])
  (:use [relationships.set]))

(facts "about the `FamilyTreeSet` protocol implementation."
       (fact "can create relationships"
             (create-family-tree nil) => (->FamilyTreeSet nil nil)
             (create-family-tree []) => (->FamilyTreeSet nil nil))

       (let [relation [{:parent "John" :child "Sue"}
                       {:parent "John" :child "Fred"}
                       {:parent "Pat" :child "Mary"}
                       {:parent "Mary" :child "Fred"}
                       {:parent "George" :child "Tom"}
                       {:parent "Tom" :child "George"}]
             fts (create-family-tree relation)]
         (fact "can query relations about isParent?"

               "and handle people that aren't in the family tree"
               (isParent? fts nil nil) => false
               (isParent? fts "Does not exist" "Sue") => false
               (isParent? fts "Mary" "Does not exist") => false

               "and people that are parents of the child"
               (isParent? fts "John" "Sue") => true
               (isParent? fts "Mary" "Fred") => true

               "and people that aren't parents child"
               (isParent? fts "Sue" "John") => false)

         (fact "can query relations about isSibling?"

               "and handle people that aren't in the family tree"
               (isSibling? fts nil nil) => false
               (isSibling? fts "Does not exist" "Sue") => false
               (isSibling? fts "Fred" "Does not exist") => false

               "and people that aren't siblings"
               (isSibling? fts "John" "Sue") => false
               (isSibling? fts "Tom" "Fred") => false

               "and people that are siblings"
               (isSibling? fts "Fred" "Sue") => true)

         (fact "can query relations about isAncestor?"

               "and handle people that aren't in the family tree"
               (isAncestor? fts nil nil) => false
               (isAncestor? fts "Does not exist" "Sue") => false

               "Mary is not related to Sue"
               (isAncestor? fts "Mary" "Sue") => false

               "Fred and Sue are siblings"
               (isAncestor? fts "Fred" "Sue") => false
               (isAncestor? fts "Sue" "Fred") => false

               "John is Fred's parent"
               (isAncestor? fts "John" "Fred") => true
               (isAncestor? fts "Fred" "John") => false

               "Pat is Fred's grandparent"
               (isAncestor? fts "Pat" "Fred") => true

               "and handle cycles; Tom is the parent of George who is the parent of Tom"
               (isAncestor? fts "Tom" "George") => true
               (isAncestor? fts "George" "Tom") => true)

         (fact "can query relations about isRelated?"

               "and handle people that aren't in the family tree"
               (isRelated? fts nil nil) => false
               (isRelated? fts "Does not exist" "Sue") => false

               "Tom and Fred have no ancestors in common"
               (isRelated? fts "Tom" "Fred") => false

               "Mary is Fred's parent"
               (isRelated? fts "Mary" "Fred") => true
               (isRelated? fts "Fred" "Mary") => true

               "Fred and Sue are siblings"
               (isRelated? fts "Fred" "Sue") => true

               "Pat is Fred's grandparent"
               (isRelated? fts "Fred" "Pat") => true
               (isRelated? fts "Pat" "Fred") => true

               "John and Mary have a child, Fred in common, but no ancestors in common"
               (isRelated? fts "John" "Mary") => false)

       (against-background [(before :facts (t-core/create-relationship-file))
                            (after :facts (t-core/delete-relationship-file))]
                           (fact "can create relationships from a file"
                                 (create-family-tree-from-file nil) => (->FamilyTreeSet nil nil)
                                 (create-family-tree-from-file "") => (->FamilyTreeSet nil nil)
                                 (create-family-tree-from-file t-core/test-path)
                                 => (->FamilyTreeSet {"Fred" #{"Mary"}, "Sam" #{"Sue"}, "Sue" #{"John" "Mary"}}
                                                     {"Fred" #{"Sue"}, "Sam" #{}, "Sue" #{"Fred"}})))

       (against-background [(before :facts (t-core/create-large-relationship-file))
                            (around :checks (let [fts (create-family-tree-from-file t-core/test-path)] ?form))
                            (after :facts (t-core/delete-large-relationship-file))]
                           (fact "can create relationships from the large file"
                                 (create-family-tree-from-file t-core/test-path)
                                 => (->FamilyTreeSet {"Chris" #{"John"}
                                                      "Fred" #{"Pat"}
                                                      "George" #{"Pat"}
                                                      "John" #{"George" "Mary" "Sue"}
                                                      "Mary" #{"George"}
                                                      "Sam" #{"Peggy"}}
                                                     {"Chris" #{}
                                                      "Fred" #{"George"}
                                                      "George" #{"Fred"}
                                                      "John" #{"Mary"}
                                                      "Mary" #{"John"}
                                                      "Sam" #{}}))

                           (fact "can query facts from the large file"

                                 "Mary is the parent of John"
                                 (isParent? fts "Mary" "John") => true

                                 "Sue is the grandparent of Chris"
                                 (isParent? fts "Sue" "Chris") => false

                                 "Mark ys the mother and sister of John"
                                 (isSibling? fts "Mary" "John") => true

                                 "Pat is the great-grandparent of Chris"
                                 (isAncestor? fts "Pat" "Chris") => true

                                 "Sam is not related to John"
                                 (isRelated? fts "Sam" "John") => false

                                 "Fred and John have Pat as a common ancestor"
                                 (isRelated? fts "Fred" "John") => true))))
