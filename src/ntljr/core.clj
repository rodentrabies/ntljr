;; ## Core functionality

(ns ntljr.core
  (:require [clojure.pprint :as pp]
            [schema.core :as scm]
            [clj-time.core :as time]
            [clj-uuid :as uuid]
            [markdown.core :as md]
            [clojure.java.io :as io])
  (:require [ntljr.storage :as storage]
            [ntljr.definition :as d]))

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

(defn extract-image-urls
  "Replace image links in markdown string with stubs
   and create a map { UUID -> ImageURL }, returning vec [mdstring imagemap]"
  [s]
  (let [images (re-seq #"(!\[.*?\]\()(.+?)(\))" s)
        immap (zipmap (distinct images) (repeatedly (comp str uuid/v1)))
        newstr (reduce (fn [s im]
                         (clojure.string/replace s im (immap im)))
                       s
                       images)]
    [newstr (zipmap (vals immap) (keys immap))]))

(defn create-definition
  "Create definition from character string entered by user, handle resources."
  [name str context]
  (let [crdate (str (time/now))
        uuid (uuid/v1)
        author (:username context)
        [mdtext resmap] (extract-image-urls str)]
    (d/make-definition author crdate name mdtext resmap)))

(defn search-definitions
  "Return all definitions from a system"
  [name]
  nil)

(defn show-definition
  "Transform definition to html."
  [definition]
  (md/md-to-html-string (d/definition-text definition)
                        :reference-links? true))







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
                     []))

(defn test-run [conffile]
  (let [conf (load-config conffile)
        dbcontext (storage/initialize-storage conf)]
    (storage/store-definition dbcontext test-definition)
    (storage/search-definitions dbcontext)))

(defn show-test-definition []
  (show-definition test-definition))
