(ns phash.dct
  (:require
   [phash.utils :as u]))

; TODO: Remove after done
(set! *warn-on-reflection* true)
(set! *unchecked-math* true)

(defonce ^:private sqrt-2-inv (/ 1 (Math/sqrt 2.0)))

(defn- coefficient [^Long u]
  (if (zero? u) sqrt-2-inv 1))

(defn- dct-sum-32x32
  "https://www.math.cuhk.edu.hk/~lmlui/dct.pdf"
  [values i j]
  (reduce +
          (for [x (range 32)
                y (range 32)]
            (* (get values (u/idx-2d->idx-lin x y 32))
               (Math/cos (quot (* (inc (* 2 x)) i Math/PI) 64))
               (Math/cos (quot (* (inc (* 2 y)) j Math/PI) 64))))))

(defonce ^:private scale-factor (/ 1 (Math/sqrt (* 2 32))))

(defn discret-cosine-transform-32x32
  "https://www.math.cuhk.edu.hk/~lmlui/dct.pdf"
  [values]
  (mapv (fn [lin-idx]
          (let [[i j] (u/idx-in->idx-2d lin-idx 32)]
            (* scale-factor
               (coefficient i) (coefficient j)
               (dct-sum-32x32 values i j))))
        (range 1024)))

; TODO: Why calculate all these frequencies when we are just going to drop them?
;       Just skip the parst outside of the first 8x8 block? (Copy these into a test and compare them via oracle)
(defn reduce-dct-32x32->8x8 [values]
  (for [x (range 32)
        y (range 32)
        :when (and (< x 8) (< y 8))]
    (get values (u/idx-2d->idx-lin x y 32))))

; TODO: Test if this works + Implement a function for fingerprint images
; https://www.hackerfactor.com/blog/index.php?/archives/432-Looks-Like-It.html
; https://www.math.cuhk.edu.hk/~lmlui/dct.pdf
; https://github.com/lacogubik/phash/blob/master/src/phash/core.clj