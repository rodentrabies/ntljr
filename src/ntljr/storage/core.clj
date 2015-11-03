(ns ntljr.storage.core
  "Use functions from imported storage subnamespace to create a db layer."
  (:require [ntljr.storage.mongodb :as db]))


(defn connect-to-store [& rest]
  (db/connect rest))


(defn add-definition
  ""
  []
  nil)

(defn search-definitions
  ""
  []
  nil)
