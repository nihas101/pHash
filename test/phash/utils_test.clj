(ns phash.utils-test
  (:require
   [clojure.test :refer :all]
   [phash.utils :refer :all]
   [phash.test-utils :as tu]))

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
    (let [pxls-brightness (brightness-per-pixel (resize-image (first tu/compr) 8 8))]
      (is (= 64 (count pxls-brightness)))
      (is (every? (complement neg?) pxls-brightness)))))

; TODO: Add property based tests