(ns relationships.core)

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
   John,Sue\n => {:parent 'John' :child 'Sue'}"
  [relation-string]
  {:parent "John" :child "Sue"})
