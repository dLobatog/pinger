(ns pinger.core
  (:gen-class))

(require '[clojure.java [io :as io]])
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
  "Creates a line-seq from a file"
  [file]
  (line-seq (try (io/reader file)
              (catch java.io.FileNotFoundException exception
                (do (println "File" file "not found. Please supply a valid file")
                  (. System exit 1))))))

(defn read-people
  "Creates a sequence of people to ping"
  [people-filepath]
  (map initialize-person (file-to-line-seq people-filepath)))

(defn -main
  "Pick randomly someone to ping in the list of people"
  [& args]
  (if (== 1 (count args))
    (println "ping" (:name (rand-nth (read-people (first args)))))
    (println "Usage: pinger FILE")))

