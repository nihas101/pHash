(ns phash.core-test
  (:require
   [clojure.test :refer :all]
   [phash.core :refer :all]
   [phash.a-hash :as a]
   [phash.test-utils :as tu]
   [clojure.string :as s]))

(defonce ^:private a-hash-fn (a/a-hash))
(defonce ^:private a-hash-bits-fn (a/a-hash conj []))

(deftest perceptual-hash-test
  (testing "a-hash test"
    (is (= 18391610165931278335 (perceptual-hash a-hash-fn tu/test-image)))))

(deftest perceptual-hash-bits-test
  (testing "a-hash-bits test"
    (is (= "1111111111111111110100111100001100000000000001000011110011111111"
           (s/join (perceptual-hash a-hash-bits-fn tu/test-image))))))

;; aHash

(deftest image-distance-a-hash-compr-test
  (testing "image-distance a-hash compr test"
    (mapv
     (fn [idx image]
       (is (= 0 (image-distance :a-hash image image))
           (str "Image with id " idx " has a hamming distance larger than 0 to itself")))
     (range)
     tu/compr)))

(deftest image-distance-a-hash-blur-test
  (testing "image-distance a-hash blur test"
    (mapv
     (fn [idx image]
       (is (= 0 (image-distance :a-hash image image))
           (str "Image with id " idx " has a hamming distance larger than 0 to itself")))
     (range)
     tu/blur)))

(deftest image-distance-a-hash-misc-test
  (testing "image-distance a-hash compr test"
    (mapv
     (fn [idx image]
       (is (= 0 (image-distance :a-hash image image))
           (str "Image with id " idx " has a hamming distance larger than 0 to itself")))
     (range)
     tu/misc)))

(deftest image-distance-a-hash-rotd-test
  (testing "image-distance a-hash rotd test"
    (mapv
     (fn [idx image]
       (is (= 0 (image-distance :a-hash image image))
           (str "Image with id " idx " has a hamming distance larger than 0 to itself")))
     (range)
     tu/rotd)))

(deftest image-distance-a-hash-compr-blur-test
  (testing "image-distance a-hash compr blur test"
    (mapv
     (fn [idx compressed blurred]
       (is (< (image-distance :a-hash compressed blurred) 7)
           (str "Image with id " idx " has a hamming distance larger than 7")))
     (range)
     tu/compr
     tu/blur)))

(deftest image-distance-a-hash-compr-misc-test
  (testing "image-distance a-hash compr misc test"
    (mapv
     (fn [idx compressed misclns]
       (is (< (image-distance :a-hash compressed misclns) 3)
           (str "Image with id " idx " has a hamming distance larger than 3")))
     (range)
     tu/compr
     tu/misc)))

; TODO: Rotate and mirror images when comparing them
;(deftest image-distance-a-hash-compr-rotd-test
;  (testing "image-distance a-hash compr rotd test"
;    (mapv
;     (fn [idx compressed rotated]
;       (is (< (image-distance compressed rotated) 1)
;           (str "Image with id " idx " has a hamming distance larger than 1")))
;     (range)
;     tu/compr
;     tu/rotd)))

(deftest image-distance-a-hash-blur-misc-test
  (testing "image-distance a-hash blur misc test"
    (mapv
     (fn [idx blurred misclns]
       (is (< (image-distance :a-hash blurred misclns) 7)
           (str "Image with id " idx " has a hamming distance larger than 7")))
     (range)
     tu/blur
     tu/misc)))

; TODO: Rotate and mirror images when comparing them
;(deftest image-distance-a-hash-blur-rotd-test
;  (testing "image-distance a-hash blur rotd test"
;    (mapv
;     (fn [idx blurred rotated]
;       (is (< (image-distance :a-hash blurred rotated) 1)
;           (str "Image with id " idx " has a hamming distance larger than 1")))
;     (range)
;     tu/blur
;     tu/rotd)))

