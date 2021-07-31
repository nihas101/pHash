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

(defonce ^:private p-hash-fn (p-hash))

(ct/defspec same-image-p-hash-prop-test 5
  (prop/for-all [im image-generator]
                (= (core/perceptual-hash p-hash-fn im)
                   (core/perceptual-hash p-hash-fn im))))

(ct/defspec noise-image-p-hash-prop-test 5
  (prop/for-all [im image-generator]
                (< (u/hamming-distance
                    (core/perceptual-hash p-hash-fn im conj [])
                    (core/perceptual-hash p-hash-fn (tu/noise-filter im) conj []))
                   2)))

(ct/defspec grayscale-image-p-hash-prop-test 5
  (prop/for-all [im image-generator]
                (< (u/hamming-distance
                    (core/perceptual-hash p-hash-fn im conj [])
                    (core/perceptual-hash p-hash-fn (u/grayscale im) conj []))
                   1)))

(ct/defspec blur-image-p-hash-prop-test 5
  (prop/for-all [im image-generator]
                (< (u/hamming-distance
                    (core/perceptual-hash p-hash-fn im conj [])
                    (core/perceptual-hash p-hash-fn (tu/blur-filter im) conj []))
                   40)))