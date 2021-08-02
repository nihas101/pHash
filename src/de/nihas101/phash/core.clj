(ns de.nihas101.phash.core
  (:require
   [de.nihas101.phash.utils :as u])
  (:gen-class))

(defn perceptual-hash
  "Calculates the perceptual hash of image `image` based on the given `hash-fn`.
   
   See: `phash.a-hash`, `phash.d-hash`, `phash.p-hash`.
   
   Can optionally accept a `reducer` and `init` to modify the process of
   combining the separate bits into the final hash."
  (^long [hash-fn ^java.awt.Image image]
   (perceptual-hash hash-fn image u/bit->long [0 1]))
  ([{:keys [width height] :as hash-fn} ^java.awt.Image image reducer init]
   (as-> image im
     (u/resize-image im width height)
     (u/grayscale im)
     (u/image->hash hash-fn im reducer init))))

(defn image-distance
  "Calculates the distance of images `image-a` and `image-b`
   based on the hash-function `hash-fn`.
   
   See: `phash.a-hash`, `phash.d-hash`, `phash.p-hash`."
  ^long [hash-fn ^java.awt.Image image-a ^java.awt.Image image-b]
  (u/hamming-distance
   (perceptual-hash hash-fn image-a)
   (perceptual-hash hash-fn image-b)))

(defn eq-images?
  "Returns true if images `image-a` and `image-b` are similar enough, based
   on the threshold `threshold` using the hash function `hash-fn`.
   
   See: `phash.a-hash`, `phash.d-hash`, `phash.p-hash`."
  [hash-fn ^java.awt.Image image-a ^java.awt.Image image-b ^long threshold]
  (< (image-distance hash-fn image-a image-b) threshold))