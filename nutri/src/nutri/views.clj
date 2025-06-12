(ns nutri.views
  (:require [hiccup.page :refer [html5]]))

(defn home-page []
  (html5
   {:lang "pt-BR"}
   [:head
    [:meta {:charset "UTF-8"}]
    [:title "JungleNutri — Calculadora de Calorias"]
    [:style
     "body{font-family:Arial,sans-serif;max-width:1000px;margin:0 auto;padding:20px}
h1,h2{text-align:center;color:#333}
.container{display:grid;grid-template-columns:2fr 1fr;gap:20px}
form-container{display:flex;flex-direction:column;gap:20px}
fieldset{margin:0;padding:1em;border:1px solid #ccc;border-radius:5px;display:grid;grid-template-columns:auto 1fr;gap:8px;align-items:center}
legend{grid-column:1 / -1;font-size:1.1em;padding:0 4px}
label{justify-self:end;white-space:nowrap}
.actions{grid-column:1 / -1;display:flex;gap:10px;margin-top:8px}
input{width:100%;padding:4px;margin:0}
button{padding:6px 12px;background:#1976D2;color:#fff;border:none;border-radius:3px;cursor:pointer}
button:hover{background:#115293}
pre{background:#f5f5f5;padding:1em;border-radius:5px;overflow-x:auto;margin:0;white-space:pre-wrap}
.resultado{display:flex;flex-direction:column}
.resultado h2{margin-top:0}"]]
   [:body
    [:h1 "JungleNutri — Calculadora de Calorias"]
    [:div.container
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
       [:label {:for "alimentoDate"} "Data (DD/MM/YYYY):"]  [:input {:type "text" :id "alimentoDate" :placeholder "12/06/2025"}]
       [:label {:for "alimentoNome"} "Nome:"]               [:input {:type "text"   :id "alimentoNome"}]
       [:label {:for "alimentoQtd"}  "Quantidade (g):"]     [:input {:type "number" :id "alimentoQtd"}]
       [:div.actions
        [:button {:id "btnRegistrarAlimento"} "Registrar Alimento"]]]

      ;; 3) Registrar Exercício
      [:fieldset
       [:legend "3. Registrar Exercício"]
       [:label {:for "exercicioDate"} "Data (DD/MM/YYYY):"]  [:input {:type "text" :id "exercicioDate" :placeholder "12/06/2025"}]
       [:label {:for "exercicioNome"} "Atividade:"]         [:input {:type "text"   :id "exercicioNome"}]
       [:label {:for "exercicioMin"}  "Duração (min):"]     [:input {:type "number" :id "exercicioMin"}]
       [:div.actions
        [:button {:id "btnRegistrarExercicio"} "Registrar Exercício"]]]

      ;; 4) Consultar Extrato
      [:fieldset
       [:legend "4. Consultar Extrato"]
       [:label {:for "extratoIni"} "Início (DD/MM/YYYY):"] [:input {:type "text" :id "extratoIni" :placeholder "12/06/2025"}]
       [:label {:for "extratoFim"} "Fim (DD/MM/YYYY):"]   [:input {:type "text" :id "extratoFim" :placeholder "15/06/2025"}]
       [:div.actions
        [:button {:id "btnConsultarExtrato"} "Consultar Extrato"]]]

      ;; 5) Consultar Saldo
      [:fieldset
       [:legend "5. Consultar Saldo"]
       [:label {:for "saldoIni"} "Início (DD/MM/YYYY):"]   [:input {:type "text" :id "saldoIni" :placeholder "12/06/2025"}]
       [:label {:for "saldoFim"} "Fim (DD/MM/YYYY):"]     [:input {:type "text" :id "saldoFim" :placeholder "15/06/2025"}]
       [:div.actions
        [:button {:id "btnConsultarSaldo"} "Consultar Saldo"]]]]

     ;; Painel de Resultado à direita
     [:div.resultado
      [:h2 "Resultado:"]
      [:pre#resultadoJson "{}"]]]

    ;; Script de interação
    [:script
     "(function(){
  function mostrar(o){document.getElementById('resultadoJson').textContent=JSON.stringify(o,null,2);}
  function tratar(e){mostrar({erro:''+e});}
  function fetchJson(url,opts){return fetch(url,opts).then(r=>{if(!r.ok)return r.text().then(t=>{throw t||r.status});return r.json();});}
  function parseDateDMY(s){var [d,m,y]=s.split('/');var iso=y+'-'+m.padStart(2,'0')+'-'+d.padStart(2,'0');var now=new Date();var hh=now.getHours().toString().padStart(2,'0');var mm=now.getMinutes().toString().padStart(2,'0');var ss=now.getSeconds().toString().padStart(2,'0');return iso+'T'+hh+':'+mm+':'+ss;}
  document.getElementById('btnCadastrarUsuario').onclick = ()=> fetchJson('/usuario',{method:'POST',headers:{'Content-Type':'application/json'},body:JSON.stringify({altura:document.getElementById('altura').value,peso:document.getElementById('peso').value,idade:document.getElementById('idade').value,sexo:document.getElementById('sexo').value})}).then(mostrar).catch(tratar);
  document.getElementById('btnConsultarUsuario').onclick = ()=> fetchJson('/usuario').then(mostrar).catch(tratar);
  document.getElementById('btnRegistrarAlimento').onclick = ()=> fetchJson('/transacao/alimento',{method:'POST',headers:{'Content-Type':'application/json'},body:JSON.stringify({date:parseDateDMY(document.getElementById('alimentoDate').value),nome:document.getElementById('alimentoNome').value,quantidade:document.getElementById('alimentoQtd').value})}).then(mostrar).catch(tratar);
  document.getElementById('btnRegistrarExercicio').onclick = ()=> fetchJson('/transacao/exercicio',{method:'POST',headers:{'Content-Type':'application/json'},body:JSON.stringify({date:parseDateDMY(document.getElementById('exercicioDate').value),nome:document.getElementById('exercicioNome').value,minutos:document.getElementById('exercicioMin').value})}).then(mostrar).catch(tratar);
  document.getElementById('btnConsultarExtrato').onclick = ()=> {var ini=document.getElementById('extratoIni').value,fim=document.getElementById('extratoFim').value;var d1=parseDateDMY(ini).split('T')[0]+'T00:00:00',d2=parseDateDMY(fim).split('T')[0]+'T23:59:59';fetchJson(`/transacao/extrato?data-ini=${encodeURIComponent(d1)}&data-fim=${encodeURIComponent(d2)}`).then(mostrar).catch(tratar);};
  document.getElementById('btnConsultarSaldo').onclick = ()=> {var ini=document.getElementById('saldoIni').value,fim=document.getElementById('saldoFim').value;var d1=parseDateDMY(ini).split('T')[0]+'T00:00:00',d2=parseDateDMY(fim).split('T')[0]+'T23:59:59';fetchJson(`/transacao/saldo?data-ini=${encodeURIComponent(d1)}&data-fim=${encodeURIComponent(d2)}`).then(mostrar).catch(tratar);};
})();"]]))
