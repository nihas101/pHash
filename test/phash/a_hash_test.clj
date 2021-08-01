(ns phash.a-hash-test
  (:require
   [clojure.test :refer :all]
   [phash.a-hash :refer :all]
   [phash.utils :as u]
   [phash.core :as core]
   [phash.test-utils :as tu]
   [clojure.test.check.clojure-test :as ct]
   [clojure.test.check.properties :as prop]))

; Creates image between 5x5 and 20x20 (inclusive)
(defonce ^:private image-generator (tu/image-gen 5 20))

(defonce ^:private a-hash-fn (a-hash))

(ct/defspec same-image-a-hash-prop-test 5
  (prop/for-all [im image-generator]
                (= (core/perceptual-hash a-hash-fn im)
                   (core/perceptual-hash a-hash-fn im))))

(ct/defspec noise-image-a-hash-prop-test 5
  (prop/for-all [im image-generator]
                (< (u/hamming-distance
                    (core/perceptual-hash a-hash-fn im)
                    (core/perceptual-hash a-hash-fn (tu/noise-filter im)))
                   15)))

(ct/defspec grayscale-image-a-hash-prop-test 5
  (prop/for-all [im image-generator]
                (< (u/hamming-distance
                    (core/perceptual-hash a-hash-fn im)
                    (core/perceptual-hash a-hash-fn (u/grayscale im)))
                   50)))

(ct/defspec blur-image-a-hash-prop-test 5
  (prop/for-all [im image-generator]
                (< (u/hamming-distance
                    (core/perceptual-hash a-hash-fn im)
                    (core/perceptual-hash a-hash-fn (tu/blur-filter im)))
                   50)))