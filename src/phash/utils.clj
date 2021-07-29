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
  ([[hash _]] hash)
  ([[hash exp] bit]
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

(defn hash-fn [core-fn]
  (fn [image] (core-fn bit->long [0 1] image)))

(defn hash-bits-fn
  "Creates a perceptual hash function.
   Faster than pHash and less prone to false positives than aHash.
   
   Returns the individual bits in a seq."
  [core-fn]
  (fn
    ([image]
     (core-fn conj [] image))
    ([image debug-fn]
     (core-fn conj [] image debug-fn))))

; TODO: Use defmethod (or defprotocol) to dispatch on :a-hash, :d-hash etc for resize and pixels-brightness->hash-bits
; TODO: When using defprotol you can keep the width and height there also
(defn hash-core [pixels-brightness->hash-bits width height]
  (fn
    ([reducer init image]
     (-> image
         (resize ,,, width height)
         grayscale
         brightness-per-pixel
         (pixels-brightness->hash-bits ,,, reducer init)))
    ([reducer init image debug-fn]
     (let [resized-image (resize image width height)
           gray (grayscale resized-image)]
       (debug-fn image)
       (debug-fn resized-image)
       (debug-fn gray)
       (-> resized-image
           brightness-per-pixel
           (pixels-brightness->hash-bits ,,, reducer init))))))