(ns phash.utils
  (:require
   [mikera.image.filters :as filt]
   [mikera.image.colours :as col]
   [mikera.image.core :as im]))

; TODO: Remove after done
(set! *warn-on-reflection* true)
(set! *unchecked-math* true)

(defn idx-in->idx-2d [^Long idx ^Long width]
  [(rem idx width) (quot idx width)])

(defn idx-2d->idx-lin [^Long x ^Long y ^Long width]
  (+ (* y width) x))

(defn bit->long
  ([] [0 1])
  ([[^Long hash _]] hash)
  ([[^Long hash ^Long exp] ^Long bit]
   [(+ hash (* bit exp)) (*' exp 2)]))

(defn hamming-distance [bits-a bits-b]
  (count
   (filter (fn [[^Long a ^Long b]] (not= a b))
           (mapv vector bits-a bits-b))))

(defonce grayscale (filt/grayscale))

(defn pixel-brightness
  ([^Long red ^Long green ^Long blue]
   ;; Source: https://www.stemmer-imaging.com/en/knowledge-base/grey-level-grey-value/
   (+ (* 0.299 red) (* 0.587 green) (* 0.114 blue)))
  ([rf]
   (fn
     ([] (rf))
     ([pxl-brightness] (rf pxl-brightness))
     ([pxl-brightness rgb]
      (let [[^Long red ^Long green ^Long blue] rgb]
        (rf pxl-brightness (pixel-brightness red green blue)))))))

(defn brightness-per-pixel [^java.awt.Image image]
  (transduce
   (comp (map col/components-rgb)
         pixel-brightness)
   conj
   (im/get-pixels image)))

(def new-image im/new-image)
(def load-image im/load-image)
(def resize im/resize)
(def get-pixels im/get-pixels)

(defprotocol HashFn
  "A hash function used to calculate a hash from an image."
  (image->hash-bits [this image] "Calculate a hash from an image"))