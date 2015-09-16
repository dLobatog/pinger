(ns pinger.core
  (:gen-class))
(require '[clojure.java [io :as io]])
(require '[postal.core :refer [send-message]])
(use 'clojure.string)

(defn increment-ping-count
  "Increment ping count on a person data structure"
  [person]
  {:name (:name person) :times-pinged (+ 1 (:times-pinged person))})

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

(defn update-people-list
  "Update ping counter of person in people list"
  [people person-pinged]
  (map (fn [person-in-list]
         (if (= (:name person-in-list) (:name person-pinged))
           person-pinged
           person-in-list)) people))

(defn send-email
  [person]
  (let [credentials (file-to-line-seq (str (System/getProperty "user.home") "/.pinger"))
        email (first credentials)
        pass  (last  credentials)
        conn {:host "smtp.gmail.com" :ssl true :user email :pass pass}]
    (send-message conn {:from email :to email
                        :subject (str "Weekly reminder: Reconnect with " (upper-case person))
                        :body (str "ping " (upper-case person))})))

(defn person-to-string
  "Turns a person vector into a string"
  [person]
  (str (:name person) ", " (:times-pinged person)))

(defn people-to-string
  "Turns a people vector into a string"
  [people]
  (join "\n" (map person-to-string people)))

(defn save-list-to-disk
  [person-pinged file]
  (let [raw-seq (update-people-list (read-people file)
                                    (increment-ping-count person-pinged))]
    (spit file (people-to-string raw-seq))))

(defn random-person
  [file]
  (rand-nth (find-least-pinged (read-people (first file)))))

(defn ping
  [person]
  (do
    (send-email (:name person))
    (println "Reminder to ping - " (:name person)
             " - you've pinged this person " (:times-pinged (increment-ping-count person)))))

(defn -main
  "Pick randomly someone to ping in the list of people"
  [& args]
  (if (== 1 (count args))
    (let [person-pinged (random-person args)]
      (do
        (ping person-pinged)
        (save-list-to-disk person-pinged (first args))))
    (println "Usage: pinger FILE")))
