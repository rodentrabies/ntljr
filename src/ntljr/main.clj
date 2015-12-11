(ns ntljr.main
  (:require [ntljr.core :as core]
            [ntljr.web :as web]
            [ring.adapter.jetty :as ring]))

(def ntljr (web/ntljrapp (core/initialize "resources/config.edn")))

(defn -devmain []
  (ring/run-jetty #'ntljr {:port 3000 :join? false}))

(defn ntljr-start [config-file]
  (let [context (core/initialize config-file)
        app (web/ntljrapp context)]
    (ring/run-jetty app {:port 3000})))

(defn -main []
  (ntljr-start "resources/config.edn"))



