(ns ntljr.storage.mongodb
  (:require [monger.core :as mg]))

(defn connect [host port]
  (let [server-addr (mg/server-address host port)]
    (mg/connect server-addr)))


(defn add-definition
  "Add definition to the persistent storage."
  [conn definition]
  ;; TODO: impl
  nil)

(defn search-definitions
  "Basic searching functionality."
  [conn query]
  ;; TODO: impl
  nil)



