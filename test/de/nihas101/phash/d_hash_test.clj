(ns de.nihas101.phash.d-hash-test
  (:require
   [clojure.string :as s]
   [clojure.test :refer :all]
   [de.nihas101.phash.d-hash :refer :all]
   [de.nihas101.phash.utils :as u]
   [de.nihas101.phash.core :as core]
   [de.nihas101.phash.test-utils :as tu]
   [clojure.test.check.generators :as gen]
   [clojure.test.check.clojure-test :as ct]
   [clojure.test.check.properties :as prop]))

; Creates images between 5x5 and 20x20 (inclusive)
(defonce ^:private image-generator (tu/image-gen 5 20))

(defonce ^:private d-hash-fn (d-hash))

(deftest image->hash-d-hash-test
  (testing "u/image->hash using d-hash"
    (is (= -8216597464464607979
           (u/image->hash d-hash-fn (-> tu/compr
                                        first
                                        (u/resize-image ,,, (:width d-hash-fn)
                                                            (:height d-hash-fn))
                                        u/grayscale))))))

(deftest d-hash-size-too-small-test
  (testing "d-hash-bits test with a hash size that is too small"
    (is (thrown? IllegalArgumentException (d-hash 1)))))

(deftest d-hash-size-negative-test
  (testing "d-hash-bits test with a hash size that is negative"
    (is (thrown? IllegalArgumentException (d-hash -1)))))

(deftest d-hash-size-too-large-test
  (testing "d-hash-bits test with a hash size that is too large"
    (is (thrown? IllegalArgumentException (d-hash 65)))))

(deftest d-hash-size-4-test
  (testing "d-hash-bits test with a hash of size 4"
    (is (= "1001"
           (s/join (core/perceptual-hash (d-hash 4)
                                         (first tu/compr) conj []))))))

(deftest d-hash-size-16-test
  (testing "d-hash-bits test with a hash of size 16"
    (is (= "1000101010111011"
           (s/join (core/perceptual-hash (d-hash 16)
                                         (first tu/compr) conj []))))))

(ct/defspec d-hash-size-prop-test 10
  (prop/for-all [im image-generator
                 hash-size (gen/fmap #(reduce * (repeat % 2)) (gen/choose 2 6))]
                (= hash-size
                   (count (s/join
                           (core/perceptual-hash (d-hash hash-size)
                                                 im conj []))))))

(ct/defspec same-image-d-hash-prop-test 10
  (prop/for-all [im image-generator]
                (= (core/perceptual-hash d-hash-fn im)
                   (core/perceptual-hash d-hash-fn im))))

(deftest d-hash-diff-images-test
  (testing "d-hash of different images should be different enough"
    (let [hash (d-hash 32)]
      (doseq [[a b] (mapv vector tu/compr (rest tu/compr))]
        (is (not (core/eq-images? hash a b 5)))))))

(ct/defspec noise-image-d-hash-prop-test 10
  (prop/for-all [im image-generator]
                (< (u/hamming-distance
                    (core/perceptual-hash d-hash-fn im)
                    (core/perceptual-hash d-hash-fn (tu/noise-filter im)))
                   20)))

(ct/defspec grayscale-image-d-hash-prop-test 10
  (prop/for-all [im image-generator]
                (< (u/hamming-distance
                    (core/perceptual-hash d-hash-fn im)
                    (core/perceptual-hash d-hash-fn (u/grayscale im)))
                   9)))

(ct/defspec blur-image-d-hash-prop-test 10
  (prop/for-all [im image-generator]
                (< (u/hamming-distance
                    (core/perceptual-hash d-hash-fn im)
                    (core/perceptual-hash d-hash-fn (tu/blur-filter im)))
                   40)))