(ns de.nihas101.phash.core-test
  (:require
   [clojure.test :refer :all]
   [de.nihas101.phash.core :refer :all]
   [de.nihas101.phash.a-hash :as a]
   [de.nihas101.phash.d-hash :as d]
   [de.nihas101.phash.p-hash :as p]
   [de.nihas101.phash.test-utils :as tu]
   [clojure.string :as s]))

(defonce ^:private a-hash-fn (a/a-hash))

(deftest eq-images?-same-image-test
  (testing "eq-images? same image test"
    (is (= true
           (eq-images? a-hash-fn (first tu/compr) (first tu/compr) 1)))))

(deftest eq-images?-different-image-test
  (testing "eq-images? different images test"
    (is (= false
           (eq-images? a-hash-fn (first tu/compr) (second tu/compr) 1)))))

(deftest perceptual-a-hash-bits-test
  (testing "a-hash-bits test"
    (is (= "1111111111111111110100111100001100000000000001000011110011111111"
           (s/join (perceptual-hash a-hash-fn (first tu/compr) conj []))))))

(deftest perceptual-a-hash-test
  (testing "a-hash test"
    (is (= -55133907778273281 (perceptual-hash a-hash-fn (first tu/compr))))))

(defonce ^:private d-hash-fn (d/d-hash))

(deftest perceptual-d-hash-test
  (testing "d-hash test"
    (is (= -8216597464464607979 (perceptual-hash d-hash-fn (first tu/compr))))))

(deftest perceptual-d-hash-bits-test
  (testing "d-hash-bits test"
    (is (= "1010100010101100011011000010110111000100000100110001111110110001"
           (s/join (perceptual-hash d-hash-fn (first tu/compr) conj []))))))

(defonce ^:private p-hash-fn (p/p-hash))

(deftest perceptual-p-hash-test
  (testing "p-hash test"
    (is (= 72060892623284135 (perceptual-hash p-hash-fn (first tu/compr))))))

(deftest perceptual-p-hash-bits-test
  (testing "p-hash-bits test"
    (is (= "1110010111100100010000001100000000000000110000000000000010000000"
           (s/join (perceptual-hash p-hash-fn (first tu/compr) conj []))))))

;; aHash

(deftest image-distance-a-hash-compr-test
  (testing "image-distance a-hash compr test"
    (mapv
     (fn [idx image]
       (is (= 0 (image-distance a-hash-fn image image))
           (str "Image with id " idx " has a hamming distance larger than 0 to itself")))
     (range)
     tu/compr)))

(deftest image-distance-a-hash-blur-test
  (testing "image-distance a-hash blur test"
    (mapv
     (fn [idx image]
       (is (= 0 (image-distance a-hash-fn image image))
           (str "Image with id " idx " has a hamming distance larger than 0 to itself")))
     (range)
     tu/blur)))

(deftest image-distance-a-hash-misc-test
  (testing "image-distance a-hash compr test"
    (mapv
     (fn [idx image]
       (is (= 0 (image-distance a-hash-fn image image))
           (str "Image with id " idx " has a hamming distance larger than 0 to itself")))
     (range)
     tu/misc)))

(deftest image-distance-a-hash-rotd-test
  (testing "image-distance a-hash rotd test"
    (mapv
     (fn [idx image]
       (is (= 0 (image-distance a-hash-fn image image))
           (str "Image with id " idx " has a hamming distance larger than 0 to itself")))
     (range)
     tu/rotd)))

(deftest image-distance-a-hash-compr-blur-test
  (testing "image-distance a-hash compr blur test"
    (mapv
     (fn [idx compressed blurred]
       (is (< (image-distance a-hash-fn compressed blurred) 7)
           (str "Image with id " idx " has a hamming distance larger than 7")))
     (range)
     tu/compr
     tu/blur)))

