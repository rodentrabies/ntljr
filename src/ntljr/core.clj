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
(def ^:const md-im-regex
  "Regex to extract image descriptions from markdown text."
  #"(!\[.*?\]\()(.+?)( .*?\)|\))")

(defn extract-image-url [s]
  (let [images (into [] (map #(% 2) (re-seq md-im-regex s)))]
    (case (count images)
      0 :none
      1 (images 0)
      ;; TODO: add error case
      :else nil))) 

(defn create-definition
  "Create definition from character string entered by user, handle resources."
  [name s context]
  (let [crdate (str (time/now))
        author (:username context)
        image (extract-image-url s)]
    (d/make-definition author crdate name s image)))

(defn save-definition
  "Save definition to a database."
  [context definition]
  (storage/store-metadata
   context
   (d/definition-metadata definition)
   (storage/store-text context (:text definition))
   (storage/store-graphic context (:graphic definition))))

(defn add-definition
  "Top-level function."
  [name str context]
  (save-definition context (create-definition name str context)))

(defn search-definitions-by-name
  "Return all definitions from a system"
  [context name]
  (storage/search-definitions-by-name context name))

(defn show-definition
  "Transform definition to html."
  [definition]
  (md/md-to-html-string (:text definition)
                        :reference-links? true))






;;;-----------------------------------------------------------------------------
;;; testing
;;;-----------------------------------------------------------------------------
(def test-markdown
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
                     test-markdown
                     "graphic"))

(defn test-run [conffile]
  (let [conf (load-config conffile)
        dbcontext (storage/initialize-storage conf)]
    (save-definition dbcontext test-definition)
    (search-definitions-by-name dbcontext "some name")))

(defn show-test-definition []
  (show-definition test-definition))
;;;-----------------------------------------------------------------------------
