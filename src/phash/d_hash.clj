(ns phash.d-hash
  (:require
   [phash.utils :as u]))

; TODO: Remove after done
(set! *warn-on-reflection* true)
(set! *unchecked-math* true)

(defrecord DHash [^Long width ^Long height reducer init]
  u/HashFn
  (image->hash-bits [_ image]
    (transduce
     (comp
      ;; Split into overlapping packs of two elements
      (mapcat #(partition 2 1 %))
      ;; If left pixel is brighter output 0 else 1
      (map (fn [[left right]] (if (neg? (- left right)) 1 0))))
     reducer init
     (partition width (u/brightness-per-pixel image)))))

(defonce ^:private width 9)
(defonce ^:private height 8)

(defn d-hash
  "Creates a hash-function for use with phash.utils/image->hash-bits.
   
   Optionally accepts a `reducer` and `init` value to join hash bits in
   an alternate manner, i.e. passing `conj` and `[]` will return the
   bits in a vector.
   Passing no arguments will create function that outputs the hash as a long.
   
   Furthermore also accepts `width` and `height`, which influence
   the size of the hash.
   By default a width and height of 8 are used, resulting in a 64-bit hash."
  ([] (d-hash u/bit->long [0 1]))
  ([reducer init] (d-hash width height reducer init))
  ([^Long width ^Long height reducer init] (DHash. width height reducer init)))