(ns de.nihas101.phash.w-hash-test
  (:require
   [clojure.string :as s]
   [clojure.test :refer :all]
   [de.nihas101.phash.w-hash :refer :all]
   [de.nihas101.phash.utils :as u]
   [de.nihas101.phash.core :as core]
   [de.nihas101.phash.test-utils :as tu]
   [clojure.test.check.generators :as gen]
   [clojure.test.check.clojure-test :as ct]
   [clojure.test.check.properties :as prop]))

; Creates images between 5x5 and 20x20 (inclusive)
(defonce ^:private image-generator (tu/image-gen 5 20))

(defonce ^:private w-hash-fn (w-hash))

(deftest image->hash-w-hash-test
  (testing "u/image->hash using w-hash"
    (is (= 73014772535
           (u/image->hash w-hash-fn (-> tu/compr
                                        first
                                        (u/resize-image ,,, (:width w-hash-fn)
                                                            (:height w-hash-fn))
                                        u/grayscale))))))

(deftest w-hash-size-too-small-test
  (testing "w-hash-bits test with a hash size that is too small"
    (is (thrown? IllegalArgumentException (w-hash 1)))))

(deftest w-hash-size-negative-test
  (testing "w-hash-bits test with a hash size that is negative"
    (is (thrown? IllegalArgumentException (w-hash -1)))))

(deftest w-hash-size-too-large-test
  (testing "w-hash-bits test with a hash size that is too large"
    (is (thrown? IllegalArgumentException (w-hash 65)))))

(deftest w-hash-size-4-test
  (testing "w-hash-bits test with a hash of size 4"
    (is (= "1011"
           (s/join (core/perceptual-hash (w-hash 4)
                                         (first tu/compr) conj []))))))

(deftest w-hash-size-16-test
  (testing "w-hash-bits test with a hash of size 16"
    (is (= "1110001010100011"
           (s/join (core/perceptual-hash (w-hash 16)
                                         (first tu/compr) conj []))))))

(deftest w-hash-size-32-test
  (testing "w-hash-bits test with a hash of size 32"
    (is (= 32
           (count (core/perceptual-hash (w-hash 32)
                                        (first tu/compr) conj []))))))

(ct/defspec w-hash-size-prop-test 10
  (prop/for-all [im image-generator
                 hash-size (gen/fmap #(reduce * (repeat % 2)) (gen/choose 2 6))]
                (= hash-size
                   (count (s/join
                           (core/perceptual-hash (w-hash hash-size)
                                                 im conj []))))))

(ct/defspec same-image-w-hash-prop-test 10
  (prop/for-all [im image-generator]
                (= (core/perceptual-hash w-hash-fn im)
                   (core/perceptual-hash w-hash-fn im))))

(ct/defspec noise-image-w-hash-prop-test 5
  (prop/for-all [im image-generator]
                (< (u/hamming-distance
                    (core/perceptual-hash w-hash-fn im)
                    (core/perceptual-hash w-hash-fn (tu/noise-filter im)))
                   1)))

(ct/defspec grayscale-image-w-hash-prop-test 5
  (prop/for-all [im image-generator]
                (< (u/hamming-distance
                    (core/perceptual-hash w-hash-fn im)
                    (core/perceptual-hash w-hash-fn (u/grayscale im)))
                   1)))

(ct/defspec blur-image-w-hash-prop-test 5
  (prop/for-all [im image-generator]
                (< (u/hamming-distance
                    (core/perceptual-hash w-hash-fn im)
                    (core/perceptual-hash w-hash-fn (tu/blur-filter im)))
                   5)))

(deftest w-hash-diff-images-test
  (testing "w-hash of different images should be different enough"
    (let [hash (w-hash 32)]
      (doseq [[a b] (mapv vector tu/compr (rest tu/compr))]
        (is (not (core/eq-images? hash a b 1)))))))