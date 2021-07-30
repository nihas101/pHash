(ns phash.d-hash
  (:require
   [phash.utils :as u]))

; TODO: Remove after done
(set! *warn-on-reflection* true)
(set! *unchecked-math* true)

(defrecord DHash [^Long width ^Long height reducer init]
  u/HashFn
  (image->hash-bits [_ image]
    (transduce
     (comp
      ;; Split into overlapping packs of two elements
      (mapcat #(partition 2 1 %))
      ;; If left pixel is brighter output 0 else 1
      (map (fn [[left right]] (if (neg? (- left right)) 1 0))))
     reducer init
     (partition width (u/brightness-per-pixel image)))))

(defonce ^:private width 9)
(defonce ^:private height 8)

(defn d-hash
  ([] (d-hash u/bit->long [0 1]))
  ([reducer init] (d-hash width height reducer init))
  ([^Long width ^Long height reducer init] (DHash. width height reducer init)))