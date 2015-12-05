(ns ntljr.web
  (:require [compojure.core :refer [routes GET POST ANY]]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [ring.util.response :as resp]
            [ring.middleware.params :as params]
            [clojure.java.io :as io])
  (:require [ntljr.core :as core]
            [ntljr.layout :as layout]))

(defn home-get-response []
  {:status 200
   :header {"Content-Type" "text/html"}
   :body (layout/home-template)})

(defn add-get-response []
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (layout/add-template)})

(defn add-post-response [context name text]
  (core/add-definition context name text)
  (str "<body><h1>Definition added</h1><hr></hr>" (class name ) "<br>" (class text) "</body>"))

(defn search-get-response []
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (layout/search-template)})

(defn search-post-response [context name]
  (let [results (core/search-definitions-by-name context name)]
    {:status 200
     :header {"Content-Type" "text/html"}
     :body (layout/search-results-template results)}))

(defn help-get-response []
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (layout/help-template)})

(defn not-found-response []
  (route/not-found (slurp (io/resource "404.html"))))

(defn ntljr-routes [context]
  (routes (GET  "/"       [] (home-get-response))
          (GET  "/add"    [] (add-get-response))
          (POST "/add"    [name text] (add-post-response context name text))
          (GET  "/search" [] (search-get-response))
          (POST "/search" [name] (search-post-response context name))
          (GET  "/help"   [] (help-get-response))
          (ANY  "*"       [] )))

(defn ntljrapp
  "Composes application from its parts."
  [context]
  (-> (ntljr-routes context)
      (params/wrap-params)
      (handler/site)))
