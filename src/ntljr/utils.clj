(ns ntljr.utils
  "Utilities for system tuning and administration."
  (:require [ntljr.core :as core]
            [ntljr.definition :as d]
            [ntljr.storage :as st]
            [ntljr.web :as web]
            [monger.core :refer [drop-db]]
            [clojure.java.shell :refer [sh]]))

(defn repopulate-database
  "Fill database with information from specified file."
  [dumpfile]
  (let [context (core/initialize "resources/config.edn")
        data (slurp dumpfile :encoding "UTF-8")
        triples (partition 3 (clojure.string/split-lines data))]
    (drop-db (:conn context) st/database-name)
    (doall (map (fn [[n s i]]
                  (ntljr.core/add-definition context n s i))
                triples))
    (.close (:conn context))))

(defn reset-database! []
  (repopulate-database "resources/data.txt"))

;; (defn dump-database
;;   "Dump information from database to a given file."
;;   [dumpfile]
;;   (sh ))



