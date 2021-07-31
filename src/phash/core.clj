(ns phash.core
  (:require
   [clojure.string :as s]
   [phash.a-hash :as ah]
   [phash.d-hash :as dh]
   [phash.p-hash :as ph]
   [phash.utils :as u]
   [phash.debug :as d])
  (:gen-class))

; TODO: Remove after done
(set! *warn-on-reflection* true)
(set! *unchecked-math* true)

(defn perceptual-hash
  ([hash-fn ^java.awt.Image image]
   (perceptual-hash hash-fn image u/bit->long [0 1]))
  ([{:keys [width height] :as hash-fn} ^java.awt.Image image reducer init]
   (as-> image im
     (u/resize-image im width height)
     (u/grayscale im)
     (u/image->hash hash-fn im reducer init)))
  ; TODO: Debug fn remove after done
  ([{:keys [width height] :as hash-fn} image debug-fn]
   (let [resized-image (u/resize-image image width height)
         gray (u/grayscale resized-image)]
     (debug-fn image)
     (debug-fn resized-image)
     (debug-fn gray)
     (u/image->hash hash-fn gray))))

(defn image-distance ^Long [hash-fn ^java.awt.Image image-a ^java.awt.Image image-b]
  (u/hamming-distance
   (perceptual-hash hash-fn image-a conj [])
   (perceptual-hash hash-fn image-b conj [])))

(defn eq-images? [hash-fn ^java.awt.Image image-a ^java.awt.Image image-b ^Long threshold]
  (< (image-distance hash-fn image-a image-b) threshold))

(defn- debug-a [^String a ^String b]
  (d/gui!)
  (let [image-a (u/load-image a)
        image-b (u/load-image b)
        debug-a (partial d/add-image-to-display! :a)
        debug-b (partial d/add-image-to-display! :b)
        im-dist (image-distance (ah/a-hash) image-a image-b)
        a-hash-bits (ah/a-hash conj [])]
    (d/add-text-to-display! :a (str " Hash:" (s/join (perceptual-hash a-hash-bits image-a debug-a)) " "))
    (d/add-text-to-display! :b (str " Hash:" (s/join (perceptual-hash a-hash-bits image-b debug-b)) " "))
    (d/add-text-to-display! :a (str " Distance:" im-dist))
    (d/add-text-to-display! :b (str " Distance:" im-dist))))

(defn- debug-d [^String a ^String b]
  (d/gui!)
  (let [image-a (u/load-image a)
        image-b (u/load-image b)
        debug-a (partial d/add-image-to-display! :a)
        debug-b (partial d/add-image-to-display! :b)
        im-dist (image-distance (dh/d-hash) image-a image-b)
        d-hash-bits (dh/d-hash conj [])]
    (d/add-text-to-display! :a (str " Hash:" (s/join (perceptual-hash d-hash-bits image-a debug-a)) " "))
    (d/add-text-to-display! :b (str " Hash:" (s/join (perceptual-hash d-hash-bits image-b debug-b)) " "))
    (d/add-text-to-display! :a (str " Distance:" im-dist))
    (d/add-text-to-display! :b (str " Distance:" im-dist))))

(defn- debug-p [^String a ^String b]
  (d/gui!)
  (let [image-a (u/load-image a)
        image-b (u/load-image b)
        debug-a (partial d/add-image-to-display! :a)
        debug-b (partial d/add-image-to-display! :b)
        im-dist (image-distance (ph/p-hash) image-a image-b)
        p-hash-bits (ph/p-hash conj [])]
    (d/add-text-to-display! :a (str " Hash:" (s/join (perceptual-hash p-hash-bits image-a debug-a)) " "))
    (d/add-text-to-display! :b (str " Hash:" (s/join (perceptual-hash p-hash-bits image-b debug-b)) " "))
    (d/add-text-to-display! :a (str " Distance:" im-dist))
    (d/add-text-to-display! :b (str " Distance:" im-dist))))

(defn -main
  ; TODO: Remove
  "TODO: Remove this after done. This is only for debug purposes"
  [i1 i2 & _]
  #_(debug-a i1 i2)
  #_(debug-d i1 i2)
  (debug-p i1 i2))

;; lein run "test/phash/test_images/compr/architecture_2.jpg" "test/phash/test_images/compr/architecture_2.jpg"