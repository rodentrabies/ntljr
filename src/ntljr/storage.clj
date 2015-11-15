(ns ntljr.storage
  (:require [monger.core :as mg]
            [monger.collection :as mcoll])
  
  (:require [ntljr.definition :as df]))


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
  (let [resources (df/definition-resources definition)]
   (mcoll/insert (:db context) "definitions" definition)))

(defn search-definitions
  "Basic searching functionality."
  [context]
  (mcoll/find-maps (:db context) "definitions"))

(defn )
