(ns phash.p-hash
  (:require
   [phash.utils :as u]
   [phash.dct :as dct]))

; TODO: Remove after done
(set! *warn-on-reflection* true)
(set! *unchecked-math* true)

(defrecord PHash [^Long width ^Long height reducer init]
  u/HashFn
  (image->hash-bits [_ image]
    (let [dct (-> image
                  u/get-pixels
                  dct/discret-cosine-transform-32x32
                  dct/reduce-dct-32x32->8x8)
          avg-dct (/ (reduce + dct) (count dct))]
      (transduce
       (map
        (fn [dct-val]
          (if (< dct-val avg-dct) 1 0)))
       reducer init
       dct))))

(defonce ^:private width 32)
(defonce ^:private height width)

(defn p-hash
  ([] (p-hash u/bit->long [0 1]))
  ([reducer init] (p-hash width height reducer init))
  ([^Long width ^Long height reducer init] (PHash. width height reducer init)))

