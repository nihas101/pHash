(ns phash.utils
  (:require
   [mikera.image.filters :as filt]
   [mikera.image.colours :as col]
   [mikera.image.core :as im]))

; TODO: Remove after done
(set! *warn-on-reflection* true)
(set! *unchecked-math* true)

(defn bit->long
  ([] [0 1])
  ([[^Long hash _]] hash)
  ([[^Long hash ^Long exp] bit]
   [(+ hash (* bit exp)) (*' exp 2)]))

(defn hamming-distance [bits-a bits-b]
  (count
   (filter (fn [[a b]] (not= a b))
           (mapv vector bits-a bits-b))))

(defonce grayscale (filt/grayscale))

(defn pixel-brightness
  ([red green blue]
   ; Source: https://www.stemmer-imaging.com/en/knowledge-base/grey-level-grey-value/
   (+ (* 0.299 red) (* 0.587 green) (* 0.114 blue)))
  ([rf]
   (fn
     ([] (rf))
     ([pxl-brightness] (rf pxl-brightness))
     ([pxl-brightness rgb]
      (let [[red green blue] rgb]
        (rf pxl-brightness (pixel-brightness red green blue)))))))

(defn brightness-per-pixel [image]
  (transduce
   (comp (map col/components-rgb)
         pixel-brightness)
   conj
   (im/get-pixels image)))

(def load-image im/load-image)
(def resize im/resize)

(defprotocol HashFn
  "A hash function used to calculate a hash from a sequence of brightness values."
  (pixels-brightness->hash-bits [this pxls-brightness] "Calculate a hash from a sequence of brightness values"))