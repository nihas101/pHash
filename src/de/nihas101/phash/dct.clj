(ns de.nihas101.phash.dct
  (:require
   [de.nihas101.phash.utils :as u]))

; TODO: Remove after done
(set! *warn-on-reflection* true)
(set! *unchecked-math* true)

(defonce ^:private inv-sqrt-2 (/ 1 (Math/sqrt 2.0)))

(defn- coefficient
  "Returns the coefficient of element `u` in the DCT calculation.
   See: https://www.math.cuhk.edu.hk/~lmlui/dct.pdf"
  [^long u]
  (if (zero? u) inv-sqrt-2 1))

(defn- dct-sum-32x32
  "Calculates the sum X(i,j) of the DCT (specific to a 32x32 matrix),
   according to: https://www.math.cuhk.edu.hk/~lmlui/dct.pdf"
  ^longs [^longs values ^long i ^long j]
  (reduce +
          (for [x (range 32)
                y (range 32)]
            (* (get values (u/idx-2d->idx-lin x y 32))
               (Math/cos (quot (* (inc (* 2 x)) i Math/PI) 64))
               (Math/cos (quot (* (inc (* 2 y)) j Math/PI) 64))))))

(defonce ^:private scale-factor (/ 1 (Math/sqrt (* 2 32))))

;; This is the slow version of the algorithm,
;; first calculating all 32x32 transformations before discarding 75% of them

(defn discret-cosine-transform-32x32-fn
  "Calculates the DCT of a 32x32 matrix, according to:
   https://www.math.cuhk.edu.hk/~lmlui/dct.pdf"
  [dct-indexes]
  (fn ^longs [values]
    (mapv (fn [[i j]]
            (* scale-factor
               (coefficient i) (coefficient j)
               (dct-sum-32x32 values i j)))
          dct-indexes)))

(defonce ^:private dct-indexes (mapv #(u/idx-in->idx-2d % 32) (range 1024)))

(defonce discret-cosine-transform-32x32
  (discret-cosine-transform-32x32-fn dct-indexes))

(defonce ^:private reduced-dct-indexes
  (for [x (range 32)
        y (range 32)
        :when (and (< x 8) (< y 8))]
    [x y]))

(defn reduce-dct-32x32->8x8
  "Reduces a DCT of a 32x32 matrix to the matrix of the lowest 8x8 frequencies."
  ^longs [values]
  (mapv (fn [[x y]] (get values (u/idx-2d->idx-lin x y 32)))
        reduced-dct-indexes))

;; This is the fast version of the algorithm,
;; only calculating the necessary transformations

(defonce discret-cosine-transform-reduced-32x32
  (discret-cosine-transform-32x32-fn reduced-dct-indexes))

; TODO: Implement a function for fingerprint images
; https://www.hackerfactor.com/blog/index.php?/archives/432-Looks-Like-It.html
; https://www.math.cuhk.edu.hk/~lmlui/dct.pdf
; https://github.com/lacogubik/phash/blob/master/src/phash/core.clj