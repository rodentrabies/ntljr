(ns ntljr.context
  (:require [com.stuartsierra.component :as component])
  (:require [ntljr.storage :as storage]))

(defrecord Database [host port connection database]
  component/Lifecycle
  (start [this]
    (let [conn (storage/initialize-storage {:dbhost host :dbport port})]
      (assoc this :connection (:conn conn) :database (:db conn))))
  (stop [this]
    (.close connection)
    (assoc this :connection nil :database nil)))
