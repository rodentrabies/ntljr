(ns ntljr.main
  (:require [ntljr.core :as core]
            [ntljr.web :as web]
            [ring.adapter.jetty :as ring]))

(defn ntljr-start [config-file]
  (let [context (atom (core/initialize config-file))
        app (web/ntljrapp context)]
    (ring/run-jetty app {:port 3000})))

(defn -main []
  (ntljr-start "resources/config.edn"))



;; dev
(def ntljr (web/ntljrapp (atom (core/initialize "resources/config.edn"))))

(defn devserver []
  (ring/run-jetty #'ntljr {:port 3000 :join? false}))
