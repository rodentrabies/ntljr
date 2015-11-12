(ns ntljr.storage.core
  "Use functions from imported storage subnamespace to create a db layer."
  (:require [ntljr.storage.mongodb :as db]))

;;
;; Operations for work with system's persistent storage (currently - mongodb),
;; namely: creating/connecting to the storage, adding new elements, changing
;; existing ones, and also operation that implement general searching
;; functionality
;;

;; TODO: abstract out operations into protocol ?

(defn initialize-storage
  "Initialize storage using config data."
  [conf]
  (db/initialize-storage conf))

(defn store-definition
  "Add definition to the persistent storage."
  [context definition]
  (db/store-definition context definition))

(defn search-definitions
  "General search utility"
  [context]
  (db/search-definitions context))
