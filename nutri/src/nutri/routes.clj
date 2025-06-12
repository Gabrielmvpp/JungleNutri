;; src/nutri/routes.clj
(ns nutri.routes
  (:require
   [compojure.core            :refer [defroutes GET POST]]
   [compojure.route           :as   route]
   [ring.middleware.json      :refer [wrap-json-body]]
   [ring.middleware.params    :refer [wrap-params]]    ;; aqui tinha typo
   [ring.util.response        :refer [response content-type]]
   [cheshire.core             :as   json]
   [nutri.views               :refer [home-page]]
   [nutri.controller          :as   ctl]))

(defn json-response [data]
  (-> (json/generate-string data)
      response
      (content-type "application/json; charset=utf-8")))

(defroutes app-routes
  (GET "/" []
    (-> (home-page)
        response
        (content-type "text/html; charset=utf-8")))

  (POST "/usuario"             {body :body}          (json-response (ctl/handle-usuario-post    body)))
  (GET  "/usuario"             []                     (json-response (ctl/handle-usuario-get)))

  (POST "/transacao/alimento"  {body :body}          (json-response (ctl/handle-alimento-post   body)))
  (POST "/transacao/exercicio" {body :body}          (json-response (ctl/handle-exercicio-post  body)))
  (GET  "/transacao/extrato"   {params :query-params} (json-response (ctl/handle-extrato-get    params)))
  (GET  "/transacao/saldo"     {params :query-params} (json-response (ctl/handle-saldo-get      params)))

  (route/not-found
   (json-response {:erro "Endpoint não encontrado."})))

(def app
  (-> app-routes
      (wrap-json-body {:keywords? true})
      wrap-params))
