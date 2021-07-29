(ns phash.core
  (:require
   [clojure.string :as s]
   [phash.a-hash :as ah]
   [phash.d-hash :as dh]
   [phash.utils :as u]
   [phash.debug :as d])
  (:gen-class))

; TODO: Remove after done
(set! *warn-on-reflection* true)
(set! *unchecked-math* true)

(defn perceptual-hash
  ([{:keys [width height] :as hash-fn} image]
   (as-> image input
     (u/resize input width height)
     (u/grayscale input)
     (u/brightness-per-pixel input)
     (u/pixels-brightness->hash-bits hash-fn input)))
  ([{:keys [width height] :as hash-fn} image debug-fn]
   (let [resized-image (u/resize image width height)
         gray (u/grayscale resized-image)]
     (debug-fn image)
     (debug-fn resized-image)
     (debug-fn gray)
     (as-> resized-image input
       (u/brightness-per-pixel input)
       (u/pixels-brightness->hash-bits hash-fn input)))))

(defn image-distance [method image-a image-b]
  (let [hash-fn (if (= method :a-hash)
                  (ah/a-hash conj [])
                  (dh/d-hash conj []))]
    (u/hamming-distance
     (perceptual-hash hash-fn image-a)
     (perceptual-hash hash-fn image-b))))

(defn- debug-d [a b]
  (d/gui!)
  (let [image-a (u/load-image a)
        image-b (u/load-image b)
        debug-a (partial d/add-image-to-display! :a)
        debug-b (partial d/add-image-to-display! :b)
        im-dist (image-distance :d-hash image-a image-b)
        d-hash-bits (dh/d-hash conj [])]
    (d/add-text-to-display! :a (str " Hash:" (s/join (perceptual-hash d-hash-bits image-a debug-a)) " "))
    (d/add-text-to-display! :b (str " Hash:" (s/join (perceptual-hash d-hash-bits image-b debug-b)) " "))
    (d/add-text-to-display! :a (str " Distance:" im-dist))
    (d/add-text-to-display! :b (str " Distance:" im-dist))))

(defn- debug-a [a b]
  (d/gui!)
  (let [image-a (u/load-image a)
        image-b (u/load-image b)
        debug-a (partial d/add-image-to-display! :a)
        debug-b (partial d/add-image-to-display! :b)
        im-dist (image-distance :a-hash image-a image-b)
        a-hash-bits (ah/a-hash u/bit->long [0 1])]
    (d/add-text-to-display! :a (str " Hash:" (s/join (perceptual-hash a-hash-bits image-a debug-a)) " "))
    (d/add-text-to-display! :b (str " Hash:" (s/join (perceptual-hash a-hash-bits image-b debug-b)) " "))
    (d/add-text-to-display! :a (str " Distance:" im-dist))
    (d/add-text-to-display! :b (str " Distance:" im-dist))))

(defn -main
  ; TODO: Remove
  "TODO: Remove this after done. This is only for debug purposes"
  [i1 i2 & _]
  #_(debug-a i1 i2)
  (debug-d i1 i2))

;; lein run "test/phash/test_images/compr/architecture_2.jpg" "test/phash/test_images/compr/architecture_2.jpg"