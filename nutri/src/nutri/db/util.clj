(ns nutri.db.util
  (:import [java.time LocalDateTime LocalDate ZoneId]
           [java.time.format DateTimeFormatter]))

(def fmt-iso (DateTimeFormatter/ofPattern "yyyy-MM-dd['T'HH:mm:ss]"))
(def fmt-dmy (DateTimeFormatter/ofPattern "dd/MM/yyyy"))

(defn agora-str []
  (.format (LocalDateTime/now (ZoneId/systemDefault)) fmt-iso))

(defn parse-datetime [s]
  (try
    (LocalDateTime/parse s fmt-iso)
    (catch Exception _
      (try
        (-> (LocalDate/parse s fmt-dmy)
            (.atStartOfDay))
        (catch Exception _ nil)))))

(defn between? [d1 d d2]
  (and d1 d d2
       (not (.isBefore d d1))
       (not (.isAfter  d d2))))
