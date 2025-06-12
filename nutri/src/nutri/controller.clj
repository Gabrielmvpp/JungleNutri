;; src/nutri/controller.clj
(ns nutri.controller
  (:require
   [nutri.db.storage            :as db]
   [nutri.api.alimento-api      :as api-alim]
   [nutri.api.exercicio-api     :as api-exer]))

(defn handle-usuario-post [body]
  (println ">>> DEBUG handle-usuario-post body:" body)
  (try
    (if (every? some? [(:altura body) (:peso body) (:idade body) (:sexo body)])
      (let [h (Double/parseDouble (str (:altura body)))
            p (Double/parseDouble (str (:peso    body)))
            i (Integer/parseInt    (str (:idade   body)))
            s (str                   (:sexo    body))]
        (db/gravar-usuario! {:altura h :peso p :idade i :sexo s})
        {:usuario (db/obter-usuario)})
      {:erro "Parâmetros obrigatórios: altura, peso, idade e sexo."})
    (catch Exception e
      {:erro (str "Erro ao gravar usuário: " (.getMessage e))})))

(defn handle-usuario-get []
  (println ">>> DEBUG handle-usuario-get")
  (try
    (if-let [u (db/obter-usuario)]
      {:usuario u}
      {:erro "Nenhum usuário cadastrado."})
    (catch Exception e
      {:erro (str "Erro ao consultar usuário: " (.getMessage e))})))

(defn handle-alimento-post
  "POST /transacao/alimento — grava com a data passada."
  [{:keys [date nome quantidade]}]
  (println ">>> DEBUG alimento" date nome quantidade)
  (let [q   (Double/parseDouble (str quantidade))
        res (api-alim/buscar-calorias nome q)
        c   (:calorias res)]
    (if c
      (db/adicionar-transacao! "alimento" nome q c date)
      res)))

(defn handle-exercicio-post
  "POST /transacao/exercicio — pega peso do usuário e grava com data."
  [{:keys [date nome minutos]}]
  (println ">>> DEBUG exercicio" date nome minutos)
  (let [usuario  (db/obter-usuario)
        peso     (:peso usuario)
        mins     (Double/parseDouble (str minutos))
        api-res  (api-exer/calorias-queima nome mins peso)
        c-total  (:calorias-totais api-res)]
    (if c-total
      (db/adicionar-transacao! "exercicio" nome mins (- c-total) date)
      api-res)))

(defn handle-extrato-get [{:strs [data-ini data-fim]}]
  (println ">>> DEBUG extrato:" data-ini data-fim)
  (if (and data-ini data-fim)
    {:extrato (db/filtrar-transacoes-por-periodo
               (db/listar-transacoes)
               data-ini data-fim)}
    {:erro "Parâmetros obrigatórios: data-ini e data-fim."}))


(defn handle-saldo-get
  "GET /transacao/saldo?data-ini=...&data-fim=...
   → {:consumidas n, :gastas m, :saldo (n-m), :status s} ou {:erro ...}."
  [{:strs [data-ini data-fim]}]
  (println ">>> DEBUG saldo:" data-ini data-fim)
  (if (and data-ini data-fim)
    (let [txs      (db/filtrar-transacoes-por-periodo (db/listar-transacoes)
                                                      data-ini data-fim)
          consumidas (->> txs
                          (filter #(pos? (:calorias %)))
                          (map :calorias)
                          (reduce + 0))
          gastas     (->> txs
                          (filter #(neg? (:calorias %)))
                          (map :calorias)
                          (reduce + 0)
                          Math/abs)
          saldo      (- consumidas gastas)
          status     (if (neg? saldo) "Perda líquida" "Ganho líquido")]
      {:consumidas consumidas
       :gastas     gastas
       :saldo      saldo
       :status     status})
    {:erro "Parâmetros obrigatórios: data-ini e data-fim."}))

