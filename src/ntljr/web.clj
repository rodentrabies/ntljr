(ns ntljr.web
  (:require [compojure.core :refer [defroutes routes GET POST ANY]]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [ring.util.response :as response]
            [ring.middleware.params :as params]
            [clojure.java.io :as io])
  (:require [ntljr.core :as core]
            [ntljr.views :as layout]))

(defn home-get-response []
  {:status 200
   :header {"Content-Type" "text/html"}
   :body (layout/home-template)})

(defn add-get-response []
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (layout/add-template)})

(defn add-post-response [context name text image]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (layout/add-template
          :definition (core/add-definition context name text image))})

(defn search-get-response []
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (layout/search-template)})

(defn search-post-response [context name]
  (let [results (core/search-definitions-by-name context name)]
    {:status 200
     :header {"Content-Type" "text/html"}
     :body (layout/search-template
            :results results)}))

(defn help-get-response []
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (layout/help-template)})

(defn not-found-response []
  (route/not-found (slurp (io/resource "404.html"))))

(defn ntljr-routes [context]
  (routes (GET  "/" [] (home-get-response))
          (GET  "/add" [] (add-get-response))
          (POST "/add" [name text image] (add-post-response context name text image))
          (GET  "/search" [] (search-get-response))
          (POST "/search" [name] (search-post-response context name))
          (GET  "/help" [] (help-get-response))
          (ANY  "*" [])
          (route/resources "/")))

(defn ntljrapp
  "Composes application from its parts."
  [context]
  (-> (ntljr-routes context)
      (params/wrap-params)
      (handler/site)))
