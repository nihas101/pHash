(ns phash.a-hash
  (:require
   [phash.utils :as u]))

; TODO: Remove after done
(set! *warn-on-reflection* true)
(set! *unchecked-math* true)

(defrecord AHash [^Long width ^Long height reducer init]
  u/HashFn
  (image->hash-bits [_ image]
    (let [pxls-brightness (u/brightness-per-pixel image)]
      (when (seq pxls-brightness)
        (let [avg-brightness (/ (reduce + pxls-brightness) (count pxls-brightness))]
          (transduce
           (map
            (fn [pxl-brightness] (if (< pxl-brightness avg-brightness) 1 0)))
           reducer init
           pxls-brightness))))))

(defonce ^:private width 8)
(defonce ^:private height width)

(defn a-hash
  "Creates a hash-function for use with phash.utils/image->hash-bits.
   
   Optionally accepts a `reducer` and `init` value to join hash bits in
   an alternate manner, i.e. passing `conj` and `[]` will return the
   bits in a vector.
   Passing no arguments will create function that outputs the hash as a long.
   
   Furthermore also accepts `width` and `height`, which influence
   the size of the hash.
   By default a width and height of 8 are used, resulting in a 64-bit hash."
  ([] (a-hash u/bit->long [0 1]))
  ([reducer init] (a-hash width height reducer init))
  ([^Long width ^Long height reducer init] (AHash. width height reducer init)))