;; ## Core functionality

(ns ntljr.core
  (:require [clojure.pprint :as pp]
            [schema.core :as scm]
            [ntljr.definition :as d]
            [markdown.core :as md]
            [clojure.java.io :as io]))

(def Config
  {;; core config
   :defsize scm/Int    ;; size of the definition seed
   ;; database config
   :dbhost scm/Str   ;; database connection hostname 
   :dbport scm/Str   ;; database connection port
   :dbcoll scm/Str}) ;; database collection name

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

(defn search-definitions-by-name
  "Return definitions with a given name"
  [name]
  )

(defn show-definition
  "Transform definition to html."
  [definition]
  (md/md-to-html-string (d/definition-text definition)
                        :reference-links? true))











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
