(ns yaml-parser.yaml-test
  (:require [clojure.test :refer :all]
            [yaml-parser.yaml :refer :all]))

(def test-files-path "resources/test-files/")

(def test-basic (str test-files-path "test-basic.yaml"))
(def test-basic-output (str test-files-path "test-basic.csv"))
(def test-basic-expectation (str test-files-path "test-basic-expectation.csv"))

(def test-comments (str test-files-path "test-comments.yaml"))

(def test-comments-expectation "- one: 1
- two: 2")

(deftest is-yaml-works
  (testing "is-yaml should output true for yaml files and false for others."
    (is 
      (and
        (is-yaml? test-basic)
        (not (is-yaml? (str test-files-path "lol.olo")))))))

(deftest as-lazy-seq-works-for-simple-input
  (testing "as-lazy-seq reads a simple file well, and outputs it as a correct sequence."
    (let [expected-output (seq ["one:1" "two:2" "three:3"])]
      (let [obtained-output (as-lazy-seq test-basic)]

        (if (not= (count obtained-output) (count expected-output))
          (is false))
            
        (for [i (range 0 (dec (count obtained-output)))]
          (if (not= (nth obtained-output i) (nth expected-output i))
            (is false)))
        
        (is true)))))

(deftest to-csv-works-for-simple-input
  (testing "to-csv writes a correct csv for a regular input"
    (let [obtained-output (to-csv test-basic)]
      (is (= (slurp test-basic-output) (slurp test-basic-expectation))))))

(deftest remove-comments-work-on-simple-input
  (testing "remove-comments should correctly cut between a comment token and a carrige return."
    (let [remove-comments #'yaml-parser.yaml/remove-comments]
      (is 
        (= 
          (remove-comments (slurp test-comments))
          test-comments-expectation)))))