(ns yaml-parser.core-test
  (:require [clojure.test :refer :all]
            [yaml-parser.core :refer :all]))

(deftest is-yaml-works
  (testing "is-yaml should output true for yaml files and false for others."
    (is (and (is-yaml? "lol.yaml") (not (is-yaml? "lol.olo"))))))
