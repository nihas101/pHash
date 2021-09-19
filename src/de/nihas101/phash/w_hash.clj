(ns de.nihas101.phash.w-hash
  "Contains the functionality specific to pHash."
  (:require
   [de.nihas101.phash.utils :as u]
   [de.nihas101.phash.dwt :as dwt]))

(defn- dwt-8x8
  "Computes the 2D DWT of a 8x8 `image`,
   only keeping the highest-frequency `m` x `n` values."
  [image m n om on]
  (-> image
      u/get-pixels
      (dwt/discrete-wavelet-transform-2d ,,, (u/image-width image)
                                             (u/image-height image))
      (u/matrix-slice ,,, 8 m n om on)))

(defrecord WHash [^long width ^long height ^long m ^long n]
  u/HashFn
  (image->hash [this image] (u/image->hash this image u/bit->long [0 1]))
  (image->hash [_ image reducer init]
    (let [dwt (dwt-8x8 image m n (- 8 m) (- 8 n))
          avg-dwt (quot (reduce + dwt) (count dwt))]
      (transduce
       (map (fn [dwt-val] (if (< dwt-val avg-dwt) 1 0)))
       reducer init
       dwt))))

(defonce ^:private dim 64)

(defn w-hash
  "Creates a hash-function for use with phash.utils/image->hash.
   Optionally also accepts a `hash-size`, which influence
   the size of the hash (in bits).
   Sizes of n*n where n = 2, ..., 8 are supported.
   By default a width and height of 8 are used, resulting in a 64-bit hash."
  ([] (w-hash dim))
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
       :else (WHash. 8 8 a b)))))