(ns phash.d-hash
  (:require
   [phash.utils :as u]))

; TODO: Add tests

(defonce ^:private width 9)
(defonce ^:private height 8)

(defn- pixels-brightness->hash-bits
  ([pxls-brightness width] (pixels-brightness->hash-bits pxls-brightness width conj []))
  ([pxls-brightness width reducer init]
   (transduce
    (comp
    ; Split into overlapping packs of two elements
     (mapcat #(partition 2 1 %))
    ; If left pixel is brighter output 0 else 1
     (map (fn [[left right]] (if (neg? (- left right)) 1 0))))
    reducer init
    (partition width pxls-brightness))))

(def ^:private d-hash-core (u/hash-core #(pixels-brightness->hash-bits %1 width %2 %3) 
                                        width height))

(def d-hash-bits
  "Difference hash implementation of the perceptual hash.
   Promises to be faster than pHash and less prone to false positives than aHash.
   
   Returns the individual bits in a seq."
  (u/hash-bits-fn d-hash-core))

(def d-hash
  "Difference hash implementation of the perceptual hash.
   Promises to be faster than pHash and less prone to false positives than aHash.
   
   Returns the hash in the form of a long."
  (u/hash-fn d-hash-core))