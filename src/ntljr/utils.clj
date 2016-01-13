(ns ntljr.utils
  "Utilities for system tuning and administration."
  (:require [ntljr.core :as core]
            [ntljr.definition :as d]
            [ntljr.storage :as st]
            [ntljr.web :as web]
            [monger.core :refer [drop-db]]
            [clojure.java.shell :refer [sh]]))

(def ntljr-definition
  "Some time around 2003, Internet community gave birth to a pedantic phrase 
\"too long; didn't read\", most often written as a famous abbreviation
\"tl;dr\". This so called meme is one of the most notable proofs, that from
the perspective of the Internet generation, there is nothing more boring and
useless as redundancy of information. The strive for conciseness has always
been the main source of inspiration for mathematicians of all ages, and as
mathematics are what really drives everything in the world of information,
here we are, children of the Internet who continue to seek conciseness,
briefness and preciseness of the information we consume. \"ntl;jr\" stands for
the opposite: \"not too long; just read\". It is a system that allows users to
define and illustrate notions from different categories of knowledge, but
imposes strong restrictions on the size of the definition, requiring to shorten
formulations as much as possible to fit into the 300 characters frame. With
that in mind, you are welcome to share what you know. (P.S. This is the only
definition that exceeds length limit, because rules are not for everyone.)")


(defn repopulate-database
  "Fill database with information from specified file."
  [config dumpfile]
  (let [context (assoc (core/initialize config) :username "admin")
        data (slurp dumpfile :encoding "UTF-8")
        triples (partition 3 (clojure.string/split-lines data))]
    (drop-db (:conn context) (:dbname context))
    (doall (map (fn [[n s i]]
                  (ntljr.core/add-definition context n s i))
                triples))
    (core/save-definition ;; a little easter egg
     context
     {:name "ntl;jr" :text ntljr-definition :author "whythat"
      :rating " ∞ " :crdate "2015.12.16" :image ""})
    (.close (:conn context))))


(defn reset-database! []
  (repopulate-database "resources/config.edn" "resources/data.txt"))
