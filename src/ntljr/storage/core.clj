(ns ntljr.storage.core
  "Use functions from imported storage subnamespace to create a db layer."
  (:require [ntljr.storage.mongodb :as db]))

;;
;; Operations for work with system's persistent storage (currently - mongodb),
;; namely: creating/connecting to the storage, adding new elements, changing
;; existing ones, and also operation that implement general searching
;; functionality
;;

(defn connect-to-store [& rest]
  (db/connect rest))


(defn add-definition
  "Add definition to the persistent storage."
  []
  nil)

(defn search-definitions
  ""
  []
  nil)
