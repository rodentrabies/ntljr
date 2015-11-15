;; ## Core functionality

(ns ntljr.core
  (:require [clojure.pprint :as pp]
            [schema.core :as scm]
            [ntljr.definition :as d]
            [markdown.core :as md]
            [clojure.java.io :as io]

            [ntljr.storage :as storage]))

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
  (load-config path-to-config))

(defn create-definition
  "Create definition with unique identifier, handle resources."
  []
  )

(defn search-definitions
  "Return all definitions from a system"
  [name]
  nil)

(defn show-definition
  "Transform definition to html."
  [definition]
  (md/md-to-html-string (d/definition-text definition)
                        :reference-links? true))







(defn test-run [conffile]
  (let [conf (load-config conffile)
        dbcontext (storage/initialize-storage conf)]
    (storage/store-definition dbcontext test-definition)
    (storage/search-definitions dbcontext)))



(def test-definition
  (d/make-definition "whythat"
                     "2015.11.03"
                     "Test"
                     "
## Test definition

- Point 1
- Point 2


![Alt text](http://25.io/mou/img/1.png \"Title\")

"
                     []))

(defn show-test-definition []
  (show-definition test-definition))
