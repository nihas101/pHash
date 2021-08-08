(ns de.nihas101.phash.p-hash
  "Contains the functionality specific to pHash."
  (:require
   [de.nihas101.phash.utils :as u]
   [de.nihas101.phash.dct :as dct]))

(defn- dct-32x32
  "Computes the DCT of a 32x32 `image`,
   only keeping the lowest-frequency `width` x `height` values."
  [image dct-reduction-size]
  (-> image
      u/get-pixels
      (dct/discret-cosine-transform-reduced-32x32 ,,, dct-reduction-size)))

(defrecord PHash [^long width ^long height ^long dct-reduction-size]
  u/HashFn
  (image->hash [this image] (u/image->hash this image u/bit->long [0 1]))
  (image->hash [_ image reducer init]
    (let [dct (dct-32x32 image dct-reduction-size)
          avg-dct (/ (reduce + dct) (count dct))]
      (transduce
       (map (fn [dct-val] (if (< dct-val avg-dct) 1 0)))
       reducer init
       dct))))

(defonce ^:private dim 64)

(defn p-hash
  "Creates a hash-function for use with phash.utils/image->hash.
   Optionally also accepts a `hash-size`, which influence the size of the hash.
   Sizes of n*n where n = 2, ..., 8 are supported.
   By default a width and height of 8 are used,
   resulting in a 64-bit hash."
  ([] (p-hash dim))
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
       :else (PHash. 32 32 (long dim))))))