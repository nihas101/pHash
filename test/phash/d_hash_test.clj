(ns phash.d-hash-test
  (:require
   [clojure.test :refer :all]
   [phash.d-hash :refer :all]
   [phash.utils :as u]
   [phash.core :as core]
   [phash.test-utils :as tu]
   [clojure.test.check.clojure-test :as ct]
   [clojure.test.check.properties :as prop]))

; Creates image between 5x5 and 20x20 (inclusive)
(defonce ^:private image-generator (tu/image-gen 5 20))

(ct/defspec same-image-d-hash-prop-test 5
  (let [hash-fn (d-hash)]
    (prop/for-all [im image-generator]
                  (= (core/perceptual-hash hash-fn im)
                     (core/perceptual-hash hash-fn im)))))

(ct/defspec noise-image-d-hash-prop-test 5
  (let [hash-fn (d-hash conj [])]
    (prop/for-all [im image-generator]
                  (< (u/hamming-distance
                      (core/perceptual-hash hash-fn im)
                      (core/perceptual-hash hash-fn (tu/noise-filter im)))
                     20))))

(ct/defspec grayscale-image-d-hash-prop-test 5
  (let [hash-fn (d-hash conj [])]
    (prop/for-all [im image-generator]
                  (< (u/hamming-distance
                      (core/perceptual-hash hash-fn im)
                      (core/perceptual-hash hash-fn (u/grayscale im)))
                     8))))

(ct/defspec blur-image-d-hash-prop-test 5
  (let [hash-fn (d-hash conj [])]
    (prop/for-all [im image-generator]
                  (< (u/hamming-distance
                      (core/perceptual-hash hash-fn im)
                      (core/perceptual-hash hash-fn (tu/blur-filter im)))
                     40))))