(ns ntljr.definition
  (:require [schema.core :as scm]))

;;
;;    Core data structure that represents unit of information in system
;;
;; TODO: REWRITE:
;;    Definition is a map with five fields: 'header' and 'seed'.
;; 'header' contains metadata about the definition and 'seed' is
;; the payload. Header is a metadata piece of the definition, abstracted
;; out to allow more flexible searches: 'date', 'rating' & 'author'.
;; 'cr-date' represents the date of creation. Seed is the payload of the
;; definition - it contains textual data in markdown format and static
;; resources (images etc).
;;

(def Definition
  "A schema for a definition data structure."
  {:rating    scm/Int     ;; integral value that represents 
   :author    scm/Str     ;; username of the person who wrote it
   :crdate    scm/Str     ;; date of creation
   :text      scm/Str     ;; markdown-formated payload
   :resources [scm/Int]}) ;; static resources used in markdown (pictures etc.)

(defn validate-definition
  "Validation logic to check if definition is consistent."
  [defmap]
  (scm/validate Definition defmap))

(defn make-definition
  "Constructor for a definition object.
   Definition is represented as a simple map
   to make system more data-centric."
  [author crdate text resources]
  (validate-definition
   {:rating 0               ;; integral value that represents 
    :author author          ;; username of the person who wrote it
    :crdate crdate          ;; date of creation
    :text text              ;; markdown-formated payload
    :resources resources})) ;; static resources used in markdown (pictures etc.)

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
  (select-keys definition [:rating :author :crdate]))

(defn definition-seed [definition]
  (select-keys definition [:text :resources]))
