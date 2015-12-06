(ns ntljr.views
  (:require [hiccup.page :as page]
            [hiccup.form :as form]
            [hiccup.element :as elem]
            [clojure.java.io :as io]))

(defn wrap-bootstrap [title & contents]
  (page/html5
   [:head
    [:title title]
    [:link {:href "http://fonts.googleapis.com/icon?family=Material+Icons" :rel "stylesheet"}]
    (page/include-css "css/materialize.min.css")
    (page/include-css "css/custom.css")]
   [:body
    (page/include-js "https://code.jquery.com/jquery-2.1.1.min.js")
    (page/include-js "js/materialize.min.js")
    [:header
     [:nav
      [:div {:class "nav-wrapper"}
       (elem/link-to {:class "brand-logo"} "/" "&nbsp;NTL;JR&nbsp;")
       [:ul {:id "nav-mobile" :class "right hide-on-med-and-down"}
        [:li (elem/link-to "/" "Home")]
        [:li (elem/link-to "/add" "Define")]
        [:li (elem/link-to "/search" "Look Up")]
        [:li (elem/link-to "/help" "Help")]]]]]
    [:main contents]
    [:footer {:class "page-footer" :height 10}
     [:div {:class "container"}
      [:div {:class "row"}
       [:div {:class "col l6 s12"}
        [:h5 {:class "white-text"} "NTL;JR"]
        [:p {:class "grey-text text-lighten-4"}
         "Define, learn, compete..."]]]]
     [:div {:class "footer-copyright"}
      [:div {:class "container"}
       "© 2034 whythat"]]]]))

(defn home-template []
  (wrap-bootstrap
   "Home | NTLJR"
   [:div {:class "container"}
    [:h2 {:class "header"} "Where the knowledge begins"]
    [:a {:class "btn-floating btn-large waves-effect waves-light red"
         :href "/add"}
     [:i {:class "material-icons"} "add"]]]))

(defn add-template []
  (wrap-bootstrap
   "Define | NTLJR"
   (form/form-to
    [:post "/add"]
    [:div {:class "row"}
     [:div {:class "container"}
      [:h2 {:class "header"} "Define"]
      [:div {:class "row"}
       [:div {:class "input-field col s6"}
        (form/text-field {:id "def_name_field"} "name")
        (form/label {:for "def_name_field"} "def_name" "Name")]]
      [:div {:class "row"}
       [:div {:class "input-field col s6"}
        (form/text-field {:id "def_image_field"} "image")
        (form/label {:for "def_image_field"} "def_image" "Illustrate")]]
      [:div {:class "row"}
       [:div {:class "input-field col s6"}
        (form/text-area {:id "def_textarea" :class "materialize-textarea"} "text")
        (form/label {:for "def_textarea"} "def_text" "Text")]]
      (form/submit-button {:class "btn waves-effect waves-light"} "Save")]])))

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

