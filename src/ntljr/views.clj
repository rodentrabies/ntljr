(ns ntljr.views
  (:require [hiccup.page :as page]
            [hiccup.form :as form]
            [hiccup.element :as elem]
            [clojure.java.io :as io]))

(def ^:const color-names
  ["teal"])

(defn wrap-bootstrap [title & contents]
  (page/html5
   [:head
    [:title title]
    [:link {:href "http://fonts.googleapis.com/icon?family=Material+Icons" :rel "stylesheet"}]
    [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
    (page/include-css "css/materialize.min.css")
    (page/include-css "css/custom.css")]
   [:body
    (page/include-js "https://code.jquery.com/jquery-2.1.1.min.js")
    (page/include-js "js/materialize.min.js")
    [:header
     [:nav {:class "hide-on-med-and-down indigo lighten-1"}
      [:div {:class "nav-wrapper"}
       (elem/link-to {:class "brand-logo"} "/" "&nbsp;NTL;JR&nbsp;")
       [:ul {:id "nav-mobile" :class "right hide-on-med-and-down"}
        [:li (elem/link-to "/" "HOME")]
        [:li (elem/link-to "/add" "NEW")]
        [:li (elem/link-to "/search" "SEARCH")]
        [:li (elem/link-to "/help" "HELP")]
        [:li (elem/link-to "#" "LOGIN")]
        [:li (elem/link-to "#" "SIGNUP")]]
       ;; [:a {:href "#" :data-activates "slide-out" :class "button-collapse"}
       ;;  [:i {:class"mdi-navigation-menu"}]]
       ]]]
    [:main contents]
    [:footer {:class "page-footer indigo lighten-1" :height 10}
     [:div {:class "container"}
      [:div {:class "row"}
       [:div {:class "col l6 s12"}
        [:h5 {:class "white-text"} "NTL;JR"]
        [:p {:class "grey-text text-lighten-4"}
         "Define, learn, compete..."]]]]
     [:div {:class "footer-copyright indigo"}
      [:div {:class "container"}
       "© 2034 whythat"]]]]))

(defn home-template []
  (wrap-bootstrap
   "Home | NTLJR"
   [:div {:class "container"}
    [:h2 {:class "header"} "Where the knowledge begins"]
    [:a {:class "btn-floating btn-large waves-effect waves-light red"
         :style "bottom: 45px; right: 24px;"
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

(defn add-response-template [name]
  (wrap-bootstrap
   "Define | NTLJR"
   [:div {:class "container"}
    [:div {:class "row"}
     [:div {:class "col s12 m6"}
      [:div {:class "card green darken-1"}
       [:div {:class "card-content white-text"}
        [:span {:class "card-title"} (str "You just defined " name)]]]]]]))

(defn make-card [name definition image]
  [:div {:class "row"}
   [:div {:class "card blue-grey darken-1"}
    [:div {:class "card-content white-text"}
     [:span {:class "card-title"} name]
     [:p definition]]
    [:div {:class "card-action"}
     [:a {:href "#"}
      [:i {:class "material-icons"} "thumb_up"]]]]])

(defn search-template [& {:keys [results] :or nil}]
  (wrap-bootstrap
   "Search | NTLJR"
   [:div {:class "container"}
    [:h2 {:class "header text-teal darken-1"} "Search"]
    [:div {:class "row"}
     [:div {:class "input-field col s6"}
      (form/form-to
       [:post "/search"]
       (form/text-field {:id "def_name_field"} "name")
       (form/label {:for "def_name_field"} "def_name" "Name")
       (form/submit-button {:class "btn waves-effect waves-light"} "search"))] 
     [:div {:class "col s6"}
      (if results
        (map (fn [res]
               (make-card (:name res) (:text res) (:image res)))
             results))]]]))

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

