(ns yaml-parser.core
  (:require [clojure.string :as string])
  (:require [clojure.java.io :as io])
  (:require [yaml-parser.yaml :refer :all]))

(defn run [user-input]
  (if (not= "exit" user-input)
    (do
      (if (not (string/blank? user-input))

        (if (is-yaml? user-input)
          (to-csv user-input)
          (println "The file is not a valid yaml file.")))

      (run (.trim (read-line))))))

(defn -main
  [& args]
  (println "Welcome to yaml-parser!")
  (println "Enter the absolute path of a yaml file you wish to convert, and obtain it as a CSV for free!")
  (println "")
  (println "You can exit at any time by typing exit.")
  (println "")
  (if (string/blank? (first args))
    (run nil)
    (run (.trim (first args)))))
