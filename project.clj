(defproject ntljr "0.1.0"
  :description "wiki-like system with tweet-sized definitions"
  :url "http://github.com/yurizhykin/ntljr"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[prismatic/schema "1.0.3"]
                 [markdown-clj "0.9.78"]
                 [com.novemberain/monger "3.0.1"]
                 [com.cemerick/friend "0.2.1"]
                 [clj-time "0.11.0"]
                 [compojure "1.4.0"]
                 [hiccup "1.0.5"]
                 [ring/ring-jetty-adapter "1.4.0"]]
  :main ntljr.main)