; TODO: Rotate and mirror images when comparing them
;(deftest image-distance-a-hash-misc-rotd-test
;  (testing "image-distance a-hash misc rotd test"
;    (mapv
;     (fn [idx misclns rotated]
;       (is (< (image-distance :a-hash misclns rotated) 1)
;           (str "Image with id " idx " has a hamming distance larger than 1")))
;     (range)
;     tu/misc
;     tu/rotd)))


;; dHash

(deftest image-distance-d-hash-compr-test
  (testing "image-distance d-hash compr test"
    (mapv
     (fn [idx image]
       (is (= 0 (image-distance :d-hash image image))
           (str "Image with id " idx " has a hamming distance larger than 0 to itself")))
     (range)
     tu/compr)))

(deftest image-distance-d-hash-blur-test
  (testing "image-distance d-hash blur test"
    (mapv
     (fn [idx image]
       (is (= 0 (image-distance :d-hash image image))
           (str "Image with id " idx " has a hamming distance larger than 0 to itself")))
     (range)
     tu/blur)))

(deftest image-distance-d-hash-misc-test
  (testing "image-distance d-hash compr test"
    (mapv
     (fn [idx image]
       (is (= 0 (image-distance :d-hash image image))
           (str "Image with id " idx " has a hamming distance larger than 0 to itself")))
     (range)
     tu/misc)))

(deftest image-distance-d-hash-rotd-test
  (testing "image-distance d-hash rotd test"
    (mapv
     (fn [idx image]
       (is (= 0 (image-distance :d-hash image image))
           (str "Image with id " idx " has a hamming distance larger than 0 to itself")))
     (range)
     tu/rotd)))

(deftest image-distance-d-hash-compr-blur-test
  (testing "image-distance d-hash compr blur test"
    (mapv
     (fn [idx compressed blurred]
       (is (< (image-distance :d-hash compressed blurred) 13)
           (str "Image with id " idx " has a hamming distance larger than 13")))
     (range)
     tu/compr
     tu/blur)))

(deftest image-distance-d-hash-compr-misc-test
  (testing "image-distance d-hash compr misc test"
    (mapv
     (fn [idx compressed misclns]
       (is (< (image-distance :d-hash compressed misclns) 11)
           (str "Image with id " idx " has a hamming distance larger than 11")))
     (range)
     tu/compr
     tu/misc)))

; TODO: Rotate and mirror images when comparing them
;(deftest image-distance-d-hash-compr-rotd-test
;  (testing "image-distance d-hash compr rotd test"
;    (mapv
;     (fn [idx compressed rotated]
;       (is (< (image-distance :d-hash compressed rotated) 1)
;           (str "Image with id " idx " has a hamming distance larger than 1")))
;     (range)
;     tu/compr
;     tu/rotd)))

(deftest image-distance-d-hash-blur-misc-test
  (testing "image-distance d-hash blur misc test"
    (mapv
     (fn [idx blurred misclns]
       (is (< (image-distance :d-hash blurred misclns) 10)
           (str "Image with id " idx " has a hamming distance larger than 10")))
     (range)
     tu/blur
     tu/misc)))

; TODO: Rotate and mirror images when comparing them
;(deftest image-distance-d-hash-blur-rotd-test
;  (testing "image-distance d-hash blur rotd test"
;    (mapv
;     (fn [idx blurred rotated]
;       (is (< (image-distance :d-hash blurred rotated) 1)
;           (str "Image with id " idx " has a hamming distance larger than 1")))
;     (range)
;     tu/blur
;     tu/rotd)))

; TODO: Rotate and mirror images when comparing them
;(deftest image-distance-d-hash-misc-rotd-test
;  (testing "image-distance d-hash misc rotd test"
;    (mapv
;     (fn [idx misclns rotated]
;       (is (< (image-distance :d-hash misclns rotated) 1)
;           (str "Image with id " idx " has a hamming distance larger than 1")))
;     (range)
;     tu/misc
;     tu/rotd)))