(ns de.nihas101.phash.dct-test
  (:require
   [clojure.test :refer :all]
   [de.nihas101.phash.dct :refer :all]
   [de.nihas101.phash.utils :as u]
   [de.nihas101.phash.test-utils :as tu]
   [clojure.test.check.generators :as gen]
   [clojure.test.check.clojure-test :as ct]
   [clojure.test.check.properties :as prop]))

(defonce test-vals (-> tu/compr
                       first
                       (u/resize-image ,,, 32 32)
                       u/grayscale
                       u/get-pixels))

(defn- close-to-equal [as bs epsilon]
  (transduce
   (comp
    (map (fn [[a b]] (if (= a b) 0 (- a b))))
    (map (fn [x] (Math/abs x)))
    (filter (fn [diff] (< epsilon diff))))
   (fn
     ;; Empty seqs are equal
     ([] true)
     ;; If we have not found an offending element we are 'close enough to equal'
     ([result] result)
     ;; We only need to look for one offending element
     ([_ _] (reduced false)))
   (mapv vector as bs)))

(defonce ^:private epsilon 0.000001)

(deftest slow-dct-32x32-test
  (testing "slow dct 32x32 test"
    (is (close-to-equal
         []
         (discrete-cosine-transform-32x32 test-vals)
         epsilon))))
#_(deftest slow-dct-32x32->8x8-test
    (testing "slow dct 32x32 + reduction to 8x8 test"
      (is (close-to-equal []
                          (u/matrix-slice
                           (discrete-cosine-transform-32x32 test-vals))
                          epsilon))))
#_(deftest fast-dct-32x32->8x8-test
    (testing "fast dct with reduction test"
      (is (close-to-equal []
                          (discrete-cosine-transform-reduced-32x32 test-vals)
                          epsilon))))

(defonce ^:private dct-values-gen (gen/vector gen/nat (* 32 32)))

(ct/defspec discrete-cosine-transform-oracle-test 20
  (prop/for-all [dct-vals dct-values-gen]
                (= (u/matrix-slice
                    (discrete-cosine-transform-32x32 dct-vals))
                   (discrete-cosine-transform-reduced-32x32 dct-vals))))