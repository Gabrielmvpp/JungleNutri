(ns nutri.cli
  (:require
   [clj-http.client :as http]
   [cheshire.core   :as json]
   [clojure.string  :as str]))

(def base-url "http://localhost:3000")

(defn prompt [msg]
  (print msg " ")
  (flush)
  (read-line))

(defn parse-json [s]
  (try
    (json/parse-string s true)
    (catch Exception _ {:erro "Resposta não é JSON válido."})))

(defn show [label data]
  (println "\n=== " label " ===")
  (println (json/generate-string data {:pretty true})))

(defn request
  ([method path]     (request method path nil))
  ([method path body]
   (let [opts   (merge {:throw-exceptions false}
                       (when body
                         {:body    (json/generate-string body)
                          :headers {"Content-Type" "application/json"}}))
         resp   (case method :get  (http/get  (str base-url path) opts)
                      :post (http/post (str base-url path) opts))
         st     (:status resp)
         bdy    (:body resp)]
     (if (= 200 st)
       (parse-json bdy)
       {:erro (str "HTTP " st ": " bdy)}))))

(defn -main []
  (println "=== CLI Nutri ===")
  (println "1) POST /usuario   2) GET /usuario")
  (println "3) POST /transacao/alimento   4) POST /transacao/exercicio")
  (println "5) GET /transacao/extrato     6) GET /transacao/saldo")
  (println "7) sair\n")
  (loop []
    (let [op (str/trim (prompt "Opção:"))]
      (case op
        "1" (let [h (prompt "altura:"), p (prompt "peso:")
                  i (prompt "idade:"),   s (prompt "sexo:")]
              (show "POST /usuario"   (request :post   "/usuario"   {:altura h :peso p :idade i :sexo s})))
        "2" (show "GET /usuario"  (request :get    "/usuario"))
        "3" (let [n (prompt "nome:"), q (prompt "quantidade:")]
              (show "POST /transacao/alimento"
                    (request :post "/transacao/alimento" {:nome n :quantidade q})))
        "4" (let [n (prompt "nome:"), m (prompt "minutos:"), p (prompt "peso:")]
              (show "POST /transacao/exercicio"
                    (request :post "/transacao/exercicio" {:nome n :minutos m :peso p})))
        "5" (let [d1 (prompt "data-ini:"), d2 (prompt "data-fim:")
                  path (str "/transacao/extrato?data-ini=" (java.net.URLEncoder/encode d1 "UTF-8")
                            "&data-fim=" (java.net.URLEncoder/encode d2 "UTF-8"))]
              (show (str "GET " path) (request :get path)))
        "6" (let [d1 (prompt "data-ini:"), d2 (prompt "data-fim:")
                  path (str "/transacao/saldo?data-ini=" (java.net.URLEncoder/encode d1 "UTF-8")
                            "&data-fim=" (java.net.URLEncoder/encode d2 "UTF-8"))]
              (show (str "GET " path) (request :get path)))
        "7" (System/exit 0)
        (println "Opção inválida.")))
    (recur)))
