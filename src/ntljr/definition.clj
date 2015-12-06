(ns ntljr.definition
  (:require [schema.core :as scm]))

;;
;; Datastructure described by a Definition schema below is a central element
;; of the system: it represents a certain notion as a unit of information,
;; similar to a definition from the dictionary, hence the name;
;;

(def Definition
  "A schema for a definition data structure."
  {:rating scm/Int  ;; integral value that shows popularity of the definition
   :author scm/Str  ;; username of the person who wrote it
   :crdate scm/Str  ;; date of creation
   :name scm/Str    ;; notion that is being defined
   :text scm/Str    ;; markdown-formated payload
   :image scm/Str}) ;; illustration

(defn validate-definition
  "Validation logic to check if definition is consistent."
  [defmap]
  (scm/validate Definition defmap))

(defn make-definition
  "Constructor for a definition object. Definition is represented
   as a simple map to make system more data-centric."
  [author date name text image]
  (validate-definition
   {:rating 0 :author author :crdate date :name name :text text :image image}))

(defn definition-metadata [definition]
  (select-keys definition [:rating :author :crdate :name]))