(deftest image-distance-a-hash-compr-misc-test
  (testing "image-distance a-hash compr misc test"
    (mapv
     (fn [idx compressed misclns]
       (is (< (image-distance a-hash-fn compressed misclns) 3)
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
       (is (< (image-distance a-hash-fn blurred misclns) 7)
           (str "Image with id " idx " has a hamming distance larger than 7")))
     (range)
     tu/blur
     tu/misc)))

; TODO: Rotate and mirror images when comparing them
;(deftest image-distance-a-hash-blur-rotd-test
;  (testing "image-distance a-hash blur rotd test"
;    (mapv
;     (fn [idx blurred rotated]
;       (is (< (image-distance a-hash-fn blurred rotated) 1)
;           (str "Image with id " idx " has a hamming distance larger than 1")))
;     (range)
;     tu/blur
;     tu/rotd)))

; TODO: Rotate and mirror images when comparing them
;(deftest image-distance-a-hash-misc-rotd-test
;  (testing "image-distance a-hash misc rotd test"
;    (mapv
;     (fn [idx misclns rotated]
;       (is (< (image-distance a-hash-fn misclns rotated) 1)
;           (str "Image with id " idx " has a hamming distance larger than 1")))
;     (range)
;     tu/misc
;     tu/rotd)))


;; dHash

(deftest image-distance-d-hash-compr-test
  (testing "image-distance d-hash compr test"
    (mapv
     (fn [idx image]
       (is (= 0 (image-distance d-hash-fn image image))
           (str "Image with id " idx " has a hamming distance larger than 0 to itself")))
     (range)
     tu/compr)))

(deftest image-distance-d-hash-blur-test
  (testing "image-distance d-hash blur test"
    (mapv
     (fn [idx image]
       (is (= 0 (image-distance d-hash-fn image image))
           (str "Image with id " idx " has a hamming distance larger than 0 to itself")))
     (range)
     tu/blur)))

(deftest image-distance-d-hash-misc-test
  (testing "image-distance d-hash compr test"
    (mapv
     (fn [idx image]
       (is (= 0 (image-distance d-hash-fn image image))
           (str "Image with id " idx " has a hamming distance larger than 0 to itself")))
     (range)
     tu/misc)))

(deftest image-distance-d-hash-rotd-test
  (testing "image-distance d-hash rotd test"
    (mapv
     (fn [idx image]
       (is (= 0 (image-distance d-hash-fn image image))
           (str "Image with id " idx " has a hamming distance larger than 0 to itself")))
     (range)
     tu/rotd)))

(deftest image-distance-d-hash-compr-blur-test
  (testing "image-distance d-hash compr blur test"
    (mapv
     (fn [idx compressed blurred]
       (is (< (image-distance d-hash-fn compressed blurred) 13)
           (str "Image with id " idx " has a hamming distance larger than 13")))
     (range)
     tu/compr
     tu/blur)))

(deftest image-distance-d-hash-compr-misc-test
  (testing "image-distance d-hash compr misc test"
    (mapv
     (fn [idx compressed misclns]
       (is (< (image-distance d-hash-fn compressed misclns) 11)
           (str "Image with id " idx " has a hamming distance larger than 11")))
     (range)
     tu/compr
     tu/misc)))

; TODO: Rotate and mirror images when comparing them
;(deftest image-distance-d-hash-compr-rotd-test
;  (testing "image-distance d-hash compr rotd test"
;    (mapv
;     (fn [idx compressed rotated]
;       (is (< (image-distance d-hash-fn compressed rotated) 1)
;           (str "Image with id " idx " has a hamming distance larger than 1")))
;     (range)
;     tu/compr
;     tu/rotd)))

(deftest image-distance-d-hash-blur-misc-test
  (testing "image-distance d-hash blur misc test"
    (mapv
     (fn [idx blurred misclns]
       (is (< (image-distance d-hash-fn blurred misclns) 10)
           (str "Image with id " idx " has a hamming distance larger than 10")))
     (range)
     tu/blur
     tu/misc)))

; TODO: Rotate and mirror images when comparing them
;(deftest image-distance-d-hash-blur-rotd-test
;  (testing "image-distance d-hash blur rotd test"
;    (mapv
;     (fn [idx blurred rotated]
;       (is (< (image-distance d-hash-fn blurred rotated) 1)
;           (str "Image with id " idx " has a hamming distance larger than 1")))
;     (range)
;     tu/blur
;     tu/rotd)))

; TODO: Rotate and mirror images when comparing them
;(deftest image-distance-d-hash-misc-rotd-test
;  (testing "image-distance d-hash misc rotd test"
;    (mapv
;     (fn [idx misclns rotated]
;       (is (< (image-distance d-hash-fn misclns rotated) 1)
;           (str "Image with id " idx " has a hamming distance larger than 1")))
;     (range)
;     tu/misc
;     tu/rotd)))

;; pHash

(deftest image-distance-p-hash-compr-test
  (testing "image-distance p-hash compr test"
    (mapv
     (fn [idx image]
       (is (= 0 (image-distance p-hash-fn image image))
           (str "Image with id " idx " has a hamming distance larger than 0 to itself")))
     (range)
     tu/compr)))

(deftest image-distance-p-hash-blur-test
  (testing "image-distance p-hash blur test"
    (mapv
     (fn [idx image]
       (is (= 0 (image-distance p-hash-fn image image))
           (str "Image with id " idx " has a hamming distance larger than 0 to itself")))
     (range)
     tu/blur)))

(deftest image-distance-p-hash-misc-test
  (testing "image-distance p-hash compr test"
    (mapv
     (fn [idx image]
       (is (= 0 (image-distance p-hash-fn image image))
           (str "Image with id " idx " has a hamming distance larger than 0 to itself")))
     (range)
     tu/misc)))

(deftest image-distance-p-hash-rotd-test
  (testing "image-distance p-hash rotd test"
    (mapv
     (fn [idx image]
       (is (= 0 (image-distance p-hash-fn image image))
           (str "Image with id " idx " has a hamming distance larger than 0 to itself")))
     (range)
     tu/rotd)))

(deftest image-distance-p-hash-compr-blur-test
  (testing "image-distance p-hash compr blur test"
    (mapv
     (fn [idx compressed blurred]
       (is (< (image-distance p-hash-fn compressed blurred) 5)
           (str "Image with id " idx " has a hamming distance larger than 5")))
     (range)
     tu/compr
     tu/blur)))

(deftest image-distance-p-hash-compr-misc-test
  (testing "image-distance p-hash compr misc test"
    (mapv
     (fn [idx compressed misclns]
       (is (< (image-distance p-hash-fn compressed misclns) 2)
           (str "Image with id " idx " has a hamming distance larger than 2")))
     (range)
     tu/compr
     tu/misc)))

; TODO: Rotate and mirror images when comparing them
;(deftest image-distance-p-hash-compr-rotd-test
;  (testing "image-distance p-hash compr rotd test"
;    (mapv
;     (fn [idx compressed rotated]
;       (is (< (image-distance p-hash-fn compressed rotated) 1)
;           (str "Image with id " idx " has a hamming distance larger than 1")))
;     (range)
;     tu/compr
;     tu/rotd)))

(deftest image-distance-p-hash-blur-misc-test
  (testing "image-distance p-hash blur misc test"
    (mapv
     (fn [idx blurred misclns]
       (is (< (image-distance p-hash-fn blurred misclns) 5)
           (str "Image with id " idx " has a hamming distance larger than 5")))
     (range)
     tu/blur
     tu/misc)))

; TODO: Rotate and mirror images when comparing them
;(deftest image-distance-p-hash-blur-rotd-test
;  (testing "image-distance p-hash blur rotd test"
;    (mapv
;     (fn [idx blurred rotated]
;       (is (< (image-distance p-hash-fn blurred rotated) 1)
;           (str "Image with id " idx " has a hamming distance larger than 1")))
;     (range)
;     tu/blur
;     tu/rotd)))

; TODO: Rotate and mirror images when comparing them
;(deftest image-distance-p-hash-misc-rotd-test
;  (testing "image-distance p-hash misc rotd test"
;    (mapv
;     (fn [idx misclns rotated]
;       (is (< (image-distance p-hash-fn misclns rotated) 1)
;           (str "Image with id " idx " has a hamming distance larger than 1")))
;     (range)
;     tu/misc
;     tu/rotd)))