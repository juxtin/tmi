(defproject tmi "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.1.6"]
                 [hiccup "1.0.5"]
                 [ring-server "0.3.1"]
                 [cheshire "5.4.0"]
                 [clj-time "0.9.0"]
                 [environ "1.0.0"]]
  :plugins [[lein-ring "0.8.12"]]
  :min-lein-version "2.5.0"
  :ring {:handler tmi.handler/app
         :init tmi.handler/init
         :destroy tmi.handler/destroy}
  :profiles
  {:uberjar {:aot :all}
   :production
   {:ring
    {:open-browser? false, :stacktraces? false, :auto-reload? true}}
   :dev
   {:dependencies [[ring-mock "0.1.5"] [ring/ring-devel "1.3.1"]]}})
