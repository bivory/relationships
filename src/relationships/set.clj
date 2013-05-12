(ns relationships.set
  (:require [relationships.core :as relationships]))

(defrecord FamilyTreeSet [relationships]
  relationships/FamilyTree
  (isParent? [parent child] true)
  (isSibling? [person1 person2] true)
  (isAncestor? [person1 person2] true)
  (isRelated? [person1 person2] true))

(defn create-family-tree
  "Creates a FamilyTreeSet from relationships in the format
   [{:parent 'John' :child 'Sue'} {:parent 'Mary' :child 'Fred'}]"
  [relationships]
  (FamilyTreeSet. {}))

