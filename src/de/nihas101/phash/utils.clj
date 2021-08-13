(ns de.nihas101.phash.utils
  "Utility functions for perceptual hashes."
  (:require
   [mikera.image.filters :as filt]
   [mikera.image.colours :as col]
   [mikera.image.core :as im]))

(defn idx-lin->idx-2d
  "Translates a 1D index into a 2D index."
  ^longs [^long idx ^long width]
  [(rem idx width) (quot idx width)])

(defn idx-2d->idx-lin
  "Translates a 2D index into a 1D index."
  ^long [^long x ^long y ^long width]
  (+ (* y width) x))

(defn bit->long
  ([] [0 1])
  ([[hash _]] hash)
  ([[^long hash ^long exp] ^long bit]
   [(if (zero? bit) hash (+ hash exp))
    (bit-shift-left exp 1)]))

(defn hamming-distance
  "Calculates the hamming distance between two longs."
  ^long [^long a ^long b]
  (Long/bitCount (bit-xor a b)))

(defn log-2
  "Calculates the log base 2 of number `n`."
  [n] (/ (Math/log n) (Math/log 2)))

(defn factor-pairs
  "Returns the factor pairs of a number `n`."
  [n]
  (transduce
   (comp
    (take-while #(<= % (Math/sqrt n)))
    (filter #(zero? (mod n %)))
    (map (fn [x] [x (quot n x)])))
   conj [] (rest (range))))

(defonce grayscale (filt/grayscale))

(defn rgb-brightness
  "Calculates the brightness of RGB-components `red`, `green` and `blue`.

   Calling the function with a reducing function will return a transducer,
   which takes a tuple of RGB-values and calculates their brightness before
   passing it along to the reducing function."
  ([^long red ^long green ^long blue]
   ;; Source:
   ;; https://www.stemmer-imaging.com/en/knowledge-base/grey-level-grey-value/
   (+ (* 0.299 red) (* 0.587 green) (* 0.114 blue)))
  ([rf]
   (fn
     ([] (rf))
     ([pxl-brightness] (rf pxl-brightness))
     ([pxl-brightness rgb]
      (let [[^long red ^long green ^long blue] rgb]
        (rf pxl-brightness (rgb-brightness red green blue)))))))

(defn brightness-per-pixel
  "Calculates the brightness of every pixel in an image and returns a linear seq
   of their brightness-values."
  ^longs [^java.awt.Image image]
  (transduce
   (comp (map col/components-rgb)
         rgb-brightness)
   conj
   (im/get-pixels image)))

(def new-image im/new-image)
(def load-image im/load-image)
(def resize-image im/resize)
(def get-pixels im/get-pixels)

(defprotocol HashFn
  "A hash function used to calculate a hash from an image.

   Optionally accepts a `reducer` and `init` value to join hash bits in
   an alternate manner, i.e. passing `conj` and `[]` will return the
   bits in a vector.
   Passing no arguments will create function that outputs the hash as a long."
  (image->hash [this image]
    [this image reducer init] "Calculate a hash from an image"))