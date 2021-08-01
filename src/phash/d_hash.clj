(ns phash.d-hash
  (:require
   [phash.utils :as u]))

; TODO: Remove after done
(set! *warn-on-reflection* true)
(set! *unchecked-math* true)

(defrecord DHash [^long width ^long height]
  u/HashFn
  (image->hash [this image] (u/image->hash this image u/bit->long [0 1]))
  (image->hash [_ image reducer init]
    (transduce
     (comp
      ;; Split into overlapping packs of two elements
      (partition-all width)
      (mapcat #(partition 2 1 %))
      ;; If left pixel is brighter output 0 else 1
      (map (fn [[left right]] (if (neg? (- left right)) 1 0))))
     reducer init
     (u/brightness-per-pixel image))))

(defonce ^:private width 9)
(defonce ^:private height 8)

(defn d-hash
  "Creates a hash-function for use with phash.utils/image->hash.
   
   Optionally also accepts `width` and `height`, which influence
   the size of the hash.
   By default a width and height of 8 are used, resulting in a 64-bit hash."
  ([] (d-hash width height))
  ([^long width ^long height] (DHash. width height)))