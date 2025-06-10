(defproject nutri "0.1.0-SNAPSHOT"
  :description "Calculadora de Calorias (com front-end web minimalista)"
  :url "http://exemplo.local"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure       "1.10.3"]
                 [ring/ring-core            "1.9.6"]
                 [ring/ring-jetty-adapter   "1.9.6"]
                 [ring/ring-json            "0.5.1"]
                 [compojure                 "1.6.2"]
                 [cheshire                  "5.10.0"]
                 [clj-http                  "3.12.3"]
                 [org.clojure/tools.logging "1.2.4"]]
  :resource-paths ["resources"]
  :main ^:skip-aot nutri.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
