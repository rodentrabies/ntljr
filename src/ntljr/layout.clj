(ns ntljr.layout
  (:require [hiccup.page :as page]
            [hiccup.form :as form]
            [hiccup.element :as elem]
            [clojure.java.io :as io]))

(defn wrap-bootstrap [title & contents]
  (page/html5
   {:ng-app "myApp" :lang "en"}
   [:head
    [:title title]
    (page/include-css "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css")
    (page/include-css "css/sticky_footer.css")
    (page/include-js "http://code.angularjs.org/1.4.8/angular.min.js")
    (page/include-js "js/ui-bootstrap-tpls-0.14.3.min.js")
    (page/include-js "js/script.js")]
   [:body
    [:div {:class "container"}
     [:h1 "NTL;JR"]
     contents]
    [:footer {:class "footer"}
     [:div {:class "container"}
      [:p {:class "text-muted"} "NTL;JR © 2015 whythat"]]]]))

(defn home-template []
  (wrap-bootstrap
   "Home | NTLJR"
   [:h2 "Home: here's where the knowledge happens..."] ;
   [:hr]
   (elem/link-to
    {:class "btn btn-primary"} "/" "Home")
   (elem/link-to
    {:class "btn btn-primary"} "/add" "Add definition")
   (elem/link-to
    {:class "btn btn-primary"} "/search" "Search")
   (elem/link-to
    {:class "btn btn-primary"} "/help" "Help")))

(defn add-template []
  (wrap-bootstrap
   "Add | NTLJR"
   [:h2 "Add definition"]
   [:hr]
   (elem/link-to
    {:class "btn btn-primary"} "/" "Home")
   (elem/link-to
    {:class "btn btn-primary"} "/add" "Add definition")
   (elem/link-to
    {:class "btn btn-primary"} "/search" "Search")
   (elem/link-to
    {:class "btn btn-primary"} "/help" "Help")
   [:hr]
   (form/form-to
    [:post "/add"]
    [:h3 "Add definition"
     [:br] (form/label "defname" "Name:")
     [:br] (form/text-field "name")
     [:br] (form/label "deftext" "Definition:")
     [:br] (form/text-area {:rows 3 :cols 50} "text")
     [:br] (form/label "defill" "Illustration:")
     [:br] (form/text-field "image")]
    (form/submit-button "save"))))

(defn search-template []
  (wrap-bootstrap
   "Search | NTLJR"
   [:h2 "Search definitions"]
   [:hr]
   (elem/link-to
    {:class "btn btn-primary"} "/" "Home")
   (elem/link-to
    {:class "btn btn-primary"} "/add" "Add definition")
   (elem/link-to
    {:class "btn btn-primary"} "/search" "Search")
   (elem/link-to
    {:class "btn btn-primary"} "/help" "Help")
   [:hr]
   (form/form-to
    [:post "/search"]
    [:h3 "Search definition"
     [:br] (form/text-field "name")]
    (form/submit-button "search"))))

(defn search-results-template [results]
  (wrap-bootstrap
   "Search | NTLJR"
   [:h2 "Search results"]
   [:hr]
   (elem/link-to
    {:class "btn btn-primary"} "/" "Home")
   (elem/link-to
    {:class "btn btn-primary"} "/add" "Add definition")
   (elem/link-to
    {:class "btn btn-primary"} "/search" "Search")
   (elem/link-to
    {:class "btn btn-primary"} "/help" "Help")
   [:hr]
   results))

(defn help-template []
  (wrap-bootstrap
   "Help | NTLJR"
   [:h2 "Help"]
   [:hr]
   (elem/link-to
    {:class "btn btn-primary"} "/" "Home")
   (elem/link-to
    {:class "btn btn-primary"} "/add" "Add definition")
   (elem/link-to
    {:class "btn btn-primary"} "/search" "Search")
   (elem/link-to
    {:class "btn btn-primary"} "/help" "Help")
   [:hr]))
