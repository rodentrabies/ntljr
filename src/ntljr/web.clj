(ns ntljr.web
  (:require [compojure.core :refer [defroutes routes GET POST ANY]]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [ring.util.response :as response]
            [ring.util.codec :as codec]
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

(defn search-post-response [name]
  (response/redirect-after-post
   (str "/search/" (codec/percent-encode name))))

(defn result-get-response [context name]
  (let [ename (codec/percent-encode name)
        results (core/search-definitions-by-name context name)]
    (wrap-encoding
     {:status 200
      :headers {"Content-Type" "text/html"}
      :body (layout/search-template :results results :name ename)})))

(defn result-post-response [context name id]
  (core/rate-definition context id)
  (let [ename (codec/percent-decode name)
        results (core/search-definitions-by-name context ename)]
    (wrap-encoding
     {:status 200
      :headers {"Content-Type" "text/html"}
      :body (layout/search-template :results results :name ename)})))

(defn about-get-response []
  (wrap-encoding
   {:status 200
    :headers {"Content-Type" "text/html"}
    :body (layout/about-template)}))

(defn not-found-response []
  (layout/not-found-template))

(defn ntljr-routes [context]
  (routes
   (GET  "/" [] (home-get-response))
   (GET  "/add" [] (add-get-response))
   (POST "/add" [name text image] (add-post-response context name text image))
   (POST "/search" [name] (search-post-response name))
   (GET  "/search/:name" [name] (result-get-response context name))
   (POST "/search/:name" [name rate] (result-post-response context name rate))
   (GET  "/about" [] (about-get-response))
   (route/resources "/")
   (route/not-found (not-found-response))))

(defn ntljrapp
  "Composes application from its parts."
  [context]
  (-> (ntljr-routes context)
      (params/wrap-params)
      (handler/site)))
