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

(defn image-distance [method image-a image-b]
  (let [hash-fn (if (= method :a-hash)
                  ah/a-hash-bits
                  dh/d-hash-bits)]
    (u/hamming-distance
     (hash-fn image-a)
     (hash-fn image-b))))

; TODO: pHash

(defn- debug-d [a b]
  (d/gui!)
  (let [image-a (u/load-image a)
        image-b (u/load-image b)
        debug-a (partial d/add-image-to-display! :a)
        debug-b (partial d/add-image-to-display! :b)
        im-dist (image-distance :d-hash image-a image-b)]
    (d/add-text-to-display! :a (str " Hash:" (s/join (dh/d-hash-bits image-a debug-a)) " "))
    (d/add-text-to-display! :b (str " Hash:" (s/join (dh/d-hash-bits image-b debug-b)) " "))
    (d/add-text-to-display! :a (str " Distance:" im-dist))
    (d/add-text-to-display! :b (str " Distance:" im-dist))))

(defn- debug-a [a b]
  (d/gui!)
  (let [image-a (u/load-image a)
        image-b (u/load-image b)
        debug-a (partial d/add-image-to-display! :a)
        debug-b (partial d/add-image-to-display! :b)
        im-dist (image-distance :a-hash image-a image-b)]
    (d/add-text-to-display! :a (str " Hash:" (s/join (ah/a-hash-bits image-a debug-a)) " "))
    (d/add-text-to-display! :b (str " Hash:" (s/join (ah/a-hash-bits image-b debug-b)) " "))
    (d/add-text-to-display! :a (str " Distance:" im-dist))
    (d/add-text-to-display! :b (str " Distance:" im-dist))))

(defn -main
  ; TODO: Remove
  "TODO: Remove this after done. This is only for debug purposes"
  [i1 i2 & _]
  #_(debug-a i1 i2)
  (debug-d i1 i2))

;; lein run "test/phash/test_images/compr/architecture_2.jpg" "test/phash/test_images/compr/architecture_2.jpg"