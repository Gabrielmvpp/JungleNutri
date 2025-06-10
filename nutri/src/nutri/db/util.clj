(ns nutri.db.util
  (:import [java.time LocalDateTime ZoneId]
           [java.time.format DateTimeFormatter]))

(def ^:private fmt
  (DateTimeFormatter/ofPattern "yyyy-MM-dd'T'HH:mm:ss"))

(defn agora-str
  "Retorna a data e hora atuais no formato ISO-8601 (yyyy-MM-dd'T'HH:mm:ss)."
  []
  (-> (LocalDateTime/now (ZoneId/systemDefault))
      (.format fmt)))

(defn parse-datetime
  "Converte uma String no formato yyyy-MM-dd'T'HH:mm:ss para LocalDateTime.
   Se input for nil ou string inválida, retorna nil."
  [s]
  (try
    (when (and s (not (empty? s)))
      (LocalDateTime/parse s fmt))
    (catch Exception _
      nil)))

(defn between?
  "Retorna true se data est estiver entre data-inicio e data-fim (inclusive).
   Todas devem ser LocalDateTime. Se qualquer uma for nil, retorna false."
  [data-inicio data-est data-fim]
  (and data-inicio data-est data-fim
       (not (.isBefore data-est data-inicio))  ; data-est >= data-inicio
       (not (.isAfter data-est data-fim))))     ; data-est <= data-fim
