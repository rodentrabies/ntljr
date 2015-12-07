;;;;----------------------------------------------------------------------------
;;;; This namespace describes core functions of the system such as creating and
;;;; adding new definitions, configuring and running searches, loading configu-
;;;; ration files etc
;;;;----------------------------------------------------------------------------

(ns ntljr.core
  (:require [clojure.pprint :as pp]
            [clojure.string :as string]
            [schema.core :as scm]
            [clj-time.core :as time]
            [markdown.core :as md]
            [clojure.java.io :as io])

  (:require [ntljr.storage :as storage]
            [ntljr.definition :as d]))


;;;-----------------------------------------------------------------------------
;;; Configuration file loading
;;;-----------------------------------------------------------------------------
(def Config
  {;; core config
   :defsize scm/Int    ;; size of the definition seed
   ;; database config
   :dbhost scm/Str   ;; database connection hostname 
   :dbport scm/Int   ;; database connection port
   :dbuser scm/Str   ;; database user
   :dbupwd scm/Str}) ;; user password

(defn load-config
  "Load configuration file to get initialization data
  (file must contain clojure map)."
  [filename]
  (with-open [r (io/reader filename)]
    (scm/validate Config (read (java.io.PushbackReader. r)))))

(defn initialize
  "Top-level function.
   TODO: move database connection here, returning more usable config data."
  [path-to-config]
  (assoc (storage/initialize-storage (load-config path-to-config))
         :username "admin"))
;;;-----------------------------------------------------------------------------


;;;-----------------------------------------------------------------------------
;;; Creating new definition
;;;-----------------------------------------------------------------------------
;; (def ^:const md-im-regex
;;   "Regex to extract image descriptions from markdown text."
;;   #"(!\[.*?\]\()(.+?)( .*?\)|\))")

(defn create-definition
  "Create definition from character string entered by user, handle resources."
  [context name s image]
  (let [crdate (str (time/now))
        author (:username context)]
    (d/make-definition author crdate (string/trim name) s image)))

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
  [context name s image]
  (save-definition context (create-definition context name s image)))
;;;-----------------------------------------------------------------------------


;;;-----------------------------------------------------------------------------
;;; searching
;;;-----------------------------------------------------------------------------
(defn search-definitions-by-name
  "Return all definitions from a system"
  [context name]
  (map #(select-keys % [:name :text :image])
       (storage/search-definitions-by-name context (string/trim name))))
;;;-----------------------------------------------------------------------------

;; (defn show-definition
;;   "Transform definition to html."
;;   [definition]
;;   (md/md-to-html-string (:text definition)
;;                         :reference-links? true))
