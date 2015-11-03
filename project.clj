(defproject ntljr "0.1.0"
  :description "definition-based wiki-system with blackjack and beautiful women"
  :url "http://www.nowhere.net"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [;; base
                 [prismatic/schema "1.0.3"]
                 [markdown-clj "0.9.78"]
                 ;; infrastructure
                 [com.novemberain/monger "3.0.1"]
                 ;; web
                 [compojure "1.4.0"]
                 [hiccup "1.0.5"]
                 [ring/ring-jetty-adapter "1.4.0"]
                 ;; ui
                 ;; TODO: add cljs libs
                 ]
  :main ntljr.web)
