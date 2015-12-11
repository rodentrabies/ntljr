(ns ntljr.utils
  (:require [ntljr.core :as core]
            [ntljr.definition :as d]
            [ntljr.storage :as st]
            [ntljr.web :as web]
            [monger.core :refer [drop-db]]))

(defn reset-database []
  (let [context (core/initialize "resources/config.edn")
        data (slurp "resources/data.txt" :encoding "UTF-8")
        triples (partition 3 (clojure.string/split-lines data))]
    (drop-db (:conn context) st/database-name)
    (doall (map (fn [[n s i]]
                  (ntljr.core/add-definition context n s i))
                triples))
    (.close (:conn context))))



