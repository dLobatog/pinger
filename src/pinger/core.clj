(ns pinger.core
  (:gen-class))

(require '[clojure.java [io :as io]])

(defn -main
  "Pick someone in the list"
  [& args]
  (if (== 1 (count args))
    (println (rand-nth (line-seq
                         (try (io/reader (first args))
                         (catch java.io.FileNotFoundException exception
                           (. System exit 1))))))
    (println "Usage: pinger FILE")))
