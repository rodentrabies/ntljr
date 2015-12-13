(ns ntljr.web
  (:require [compojure.core :refer [defroutes routes GET POST ANY]]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [ring.util.response :as response]
            [ring.middleware.params :as params]
            [clojure.java.io :as io])
  (:require [ntljr.core :as core]
            [ntljr.views :as layout]))

(defn wrap-encoding [resp]
  (response/charset resp "UTF-8"))

(defn home-get-response []
  (wrap-encoding
   {:status 200
    :headers {"Content-Type" "text/html"}
    :body (layout/home-template)}))

(defn add-get-response []
  (wrap-encoding {:status 200
                  :headers {"Content-Type" "text/html"}
                  :body (layout/add-template)}))

(defn add-post-response [context name text image]
  (wrap-encoding
   {:status 200
    :headers {"Content-Type" "text/html"}
    :body (layout/add-template
           :definition (core/add-definition context name text image))}))

(defn search-get-response []
  (wrap-encoding
   {:status 200
    :headers {"Content-Type" "text/html"}
    :body (layout/search-template)}))

(defn search-post-response [context name]
  (let [results (core/search-definitions-by-name context name)]
    (wrap-encoding
     {:status 200
      :headers {"Content-Type" "text/html"}
      :body (layout/search-template
             :results results)})))

(defn about-get-response []
  (wrap-encoding
   {:status 200
    :headers {"Content-Type" "text/html"}
    :body (layout/about-template)}))

(defn not-found-response []
  (route/not-found (layout/not-found-template)))

(defn ntljr-routes [context]
  (routes (GET  "/" [] (home-get-response))
          (GET  "/add" [] (add-get-response))
          (POST "/add" [name text image] (add-post-response context name text image))
          (GET  "/search" [] (search-get-response))
          (POST "/search" [name] (search-post-response context name))
          (GET  "/about" [] (about-get-response))
          ;; (ANY  "*" [] (not-found-response))
          (route/resources "/")))

(defn ntljrapp
  "Composes application from its parts."
  [context]
  (-> (ntljr-routes context)
      (params/wrap-params)
      (handler/site)))
