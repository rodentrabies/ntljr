;; ## Core functionality

(ns ntljr.core
  (:require [clojure.pprint :as pp]
            [ntljr.definition :as d]
            [markdown.core :as md]
            [clojure.java.io :as io]))

(defn load-props
  "Load properties to configure system and connections."
  [file-name]
  (with-open [reader (io/reader file-name)] 
    (let [props (java.util.Properties.)]
      (.load props reader)
      (into {} (for [[k v] props]
                 [(keyword k) (read-string v)])))))



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
