(ns yaml-parser.yaml
  (:require [clojure.string :as string])
  (:require [clojure.java.io :as io]))

(defn lol [] (println "lol"))

(def test-files-path "resources/test-files/")

(defn is-yaml? [filename]
  "Verifies that the given path is the one of a yaml file."
  (string/ends-with? filename ".yaml"))

(defn- csv-format [seq]
  (map #(str % ",") (map #(string/replace % ":" ",") seq)))

(defn as-lazy-seq [yaml-file]
  "Returns a yaml-file whose path is given as an argument as a lazy seq."
  (if (not (is-yaml? yaml-file))
      (throw (IllegalArgumentException. (str "Expected a yaml-file, but got " yaml-file " instead.")))

   (-> (slurp (str test-files-path yaml-file))
       (.replace " " "") 
       (.replace "\n" "") 
       (.split "-")
       (rest))))

(defn to-csv [yaml-file]
  "Writes the yaml-file as a .csv with the same name as the original file."
  (if (not (is-yaml? yaml-file))
    (throw (IllegalArgumentException. (str "Expected a yaml-file, but got " yaml-file " instead."))))
    
  (let [output-path (str test-files-path yaml-file ".csv")]
    (spit output-path nil :append false) ; Erasing the previous file of the same name
      ;; TODO - ask user for that

    (let [tuples (csv-format (as-lazy-seq yaml-file))]
      (doseq [line tuples]
        (spit output-path line :append true)))))
