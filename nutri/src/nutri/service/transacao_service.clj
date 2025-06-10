(ns nutri.service.transacao-service
  (:require [nutri.api.alimento-api :as api-alim]
            [nutri.api.exercicio-api :as api-exer]
            [nutri.db.storage :as db]))

(defn registrar-alimento
  "Lê nome (String) e quantidade (Double), chama API externa para buscar calorias
   e, se sucesso, grava no DB e retorna a transação inserida.
   Se erro na API, retorna mapa {:erro ...}."
  [nome quantidade]
  (let [resultado (api-alim/buscar-calorias nome quantidade)]
    (if-let [c (:calorias resultado)]
      (let [tx (db/adicionar-transacao! "alimento" nome quantidade c)]
        (assoc tx :status "ok"))
      resultado)))  

(defn registrar-exercicio
  "Lê nome (String), minutos (Double) e peso (Double). Chama API externa para calorias queimadas.
   Se sucesso, grava no DB (calorias negativas) e retorna a transação.
   Se erro, devolve {:erro ...}."
  [nome minutos peso]
  (let [resultado (api-exer/calorias-queima nome minutos peso)]
    (if-let [c (:calorias-totais resultado)]
      (let [calorias-neg (- c)
            tx (db/adicionar-transacao! "exercicio" nome minutos calorias-neg)]
        (assoc tx :status "ok"))
      resultado)))

(defn obter-extrato
  "Retorna todas as transações no período [data-ini data-fim] (Strings ISO-8601)."
  [data-ini-str data-fim-str]
  (db/filtrar-transacoes-por-periodo (db/listar-transacoes) data-ini-str data-fim-str))

(defn obter-saldo
  "Retorna mapa {:saldo total-calorias} no período [data-ini data-fim]."
  [data-ini-str data-fim-str]
  {:saldo (db/total-calorias-por-periodo data-ini-str data-fim-str)})
