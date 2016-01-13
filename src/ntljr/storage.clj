(ns ntljr.storage
  (:import org.bson.types.ObjectId)
  (:require [monger.core :as mg]
            [monger.collection :as mcoll]
            [monger.operators :refer :all]
            [monger.result :refer [acknowledged?]]
            [monger.conversion :refer [from-db-object]]
            [clojure.set :as clj-set]
            [clojure.java.shell :refer [sh]]
            [pandect.algo.sha1 :as sha])
  (:require [ntljr.definition :as df])
  (:import [com.mongodb MapReduceCommand$OutputType MapReduceOutput]))

;;;-----------------------------------------------------------------------------
;;; initialization
;;;-----------------------------------------------------------------------------
(def user-collection "users") ;; collection to store user credentials
(def text-collection "text") ;; collection to store markdown strings
(def images-collection "images") ;; collection to store illustrations
(def metadata-collection "metadata") ;; collection to store metadata
(def stats-collection "mrstats")

(defn initialize-storage 
  "Create general db structure and return database context map."
  [conf]
  (let [sa (mg/server-address (:dbhost conf) (:dbport conf))
        conn (mg/connect sa)
        db (mg/get-db conn (:dbname conf))]
    (mcoll/update
     db
     user-collection
     {:_id (:rootname conf)}
     {:pwd (sha/sha1 (:rootpwd conf))}
     {:upsert true})
    (assoc conf :conn conn :db db)))
;;;-----------------------------------------------------------------------------


;;;-----------------------------------------------------------------------------
;;; store
;;;-----------------------------------------------------------------------------
(defn store-text [context text]
  (str (:_id (mcoll/insert-and-return
              (:db context) text-collection {:text text}))))

(defn store-image
  "NOTICE: currently just a link"
  [context image]
  (str (:_id (mcoll/insert-and-return
              (:db context) images-collection {:image image}))))

(defn store-metadata
  "Store metadata with foreign keys for text (tid)."
  [context meta tid imid]
  (str (:_id (mcoll/insert-and-return
              (:db context)
              metadata-collection
              (assoc meta :_textID tid :_imageID imid)))))

(defn store-user-creds
  "Store user credentials."
  [context username phash]
  (mcoll/insert (:db context) user-collection {:_id username :pwd phash}))
;;;-----------------------------------------------------------------------------


;;;-----------------------------------------------------------------------------
;;; search
;;;-----------------------------------------------------------------------------
(defn search-text
  "Text ID (tid) comes as a string to abstract out MongoDB objects."
  [context tid]
  (:text (mcoll/find-one-as-map
          (:db context) text-collection {:_id (ObjectId. tid)})))

(defn search-image [context imid]
  (:image (mcoll/find-one-as-map
           (:db context) images-collection {:_id (ObjectId. imid)})))

(defn get-user-creds [context uname]
  (let [umap (mcoll/find-one-as-map (:db context) user-collection {:_id uname})]
    (and umap (clj-set/rename-keys umap {:_id :username :pwd :password}))))

(defn change-keyval
  "Update key 'k' of database entry with id 'id' with function 'f'."
  [context id k f]
  (let [oid (ObjectId. id)
        d (mcoll/find-one-as-map (:db context) metadata-collection {:_id oid})]
    (mcoll/update-by-id
     (:db context) metadata-collection oid (assoc d k (f (k d))))))

(defn search-definition-by-id [context id]
  (mcoll/find-one-as-map (:db context) metadata-collection {:_id id}))

(defn search-definitions-by-name
  "Basic searching functionality."
  [context name]
  (let [mlist
        (mcoll/find-maps
         (:db context) metadata-collection
         {:name {$regex name $options "i"}})]
    (map (fn [x]
           (let [{:keys [_textID _imageID]} x]
             (assoc (dissoc x :_textID :_imageID)
                    :text (search-text context _textID)
                    :image (search-image context _imageID))))
         mlist)))

(defn get-user-stats [context]
  (let [mapper "function () {
                  emit(this.author, 1);
               }"
        reducer "function (key, values) {
                   var total = 0;;
                   for (var i = 0; i < values.length; i++) {
                     total += 1;
                   }
                   return total;
                 }"]
    (mcoll/drop (:db context) stats-collection)
    (mcoll/map-reduce
     (:db context) metadata-collection mapper reducer stats-collection {})
    (map #(clojure.set/rename-keys
           (assoc % :value (int (:value %))) {:_id :name :value :definitions})
         (mcoll/find-maps (:db context) stats-collection))))
;;;-----------------------------------------------------------------------------


;;;-----------------------------------------------------------------------------
;;; dump storage
;;;-----------------------------------------------------------------------------
(defn dump-database
  "Dump information from database to a destination."
  [context dest]
  (. (java.lang.Runtime/getRuntime) exec
     (str "mongodump --host " (:dbhost context) 
          " --port " (:dbport context) " --db " (:dbname context) 
          " --out " dest)))

(defn load-database
  "Load database from dump destination."
  [context dest]
    (. (java.lang.Runtime/getRuntime) exec
     (str "mongorestore --host " (:dbhost context) 
          " --port " (:dbport context) " --db " (:dbname context) dest)))
;;;-----------------------------------------------------------------------------
