(ns nutri.model.transacao)

;; Cada transação é um mapa com:
;;  :id         (Integer, gerado via contador simples)
;;  :tipo       ("alimento" ou "exercicio")
;;  :nome       (String)
;;  :quantidade (Double)  ; se for alimento: gramas; se for exercício: minutos
;;  :calorias   (Double)  ; se for alimento: calorias positivas; exercício: negativas
;;  :data-hora  (String ISO-8601, ex.: "2025-06-03T14:22:00")
