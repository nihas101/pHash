(ns phash.a-hash
  (:require
   [phash.utils :as u]))

; TODO: Remove after done
(set! *warn-on-reflection* true)
(set! *unchecked-math* true)

(defrecord AHash [^Long width ^Long height reducer init]
  u/HashFn
  (pixels-brightness->hash-bits [_ pxls-brightness]
    (when (seq pxls-brightness)
      (let [avg-brightness (/ (apply + pxls-brightness) (count pxls-brightness))]
        (transduce
         (map
          (fn [pxl-brightness]
            (if (< pxl-brightness avg-brightness) 1 0)))
         reducer init
         pxls-brightness)))))

(defonce ^:private width 8)
(defonce ^:private height width)

(defn a-hash
  ([] (a-hash u/bit->long [0 1]))
  ([reducer init] (a-hash width height reducer init))
  ([width height reducer init] (AHash. width height reducer init)))