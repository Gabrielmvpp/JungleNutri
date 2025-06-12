(ns nutri.service.transacao-service
  (:require
   [nutri.api.alimento-api   :as api-alim]
   [nutri.api.exercicio-api  :as api-exer]
   [nutri.db.storage         :as db]))

(defn registrar-alimento [nome quantidade]
  (let [res (api-alim/buscar-calorias nome quantidade)]
    (if-let [c (:calorias res)]
      (assoc (db/adicionar-transacao! "alimento" nome quantidade c)
             :tx-status "ok")
      res)))

(defn registrar-exercicio [nome minutos peso]
  (let [res (api-exer/calorias-queima nome minutos peso)]
    (if-let [c (:calorias-totais res)]
      (assoc (db/adicionar-transacao! "exercicio" nome minutos (- c))
             :tx-status "ok")
      res)))

(defn obter-extrato [d1 d2]
  (db/filtrar-transacoes-por-periodo (db/listar-transacoes) d1 d2))

(defn obter-saldo [d1 d2]
  {:saldo (db/total-calorias-por-periodo d1 d2)})
