;; ## web part

(ns ntljr.web
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [hiccup.core :as hiccup]
            [ntljr.core :as ntljr]))

(defn initialize [kernel]
  (routes 
   (GET "/" [] (ntljr/show-test-definition))
   (route/not-found "<h1>Page not found</h1>")))
