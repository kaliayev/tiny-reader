(defproject tiny-reader "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [compojure "1.5.1"]
                 [enlive "1.1.6"]
                 [cheshire "5.7.0"]
                 [hiccup "1.0.4"]
                 [io.aviso/pretty "0.1.33"]
                 [ring/ring-defaults "0.2.1"]]
  :plugins [[lein-ring "0.9.7"]
            [cider/cider-nrepl "0.14.0"]]
  :ring {:handler tiny-reader.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.0"]]}})
