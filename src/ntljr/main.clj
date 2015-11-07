(ns ntljr.main
  (:require [ntljr.core :as ntljr]
            [ntljr.web :as web]
            [ring.adapter.jetty :as ring]))

(defn ntljr-start [config-file]
  (let [kernel (ntljr/initialize config-file) ;; rather loud name
        ntljrweb (web/initialize kernel)] ;; TODO: these are temporary
    (ring/run-jetty ntljrweb {:port 3000})))

(defn -main []
  (ntljr-start "resources/static/ntljrrc.clj"))

