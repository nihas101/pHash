(ns phash.a-hash-test
  (:require
   [clojure.test :refer :all]
   [clojure.string :as s]
   [phash.a-hash :refer :all]
   [phash.test-utils :as u]))

(deftest a-hash-test
  (testing "a-hash test"
    (is (= 18391610165931278335 (a-hash u/test-image)))))

(deftest a-hash-bits-test
  (testing "a-hash-bits test"
    (is (= "1111111111111111110100111100001100000000000001000011110011111111"
           (s/join (a-hash-bits u/test-image))))))

(deftest pixels-brightness->hash-bits-test
  (testing (str "pixels-brightness->hash-bits of an empty image"
                " should result in an \"empty\" hash.")
    (is (= nil
           (#'phash.a-hash/pixels-brightness->hash-bits [])))))

(deftest pixels-brightness->hash-bits-0-test
  (testing "pixels-brightness->hash-bits of 0 should result in [0]."
    (is (= [0]
           (#'phash.a-hash/pixels-brightness->hash-bits [0])))))

(deftest pixels-brightness->hash-bits-1-test
  (testing "pixels-brightness->hash-bits of 0 should result in [1]."
    (is (= [0]
           (#'phash.a-hash/pixels-brightness->hash-bits [1])))))

(deftest pixels-brightness->hash-bits-2-0-test
  (testing "pixels-brightness->hash-bits of [2 0] should result in [0 1]."
    (is (= [0 1]
           (#'phash.a-hash/pixels-brightness->hash-bits [2 0])))))

; TODO: Add property based tests (can make own images (and copy) with imagez!)