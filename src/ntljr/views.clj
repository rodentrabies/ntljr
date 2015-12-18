(ns ntljr.views
  (:require [hiccup.page :as page]
            [hiccup.form :as form]
            [hiccup.element :as elem]
            [clojure.java.io :as io]))

(def ^:const colorset
  (into [] (mapcat (fn [x] (map #(str x " darken-" %) [1 2 3 4]))
                   ["red" "purple" "deep-purple" "indigo" "blue" "light-blue"
                    "cyan" "teal" "green" "light-green" "deep-orange" "brown"
                    "grey" "blue-grey"])))

(def ^:const colorset-len (count colorset))

(defn get-random-color [text]
  (colorset (mod (hash text) colorset-len)))

(def ^:const home-p1
  [:p {:class "flow-text"}
   "Some time around 2003, Internet community gave birth to a pedantic phrase "
   [:b " \"too long; didn't read\" "]
   ", most often written as a famous abbreviation"
   [:b " \"tl;dr\" "]
   ". This so called"
   [:i " meme "]
   "is one of the most notable proofs, that from the perspective of the Internet
    generation, there is nothing more boring and useless as redundancy of
    information. The strive for conciseness has always been the main source of
    inspiration for mathematicians of all ages, and as mathematics are what
    really drives everything in the world of information, here we are, children
    of the Internet who continue to seek conciseness, briefness and preciseness
    of the information we consume."])

(def ^:const home-p2
  [:p {:class "flow-text"}
   [:b " \"ntl;jr\" "]
   "stands for the opposite:"
   [:b " \"not too long; just read\" "]
   ". It is a system that allows users to define and illustrate notions from
    different categories of knowledge, but imposes strong restrictions on the
    size of the definition, requiring to shorten formulations as much as possible
    to fit into the 300 characters frame. With that in mind, you are welcome to
    share what you know."])

(defn unautomate
  "turn off auto(complete|correct|capitalize) text input features"
  [attrmap]
  (assoc attrmap :autocomplete "off" :autocorrect "off" :autocapitalize "off"))

(defn wrap-main-template [user title & contents]
  (page/html5
   [:head
    [:title title]
    [:link {:href "http://fonts.googleapis.com/icon?family=Material+Icons"
            :rel "stylesheet"}]
    [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
    (page/include-css "/css/materialize.min.css")
    (page/include-css "/css/custom.css")]
   [:body
    (page/include-js "https://code.jquery.com/jquery-2.1.1.min.js")
    (page/include-js "/js/materialize.min.js")
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
         [:img {:src "/images/logo.png" :style "width:350px; height:85px;"}])
        `[:ul {:class "right hide-on-med-and-down"}
          ~[:li (form/form-to
                 [:post "/search"]
                 [:div {:class "input-field"}
                  [:input (unautomate {:id "srch" :name "name" :type "search"
                                       :required true :placeholder "LOOK UP"})]
                  [:label {:for "srch"}
                   [:i {:class "material-icons"} "search"]]
                  [:i {:class "material-icons"} "close"]])]
          ~[:li (elem/link-to "/" "  HOME ")]
          ~[:li (elem/link-to "/about" " ABOUT")]
          ~@(if user
              [[:li (elem/link-to "/add" "  NEW ")]
               [:li (elem/link-to "/logout" "LOGOUT")]]
              [[:li (elem/link-to "/login" " LOGIN")]
               [:li (elem/link-to "/signup" "SIGNUP")]])]]]]]
    [:main contents]
    [:div {:class "fixed-action-btn horizontal"}
     [:a {:class "btn-floating btn-large waves-effect waves-light teal"
          :style "bottom: 45px; right: 24px;"}
      [:i {:class "material-icons"} "mode_edit"]]
     `[:ul
       ~[:li [:a {:class "btn-floating teal" :href "/"}
             [:i {:class "material-icons"} "home"]]]
       ~[:li [:a {:class "btn-floating teal" :href "/about"}
             [:i {:class "material-icons"} "info"]]]
       ~@(if user
           [[:li [:a {:class "btn-floating teal" :href "/add"}
                  [:i {:class "material-icons"} "add"]]]
            [:li [:a {:class "btn-floating teal" :href "/logout"}
                  [:i {:class "material-icons"} "input"]]]]
           [[:li [:a {:class "btn-floating teal" :href "/login"}
                  [:i {:class "material-icons"} "verified_user"]]]
            [:li [:a {:class "btn-floating teal" :href "/signup"}
                  [:i {:class "material-icons"} "perm_identity"]]]])]]
    [:div {:class "footer-copyright indigo"}
     [:div {:class "container"}
      [:p {:class "white-text"} "NTL;JR © 2015 whythat"]]]]))

(defn make-card [definition name]
  (if definition
    [:div {:class "row"}
     [:div {:class (str "card " (get-random-color (:_id definition)))}
      [:div {:class "card-content white-text"}
       [:span {:class "card-title"} (:name definition)]
       [:p {:class "white-text"}
        (:author definition) " @ " (:crdate definition)]
       [:hr]
       [:p (:text definition)]]
      [:div {:class "card-action"}
       (if (not= (:rating definition) " ∞ ")
         [:form {:action (str "/search/" name) :method "post"}
          [:button {:type "submit" :class "btn-flat waves-effect"
                    :name "rate" :value (:_id definition)}
           [:i {:class "large material-icons white-text"} "thumb_up"]
           [:b {:class "white-text"} (str " " (:rating definition))]]]
         [:div
          [:i {:class "material-icons white-text"} "thumb_up"]
          [:b {:class "white-text"} (str " " (:rating definition))]])]]]
    ""))

(defn home-template [user]
  (wrap-main-template
   user
   "Home | NTLJR"
   [:div {:class "container"}
    [:h2 {:class "header"}
     [:b {:class "teal-text"} "NTL;JR:"]
     "  welcome"]
    [:div {:class "card-panel teal lighten-5"} home-p1 home-p2]]))

(defn add-template [user & {:keys [definition] :or {definition :empty}}]
  (wrap-main-template
   user
   "Define | NTLJR"
   (form/form-to
    [:post "/add"]
    [:div {:class "container"}
     [:h2 {:class "header"}
      [:b {:class "teal-text"} "NTL;JR:"]
      "  define piece of your world"]
     [:div {:class "row"}
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
     [:div {:class "row"}
      (case definition
        nil "Bad definition specification"
        :empty ""
        [:div {:class (str "card " (get-random-color (:text definition)))}
         [:div {:class "card-content white-text"}
          [:span {:class "card-title"} (:name definition)]
          [:p
           "You've just defined \""
           [:b (:name definition)]
           "\". Thanks for sharing!"]]])]])))

(defn search-template [user & {:keys [results name] :or nil}]
  (wrap-main-template
   user
   "Search | NTLJR"
   [:div {:class "container"}
    (if results
      (map (fn [res]
             (make-card res name))
           results))]))

(defn about-template [user]
  (wrap-main-template
   user
   "About | NTLJR"
   [:div {:class "container"}
    [:h2 {:class "header"}
     [:b {:class "teal-text"} "NTL;JR:"]
     "  about us - still deciding..."]]))

(defn auth-error-card [msg]
  (when msg
    [:div {:class "row"}
     [:div {:class "card black"}
      [:div {:class "card-content white-text"}
       [:span {:class "card-title"} "Whoops!"]
       [:p msg]]]]))

(defn login-template [user err]
  (wrap-main-template
   user
   "Login | NTLJR"
   (form/form-to
    [:post "/login"]
    [:div {:class "container"}
     [:h2 {:class "header"}
      [:b {:class "teal-text"} "NTL;JR:"]
      "  login"]
     [:div {:class "row"}
      [:div {:class "card-panel teal lighten-5"}
       [:div {:class "row"}
        [:div {:class "input-field"}
         (form/text-field (unautomate {:id "uname_field"}) "uname")
         (form/label {:for "uname_field"} "un" "Username")]]
       [:div {:class "row"}
        [:div {:class "input-field"}
         (form/text-field (unautomate {:id "pwd_field" :type "password"}) "pwd")
         (form/label {:for "pwd_field"} "pw" "Password")]]
       (form/submit-button {:class "btn waves-effect waves-light"} "Login")]]
     (auth-error-card err)])))

(defn signup-template [user err]
  (wrap-main-template
   user
   "Sign Up | NTLJR"
   (form/form-to
    [:post "/signup"]
    [:div {:class "container"}
     [:h2 {:class "header"}
      [:b {:class "teal-text"} "NTL;JR:"]
      "  sign up"]
     [:div {:class "row"}
      [:div {:class "card-panel teal lighten-5"}
       [:div {:class "row"}
        [:div {:class "input-field"}
         (form/text-field (unautomate {:id "uname_field"}) "uname")
         (form/label {:for "uname_field"} "uname_lbl" "Username")]]
       [:div {:class "row"}
        [:div {:class "input-field"}
         (form/text-field (unautomate {:id "pwd1_field" :type "password"}) "p1")
         (form/label {:for "pwd1_field"} "pwd1" "Password")]]
       [:div {:class "row"}
        [:div {:class "input-field"}
         (form/text-field (unautomate {:id "pwd2_field" :type "password"}) "p2")
         (form/label {:for "pwd2_field"} "pwd2" "Repeat password")]]
       (form/submit-button {:class "btn waves-effect waves-light"} "Sign Up")]]
     (auth-error-card err)])))

(defn not-found-template [user]
  (wrap-main-template
   user
   "Not found | NTLJR"
   [:div {:class "container"}
    [:h2 "Oops... 404, dear."]]))
