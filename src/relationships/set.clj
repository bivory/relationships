(ns relationships.set
  (:require [clojure.set :as cset])
  (:require [relationships.core :as relationships]))

(defn- get-parents
  "Gets the parents of the child."
  [relations child]
  (get-in relations [:child->parents child]))

(defrecord FamilyTreeSet [child->parents]
  relationships/FamilyTree

  (isParent? [relations parent child]
    (not (empty? (get (get-parents relations child) parent))))

  (isSibling? [relations person1 person2]
    (let [p1-parents (get-parents relations person1)
          p2-parents (get-parents relations person2)]
      (not (empty? (cset/intersection p1-parents p2-parents)))))

  (isAncestor? [relations ancestor? descendant?]
    (loop [to-check #{descendant?}
           checked #{}]
      (let [check-person (first to-check)
            to-check-next (-> to-check
                              (cset/union (get-parents relations check-person))
                              (disj check-person))
            checked-next (conj checked check-person)]
        (if (relationships/isParent? relations ancestor? check-person)
          true
          (if (nil? check-person)
            false
            (recur to-check-next checked-next))))))

  (isRelated? [relations person1 person2]
    (or (relationships/isParent? relations person1 person2)
        (relationships/isParent? relations person2 person1)
        (relationships/isSibling? relations person1 person2)
        (relationships/isAncestor? relations person1 person2)
        (relationships/isAncestor? relations person2 person1))))

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
