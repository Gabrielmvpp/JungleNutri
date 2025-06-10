(ns nutri.api.alimento-api
  (:require [clj-http.client :as http]
            [cheshire.core :as json]))

(def ^:private api-key "Q1QCpllMUos1VCXtbDZTQgtc5tPIntZYyiY7E2PO")

(defn buscar-calorias
  "Dado um nome de alimento (String) e quantidade em gramas (Double),
   faz chamada à API USDA e retorna um mapa:
   - se encontrar: {:alimento este-nome :calorias valor-em-Double :quantidade gramas}
   - se não encontrar: {:erro \"Alimento não encontrado\"}
   Observação: a USDA retorna calorias por 100g, então ajustamos para a qtd fornecida."
  [nome quantidade]
  (let [search-url (str "https://api.nal.usda.gov/fdc/v1/foods/search"
                        "?query=" (java.net.URLEncoder/encode nome "UTF-8")
                        "&pageSize=1"
                        "&api_key=" api-key)
        search-res (try
                     (http/get search-url {:as :json})
                     (catch Exception e
                       {:status 500 :body nil}))
        foods      (get-in search-res [:body :foods])
        primeiro   (first foods)]
    (if (and primeiro (contains? primeiro :fdcId))
      (let [fdc-id       (:fdcId primeiro)
            detail-url   (str "https://api.nal.usda.gov/fdc/v1/food/" fdc-id
                              "?api_key=" api-key)
            detail-res   (try
                           (http/get detail-url {:as :json})
                           (catch Exception e
                             {:status 500 :body nil}))
            food-data    (:body detail-res)
            calorias-100g (or (get-in food-data [:labelNutrients :calories :value])
                              (->> (:foodNutrients food-data)
                                   (filter #(= "Energy" (get-in % [:nutrient :name])))
                                   first
                                   :amount)
                              0.0)
            calorias-ajustadas (* calorias-100g (/ quantidade 100.0))]
        {:alimento  nome
         :calorias calorias-ajustadas
         :quantidade quantidade})
      {:erro "Alimento não encontrado"})))
