;; ## web part

(ns ntljr.web
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.util.response :as resp]
            [hiccup.core :as hiccup]
            [ntljr.core :as ntljr]))

(defn initialize [kernel]
  (routes 
   (GET "/" [] "Hello")
   (route/resources "/")
   (route/not-found "<h1>Page not found</h1>")))
