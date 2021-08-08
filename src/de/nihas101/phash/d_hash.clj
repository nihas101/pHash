(ns de.nihas101.phash.d-hash
  "Contains the functionality specific to dHash."
  (:require
   [de.nihas101.phash.utils :as u]))

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

(defonce ^:private dim 64)

(defn d-hash
  "Creates a hash-function for use with phash.utils/image->hash.
   Optionally also accepts a `hash-size`, which influence the size of the hash.
   Sizes of n*n where n = 2, ..., 8 are supported.
   By default a width and height of 9 and 8 are used respectively,
   resulting in a 64-bit hash."
  ([] (d-hash dim))
  ([^long hash-size]
   (let [dim (long (Math/sqrt hash-size))]
     (cond
       (< hash-size 4) (throw
                        (IllegalArgumentException.
                         (str hash-size " is too small (min: 4)")))
       (< 64 hash-size) (throw
                         (IllegalArgumentException.
                          (str hash-size " is too large (max: 64)")))
       (not (zero? (mod dim 1))) (throw
                                  (IllegalArgumentException.
                                   (str hash-size " is not a square")))
       :else (DHash. (inc (long dim)) (long dim))))))