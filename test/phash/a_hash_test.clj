(ns phash.a-hash-test
  (:require
   [clojure.test :refer :all]
   [phash.a-hash :refer :all]
   [phash.utils :as u]))

(defonce ^:private a-hash-bits-fn (a-hash conj []))

(deftest pixels-brightness->hash-bits-test
  (testing (str "pixels-brightness->hash-bits of an empty image"
                " should result in an \"empty\" hash.")
    (is (= nil
           (u/pixels-brightness->hash-bits a-hash-bits-fn [])))))

(deftest pixels-brightness->hash-bits-0-test
  (testing "pixels-brightness->hash-bits of 0 should result in [0]."
    (is (= [0]
           (u/pixels-brightness->hash-bits a-hash-bits-fn [0])))))

(deftest pixels-brightness->hash-bits-1-test
  (testing "pixels-brightness->hash-bits of 0 should result in [1]."
    (is (= [0]
           (u/pixels-brightness->hash-bits a-hash-bits-fn [1])))))

(deftest pixels-brightness->hash-bits-2-0-test
  (testing "pixels-brightness->hash-bits of [2 0] should result in [0 1]."
    (is (= [0 1]
           (u/pixels-brightness->hash-bits a-hash-bits-fn [2 0])))))

; TODO: Add property based tests (can make own images (and copy) with imagez!)