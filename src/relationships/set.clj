(ns relationships.set
  (:require [clojure.set :as cset])
  (:require [relationships.core :as relationships]))

(defn- get-parents
  "Gets the parents of the child."
  [relations child]
  (get-in relations [:child->parents child]))

(defn- get-siblings
  "Gets the siblings of the person."
  [relations person]
  (get-in relations [:person->siblings person]))

(defrecord FamilyTreeSet [child->parents person->siblings]
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
            checked-next (conj checked check-person)
            to-check-next (-> to-check
                              (cset/union (get-parents relations check-person))
                              (cset/difference checked-next))]
        (if (relationships/isParent? relations ancestor? check-person)
          true
          (if (nil? check-person)
            false
            (recur to-check-next checked-next))))))

  (isRelated? [relations person1 person2]
    (loop [to-check #{person1}
           checked #{}]
      (let [check-person (first to-check)
            checked-next (conj checked check-person)
            to-check-next (-> to-check
                              (cset/union (get-parents relations check-person))
                              (cset/union (get-siblings relations check-person))
                              (cset/difference checked-next))]
        (if (nil? check-person)
          false
          (if (or (= check-person person2)
                  (relationships/isParent? relations check-person person2)
                  (relationships/isAncestor? relations check-person person2)
                  (relationships/isSibling? relations check-person person2))
            true
            (recur to-check-next checked-next)))))))

(defn create-family-tree
  "Creates a FamilyTreeSet from relationships in the format
   [{:parent 'John' :child 'Sue'} {:parent 'Mary' :child 'Fred'}]"
  [relationships]
  (let [child->parents (->> (map (fn [{:keys [parent child]}] {child #{parent}}) relationships)
                            (apply merge-with cset/union))
        parent->children (->> (map (fn [{:keys [parent child]}] {parent #{child}}) relationships)
                              (apply merge-with cset/union))
        person->siblings (->> (map (fn [[parent children]]
                                     (map (fn [c] {c (disj children c)}) children))
                                   parent->children)
                              (flatten)
                              (apply merge-with cset/union))]
    (FamilyTreeSet. child->parents person->siblings)))

(defn create-family-tree-from-file
  "Creates a FamilyTreeSet from relationships in the format
   [{:parent 'John' :child 'Sue'} {:parent 'Mary' :child 'Fred'}]
   read in from a file"
  [file-path]
  (-> (relationships/parse-relations-file file-path)
      (create-family-tree)))
