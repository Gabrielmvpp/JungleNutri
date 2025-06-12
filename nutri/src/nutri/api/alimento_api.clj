(ns nutri.api.alimento-api
  (:require [clj-http.client :as http]))

(def api-key "Q1QCpllMUos1VCXtbDZTQgtc5tPIntZYyiY7E2PO")

(defn buscar-calorias [nome quantidade]
  (let [search-url (str "https://api.nal.usda.gov/fdc/v1/foods/search?query=" nome "&api_key=" api-key)
        search-res (http/get search-url {:as :json})
        fdc-id     (get-in search-res [:body :foods 0 :fdcId])]
    (if fdc-id
      (let [detail-url  (str "https://api.nal.usda.gov/fdc/v1/food/" fdc-id "?api_key=" api-key)
            detail-res  (http/get detail-url {:as :json})
            food-data   (:body detail-res)
            cal-100g    (or (get-in food-data [:labelNutrients :calories :value])
                            (->> (:foodNutrients food-data)
                                 (filter #(= "Energy" (get-in % [:nutrient :name])))
                                 first
                                 :amount)
                            0)
            gramas      (if quantidade (Double/parseDouble (str quantidade)) 100.0)
            cal-ajustada (* cal-100g (/ gramas 100.0))]
        {:alimento   nome
         :calorias   cal-ajustada
         :quantidade gramas
         :base       "por 100g"})
      {:erro "Alimento não encontrado"})))
