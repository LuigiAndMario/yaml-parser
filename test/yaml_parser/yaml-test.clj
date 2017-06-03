(ns yaml-parser.core-test
  (:require [clojure.test :refer :all]
            [yaml-parser.core :refer :all]))

(deftest is-yaml-works
  (testing "is-yaml should output true for yaml files and false for others."
    (is (and (is-yaml? "lol.yaml") (not (is-yaml? "lol.olo"))))))

(deftest as-lazy-seq-works-for-simple-input
  (testing "as-lazy-seq reads a simple file well, and outputs it as a correct sequence."
    (let [expected-output (seq ["one:1" "two:2" "three:3"])]
      (let [obtained-output (as-lazy-seq "test01.yaml")]

        (if (not= (count obtained-output) (count expected-output))
          (is false))
            
        (for [i (range 0 (dec (count obtained-output)))]
          (if (not= (nth obtained-output i) (nth expected-output i))
            (is false)))
        
        (is true)))))

(deftest to-csv-works-for-simple-input
  (testing "to-csv writes a correct csv for a regular input"
    (let [obtained-output (to-csv "test01.yaml")])))

