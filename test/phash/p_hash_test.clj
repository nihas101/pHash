(ns phash.p-hash-test
  (:require
   [clojure.test :refer :all]
   [phash.p-hash :refer :all]
   [phash.utils :as u]
   [phash.core :as core]
   [phash.test-utils :as tu]
   [clojure.test.check.clojure-test :as ct]
   [clojure.test.check.properties :as prop]))

; Creates image between 5x5 and 20x20 (inclusive)
(defonce ^:private image-generator (tu/image-gen 5 20))

(ct/defspec same-image-p-hash-prop-test 5
  (let [hash-fn (p-hash)]
    (prop/for-all [im image-generator]
                  (= (core/perceptual-hash hash-fn im)
                     (core/perceptual-hash hash-fn im)))))

(ct/defspec noise-image-p-hash-prop-test 5
  (let [hash-fn (p-hash conj [])]
    (prop/for-all [im image-generator]
                  (< (u/hamming-distance
                      (core/perceptual-hash hash-fn im)
                      (core/perceptual-hash hash-fn (tu/noise-filter im)))
                     1))))

(ct/defspec grayscale-image-p-hash-prop-test 5
  (let [hash-fn (p-hash conj [])]
    (prop/for-all [im image-generator]
                  (< (u/hamming-distance
                      (core/perceptual-hash hash-fn im)
                      (core/perceptual-hash hash-fn (u/grayscale im)))
                     1))))

(ct/defspec blur-image-p-hash-prop-test 5
  (let [hash-fn (p-hash conj [])]
    (prop/for-all [im image-generator]
                  (< (u/hamming-distance
                      (core/perceptual-hash hash-fn im)
                      (core/perceptual-hash hash-fn (tu/blur-filter im)))
                     25))))