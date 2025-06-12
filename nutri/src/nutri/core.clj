(ns nutri.core
  (:gen-class)
  (:require
   [ring.adapter.jetty       :refer [run-jetty]]
   [ring.middleware.resource :refer [wrap-resource]]
   [nutri.routes             :refer [app]]
   [clojure.tools.logging    :as log]))

(defn -main [& _]
  (log/info "Iniciando Nutri na porta 3000...")
  (run-jetty
   (-> app
       (wrap-resource "public"))
   {:port 3000 :join? true}))
