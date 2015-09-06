(ns pinger.core
  (:gen-class))
(require '[clojure.java [io :as io]])
(require '[postal.core :refer [send-message]])
(use 'clojure.string)

(defn initialize-person
  "Create a person structure defined by '$NAME, $PINGS'"
  [raw-person-string]
  (let [raw-person-array (map trim (split raw-person-string #","))]
    {:name (first raw-person-array)
     :times-pinged (if (= 1 (count raw-person-array))
                     0
                     (read-string (second raw-person-array)))}))

(defn file-to-line-seq
  "Create a line-seq from a file"
  [file]
  (line-seq (try (io/reader file)
              (catch java.io.FileNotFoundException exception
                (do (println "File" file "not found. Please supply a valid file")
                  (System/exit 1))))))

(defn read-people
  "Create a sequence of people to ping"
  [people-filepath]
  (map initialize-person (file-to-line-seq people-filepath)))

(defn find-least-pinged
  "Given a map with names and number of pings, find the group of people that has
  been pinged the least"
  [people]
  (let [least-pinged (apply min (map :times-pinged people))]
    (filter (fn [x] (= (:times-pinged x) least-pinged)) people)))

(defn send-email
  [person]
  (let [email ""
        pass ""
        conn {:host "smtp.gmail.com" :ssl true :user email :pass pass}]
    (send-message conn {:from email :to email
                        :subject (str "Weekly reminder: Reconnect with " (upper-case person))
                        :body (str "ping " (upper-case person))})))

(defn random-person
  [file]
  (:name (rand-nth (find-least-pinged (read-people (first file))))))

(defn -main
  "Pick randomly someone to ping in the list of people"
  [& args]
  (if (== 1 (count args))
    (send-email (random-person args))
    (println "Usage: pinger FILE")))
