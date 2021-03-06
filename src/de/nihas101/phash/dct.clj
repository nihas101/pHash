(ns de.nihas101.phash.dct
  "A implementation of the discret cosine transformation algorithm specific
   to 32x32 images."
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
         ^double (Math/cos (/ (* (inc (* 2 x)) i Math/PI) 64))
         ^double (Math/cos (/ (* (inc (* 2 y)) j Math/PI) 64)))))
   + dct-indexes))

(defonce ^:private scale-factor (double (/ 1 (Math/sqrt (* 32 32)))))

(defn discrete-cosine-transform-32x32
  "Calculates the DCT of a 32x32 matrix, according to:
   https://www.math.cuhk.edu.hk/~lmlui/dct.pdf"
  (^longs [values] (discrete-cosine-transform-32x32 values dct-indexes))
  (^longs [values dct-indexes]
   (mapv (fn [[i j]]
           (* ^double scale-factor
              (coefficient i)
              (coefficient j)
              (dct-sum-32x32 values i j)))
         dct-indexes)))

(defn discrete-cosine-transform-reduced-32x32
  "Calculates a reduced (`matrix-size` x `matrix-size`) DCT of a 32x32 matrix."
  ([values] (discrete-cosine-transform-reduced-32x32 values 8))
  ([values ^long matrix-size]
   (discrete-cosine-transform-reduced-32x32 values matrix-size matrix-size))
  ([values ^long m ^long n]
   (discrete-cosine-transform-32x32 values
                                    (for [x (range m)
                                          y (range n)]
                                      [x y]))))
