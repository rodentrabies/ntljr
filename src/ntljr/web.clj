;; ## web part

(ns ntljr.web
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [hiccup.core :as hiccup]
            [ring.adapter.jetty :as ring]
            [ntljr.core :as ntljr]))

(defroutes ntljrapp
  (GET "/" [] (ntljr/show-test-definition)))

(defn -main []
  (ring/run-jetty ntljrapp {:port 3000}))

