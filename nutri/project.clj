(defproject nutri "0.1.0-SNAPSHOT"
  :description "Calculadora de Calorias – Front + API em Clojure"
  :url "http://example.com/nutri"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure       "1.10.3"]
                 [compojure                 "1.6.2"]
                 [ring/ring-core            "1.9.6"]
                 [ring/ring-jetty-adapter   "1.9.6"]
                 [ring/ring-json            "0.5.1"]
                 [ring/ring-defaults        "0.3.3"]
                 [hiccup                    "1.0.5"]
                 [clj-http                  "3.12.3"]
                 [cheshire                  "5.10.0"]
                 [org.clojure/tools.logging "1.2.4"]]
  :main ^:skip-aot nutri.core
  :resource-paths ["resources"]
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
