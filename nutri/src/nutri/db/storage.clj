;; src/nutri/db/storage.clj
(ns nutri.db.storage
  (:require [nutri.db.util :as util]))

(def usuario-atom    (atom nil))
(def transacoes-atom (atom []))
(def id-counter      (atom 0))

(defn novo-id [] (swap! id-counter inc))

(defn gravar-usuario! [m]
  (reset! usuario-atom m))

(defn obter-usuario [] @usuario-atom)

(defn adicionar-transacao!
  ([tipo nome quantidade calorias]
   (adicionar-transacao! tipo nome quantidade calorias (util/agora-str)))
  ([tipo nome quantidade calorias date-str]
   (let [tx {:id         (novo-id)
             :tipo       tipo
             :nome       nome
             :quantidade quantidade
             :calorias   calorias
             :data-hora  date-str}]
     (swap! transacoes-atom conj tx)
     tx)))

(defn listar-transacoes [] @transacoes-atom)

(defn filtrar-transacoes-por-periodo [txs d1 d2]
  (let [dt1 (util/parse-datetime d1)
        dt2 (util/parse-datetime d2)]
    (filter (fn [{:keys [data-hora]}]
              (when-let [d (util/parse-datetime data-hora)]
                (util/between? dt1 d dt2)))
            txs)))

(defn total-calorias-por-periodo [d1 d2]
  (->> (filtrar-transacoes-por-periodo (listar-transacoes) d1 d2)
       (map :calorias)
       (reduce + 0)))
