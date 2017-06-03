(ns yaml-parser.core
  (:require [clojure.string :as string]))

(defn is-yaml? [path] 
  "Verifies that the given path is the one of a yaml file."
  (string/ends-with? path ".yaml"))



(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, world!"))
