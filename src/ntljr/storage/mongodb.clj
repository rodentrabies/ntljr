(ns ntljr.storage.mongodb
  (:require [monger.core :as mg]
            [monger.collection :as mconn]))


(defn initialize-storage 
  "Create general db structure and return database contest map."
  [conf]
  (let [sa (mg/server-address (:dbhost conf) (:dbport conf))
        conn (mg/connect sa)
        db (mg/get-db conn "ntljr")]
    {:conn conn :db db}))

(defn store-definition
  "Add definition to the persistent storage."
  [context definition]
  (let [resources nil]
   (mconn/insert (:db context) "definitions" definition)))

(defn search-definitions
  "Basic searching functionality."
  [context]
  (mconn/find-maps (:db context) "definitions"))
