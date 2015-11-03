;; ## Core functionality

(ns ntljr.core
  (:require [clojure.pprint :as pp]
            [ntljr.definition :as d]
            [markdown.core :as md]))

(defn show-definition
  "Transform definition to html."
  [definition]
  (md/md-to-html-string (d/definition-text definition)
                        :code-style #(str "class=\"brush: " % "\"")))


(def test-definition
  (d/make-definition "whythat"
                     "2015.11.03"
                     "
## Test definition

- Point 1
- Point 2

```clojure
(defn foo []
  \"bar\")
```
"
                     []))

(defn show-test-definition []
  (show-definition test-definition))
