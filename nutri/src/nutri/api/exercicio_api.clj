(ns nutri.api.exercicio-api
  (:require [clj-http.client :as http]))

(def api-key "wRMMontHvh8DrTPa7tyCgQ==rMUunoLI6auqp4XT")

(defn calorias-queima [nome minutos peso]
  (let [url    "https://api.api-ninjas.com/v1/caloriesburned"
        params {:query-params {"activity" nome
                               "weight"   peso
                               "duration" minutos}
                :headers      {"X-Api-Key" api-key}
                :as           :json}
        dados  (:body (http/get url params))]
    (if (seq dados)
      (let [r (first dados)]
        {:atividade         (:name r)
         :calorias-por-hora (:calories_per_hour r)
         :duracao-minutos   (:duration_minutes r)
         :calorias-totais   (:total_calories r)})
      {:erro "Atividade não encontrada"})))
