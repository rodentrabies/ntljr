(ns ntljr.views
  (:require [hiccup.page :as page]
            [hiccup.form :as form]
            [hiccup.element :as elem]
            [clojure.java.io :as io]))

(def ^:const color-names
  ["teal"])


(defn pour-file
  "Insert contents of the static file."
  [fname]
  (slurp fname))

(def ^:const home-p1
  [:p {:class "flow-text"}
   "Some time around 2003, Internet community gave birth to a pedantic phrase "
   [:b " \"too long; didn't read\" "]
   ", most often written as a famous abbreviation"
   [:b " \"tl;dr\" "]
   ". This so called"
   [:i " meme "]
   "is one of the most notable proofs, that from the perspective of the Internet generation,
    there is nothing more boring and useless as redundancy of information.
    The strive for conciseness has always been the main source of inspiration for mathematicians
    of all ages, and as mathematics are what really drives everything in the world of information,
    here we are, children of the Internet who continues to seek for conciseness,
    briefness and preciseness of the information we consume."])

(def ^:const home-p2
  [:p {:class "flow-text"}
   [:b " \"ntl;jr\" "]
   "stands for the opposite:"
   [:b " \"not too long; just read\" "]
   ". It is a system that allows users to define and illustrate notions from different
    categories of knowledge, but imposes strong restrictions on the size of the definition,
    requiring to shorten formulations as much as possible to fit into the 300 characters frame.
    With that in mind, you are welcome to share what you know."])

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
     [:div {:class "navbar-fixed"}
      [:nav
       [:div {:class "nav-wrapper indigo lighten-1"}
        (elem/link-to {:class "brand-logo"} "/"
                      [:img {:src "images/lg.png" :style "width:350px; height:85px;"}])
        [:ul {:class "right hide-on-med-and-down"}
         [:li (form/form-to
               [:post "/search"]
               [:div {:class "input-field"}
                [:input {:id "srch" :name "name" :type "search"
                         :required true :placeholder "look up"}]
                [:label {:for "srch"}
                 [:i {:class "material-icons"} "search"]]
                [:i {:class "material-icons"} "close"]])]
         [:li (elem/link-to "/" "HOME")]
         [:li (elem/link-to "/add" "NEW")]
         [:li (elem/link-to "/search" "SEARCH")]
         [:li (elem/link-to "/help" "HELP")]
         [:li (elem/link-to "#" "LOGIN")]
         [:li (elem/link-to "#" "SIGNUP")]]]]]]
    [:main contents]
    [:div {:class "fixed-action-btn"}
     [:a {:class "btn-floating btn-large waves-effect waves-light teal"
          :style "bottom: 45px; right: 24px;"
          :href "/add"}
      [:i {:class "material-icons"} "add"]]]
    [:footer {:class "page-footer indigo lighten-1" :height 3}

     [:div {:class "container"}
      [:div {:class "row"}
       [:div {:class "col s12"}
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
    [:h2 {:class "header"} "Welcome to" [:b " NTL;JR "]]
    [:div {:class "card-panel teal lighten-5"}
     home-p1
     home-p2]]))

(defn add-template [& {:keys [name] :or nil}]
  (wrap-bootstrap
   "Define | NTLJR"
   (form/form-to
    [:post "/add"]
    [:div {:class "container"}
     [:h2 {:class "header"} "Define"]
     [:div {:class "card-panel teal lighten-5"}
      [:div {:class "row"}
       [:div {:class "col s6"}
        [:div {:class "row"}
         [:div {:class "input-field"}
          (form/text-field {:id "def_name_field"} "name")
          (form/label {:for "def_name_field"} "def_name" "Name")]]
        [:div {:class "row"}
         [:div {:class "input-field"}
          (form/text-field {:id "def_image_field"} "image")
          (form/label {:for "def_image_field"} "def_image" "Illustrate")]]
        [:div {:class "row"}
         [:div {:class "input-field"}
          (form/text-area {:id "def_textarea" :class "materialize-textarea"} "text")
          (form/label {:for "def_textarea"} "def_text" "Text")]]
        (form/submit-button {:class "btn waves-effect waves-light"} "Save")]
       [:div {:class "col s6"}
        (when name
          [:div {:class "card green darken-1"}
           [:div {:class "card-content white-text"}
            [:span {:class "card-title"} (str "You just defined " name)]]])]]]])))

(defn add-response-template [name]
  (wrap-bootstrap
   "Define | NTLJR"
   [:div {:class "container"}
    [:div {:class "row"}
     [:div {:class "col s12 m6"}
      ]]]))

(defn make-card [name definition image]
  [:div {:class "row"}
   [:div {:class "card blue-grey darken-1"}
    [:div {:class "card-content white-text"}
     [:span {:class "card-title"} name]
     [:p definition]]
    [:div {:class "card-action"}
     [:a {:href "#"} [:i {:class "material-icons"} "thumb_up"] "377"]]]])

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
   [:div {:class "container"}
    [:h2 "Help"]]))

