(ns nutri.core
  (:gen-class)
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.resource :refer [wrap-resource]]
            [nutri.routes :refer [app]]
            [clojure.tools.logging :as log]))

(defn -main
  "Ponto de entrada. Inicia o servidor Jetty na porta 3003."
  [& args]
  (log/info "Iniciando API e Frontend em Clojure na porta 3003...")
  (run-jetty
   (-> app
       (wrap-resource "public"))
   {:port 3003 :join? true}))
