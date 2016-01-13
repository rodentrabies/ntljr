(ns ntljr.web
  (:require [compojure.core
             :as compojure
             :refer [defroutes routes GET POST ANY]]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [ring.util.response :as response]
            [ring.util.codec :as codec]
            [ring.middleware.params :as params]
            [clojure.java.io :as io])
  (:require [ntljr.core :as core]
            [ntljr.auth :as auth]
            [ntljr.views :as layout]))

(defn wrap-encoding [resp]
  (response/charset resp "UTF-8"))

(defn not-found-response [context]
  (layout/not-found-template (:username @context)))

(defn home-get-response [context]
  (wrap-encoding
   {:status 200
    :headers {"Content-Type" "text/html"}
    :body (layout/home-template (:username @context))}))

(defn add-get-response [context]
  (if (auth/authenticated? @context)
    (wrap-encoding {:status 200
                    :headers {"Content-Type" "text/html"}
                    :body (layout/add-template (:username @context))})
    (not-found-response context)))

(defn add-post-response [context name text image]
  (if (auth/authenticated? @context)
    (wrap-encoding
     {:status 200
      :headers {"Content-Type" "text/html"}
      :body (layout/add-template (:username @context)
             :definition (core/add-definition @context name text image))})
    (not-found-response context)))

(defn stats-get-response [context]
  (if (and (auth/authenticated? @context)
           (auth/root-user? @context))
    (wrap-encoding
     {:status 200
      :headers {"Content-Type" "text/html"}
      :body (layout/stats-template (core/user-stats @context))})
    (not-found-response context)))

(defn dump-get-response [context]
  (if (and (auth/authenticated? @context)
           (auth/root-user? @context))
    (wrap-encoding
     {:status 200
      :headers {"Content-Type" "text/html"}
      :body (layout/dump-template)})
    (not-found-response context)))

(defn dump-post-response [context path]
  (if (and (auth/authenticated? @context)
           (auth/root-user? @context))
    (let [res (core/save-storage @context path)]
      (wrap-encoding
       {:status 200
        :headers {"Content-Type" "text/html"}
        :body (layout/dump-template 
               (case res
                 :invalid "Invalid storage path."
                 :exists "File already exists."
                 nil))}))
    (not-found-response context)))

(defn restore-get-response [context]
  (if (and (auth/authenticated? @context)
           (auth/root-user? @context))
    (wrap-encoding
     {:status 200
      :headers {"Content-Type" "text/html"}
      :body (layout/restore-template)})
    (not-found-response context)))

(defn restore-post-response [context path]
  (if (and (auth/authenticated? @context)
           (auth/root-user? @context))
    (let [res (core/restore-storage @context path)]
      (wrap-encoding
       {:status 200
        :headers {"Content-Type" "text/html"}
        :body (layout/restore-template 
               (case res
                 :invalid "Invalid storage path."
                 :exists "No such file/directory."
                 nil))}))
    (not-found-response context)))

(defn search-get-response [context]
  (wrap-encoding
   {:status 200
    :headers {"Content-Type" "text/html"}
    :body (layout/search-template (:username @context))}))

(defn search-post-response [context name]
  (response/redirect (str "/search/" (codec/percent-encode name))))

(defn result-get-response [context name]
  (let [ename (codec/percent-encode name)
        results (core/search-definitions-by-name @context name)]
    (wrap-encoding
     {:status 200
      :headers {"Content-Type" "text/html"}
      :body (layout/search-template
             (:username @context) :results results :name ename)})))

(defn result-post-response [context name id]
  (core/rate-definition @context id)
  (let [ename (codec/percent-decode name)
        results (core/search-definitions-by-name @context ename)]
    (wrap-encoding
     {:status 200
      :headers {"Content-Type" "text/html"}
      :body (layout/search-template
             (:username @context) :results results :name ename)})))

(defn about-get-response [context]
  (wrap-encoding
   {:status 200
    :headers {"Content-Type" "text/html"}
    :body (layout/about-template (:username @context))}))

(defn login-get-response [context err]
  (wrap-encoding
   {:status 200
    :headers {"Content-Type" "text/html"}
    :body (layout/login-template (:username @context) err)}))

(defn login-post-response [context username password]
  (let [res (auth/log-in context username password)]
    (case res
      (:unmatched :exists) (login-get-response
                            context "Wrong username or password.")
      (response/redirect-after-post "/"))))

(defn signup-get-response [context err]
  (wrap-encoding
   {:status 200
    :headers {"Content-Type" "text/html"}
    :body (layout/signup-template (:username @context) err)}))

(defn signup-post-response [context uname p1 p2]
  (if (or (= p1 "") (= p2 "") (= uname ""))
    (signup-get-response context "All fields must be non-empty.")
    (let [res (auth/sign-up @context uname p1 p2)]
      (case res
        :unmatched (signup-get-response context "Passwords do not match.")
        :exists (signup-get-response context (str "User " uname " exists."))
        (response/redirect-after-post "/")))))

(defn logout-response [context]
  (auth/log-out context)
  (response/redirect "/"))

(defn ntljr-routes [context]
  "Context is considered an immutable config storage. The only two routes,
   that actually need to mutate context are `/login` and `/logout` so they
   receive context as ref, while others receive immutable map."
  (routes
   (GET  "/" [] (home-get-response context))
   (GET  "/add" [] (add-get-response context))
   (POST "/add" [name text image] (add-post-response context name text image))
   (GET  "/stats" [] (stats-get-response context))
   (GET  "/dump" [] (dump-get-response context))
   (POST "/dump" [path] (dump-post-response context path))
   (GET  "/restore" [] (restore-get-response context))
   (POST "/restore" [path] (restore-post-response context path))
   (POST "/search" [name] (search-post-response context name))
   (GET  "/search/:name" [name] (result-get-response context name))
   (POST "/search/:name" [name rate] (result-post-response context name rate))
   (GET  "/about" [] (about-get-response context))
   (GET  "/login" [] (login-get-response context nil))
   (POST "/login" [uname pwd] (login-post-response context uname pwd))
   (GET  "/signup" [] (signup-get-response context nil))
   (POST "/signup" [uname p1 p2] (signup-post-response context uname p1 p2))
   (ANY  "/logout" [] (logout-response context))
   (route/resources "/")
   (route/not-found (not-found-response context))))

(defn ntljrapp
  "Composes application from its parts."
  [context]
  (-> (ntljr-routes context)
      (params/wrap-params)
      (handler/site)))
