(ns phash.p-hash
  (:require
   [phash.utils :as u]
   [phash.dct :as dct]))

; TODO: Remove after done
(set! *warn-on-reflection* true)
(set! *unchecked-math* true)

(defn- dct-32x32
  "Computes the DCT of a 32x32 image,
   only keeping the lowest-frequency 8x8 values."
  [image]
  (-> image
      u/get-pixels
      dct/discret-cosine-transform-reduced-32x32))

(defrecord PHash [^long width ^long height]
  u/HashFn
  (image->hash [this image] (u/image->hash this image u/bit->long [0 1]))
  (image->hash [_ image reducer init]
    (let [dct (dct-32x32 image)
          avg-dct (/ (reduce + dct) (count dct))]
      (transduce
       (map (fn [dct-val] (if (< dct-val avg-dct) 1 0)))
       reducer init
       dct))))

(defonce ^:private width 32)
(defonce ^:private height width)

(defn p-hash
  "Creates a hash-function for use with phash.utils/image->hash."
  [] (PHash. width height))