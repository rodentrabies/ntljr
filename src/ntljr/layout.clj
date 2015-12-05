(ns ntljr.layout
  (:require [hiccup.page :as page]
            [hiccup.form :as form]
            [hiccup.element :as elem]))

(defn home-template []
  (page/html5
   [:head [:title "Home | NTLJR"]]
   [:body [:h1 "NTL;JR Home: here's where the knowledge happens..."]
    [:hr]
    [:br (elem/link-to
          {:class "btn btn-primary"} "/add" "Add definition")]
    [:br (elem/link-to
          {:class "btn btn-primary"} "/search" "Search")]
    [:br (elem/link-to
          {:class "btn btn-primary"} "/help" "Help")]]))

(defn add-template []
  (page/html5
   [:head [:title "Create | NTLJR"]]
   [:body [:h1 "NTL;JR Create"]
    [:hr]
    [:br (elem/link-to
          {:class "btn btn-primary"} "/add" "Add definition")]
    [:br (elem/link-to
          {:class "btn btn-primary"} "/search" "Search")]
    [:br (elem/link-to
          {:class "btn btn-primary"} "/help" "Help")]
    [:hr]
    (form/form-to
     [:post "/add"]
     [:h3 ""
      [:br]
      (form/text-area {:rows 3 :cols 50} "AddDefinition")]
     (form/submit-button "save"))]))

(defn search-template []
  (page/html5
   [:head [:title "Search | NTLJR"]]
   [:body [:h1 "NTL;JR Search"]
    [:hr]
    [:br (elem/link-to
          {:class "btn btn-primary"} "/add" "Add definition")]
    [:br (elem/link-to
          {:class "btn btn-primary"} "/search" "Search")]
    [:br (elem/link-to
          {:class "btn btn-primary"} "/help" "Help")]
    [:hr]]))

(defn help-template []
  (page/html5
   [:head [:title "Help | NTLJR"]]
   [:body [:h1 "NTL;JR Help"]
    [:hr]
    [:br (elem/link-to
          {:class "btn btn-primary"} "/add" "Add definition")]
    [:br (elem/link-to
          {:class "btn btn-primary"} "/search" "Search")]
    [:br (elem/link-to
          {:class "btn btn-primary"} "/help" "Help")]
    [:hr]]))
