(ns ntljr.views
  (:require [hiccup.page :as page]
            [hiccup.form :as form]
            [hiccup.element :as elem]
            [clojure.java.io :as io]))

(def ^:const colorset
  ["teal darken-3" "grey darken-3" "red darken-4" "blue darken-4"
   "green darken-4" "blue-grey darken-2"])

(def ^:const colorset-len (count colorset))

(defn get-random-color []
  (colorset (rand-int colorset-len)))

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
    here we are, children of the Internet who continue to seek conciseness,
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

(defn unautomate
  "turn off auto(complete|correct|capitalize) text input features"
  [attrmap]
  (assoc attrmap :autocomplete "off" :autocorrect "off" :autocapitalize "off"))

(defn wrap-main-template [title & contents]
  (page/html5
   [:head
    [:title title]
    [:link {:href "http://fonts.googleapis.com/icon?family=Material+Icons"
            :rel "stylesheet"}]
    [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
    (page/include-css "css/materialize.min.css")
    (page/include-css "css/custom.css")]
   [:body
    (page/include-js "https://code.jquery.com/jquery-2.1.1.min.js")
    (page/include-js "js/materialize.min.js")
    [:header
     [:div {:class "navbar-fixed"}
      [:nav
       [:div {:class "nav-wrapper indigo lighten-1 hide-on-large-only"}
        (form/form-to
         [:post "/search"]
         [:div {:class "input-field"}
          [:input (unautomate {:id "lookup_mobile" :name "name"
                               :type "search" :required true})]
          [:label {:for "lookup_mobile"}
           [:i {:class "material-icons"} "search"]]])]
       [:div {:class "nav-wrapper indigo lighten-1 hide-on-med-and-down"}
        (elem/link-to
         {:class "brand-logo hide-on-med-and-down"} "/"
         [:img {:src "images/logo.png" :style "width:350px; height:85px;"}])
        [:ul {:class "right hide-on-med-and-down"}
         [:li (form/form-to
               [:post "/search"]
               [:div {:class "input-field"}
                [:input (unautomate {:id "srch" :name "name" :type "search"
                                     :required true :placeholder "LOOK UP"})]
                [:label {:for "srch"}
                 [:i {:class "material-icons"} "search"]]
                [:i {:class "material-icons"} "close"]])]
         [:li (elem/link-to "/" "HOME")]
         [:li (elem/link-to "/add" "NEW")]
         [:li (elem/link-to "/about" "ABOUT")]
         [:li (elem/link-to "#" "LOGIN")]
         [:li (elem/link-to "#" "SIGNUP")]]]]]]
    [:main contents]
    [:div {:class "fixed-action-btn horizontal"}
     [:a {:class "btn-floating btn-large waves-effect waves-light teal"
          :style "bottom: 45px; right: 24px;"}
      [:i {:class "material-icons"} "mode_edit"]]
     [:ul
      [:li [:a {:class "btn-floating teal" :href "/"}
                [:i {:class "material-icons"} "home"]]]
      [:li [:a {:class "btn-floating teal" :href "/add"}
                [:i {:class "material-icons"} "add"]]]
      [:li [:a {:class "btn-floating teal" :href "/about"}
                [:i {:class "material-icons"} "info"]]]]]
    [:div {:class "footer-copyright indigo"}
     [:div {:class "container"}
      [:p {:class "white-text"}  "NTL;JR © 2034 whythat"]]]]))

(defn make-card [definition]
  (if definition
    [:div {:class "row"}
     [:div {:class (str "card " (get-random-color))}
      [:div {:class "card-content white-text"}
       [:span {:class "card-title"} (:name definition)]
       [:p (:text definition)]]
      [:div {:class "card-action"}
       [:a {:href "#"}
        [:i {:class "material-icons"} "thumb_up"] (:rating definition)]]]]
    ""))

(defn home-template []
  (wrap-main-template
   "Home | NTLJR"
   [:div {:class "container"}
    [:h2 {:class "header"} "Welcome to" [:b " NTL;JR "]]
    [:div {:class "card-panel teal lighten-5"}
     home-p1
     home-p2]]))

(defn add-template [& {:keys [definition] :or {definition :empty}}]
  (wrap-main-template
   "Define | NTLJR"
   (form/form-to
    [:post "/add"]
    [:div {:class "container"}
     [:h2 {:class "header"} "Define"]
     [:div {:class "row"}
      [:div {:class "col s6"}
       [:div {:class "card-panel teal lighten-5"}
        [:div {:class "row"}
         [:div {:class "input-field"}
          (form/text-field (unautomate {:id "def_name_field"}) "name")
          (form/label {:for "def_name_field"} "def_name" "Name")]]
        [:div {:class "row"}
         [:div {:class "input-field"}
          (form/text-field (unautomate {:id "def_image_field"}) "image")
          (form/label {:for "def_image_field"} "def_image" "Illustrate")]]
        [:div {:class "row"}
         [:div {:class "input-field"}
          (form/text-area
           (unautomate {:id "def_textarea" :class "materialize-textarea"}) "text")
          (form/label {:for "def_textarea"} "def_text" "Text")]]
        (form/submit-button {:class "btn waves-effect waves-light"} "Save")]]
      [:div {:class "col s6"}
       [:div {:class "row"}
        (case definition
          nil "Bad definition specification"
          :empty ""
          (make-card definition))]]]])))

(defn search-template [& {:keys [results] :or nil}]
  (wrap-main-template
   "Search | NTLJR"
   [:div {:class "container"}
    [:h2 {:class "header text-teal darken-1"} "Search"]
    (if results
      (map (fn [[r1 r2]]
             [:div {:class "row"} (make-card r1) (make-card r2)])
           (partition 2 2 "" results)))
    ;; [:div {:class "row"}
    ;;  [:div {:class "input-field col s6"}
    ;;   (form/form-to
    ;;    [:post "/search"]
    ;;    (form/text-field {:id "def_name_field"} "name")
    ;;    (form/label {:for "def_name_field"} "def_name" "Name")
    ;;    (form/submit-button {:class "btn waves-effect waves-light"} "search"))] 
    ;;  [:div {:class "col s6"}
    ;;   (if results
    ;;     (map (fn [res]
    ;;            (make-card res))
    ;;          results))]]
    ]))

(defn about-template []
  (wrap-main-template
   "About | NTLJR"
   [:div {:class "container"}
    [:h2 "Help"]]))

(defn not-found-template []
  (wrap-main-template
   "Not found | NTLJR"
   [:div {:class "container"}
    [:h2 "Oops... 404, dear."]]))
