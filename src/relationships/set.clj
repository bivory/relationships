(ns relationships.set
  (:require [clojure.set :as cset])
  (:require [relationships.core :as relationships]))

(defrecord FamilyTreeSet [child->parents]
  relationships/FamilyTree

  (isParent? [relations parent child]
    (not (nil? (get-in relations [:child->parents child parent]))))

  (isSibling? [relations person1 person2]
    (let [p1-parents (get-in relations [:child->parents person1])
          p2-parents (get-in relations [:child->parents person2])]
      (not (nil? (cset/intersection p1-parents p2-parents)))))

  (isAncestor? [relations person1 person2]
    true)

  (isRelated? [relations person1 person2]
    true))

(defn create-family-tree
  "Creates a FamilyTreeSet from relationships in the format
   [{:parent 'John' :child 'Sue'} {:parent 'Mary' :child 'Fred'}]"
  [relationships]
  (let [parent-child (map (fn [{:keys [parent child]}] {child #{parent}}) relationships)
        parent-relations (apply merge-with cset/union parent-child)]
    (FamilyTreeSet. parent-relations)))

(defn create-family-tree-from-file
  "Creates a FamilyTreeSet from relationships in the format
   [{:parent 'John' :child 'Sue'} {:parent 'Mary' :child 'Fred'}]
   read in from a file"
  [file-path]
  (-> (relationships/parse-relations-file file-path)
      (create-family-tree)))
