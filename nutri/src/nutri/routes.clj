(ns nutri.routes
  (:require [compojure.core      :refer [defroutes GET POST]]
            [compojure.route     :as route]
            [ring.util.response  :refer [resource-response]]
            [ring.middleware.params      :refer [wrap-params]]
            [ring.middleware.json        :refer [wrap-json-body wrap-json-response]]
            [nutri.controller.transacao-controller :as ctl]))

(defroutes app-routes
  (GET "/" [] (resource-response "index.html" {:root "public"}))

  (POST "/usuario" {body :body}
    (ctl/handle-usuario-post body))
  (GET  "/usuario"  {params :query-params}
    (ctl/handle-usuario-get))

  (POST "/transacao/alimento" {body :body}
    (ctl/handle-alimento-post body))
  (POST "/transacao/exercicio" {body :body}
    (ctl/handle-exercicio-post body))
  (GET  "/transacao/extrato" {params :query-params}
    (ctl/handle-extrato-get params))
  (GET  "/transacao/saldo" {params :query-params}
    (ctl/handle-saldo-get params))

  (route/not-found {:erro "Endpoint não encontrado."}))

(def app
  (-> app-routes
      (wrap-json-body {:keywords? true})
      (wrap-params)
      (wrap-json-response)))
