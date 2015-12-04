(ns ntljr.storage
  (:import org.bson.types.ObjectId)
  (:require [monger.core :as mg]
            [monger.collection :as mcoll])
  
  (:require [ntljr.definition :as df]))

(def database-name "ntljr") ;; name of the database to use
(def text-collection "text") ;; collection to store markdown strings
(def metadata-collection "metadata") ;; collection to store metadata

(defn initialize-storage 
  "Create general db structure and return database context map."
  [conf]
  (let [sa (mg/server-address (:dbhost conf) (:dbport conf))
        conn (mg/connect sa)
        db (mg/get-db conn "ntljr")]
    {:conn conn :db db}))

;;;-----------------------------------------------------------------------------
;;; store
;;;-----------------------------------------------------------------------------
(defn store-text [context text]
  (str (:_id (mcoll/insert-and-return
              (:db context) text-collection {:text text}))))

(defn store-metadata
  "Store metadata with foreign keys for text (tid)."
  [context meta tid]
  (str (:_id (mcoll/insert-and-return
              (:db context) metadata-collection (assoc meta :_textID tid)))))
;;;-----------------------------------------------------------------------------


;;;-----------------------------------------------------------------------------
;;; search
;;;-----------------------------------------------------------------------------
(defn search-text
  "Text ID (tid) comes as a string to abstract out MongoDB objects."
  [context tid]
  (:text (mcoll/find-one-as-map
          (:db context) text-collection {:_id (ObjectId. tid)})))

(defn search-definitions-by-name
  "Basic searching functionality."
  [context name]
  (let [mlist (mcoll/find-maps (:db context) metadata-collection {:name name})]
    (map (fn [x]
           (let [tid (:_textID x)]
             (assoc (dissoc x :_textID)
                    :text (search-text context tid))))
         mlist)))
;;;-----------------------------------------------------------------------------
