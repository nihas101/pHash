(ns de.nihas101.phash.p-hash-test
  (:require
   [clojure.test :refer :all]
   [clojure.string :as s]
   [de.nihas101.phash.p-hash :refer :all]
   [de.nihas101.phash.utils :as u]
   [de.nihas101.phash.core :as core]
   [de.nihas101.phash.test-utils :as tu]
   [clojure.test.check.generators :as gen]
   [clojure.test.check.clojure-test :as ct]
   [clojure.test.check.properties :as prop]))

; Creates image between 5x5 and 20x20 (inclusive)
(defonce ^:private image-generator (tu/image-gen 5 20))

(defonce ^:private p-hash-fn (p-hash))

(deftest image->hash-p-hash-test
  (testing "u/image->hash using p-hash"
    (is (= 72060892623284135
           (u/image->hash p-hash-fn (-> tu/compr
                                        first
                                        (u/resize-image ,,, (:width p-hash-fn)
                                                            (:height p-hash-fn))
                                        u/grayscale))))))

(deftest p-hash-size-too-small-test
  (testing "p-hash-bits test with a hash size that is too small"
    (is (thrown? IllegalArgumentException (p-hash 1)))))

(deftest p-hash-size-negative-test
  (testing "p-hash-bits test with a hash size that is negative"
    (is (thrown? IllegalArgumentException (p-hash -1)))))

(deftest p-hash-size-too-large-test
  (testing "p-hash-bits test with a hash size that is too large"
    (is (thrown? IllegalArgumentException (p-hash 65)))))

(deftest p-hash-size-4-test
  (testing "p-hash-bits test with a hash of size 4"
    (is (= "1000"
           (s/join (core/perceptual-hash (p-hash 4)
                                         (first tu/compr) conj []))))))

(deftest p-hash-size-8-test
  (testing "p-hash-bits test with a hash of size 8"
    (is (= "11001000"
           (s/join (core/perceptual-hash (p-hash 8)
                                         (first tu/compr) conj []))))))

(deftest p-hash-size-16-test
  (testing "p-hash-bits test with a hash of size 16"
    (is (= "1110110000000000"
           (s/join (core/perceptual-hash (p-hash 16)
                                         (first tu/compr) conj []))))))

(ct/defspec p-hash-size-prop-test 10
  (prop/for-all [im image-generator
                 hash-size (gen/fmap #(reduce * (repeat % 2)) (gen/choose 2 6))]
                (= hash-size
                   (count (s/join
                           (core/perceptual-hash (p-hash hash-size)
                                                 im conj []))))))

(ct/defspec same-image-p-hash-prop-test 5
  (prop/for-all [im image-generator]
                (= (core/perceptual-hash p-hash-fn im)
                   (core/perceptual-hash p-hash-fn im))))

(ct/defspec noise-image-p-hash-prop-test 5
  (prop/for-all [im image-generator]
                (< (u/hamming-distance
                    (core/perceptual-hash p-hash-fn im)
                    (core/perceptual-hash p-hash-fn (tu/noise-filter im)))
                   2)))

(ct/defspec grayscale-image-p-hash-prop-test 5
  (prop/for-all [im image-generator]
                (< (u/hamming-distance
                    (core/perceptual-hash p-hash-fn im)
                    (core/perceptual-hash p-hash-fn (u/grayscale im)))
                   1)))

(ct/defspec blur-image-p-hash-prop-test 5
  (prop/for-all [im image-generator]
                (< (u/hamming-distance
                    (core/perceptual-hash p-hash-fn im)
                    (core/perceptual-hash p-hash-fn (tu/blur-filter im)))
                   40)))