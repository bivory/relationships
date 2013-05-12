(ns relationships.core
  (:require [clojure.string :as s]))

(defprotocol FamilyTree
  "A protocol that allows the following queries about a family structure:"
  (isParent?
    [parent child]
    "This returns true if the parent is a parent of the child.")
  (isSibling?
    [person1 person2]
    "This returns true if person1 and person2 have at least one common parent.")
  (isAncestor?
    [person1 person2]
    "This returns true if person1 is an ancestor of person2.")
  (isRelated?
    [person1 person2]
    "This returns true if person1 and person2 have at least one common ancestor.
     This would mean that siblings satisfy this relations as well."))

(defn parse-relation
  "Parses a simple parent child relation of the form:
   John,Sue => {:parent 'John' :child 'Sue'}"
  [relation-string]
  (let [safe-string (if (nil? relation-string) "" relation-string)
        [parent child] (s/split safe-string #",")]
    (if (or (nil? parent) (nil? child))
      nil
      {:parent parent :child child})))

(defn parse-relations
  "Parses multiple parent child relation of the form:
   John,Sue\nMary,Fred\n
   => [{:parent 'John' :child 'Sue'} {:parent 'Mary' :child 'Fred'}]"
  [relation-strings]
  (if (nil? relation-strings)
    []
    (let [relations (s/split-lines relation-strings)]
      (map parse-relation relations))))

(defn parse-relations-file
  "Parses multiple parent child relation of the form:
   John,Sue\nMary,Fred\n
   => [{:parent 'John' :child 'Sue'} {:parent 'Mary' :child 'Fred'}]
   from a file."
  [file-path]
  (if (nil? file-path)
    []
    (try
      (-> (slurp file-path) (parse-relations))
      (catch java.io.FileNotFoundException e []))))
