(ns de.nihas101.phash.dct
  (:require
   [de.nihas101.phash.utils :as u]))

(defonce ^:private inv-sqrt-2 (double (/ 1 (Math/sqrt 2.0))))

(defn- coefficient
  "Returns the coefficient of element `u` in the DCT calculation.
   See: https://www.math.cuhk.edu.hk/~lmlui/dct.pdf"
  ^double [^long u]
  (if (zero? u) inv-sqrt-2 1.0))

(defonce ^:private dct-indexes
  (mapv #(u/idx-lin->idx-2d % 32) (range 1024)))

(defn- dct-sum-32x32
  "Calculates the sum X(i,j) of the DCT (specific to a 32x32 matrix),
   according to: https://www.math.cuhk.edu.hk/~lmlui/dct.pdf"
  ^double [^longs values ^long i ^long j]
  (transduce
   (map
    (fn [[^long x ^long y]]
      (* ^long (get values (u/idx-2d->idx-lin x y 32))
         ^double (Math/cos (quot (* (inc (* 2 x)) i Math/PI) 64))
         ^double (Math/cos (quot (* (inc (* 2 y)) j Math/PI) 64)))))
   + dct-indexes))

(defonce ^:private scale-factor (double (/ 1 (Math/sqrt 64))))

(defn discret-cosine-transform-32x32-fn
  "Calculates the DCT of a 32x32 matrix, according to:
   https://www.math.cuhk.edu.hk/~lmlui/dct.pdf"
  [dct-indexes]
  (fn ^longs [values]
    (mapv (fn [[i j]]
            (* ^double scale-factor
               (coefficient i)
               (coefficient j)
               (dct-sum-32x32 values i j)))
          dct-indexes)))

;; This is the slow version of the algorithm:
;; First calculates all 32x32 terms before discarding most of them again

(defonce discret-cosine-transform-32x32
  (discret-cosine-transform-32x32-fn dct-indexes))

(defonce ^:private reduced-dct-indexes
  (for [x (range 8)
        y (range 8)]
    [x y]))

(defn reduce-dct-32x32->8x8
  "Reduces a DCT of a 32x32 matrix to the matrix of the lowest 8x8 frequencies."
  ^longs [values]
  (mapv (fn [[x y]] (get values (u/idx-2d->idx-lin x y 32)))
        reduced-dct-indexes))

;; This is the fast version of the algorithm:
;; Only calculates the necessary terms

(defonce discret-cosine-transform-reduced-32x32
  (discret-cosine-transform-32x32-fn reduced-dct-indexes))
