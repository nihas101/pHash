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

(defrecord PHash [^Long width ^Long height reducer init]
  u/HashFn
  (image->hash-bits [_ image]
    (let [dct (dct-32x32 image)
          avg-dct (/ (reduce + dct) (count dct))]
      (transduce
       (map
        (fn [dct-val]
          (if (< dct-val avg-dct) 1 0)))
       reducer init
       dct))))

(defonce ^:private width 32)
(defonce ^:private height width)

(defn p-hash
  "Creates a hash-function for use with phash.utils/image->hash-bits.
   
   Optionally accepts a `reducer` and `init` value to join hash bits in
   an alternate manner, i.e. passing `conj` and `[]` will return the
   bits in a vector.
   Passing no arguments will create function that outputs the hash as a long.
   
   Furthermore also accepts `width` and `height`, which influence
   the size of the hash.
   By default a width and height of 8 are used, resulting in a 64-bit hash."
  ([] (p-hash u/bit->long [0 1]))
  ([reducer init] (p-hash width height reducer init))
  ([^Long width ^Long height reducer init] (PHash. width height reducer init)))