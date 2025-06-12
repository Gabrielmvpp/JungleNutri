(ns nutri.views
  (:require
   [hiccup.page  :refer [html5 include-js]]
   [garden.core  :refer [css]]
   [garden.units :refer [px]]))

(def style-css
  (css
   [:body {:font-family    "Arial, sans-serif"
           :max-width      (px 1000)
           :margin         "0 auto"
           :padding        (px 20)}]
   [:h1 :h2 {:text-align "center" :color "#333"}]
   [:.container {:display              "grid"
                 :grid-template-columns "2fr 1fr"
                 :gap                   (px 20)}]
   [:.form-container {:display        "flex"
                      :flex-direction "column"
                      :gap            (px 20)}]
   [:fieldset {:margin             0
               :padding            (px 16)
               :border             "1px solid #ccc"
               :border-radius      (px 5)
               :display            "grid"
               :grid-template-columns "auto 1fr"
               :gap                (px 8)
               :align-items        "center"}]
   [:legend {:grid-column "1 / -1" :font-size "1.1em" :padding "0 4px"}]
   [:label {:justify-self "end" :white-space "nowrap"}]
   [:.actions {:grid-column "1 / -1" :display "flex" :gap (px 10) :margin-top (px 8)}]
   [:input {:width   "100%" :padding (px 4) :margin 0}]
   [:button {:padding       "6px 12px"
             :background    "#1976D2"
             :color         "#fff"
             :border        "none"
             :border-radius (px 3)
             :cursor        "pointer"}]
   [:button:hover {:background "#115293"}]
   [:pre {:background    "#f5f5f5"
          :padding       (px 16)
          :border-radius (px 5)
          :overflow-x    "auto"
          :margin        0
          :white-space   "pre-wrap"}]
   [:.resultado {:display        "flex"
                 :flex-direction "column"}]
   [:.resultado :h2 {:margin-top 0}]))

(defn home-page []
  (html5
   {:lang "pt-BR"}
   [:head
    [:meta {:charset "UTF-8"}]
    [:title "JungleNutri — Calculadora de Calorias"]
    [:style style-css]
    ;; injeta o bundle compilado pelo cljsbuild:
    (include-js "/js/app.js")]
   [:body
    [:h1 "JungleNutri — Calculadora de Calorias"]
    [:div.container
     ;; Formulários à esquerda
     [:div.form-container
      ;; 1) Perfil do Usuário
      [:fieldset
       [:legend "1. Perfil do Usuário"]
       [:label {:for "altura"} "Altura (cm):"] [:input {:type "number" :id "altura"}]
       [:label {:for "peso"}   "Peso (kg):"]   [:input {:type "number" :id "peso"}]
       [:label {:for "idade"}  "Idade:"]       [:input {:type "number" :id "idade"}]
       [:label {:for "sexo"}   "Sexo:"]        [:input {:type "text"   :id "sexo"}]
       [:div.actions
        [:button {:id "btnCadastrarUsuario"} "Salvar Usuário"]
        [:button {:id "btnConsultarUsuario"} "Mostrar Usuário"]]]
      ;; 2) Registrar Alimento
      [:fieldset
       [:legend "2. Registrar Consumo de Alimento"]
       [:label {:for "alimentoDate"} "Data (DD/MM/YYYY):"] [:input {:type "text"   :id "alimentoDate"   :placeholder "12/06/2025"}]
       [:label {:for "alimentoNome"} "Nome:"]              [:input {:type "text"   :id "alimentoNome"}]
       [:label {:for "alimentoQtd"}  "Quantidade (g):"]    [:input {:type "number" :id "alimentoQtd"}]
       [:div.actions
        [:button {:id "btnRegistrarAlimento"} "Registrar Alimento"]]]
      ;; 3) Registrar Exercício
      [:fieldset
       [:legend "3. Registrar Exercício"]
       [:label {:for "exercicioDate"} "Data (DD/MM/YYYY):"] [:input {:type "text"   :id "exercicioDate"  :placeholder "12/06/2025"}]
       [:label {:for "exercicioNome"} "Atividade:"]         [:input {:type "text"   :id "exercicioNome"}]
       [:label {:for "exercicioMin"}  "Duração (min):"]     [:input {:type "number" :id "exercicioMin"}]
       [:div.actions
        [:button {:id "btnRegistrarExercicio"} "Registrar Exercício"]]]
      ;; 4) Consultar Extrato
      [:fieldset
       [:legend "4. Consultar Extrato"]
       [:label {:for "extratoIni"} "Início (DD/MM/YYYY):"] [:input {:type "text" :id "extratoIni"   :placeholder "12/06/2025"}]
       [:label {:for "extratoFim"} "Fim (DD/MM/YYYY):"]   [:input {:type "text" :id "extratoFim"   :placeholder "15/06/2025"}]
       [:div.actions
        [:button {:id "btnConsultarExtrato"} "Consultar Extrato"]]]
      ;; 5) Consultar Saldo
      [:fieldset
       [:legend "5. Consultar Saldo"]
       [:label {:for "saldoIni"} "Início (DD/MM/YYYY):"]   [:input {:type "text" :id "saldoIni"     :placeholder "12/06/2025"}]
       [:label {:for "saldoFim"} "Fim (DD/MM/YYYY):"]     [:input {:type "text" :id "saldoFim"     :placeholder "15/06/2025"}]
       [:div.actions
        [:button {:id "btnConsultarSaldo"} "Consultar Saldo"]]]]
     ;; Painel de Resultado à direita
     [:div.resultado
      [:h2 "Resultado:"]
      [:pre#resultadoJson "{}"]]]]))
