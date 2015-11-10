(ns ntljr.definition
  (:require [schema.core :as scm]))

;;
;; Datastructure described by a Definition schema below is a central element
;; of the system: it represents a certain notion as a unit of information,
;; similar to a definition from the dictionary, hence the name;
;;

(def Definition
  "A schema for a definition data structure."
  {:rating    scm/Int     ;; integral value that shows popularity of the definition
   :author    scm/Str     ;; username of the person who wrote it
   :crdate    scm/Str     ;; date of creation
   :name      scm/Str     ;; notion that is being defined
   :text      scm/Str     ;; markdown-formated payload
   :resources [scm/Int]}) ;; static resources used in markdown (pictures etc.)

(defn validate-definition
  "Validation logic to check if definition is consistent."
  [defmap]
  (scm/validate Definition defmap))

(defn make-definition
  "Constructor for a definition object. Definition is represented
   as a simple map to make system more data-centric."
  [author crdate name text resources]
  (validate-definition
   {:rating 0
    :author author
    :crdate crdate
    :name name
    :text text
    :resources resources}))

(defn definition-name [definition]
  (:name definition))

(defn definition-rating [definition]
  (:rating definition))

(defn definition-author [definition]
  (:author definition))

(defn definition-crdate [definition]
  (:crdate definition))

(defn definition-text [definition]
  (:text definition))

(defn definition-resources [definition]
  (:resources definition))

(defn definition-header [definition]
  (select-keys definition [:rating :author :crdate :name]))

(defn definition-seed [definition]
  (select-keys definition [:name :text :resources]))
