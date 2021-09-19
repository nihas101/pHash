(ns de.nihas101.phash.dwt-test
  (:require
   [clojure.test :refer :all]
   [de.nihas101.phash.dwt :refer :all]))

(deftest discrete-wavelet-transform-test
  (testing (str "(discrete-wavelet-transform [56 40 8 24 48 48 40 40])"
                " => [304 -48 64 16 16 -16 0 0]")
    (is (= [304 -48 64 16 16 -16 0 0]
           (discrete-wavelet-transform [56 40 8 24 48 48 40 40])))))

(deftest discrete-wavelet-transform-range-test
  (testing (str "(discrete-wavelet-transform [1 2 3 4 5 6 7 8])"
                " => [36 -16 -4 -4 -1 -1 -1 -1]")
    (is (= [36 -16 -4 -4 -1 -1 -1 -1]
           (discrete-wavelet-transform [1 2 3 4 5 6 7 8])))))

(deftest discrete-wavelet-transform-2-test
  (testing (str "(discrete-wavelet-transform [1 2])"
                " => [3 -1]")
    (is (= [3 -1]
           (discrete-wavelet-transform [1 2])))))

(deftest rows->columns-2-test
  (testing "rows->columns 2 x 4 -> 4 x 2"
    (is (= [1 3 5 7
            2 4 6 8]
           (rows->columns [1 2
                           3 4
                           5 6
                           7 8] 2)))))

(deftest rows->columns-4-test
  (testing "rows->columns 4 x 2 -> 2 x 4"
    (is (= [1 5
            2 6
            3 7
            4 8]
           (rows->columns [1 2 3 4
                           5 6 7 8] 4)))))

(deftest rows->columns-square-test
  (testing "rows->columns 4 x 4 -> 4 x 4"
    (is (= [1 5  9 13
            2 6 10 14
            3 7 11 15
            4 8 12 16]
           (rows->columns [1   2  3  4
                           5   6  7  8
                           9  10 11 12
                           13 14 15 16] 4)))))