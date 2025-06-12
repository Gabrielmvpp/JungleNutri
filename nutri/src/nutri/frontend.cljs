(ns nutri.frontend
  (:require [goog.dom :as gdom]))

(defn parse-date-dmy [s]
  (let [[d m y] (.split s "/")
        now      (js/Date.)
        pad2     (fn [n] (-> n str (.padStart 2 "0")))
        hh       (pad2 (.getHours now))
        mm       (pad2 (.getMinutes now))
        ss       (pad2 (.getSeconds now))]
    (str y "-" (pad2 m) "-" (pad2 d) "T" hh ":" mm ":" ss)))

(defn fetch-json [url opts]
  (-> (js/fetch url (clj->js opts))
      (.then (fn [resp]
               (if (.-ok resp)
                 (.json resp)
                 (.then (.text resp)
                        (fn [t] (throw (js/Error. (or t (.-status resp)))))))))
      (.catch (fn [e] (throw e)))))

(defn mostrar [o]
  (set! (.-textContent (gdom/getElement "resultadoJson"))
        (js/JSON.stringify (clj->js o) nil 2)))

(defn tratar [e]
  (mostrar {:erro (.-message e)}))

(defn attach! []
  (let [get-el #(gdom/getElement %)]
    ;; Cadastrar/Atualizar Usuário
    (.addEventListener (get-el "btnCadastrarUsuario") "click"
                       (fn []
                         (-> (fetch-json "/usuario"
                                         {:method  "POST"
                                          :headers {"Content-Type" "application/json"}
                                          :body    (js/JSON.stringify
                                                    (clj->js {:altura (.-value (get-el "altura"))
                                                              :peso   (.-value (get-el "peso"))
                                                              :idade  (.-value (get-el "idade"))
                                                              :sexo   (.-value (get-el "sexo"))}))})
                             (.then mostrar)
                             (.catch tratar))))
    ;; Consultar Usuário
    (.addEventListener (get-el "btnConsultarUsuario") "click"
                       (fn []
                         (-> (fetch-json "/usuario" nil)
                             (.then mostrar)
                             (.catch tratar))))
    ;; Registrar Alimento
    (.addEventListener (get-el "btnRegistrarAlimento") "click"
                       (fn []
                         (-> (fetch-json "/transacao/alimento"
                                         {:method  "POST"
                                          :headers {"Content-Type" "application/json"}
                                          :body    (js/JSON.stringify
                                                    (clj->js {:date       (parse-date-dmy (.-value (get-el "alimentoDate")))
                                                              :nome       (.-value (get-el "alimentoNome"))
                                                              :quantidade (.-value (get-el "alimentoQtd"))}))})
                             (.then mostrar)
                             (.catch tratar))))
    ;; Registrar Exercício
    (.addEventListener (get-el "btnRegistrarExercicio") "click"
                       (fn []
                         (-> (fetch-json "/transacao/exercicio"
                                         {:method  "POST"
                                          :headers {"Content-Type" "application/json"}
                                          :body    (js/JSON.stringify
                                                    (clj->js {:date    (parse-date-dmy (.-value (get-el "exercicioDate")))
                                                              :nome    (.-value (get-el "exercicioNome"))
                                                              :minutos (.-value (get-el "exercicioMin"))}))})
                             (.then mostrar)
                             (.catch tratar))))
    ;; Consultar Extrato
    (.addEventListener (get-el "btnConsultarExtrato") "click"
                       (fn []
                         (let [ini (.-value (get-el "extratoIni"))
                               fim (.-value (get-el "extratoFim"))
                               d1  (str (.split (parse-date-dmy ini) "T" 1) "T00:00:00")
                               d2  (str (.split (parse-date-dmy fim) "T" 1) "T23:59:59")]
                           (-> (fetch-json (str "/transacao/extrato?data-ini="
                                                (js/encodeURIComponent d1)
                                                "&data-fim=" (js/encodeURIComponent d2))
                                           nil)
                               (.then mostrar)
                               (.catch tratar)))))
    ;; Consultar Saldo
    (.addEventListener (get-el "btnConsultarSaldo") "click"
                       (fn []
                         (let [ini (.-value (get-el "saldoIni"))
                               fim (.-value (get-el "saldoFim"))
                               d1  (str (.split (parse-date-dmy ini) "T" 1) "T00:00:00")
                               d2  (str (.split (parse-date-dmy fim) "T" 1) "T23:59:59")]
                           (-> (fetch-json (str "/transacao/saldo?data-ini="
                                                (js/encodeURIComponent d1)
                                                "&data-fim=" (js/encodeURIComponent d2))
                                           nil)
                               (.then mostrar)
                               (.catch tratar))))))
  ;; fecha let e defn
  )
;; registra no load da janela
(.addEventListener js/window "load" attach!)
