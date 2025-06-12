(defproject nutri "0.1.0-SNAPSHOT"
  :description "Calculadora de Calorias – Front + API em Clojure/ClojureScript"
  :url "http://example.com/nutri"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}

  ;; Dependências
  :dependencies [[org.clojure/clojure       "1.10.3"]
                 [org.clojure/clojurescript "1.10.773"]
                 [compojure                 "1.6.2"]
                 [ring/ring-core            "1.9.6"]
                 [ring/ring-jetty-adapter   "1.9.6"]
                 [ring/ring-json            "0.5.1"]
                 [ring/ring-defaults        "0.3.3"]
                 [hiccup                    "1.0.5"]
                 [clj-http                  "3.12.3"]
                 [cheshire                  "5.10.0"]
                 [garden                    "1.3.10"]
                 [org.clojure/tools.logging "1.2.4"]]

  ;; Plugin para compilar ClojureScript
  :plugins [[lein-cljsbuild "1.1.8"]]

  ;; ajustado para olhar diretamente em src/
  :source-paths ["src"]
  :resource-paths ["resources"]
  :clean-targets ^{:protect false} ["target" "resources/public/js"]

  :cljsbuild
  {:builds
   [{:id           "app"
     :source-paths ["src"]       ;; aqui o cljs também olha em src/, desde que o namespace esteja em src/nutri/frontend.cljs
     :compiler     {:output-to     "resources/public/js/app.js"
                    :optimizations :whitespace
                    :pretty-print  true}}]}

  :main ^:skip-aot nutri.core
  :target-path "target/%s"

  :profiles {:uberjar {:aot :all}})
