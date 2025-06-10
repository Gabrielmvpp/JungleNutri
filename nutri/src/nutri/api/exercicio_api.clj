(ns nutri.api.exercicio-api
  (:require [clj-http.client :as http]
            [cheshire.core :as json]))

(def ^:private api-key "wRMMontHvh8DrTPa7tyCgQ==rMUunoLI6auqp4XT")

(defn calorias-queima
  "Dado:
     - nome (String) da atividade, ex.: \"running\"
     - duracao (Double) em minutos
     - peso (Double) em kg
   Retorna mapa:
     - se encontrado: {:atividade nome :calorias-totais Double :duracao-minutos duracao}
     - se não encontrar: {:erro \"Atividade não encontrada ou dados insuficientes.\"}"
  [nome minutos peso]
  (let [url "https://api.api-ninjas.com/v1/caloriesburned"
        params {:query-params {"activity" nome
                               "weight"   (str peso)
                               "duration" (str minutos)}
                :headers      {"X-Api-Key" api-key}
                :as           :json}
        response (try
                   (http/get url params)
                   (catch Exception e
                     {:status 500 :body nil}))
        dados    (:body response)]
    (if (and (seq dados) (vector? dados))
      (let [res (first dados)
            total-cal (:total_calories res)]
        (if total-cal
          {:atividade       (:name res)
           :calorias-por-hora (:calories_per_hour res)
           :duracao-minutos (:duration_minutes res)
           :calorias-totais total-cal}
          {:erro "Dados insuficientes na resposta da API."}))
      {:erro "Atividade não encontrada ou dados insuficientes."})))
