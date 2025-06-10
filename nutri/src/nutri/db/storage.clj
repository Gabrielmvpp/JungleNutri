(ns nutri.db.storage
  (:require [nutri.db.util :as util]))

;; Átomo que guarda o perfil do usuário (único, pois o projeto não prevê multiusuários).
;; Será um mapa com chaves :altura, :peso, :idade, :sexo.
(def usuario-atom
  (atom nil))

;; Átomo que guarda todas as transações realizadas.
;;  :id          - um UUID ou contador simples (opcional)
;;  :tipo        - "alimento" ou "exercicio"
;;  :nome        - nome do alimento ou exercício
;;  :quantidade  - se for alimento, quant em gramas; se exercício, número de minutos
;;  :calorias    - número de calorias ganhas (+) ou perdidas (−)
;;  :data-hora   - String ISO-8601 (ex.: "2025-06-03T14:22:00")
(def transacoes-atom
  (atom []))

;;(contador simples para id de transações, se quisermos)
(def ^:private id-counter (atom 0))

(defn novo-id
  "Retorna um novo ID incremental (1,2,3,...)."
  []
  (swap! id-counter inc))

(defn gravar-usuario!
  "Recebe um mapa {:altura h :peso p :idade i :sexo s}
   e faz swap! em usuario-atom. Retorna o usuário gravado."
  [{:keys [altura peso idade sexo] :as usuario-map}]
  (let [valid-keys #{:altura :peso :idade :sexo}]
    (if (and altura peso idade sexo)
      (reset! usuario-atom (select-keys usuario-map valid-keys))
      (throw (ex-info "Parâmetros inválidos para usuário" {:dados usuario-map})))))

(defn obter-usuario
  "Retorna o mapa de usuário ou nil se não existir."
  []
  @usuario-atom)

(defn adicionar-transacao!
  "Adiciona uma nova transação no transacoes-atom.
   Espera os argumentos:
   - tipo: \"alimento\" ou \"exercicio\"
   - nome: String
   - quantidade: número (Double)
   - calorias: Double (positivo para alimento, negativo para exercício)
   Adiciona também o campo :data-hora com timestamp atual.
   Retorna o mapa de transação que foi inserido."
  [tipo nome quantidade calorias]
  (let [id        (novo-id)
        data-hora (util/agora-str)
        nova-tx   {:id         id
                   :tipo       tipo
                   :nome       nome
                   :quantidade quantidade
                   :calorias   calorias
                   :data-hora  data-hora}]
    (swap! transacoes-atom conj nova-tx)
    nova-tx))

(defn listar-transacoes
  "Retorna todas as transações (vector de mapas)."
  []
  @transacoes-atom)

(defn filtrar-transacoes-por-periodo
  "Dada uma lista de transações (mapas) e duas strings data-inicio e data-fim
   (no formato ISO-8601), retorna somente aquelas cujas :data-hora estejam entre
   data-inicio e data-fim (inclusive)."
  [transacoes data-ini-str data-fim-str]
  (let [dti (util/parse-datetime data-ini-str)
        dtf (util/parse-datetime data-fim-str)]
    (->> transacoes
         (filter (fn [{dh :data-hora}]
                   (when-let [d (util/parse-datetime dh)]
                     (util/between? dti d dtf)))))))

(defn total-calorias-por-periodo
  "Soma as calorias (campo :calorias) de todas as transações no intervalo dado.
   Recebe data-ini-str e data-fim-str (ISO-8601)."
  [data-ini-str data-fim-str]
  (let [txs (filtrar-transacoes-por-periodo (listar-transacoes) data-ini-str data-fim-str)]
    (reduce #(+ %1 (:calorias %2)) 0 txs)))

(defn limpar-transacoes!
  "Limpa todo o histórico de transações."
  []
  (reset! transacoes-atom []))
