(ns de.nihas101.phash.utils-test
  (:require
   [clojure.test :refer :all]
   [de.nihas101.phash.utils :refer :all]
   [de.nihas101.phash.test-utils :as tu]
   [clojure.test.check.generators :as gen]
   [clojure.test.check.clojure-test :as ct]
   [clojure.test.check.properties :as prop]))

(deftest bits->long-0-0-test
  (testing "bit->long 0 0 test"
    (is (= [0 2] (bit->long [0 1] 0)))))

(deftest bits->long-0-1-test
  (testing "bit->long 0 1 test"
    (is (= [1 2] (bit->long [0 1] 1)))))

(deftest bits->long-0-4-test
  (testing "bit->long 0 4 test"
    (is (= [4 8] (bit->long [0 4] 1)))))

(deftest bits->long-4-1-test
  (testing "bit->long 4 1 test"
    (is (= [12 16] (bit->long [4 8] 1)))))

(deftest bits->long-reduce-test
  (testing "bit->long reduce test"
    (is (= 0 (first (reduce bit->long [0 1] (repeat 64 0)))))))

(deftest hamming-distance-empty-test
  (testing "hamming-distance of an empty seq is 0"
    (is (= 0 (hamming-distance 0 0)))))

(deftest hamming-distance-equal-test
  (testing "hamming-distance of the same seq is 0"
    (is (= 0 (hamming-distance 2r1 2r1)))))

(deftest hamming-distance-one-diff-test
  (testing "hamming-distance of the a seq with one different elements is 1"
    (is (= 1 (hamming-distance 2r11 2r01)))))

(deftest hamming-distance-two-diffs-test
  (testing "hamming-distance of the a seq with two different elements is 2"
    (is (= 2 (hamming-distance 2r10  2r01)))))

(defonce ^:private bit-gen (gen/choose 0 1))

(defn- bit-pattern-gen [n]
  (gen/vector bit-gen n))

(ct/defspec hamming-distance-prop-test 100
  (prop/for-all [[a b] (gen/tuple (bit-pattern-gen 64)
                                  (bit-pattern-gen 64))]
                (= (count (filter (fn [[a b]] (not= a b))
                                  (mapv vector a b)))
                   (hamming-distance
                    (first (reduce bit->long [0 1] a))
                    (first (reduce bit->long [0 1] b))))))

(deftest rgb-brightness-0-test
  (testing "rgb-brightness of no color is 0"
    (is (= 0.0 (rgb-brightness 0 0 0)))))

(deftest rgb-brightness-255-test
  (testing "rgb-brightness of [255 255 255] is 255"
    (is (= 255.0 (rgb-brightness 255 255 255)))))

(deftest rgb-brightness-red-test
  (testing "rgb-brightness of red is 0"
    (is (= 0.299 (rgb-brightness 1 0 0)))))

(deftest rgb-brightness-blue-test
  (testing "rgb-brightness of blue is 0"
    (is (= 0.587 (rgb-brightness 0 1 0)))))

(deftest rgb-brightness-green-test
  (testing "rgb-brightness of green is 0"
    (is (= 0.114 (rgb-brightness 0 0 1)))))

(deftest brightness-per-pixel-test
  (testing "brightness-per-pixel of test-image"
    (let [pxls-brightness (brightness-per-pixel
                           (resize-image (first tu/compr) 8 8))]
      (is (= 64 (count pxls-brightness)))
      (is (every? (complement neg?) pxls-brightness)))))

(deftest idx-lin->idx-2d-0-1-test
  (testing "(idx-lin->idx-2d 0 1) => [0 0]"
    (is (= [0 0] (idx-lin->idx-2d 0 1)))))

(deftest idx-lin->idx-2d-1-1-test
  (testing "(idx-lin->idx-2d 1 1) => [0 1]"
    (is (= [0 1] (idx-lin->idx-2d 1 1)))))

(deftest idx-lin->idx-2d-2-1-test
  (testing "(idx-lin->idx-2d 2 1) => [0 1]"
    (is (= [0 2] (idx-lin->idx-2d 2 1)))))

(deftest idx-lin->idx-2d-edge-test
  (testing "(idx-lin->idx-2d 89 90) => [89 0]"
    (is (= [89 0] (idx-lin->idx-2d 89 90)))))

(deftest idx-lin->idx-2d-10-4-test
  (testing "(idx-lin->idx-2d 10 4) => [2 2]"
    (is (= [2 2] (idx-lin->idx-2d 10 4)))))

(defonce ^:private idx-lin-gen
  (gen/fmap
   (fn [[a b]] [a (inc b)])
   (gen/tuple gen/nat gen/nat gen/nat)))

(ct/defspec idx-lin->idx-2d-prop-test 100
  (prop/for-all [[x width] idx-lin-gen]
                (let [[xx yy] (idx-lin->idx-2d x width)]
                  (is (and
                       (<= 0 xx width)
                       (<= 0 yy)
                       (= x (+ xx (* yy width))))))))

(deftest idx-2d->idx-lin-2-1-test
  (testing "(idx-2d->idx-lin 0 2 1) => 2"
    (is (= 2 (idx-2d->idx-lin 0 2 1)))))

(deftest idx-2d->idx-lin-edge-test
  (testing "(idx-2d->idx-lin 89 0 90) => 89"
    (is (= 89 (idx-2d->idx-lin 89 0 90)))))

(deftest idx-2d->idx-lin-10-4-test
  (testing "(idx-2d->idx-lin 2 2 4) => 10"
    (is (= 10 (idx-2d->idx-lin 2 2 4)))))

(defonce ^:private idx-2d-gen
  (gen/bind (gen/fmap (fn [[b c]] [b (inc c)])
                      (gen/tuple gen/nat gen/nat))
            (fn [[b c]]
              ;; a MUST be smaller than c,
              ;; otherwise this is not a valid 2d index
              (gen/tuple (gen/choose 0 (dec c))
                         (gen/return b)
                         (gen/return c)))))

(ct/defspec idx-2d->idx-lin-prop-test 100
  (prop/for-all [[x y width] idx-2d-gen]
                (let [xx (idx-2d->idx-lin x y width)]
                  (is
                   (and (<= 0 xx)
                        (= [x y] [(rem xx width) (quot xx width)]))))))