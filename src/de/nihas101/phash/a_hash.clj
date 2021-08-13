(ns de.nihas101.phash.a-hash
  "Contains the functionality specific to aHash."
  (:require
   [de.nihas101.phash.utils :as u]))

(defrecord AHash [^long width ^long height]
  u/HashFn
  (image->hash [this image] (u/image->hash this image u/bit->long [0 1]))
  (image->hash [_ image reducer init]
    (let [pxls-brightness (u/brightness-per-pixel image)]
      (when (seq pxls-brightness)
        (let [avg-brightness (/ (reduce + pxls-brightness)
                                (count pxls-brightness))]
          (transduce
           (map
            (fn [pxl-brightness] (if (< pxl-brightness avg-brightness) 1 0)))
           reducer init
           pxls-brightness))))))

(defonce ^:private dim 64)

(defn a-hash
  "Creates a hash-function for use with phash.utils/image->hash.
   Optionally also accepts a `hash-size`, which influence
   the size of the hash (in bits).
   Sizes of size = 2^n where n = 2, ..., 6 are supported.
   By default a width and height of 8 are used,
   resulting in a 64-bit (2^6) hash."
  ([] (a-hash dim))
  ([^long hash-size]
   (let [log (u/log-2 hash-size)
         [a b] (peek (u/factor-pairs hash-size))]
     (cond
       (< hash-size 4)
       (throw (IllegalArgumentException.
               (str hash-size " is too small (min: 4)")))
       (< 64 hash-size)
       (throw (IllegalArgumentException.
               (str hash-size " is too large (max: 64)")))
       (not= (Math/ceil log) (Math/floor log))
       (throw (IllegalArgumentException.
               (str hash-size " is not a power of 2")))
       :else (AHash. a b)))))