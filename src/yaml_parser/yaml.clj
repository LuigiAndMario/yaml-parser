(ns yaml-parser.yaml
  (:require [clojure.string :as string])
  (:require [clojure.java.io :as io]))

;; Name manipulation & verification

(defn is-yaml? [filename]
  "Verifies that the given path is the one of a yaml file."
  (and (.exists (io/as-file filename)) (string/ends-with? filename ".yaml")))

(defn prefix [filename]
  "Given an absolute file path, returns the same file path without the extension."
  (let [prefix (string/split filename #"\.")]
    (if (> (count prefix) 2) ; 1 for the parent name and 1 for the child name
      (throw (IllegalArgumentException. "Cannot perform operations in a hidden folder or for a hidden file.")))
    (first prefix)))

(defn- child-name [filename]
  (last (string/split filename #"/")))

;; Filtering

(defn- remove-comments [file-content]
  "Removes the comments inside a yaml file."
  (string/replace file-content #"\s?#.*" ""))

;; Reading

(defn as-lazy-seq [yaml-file]
  "Returns a yaml-file whose path is given as an argument as a lazy seq."
  (if (not (is-yaml? yaml-file))
    (throw (IllegalArgumentException. (str "Expected a yaml file, but got " yaml-file " instead."))))

  (-> (slurp yaml-file)
      (remove-comments)
      (.replace " " "") 
      (.replace "\n" "") 
      (.split "-")
      (rest)))

;; Writing / CSV

(defn- csv-format [seq]
  (map #(str % ",") (map #(string/replace % ":" ",") seq)))

(defn- write-file [path content]
  "Writes content onto a file whose path is given as an argument."
  (doseq [line (csv-format (as-lazy-seq content))]
    (spit path line :append true)))

(defn to-csv [yaml-file]
  "Writes the yaml-file as a .csv with the same name as the original file."
  (if (not (is-yaml? yaml-file))
    (throw (IllegalArgumentException. (str "Expected a yaml file, but got " yaml-file " instead."))))

  (let [output-path (str (prefix yaml-file) ".csv")]
    (if (.exists (io/as-file output-path))
      (do
        (println (str "The file " (child-name output-path) " already exists in this location."))
        (println "Do you want to overwrite it ? (y|n)")

        (if (= (read-line) "y")
          (do
            (spit output-path nil :append false) ; Overriding the previous content of the file.
            (write-file output-path yaml-file))))

      (write-file output-path yaml-file))))