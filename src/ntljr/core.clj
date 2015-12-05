;;;;----------------------------------------------------------------------------
;;;; This namespace describes core functions of the system such as creating and
;;;; adding new definitions, configuring and running searches, loading configu-
;;;; ration files etc
;;;;----------------------------------------------------------------------------

(ns ntljr.core
  (:require [clojure.pprint :as pp]
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
  [context name s]
  (let [crdate (str (time/now))
        author (:username context)]
    (d/make-definition author crdate name s)))

(defn save-definition
  "Save definition to a database."
  [context definition]
  (storage/store-metadata
   context
   (d/definition-metadata definition)
   (storage/store-text context (:text definition))))

(defn add-definition
  "Top-level function."
  [context name s]
  (save-definition context (create-definition context name s)))

(defn search-definitions-by-name
  "Return all definitions from a system"
  [context name]
  (apply str (map #(select-keys % [:name :text])
                  (storage/search-definitions-by-name context name))))

(defn show-definition
  "Transform definition to html."
  [definition]
  (md/md-to-html-string (:text definition)
                        :reference-links? true))






;;;-----------------------------------------------------------------------------
;;; testing
;;;-----------------------------------------------------------------------------
(def test-text
  "
## Test definition

- Point 1
- Point 2


![Alt text](http://25.io/mou/img/1.png \"Title\")

")



(def test-definition
  (d/make-definition "whythat"
                     "2015.11.03"
                     "Test"
                     test-text))

(defn test-run [conffile]
  (let [conf (load-config conffile)
        dbcontext (storage/initialize-storage conf)]
    (save-definition dbcontext test-definition)
    (search-definitions-by-name dbcontext "some name")))

(defn show-test-definition []
  (show-definition test-definition))
;;;-----------------------------------------------------------------------------
