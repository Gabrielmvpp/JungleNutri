(ns nutri.controller.transacao-controller
  (:require [nutri.db.storage            :as db]
            [nutri.service.transacao-service :as svc]
            [cheshire.core               :as json])
  (:import (java.time LocalDateTime)))

(defn handle-usuario-post
  "Botão POST /usuario
   Espera JSON body com chaves: altura, peso, idade, sexo (valores Strings ou Numbers).
   Converte para número quando necessário e grava no DB.
   Retorna mapa JSON com usuário ou {:erro ...}."
  [body-map]
  (try
    (let [altura (Double/parseDouble (str (:altura body-map)))
          peso   (Double/parseDouble (str (:peso body-map)))
          idade  (Integer/parseInt (str (:idade body-map)))
          sexo   (str (:sexo body-map))
          usuario {:altura altura :peso peso :idade idade :sexo sexo}
          gravado (db/gravar-usuario! usuario)]
      {:usuario gravado})
    (catch Exception e
      {:erro (str "Erro ao gravar usuário: " (.getMessage e))})))

(defn handle-usuario-get
  "GET /usuario → retorna o mapa de usuário ou nil."
  []
  (if-let [u (db/obter-usuario)]
    {:usuario u}
    {:erro "Nenhum usuário cadastrado."}))

(defn handle-alimento-post
  "POST /transacao/alimento
   Espera JSON body com: nome, quantidade (strings ou numbers).
   Chama service/registrar-alimento e retorna JSON."
  [body-map]
  (if (and (:nome body-map) (:quantidade body-map))
    (let [nome       (str (:nome body-map))
          quantidade (Double/parseDouble (str (:quantidade body-map)))]
      (svc/registrar-alimento nome quantidade))
    {:erro "Parâmetros obrigatórios: nome e quantidade."}))

(defn handle-exercicio-post
  "POST /transacao/exercicio
   Espera JSON body com: nome, minutos, peso.
   Chama service/registrar-exercicio e retorna JSON."
  [body-map]
  (if (and (:nome body-map) (:minutos body-map) (:peso body-map))
    (let [nome    (str (:nome body-map))
          minutos (Double/parseDouble (str (:minutos body-map)))
          peso    (Double/parseDouble (str (:peso body-map)))]
      (svc/registrar-exercicio nome minutos peso))
    {:erro "Parâmetros obrigatórios: nome, minutos e peso."}))

(defn handle-extrato-get
  "GET /transacao/extrato?data-ini=YYYY-MM-dd'T'HH:mm:ss&data-fim=...
   Retorna vetor de transações no período."
  [query-params]
  (let [dini (get query-params "data-ini")
        dfim (get query-params "data-fim")]
    (if (and dini dfim)
      (let [lista (svc/obter-extrato dini dfim)]
        {:extrato lista})
      {:erro "Parâmetros obrigatórios: data-ini e data-fim (ISO-8601)."})))

(defn handle-saldo-get
  "GET /transacao/saldo?data-ini=...&data-fim=...
   Retorna mapa {:saldo número}."
  [query-params]
  (let [dini (get query-params "data-ini")
        dfim (get query-params "data-fim")]
    (if (and dini dfim)
      (svc/obter-saldo dini dfim)
      {:erro "Parâmetros obrigatórios: data-ini e data-fim (ISO-8601)."})))
