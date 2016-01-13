(ns ntljr.auth
  (:require [pandect.algo.sha1 :as sha])
  (:require [ntljr.storage :as st]))

(defn log-in
  "If logging in is successful, swap username into context ref."
  [context-ref uname password]
  (let [stored (st/get-user-creds @context-ref uname)
        phash (sha/sha1 password)]
    (cond (not stored) :exists
          (not (= (:password stored) phash)) :unmatched
          :else (swap! context-ref assoc :username uname))))

(defn log-out [context-atom]
  "Add `:username` key."
  (swap! context-atom dissoc :username))

(defn authenticated? [context]
  (contains? context :username))

(defn root-user? [context]
  (= (:username context) "root"))

(defn sign-up
  "Add new user if 'uname' is free and log in."
  [context uname password repeated]
  (cond (not= password repeated) :unmatched
        (st/get-user-creds context uname) :exists
        :else (let [phash (sha/sha1 password)]
                (st/store-user-creds context uname phash))))
