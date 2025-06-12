(ns nutri.controller.transacao-controller
  (:require
   [nutri.db.storage                :as db]
   [nutri.service.transacao-service :as svc]))

(defn handle-usuario-post [body]
  (try
    (if (and (:altura body) (:peso body) (:idade body) (:sexo body))
      (let [altura  (Double/parseDouble (str (:altura body)))
            peso    (Double/parseDouble (str (:peso body)))
            idade   (Integer/parseInt    (str (:idade body)))
            sexo    (str (:sexo body))
            usuario {:altura altura :peso peso :idade idade :sexo sexo}
            gravado (db/gravar-usuario! usuario)]
        {:usuario gravado})
      {:erro "Parâmetros obrigatórios: altura, peso, idade e sexo."})
    (catch Exception e
      {:erro (str "Erro ao gravar usuário: " (.getMessage e))})))

(defn handle-usuario-get []
  (try
    (if-let [u (db/obter-usuario)]
      {:usuario u}
      {:erro "Nenhum usuário cadastrado."})
    (catch Exception e
      {:erro (str "Erro ao consultar usuário: " (.getMessage e))})))

(defn handle-alimento-post [body]
  (try
    (if (and (:nome body) (:quantidade body))
      (let [nome       (str (:nome body))
            quantidade (Double/parseDouble (str (:quantidade body)))]
        (svc/registrar-alimento nome quantidade))
      {:erro "Parâmetros obrigatórios: nome e quantidade."})
    (catch Exception e
      {:erro (str "Erro ao registrar alimento: " (.getMessage e))})))

(defn handle-exercicio-post [body]
  (try
    (if (and (:nome body) (:minutos body) (:peso body))
      (let [nome    (str (:nome body))
            minutos (Double/parseDouble (str (:minutos body)))
            peso    (Double/parseDouble (str (:peso body)))]
        (svc/registrar-exercicio nome minutos peso))
      {:erro "Parâmetros obrigatórios: nome, minutos e peso."})
    (catch Exception e
      {:erro (str "Erro ao registrar exercício: " (.getMessage e))})))

(defn handle-extrato-get [params]
  (try
    (let [d1 (get params "data-ini")
          d2 (get params "data-fim")]
      (if (and d1 d2)
        {:extrato (svc/obter-extrato d1 d2)}
        {:erro "Parâmetros obrigatórios: data-ini e data-fim."}))
    (catch Exception e
      {:erro (str "Erro ao consultar extrato: " (.getMessage e))})))

(defn handle-saldo-get [params]
  (try
    (let [d1 (get params "data-ini")
          d2 (get params "data-fim")]
      (if (and d1 d2)
        (svc/obter-saldo d1 d2)
        {:erro "Parâmetros obrigatórios: data-ini e data-fim."}))
    (catch Exception e
      {:erro (str "Erro ao consultar saldo: " (.getMessage e))})))
