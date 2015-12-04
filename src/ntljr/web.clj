;; ## web part

(ns ntljr.web
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.util.response :as resp]
            [clojure.java.io :as io]
            [hiccup.core :as hiccup]
            [ntljr.core :as ntljr]))

(defn initialize [kernel]
  (routes 
   (GET "/" [] {:status 200
                :headers {"Content-Type" "text/html"}
                :body (-> "public/create_definition.html"
                          io/resource
                          slurp)})
   ))
