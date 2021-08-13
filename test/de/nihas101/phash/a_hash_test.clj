(ns de.nihas101.phash.a-hash-test
  (:require
   [clojure.string :as s]
   [clojure.test :refer :all]
   [de.nihas101.phash.a-hash :refer :all]
   [de.nihas101.phash.utils :as u]
   [de.nihas101.phash.core :as core]
   [de.nihas101.phash.test-utils :as tu]
   [clojure.test.check.generators :as gen]
   [clojure.test.check.clojure-test :as ct]
   [clojure.test.check.properties :as prop]))

; Creates image between 5x5 and 20x20 (inclusive)
(defonce ^:private image-generator (tu/image-gen 5 20))

(defonce ^:private a-hash-fn (a-hash))

(deftest image->hash-a-hash-test
  (testing "u/image->hash using a-hash"
    (is (= -55133907778273281
           (u/image->hash a-hash-fn (-> tu/compr
                                        first
                                        (u/resize-image ,,, (:width a-hash-fn)
                                                            (:height a-hash-fn))
                                        u/grayscale))))))

(deftest a-hash-size-too-small-test
  (testing "a-hash-bits test with a hash size that is too small"
    (is (thrown? IllegalArgumentException (a-hash 1)))))

(deftest a-hash-size-negative-test
  (testing "a-hash-bits test with a hash size that is negative"
    (is (thrown? IllegalArgumentException (a-hash -1)))))

(deftest a-hash-size-too-large-test
  (testing "a-hash-bits test with a hash size that is too large"
    (is (thrown? IllegalArgumentException (a-hash 65)))))

(deftest a-hash-size-4-test
  (testing "a-hash-bits test with a hash of size 4"
    (is (= "1100"
           (s/join (core/perceptual-hash (a-hash 4)
                                         (first tu/compr) conj []))))))

(deftest a-hash-size-16-test
  (testing "a-hash-bits test with a hash of size 16"
    (is (= "1111100100001111"
           (s/join (core/perceptual-hash (a-hash 16)
                                         (first tu/compr) conj []))))))

(deftest a-hash-size-32-test
  (testing "a-hash-bits test with a hash of size 32"
    (is (= 32
           (count (core/perceptual-hash (a-hash 32)
                                        (first tu/compr) conj []))))))

(ct/defspec a-hash-size-prop-test 10
  (prop/for-all [im image-generator
                 hash-size (gen/fmap #(reduce * (repeat % 2)) (gen/choose 2 6))]
                (= hash-size
                   (count (s/join
                           (core/perceptual-hash (a-hash hash-size)
                                                 im conj []))))))

(ct/defspec same-image-a-hash-prop-test 10
  (prop/for-all [im image-generator]
                (= (core/perceptual-hash a-hash-fn im)
                   (core/perceptual-hash a-hash-fn im))))

(ct/defspec noise-image-a-hash-prop-test 10
  (prop/for-all [im image-generator]
                (< (u/hamming-distance
                    (core/perceptual-hash a-hash-fn im)
                    (core/perceptual-hash a-hash-fn (tu/noise-filter im)))
                   15)))

(ct/defspec grayscale-image-a-hash-prop-test 10
  (prop/for-all [im image-generator]
                (< (u/hamming-distance
                    (core/perceptual-hash a-hash-fn im)
                    (core/perceptual-hash a-hash-fn (u/grayscale im)))
                   50)))

(ct/defspec blur-image-a-hash-prop-test 10
  (prop/for-all [im image-generator]
                (< (u/hamming-distance
                    (core/perceptual-hash a-hash-fn im)
                    (core/perceptual-hash a-hash-fn (tu/blur-filter im)))
                   50)))