;;;;----------------------------------------------------------------------------
;;;; This namespace describes core functions of the system such as creating and
;;;; adding new definitions, configuring and running searches, loading configu-
;;;; ration files etc
;;;;----------------------------------------------------------------------------

(ns ntljr.core
  (:require [clojure.pprint :as pp]
            [clojure.string :as s]
            [schema.core :as scm]
            [clj-time.core :as time]
            ;; [markdown.core :as md]
            [clojure.java.io :as io])

  (:require [ntljr.storage :as storage]
            [ntljr.definition :as d]
            [ntljr.auth :as auth]))


;;;-----------------------------------------------------------------------------
;;; Configuration file loading
;;;-----------------------------------------------------------------------------
(def Config
  {:defsize scm/Int  ;; size of the definition seed
   :dbname  scm/Str   ;; database name
   :dbhost  scm/Str   ;; database connection hostname 
   :dbport  scm/Int   ;; database connection port
   :dbuser  scm/Str   ;; database user
   :dbupwd  scm/Str}) ;; user password

(defn load-config
  "Load .edn configuration file to get initialization data,
   ensure (with Schema) that config does not contain username specification."
  [filename]
  (with-open [r (io/reader filename)]
    (scm/validate Config (read (java.io.PushbackReader. r)))))

(defn initialize [path-to-config]
  (storage/initialize-storage (load-config path-to-config)))
;;;-----------------------------------------------------------------------------


;;;-----------------------------------------------------------------------------
;;; Creating new definition
;;;-----------------------------------------------------------------------------
(defn create-definition
  "Create definition from character string entered by user, handle resources."
  [context name string image]
  (let [crdate (str (time/now))
        author (:username context)
        text (s/trim string)]
    (when (<= (count text) (:defsize context))
      (d/make-definition author crdate name text image))))

(defn save-definition
  "Save definition to a database."
  [context definition]
  (storage/store-metadata
   context
   (d/definition-metadata definition)
   (storage/store-text context (:text definition))
   (storage/store-image context (:image definition))))

(defn add-definition
  "Top-level function."
  [context name string image]
  (let [definition (create-definition context name string image)]
    (when definition
      (save-definition context definition)
      definition)))
;;;-----------------------------------------------------------------------------


;;;-----------------------------------------------------------------------------
;;; searching
;;;-----------------------------------------------------------------------------
(defn search-definitions-by-name
  "Return all definitions from a system"
  [context name]
  (sort-by
   :rating >
   (map #(select-keys % [:_id :name :text :image :rating :author :crdate])
        (storage/search-definitions-by-name context (s/trim name)))))
;;;-----------------------------------------------------------------------------


;;;-----------------------------------------------------------------------------
;;; updating rating
;;;-----------------------------------------------------------------------------
(defn rate-definition [context id]
  (storage/change-keyval context id :rating inc))
;;;-----------------------------------------------------------------------------
