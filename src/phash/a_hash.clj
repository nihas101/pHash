(ns phash.a-hash
  (:require
   [phash.utils :as u]))

(defonce ^:private width 8)
(defonce ^:private height width)

(defn- pixels-brightness->hash-bits
  ([pxls-brightness] (pixels-brightness->hash-bits pxls-brightness conj []))
  ([pxls-brightness reducer init]
   (when (seq pxls-brightness)
     (let [avg-brightness (/ (apply + pxls-brightness) (count pxls-brightness))]
       (transduce
        (map
         (fn [pxl-brightness]
           (if (< pxl-brightness avg-brightness) 1 0)))
        reducer init
        pxls-brightness)))))

(def ^:private a-hash-core (u/hash-core pixels-brightness->hash-bits width height))

(def a-hash-bits
  "Average (or mean) hash implementation of the perceptual hash.
   Fast but inaccurate and may lead to some false positives.
   
   Returns the individual bits in a seq."
  (u/hash-bits-fn a-hash-core))

(def a-hash
  "Average (or mean) hash implementation of the perceptual hash.
   Fast but inaccurate and may lead to some false positives.
   
   Returns the hash in the form of a long."
  (u/hash-fn a-hash-core))