(ns ntljr.storage
  (:require [monger.core :as mg]
            [monger.collection :as mcoll]
            [pandect.algo.sha256 :as sha])
  
  (:require [ntljr.definition :as df]))

(def database-name "ntljr") ;; name of the database to use
(def markdown-collection "definitions") ;; collection to store markdown strings
(def meta-collection "metadata") ;; collection to store metadata
(def graphic-collection "images") ;; collection to store images

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
(defn store-graphic 
  "Save graphic (image) with it's hash-sum as key and return key."
  [context graphic]
  ;; currently just insert graphic
  (and graphic
       (let [hash (sha/sha256 graphic)]
         (mcoll/update (:db context) graphic-collection {} {:_id hash :blob graphic} {:upsert true}) 
         hash)))

(defn store-text [context text]
  (str (:_id (mcoll/insert-and-return (:db context)
                                      markdown-collection
                                      {:text text}))))

(defn store-metadata
  "Store metadata with foreign keys for text (tid) and graphic (gid)."
  [context meta tid gid]
  (str (:_id (mcoll/insert-and-return
              (:db context)
              meta-collection
              (assoc meta :_textID tid :_graphicID gid)))))
;;;-----------------------------------------------------------------------------


;;;-----------------------------------------------------------------------------
;;; search
;;;-----------------------------------------------------------------------------
(defn search-text [context tid]
  (:text (mcoll/find-one-as-map (:db context) markdown-collection {:_id tid})))

(defn search-graphic [context gid]
  (if gid
    (:blob (mcoll/find-one-as-map (:db context) graphic-collection {:_id gid}))
    :none))

(defn search-definitions-by-name
  "Basic searching functionality."
  [context name]
  (let [meta-list (mcoll/find-maps (:db context) meta-collection {:name name})]
    (map (fn [x]
           (let [tid (:_textID x)
                 gid (:_graphicID x)]
             (assoc (dissoc x :_textID :_graphicID)
                    :text (search-text context tid)
                    :graphic (search-graphic context gid))))
         meta-list)))
;;;-----------------------------------------------------------------------------
