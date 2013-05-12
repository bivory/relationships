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

(defn create-family-tree-from-file
  "Creates a FamilyTreeSet from relationships in the format
   [{:parent 'John' :child 'Sue'} {:parent 'Mary' :child 'Fred'}]
   read in from a file"
  [file-path]
  (-> (relationships/parse-relations-file file-path)
      (create-family-tree)))
