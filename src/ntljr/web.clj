(ns ntljr.web
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [ring.util.response :as resp]
            [ring.middleware.params :as params]
            [clojure.java.io :as io])
  (:require [ntljr.core :as ntljr]
            [ntljr.layout :as layout]))

(defn home-get-response []
  {:status 200
   :header {"Content-Type" "text/html"}
   :body (layout/home-template)})

(defn add-get-response []
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (layout/add-template)})

(defn add-post-response [context text]
  (str "<body><h1>Definition added</h1><hr></hr>" (or text "nil") "</body>"))

(defn search-get-response []
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (layout/search-template)})

(defn help-get-response []
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (layout/help-template)})

(defn not-found-response []
  (route/not-found (slurp (io/resource "404.html"))))

(defn ntljr-routes [context]
  (routes (GET  "/"       [] (home-get-response))
          (GET  "/add"    [] (add-get-response))
          (POST "/add"    [text] (add-post-response context text))
          (GET  "/search" [] (search-get-response))
          (GET  "/help"   [] (help-get-response))
          (ANY  "*"       [] )))

(defn ntljrapp
  "Composes application from its parts."
  [context]
  (-> (ntljr-routes context)
      (params/wrap-params)
      (handler/site)))
