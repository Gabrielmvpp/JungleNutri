(ns nutri.model.usuario)

;; Um mapa de usuário deve ter exatamente estas chaves:
;;  :altura (Double ou Integer, em cm ex.: 175.0)
;;  :peso   (Double ou Integer, em kg ex.: 70.5)
;;  :idade  (Integer)
;;  :sexo   (String, ex.: "M" ou "F" ou outro)
;; Para consistência, podemos assumir:
;;  altura, peso, idade ⇒ devem ser parseados como Double ou Integer
