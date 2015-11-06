;; ## Core functionality

(ns ntljr.core
  (:require [clojure.pprint :as pp]
            [schema.core :as scm]
            [ntljr.definition :as d]
            [markdown.core :as md]
            [clojure.java.io :as io]))

;; (def Config
;;   {:dsize scm/Int
;;    :host scm/Str
;;    :port scm/Str
;;    :collection scm/Str})

(defn load-config
  "Load configuration file to get initialization data
  (file must contain clojure map)."
  [filename]
  (with-open [r (io/reader filename)]
    (read (java.io.PushbackReader. r))))

(defn show-definition
  "Transform definition to html."
  [definition]
  (md/md-to-html-string (d/definition-text definition)
                        :reference-links? true))












(def test-definition
  (d/make-definition "whythat"
                     "2015.11.03"
                     "
## Test definition

- Point 1
- Point 2


![Alt text](http://25.io/mou/img/1.png \"Title\")

"
                     []))

(defn show-test-definition []
  (show-definition test-definition))
