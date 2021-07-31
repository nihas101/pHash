(ns phash.a-hash
  (:require
   [phash.utils :as u]))

; TODO: Remove after done
(set! *warn-on-reflection* true)
(set! *unchecked-math* true)

(defrecord AHash [^Long width ^Long height]
  u/HashFn
  (image->hash [this image] (u/image->hash this image u/bit->long [0 1]))
  (image->hash [_ image reducer init]
    (let [pxls-brightness (u/brightness-per-pixel image)]
      (when (seq pxls-brightness)
        (let [avg-brightness (/ (reduce + pxls-brightness) (count pxls-brightness))]
          (transduce
           (map
            (fn [pxl-brightness] (if (< pxl-brightness avg-brightness) 1 0)))
           reducer init
           pxls-brightness))))))

(defonce ^:private width 8)
(defonce ^:private height width)

(defn a-hash
  "Creates a hash-function for use with phash.utils/image->hash.
   
   Optionally also accepts `width` and `height`, which influence
   the size of the hash.
   By default a width and height of 8 are used, resulting in a 64-bit hash."
  ([] (a-hash width height))
  ([^Long width ^Long height] (AHash. width height)))