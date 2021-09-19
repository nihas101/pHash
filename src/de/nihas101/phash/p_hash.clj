(ns de.nihas101.phash.p-hash
  "Contains the functionality specific to pHash."
  (:require
   [de.nihas101.phash.utils :as u]
   [de.nihas101.phash.dct :as dct]))

(defn- dct-32x32
  "Computes the DCT of a 32x32 `image`,
   only keeping the lowest-frequency `m` x `n` values."
  [image m n]
  (-> image
      u/get-pixels
      (dct/discrete-cosine-transform-reduced-32x32 ,,, m n)))

(defrecord PHash [^long width ^long height ^long m ^long n]
  u/HashFn
  (image->hash [this image] (u/image->hash this image u/bit->long [0 1]))
  (image->hash [_ image reducer init]
    (let [dct (dct-32x32 image m n)
          avg-dct (quot (reduce + dct) (count dct))]
      (transduce
       (map (fn [^long dct-val] (if (< dct-val ^long avg-dct) 1 0)))
       reducer init
       dct))))

(defonce ^:private dim 64)

(defn p-hash
  "Creates a hash-function for use with phash.utils/image->hash.
   Optionally also accepts a `hash-size`, which influence the size of the hash.
   Sizes of size = 2^n where n = 2, ..., 6 are supported.
   By default a width and height of 8 are used,
   resulting in a 64-bit (2^6) hash."
  ([] (p-hash dim))
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
       :else (PHash. 32 32 a b)))))