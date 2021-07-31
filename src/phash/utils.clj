(ns phash.utils
  (:require
   [mikera.image.filters :as filt]
   [mikera.image.colours :as col]
   [mikera.image.core :as im]))

; TODO: Remove after done
(set! *warn-on-reflection* true)
(set! *unchecked-math* true)

(defn idx-in->idx-2d
  "Translates a 1D index into a 2D index."
  [^Long idx ^Long width]
  [(rem idx width) (quot idx width)])

(defn idx-2d->idx-lin
  "Translates a 2D index into a 1D index."
  [^Long x ^Long y ^Long width]
  (+ (* y width) x))

(defn bit->long
  ([] [0 1])
  ([[^Long hash _]] hash)
  ([[^Long hash ^Long exp] ^Long bit]
   [(+ hash (* bit exp)) (*' exp 2)]))

(defn hamming-distance
  "Calculates the hamming distance between two Seqable collections."
  [seq-a seq-b]
  (count
   (filter (fn [[^Long a ^Long b]] (not= a b))
           (mapv vector seq-a seq-b))))

(defonce grayscale (filt/grayscale))

(defn rgb-brightness
  "Calculates the brightness of RGB-components `red`, `green` and `blue`.
   
   Calling the function with a reducing function will return a transducer,
   which takes a tuple of RGB-values and calculates their brightness before
   passing it along to the reducing function."
  ([^Long red ^Long green ^Long blue]
   ;; Source: https://www.stemmer-imaging.com/en/knowledge-base/grey-level-grey-value/
   (+ (* 0.299 red) (* 0.587 green) (* 0.114 blue)))
  ([rf]
   (fn
     ([] (rf))
     ([pxl-brightness] (rf pxl-brightness))
     ([pxl-brightness rgb]
      (let [[^Long red ^Long green ^Long blue] rgb]
        (rf pxl-brightness (rgb-brightness red green blue)))))))

(defn brightness-per-pixel
  "Calculates the brightness of every pixel in an image and returns a linear seq
   of their brightness-values."
  [^java.awt.Image image]
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
  (image->hash-bits [this image]
    [this image reducer init] "Calculate a hash from an image"))