(ns de.nihas101.phash.debug
  (:require
   [de.nihas101.phash.core :as core]
   [de.nihas101.phash.utils :as u]
   [de.nihas101.phash.a-hash :as ah]
   [de.nihas101.phash.d-hash :as dh]
   [de.nihas101.phash.p-hash :as ph]
   [clojure.string :as s]
   [seesaw.core :as sc]
   [mikera.image.core :as im])
  (:import
   [java.awt.image AffineTransformOp]
   [java.awt.geom AffineTransform]))

(defonce ^:private panels
  (atom {:a nil
         :b nil}))

(defonce max-image-size 200)

(defn- scale-image-to-size
  "Scales the pixels an image to the appropriate size for the debug-gui."
  [image]
  (let [largest-side (max (im/width image) (im/height image))
        factor (/ max-image-size largest-side)
        scaled-image (im/new-image (* factor (im/width image)) (* factor (im/height image)))
        scale-instance (AffineTransform/getScaleInstance factor factor)
        ;; Use nearest neighbour so as to not introduce new colors back into the debug images
        scaler (AffineTransformOp. scale-instance AffineTransformOp/TYPE_NEAREST_NEIGHBOR)]
    (.filter ^AffineTransformOp scaler
             ^java.awt.image.BufferedImage image
             ^java.awt.image.BufferedImage scaled-image)))

(defn- add-node-to-display!
  "Adds node `node` to panel with panel-ID `panel-id`."
  [panel-id node]
  (sc/invoke-later
   (swap! panels (fn [panels]
                   (update panels panel-id
                           (fn [panel]
                             (sc/config! panel :items
                                         (concat (sc/config panel :items)
                                                 [node]))))))))

(defn add-image-to-display!
  "Adds image `image` to panel with panel-ID `panel-id`."
  [id image]
  (let [icon (sc/label :icon
                       (scale-image-to-size image))]
    (add-node-to-display! id icon)))

(def add-text-to-display! add-node-to-display!)

(defn gui!
  "Sets up and displays a GUI for debug purposes."
  []
  (sc/native!)
  (let [a (sc/horizontal-panel)
        b (sc/horizontal-panel)]
    (reset! panels {:a a :b b})
    (sc/invoke-later
     (-> (sc/frame :title "Debug - pHash"
                   :content (sc/vertical-panel :items [a b])
                   :on-close :exit)
         sc/pack!
         sc/show!))))

(defn perceptual-hash
  [{:keys [width height] :as hash-fn} image debug-fn reducer init]
  (let [resized-image (u/resize-image image width height)
        gray (u/grayscale resized-image)]
    (debug-fn image)
    (debug-fn resized-image)
    (debug-fn gray)
    (u/image->hash hash-fn gray reducer init)))

(defn debug-a [^String a ^String b]
  (gui!)
  (let [image-a (u/load-image a)
        image-b (u/load-image b)
        debug-a (partial add-image-to-display! :a)
        debug-b (partial add-image-to-display! :b)
        im-dist (core/image-distance (ah/a-hash) image-a image-b)
        a-hash-bits (ah/a-hash)]
    (add-text-to-display! :a (str " Hash:" (s/join (perceptual-hash a-hash-bits image-a debug-a conj [])) " "))
    (add-text-to-display! :b (str " Hash:" (s/join (perceptual-hash a-hash-bits image-b debug-b  conj [])) " "))
    (add-text-to-display! :a (str " Distance:" im-dist))
    (add-text-to-display! :b (str " Distance:" im-dist))))

(defn debug-d [^String a ^String b]
  (gui!)
  (let [image-a (u/load-image a)
        image-b (u/load-image b)
        debug-a (partial add-image-to-display! :a)
        debug-b (partial add-image-to-display! :b)
        im-dist (core/image-distance (dh/d-hash) image-a image-b)
        d-hash-bits (dh/d-hash)]
    (add-text-to-display! :a (str " Hash:" (s/join (perceptual-hash d-hash-bits image-a debug-a  conj [])) " "))
    (add-text-to-display! :b (str " Hash:" (s/join (perceptual-hash d-hash-bits image-b debug-b  conj [])) " "))
    (add-text-to-display! :a (str " Distance:" im-dist))
    (add-text-to-display! :b (str " Distance:" im-dist))))

(defn debug-p [^String a ^String b]
  (gui!)
  (let [image-a (u/load-image a)
        image-b (u/load-image b)
        debug-a (partial add-image-to-display! :a)
        debug-b (partial add-image-to-display! :b)
        im-dist (core/image-distance (ph/p-hash) image-a image-b)
        p-hash-bits (ph/p-hash)]
    (add-text-to-display! :a (str " Hash:" (s/join (perceptual-hash p-hash-bits image-a debug-a conj [])) " "))
    (add-text-to-display! :b (str " Hash:" (s/join (perceptual-hash p-hash-bits image-b debug-b conj [])) " "))
    (add-text-to-display! :a (str " Distance:" im-dist))
    (add-text-to-display! :b (str " Distance:" im-dist))))

(comment
  (debug-a "test/de/nihas101/phash/test_images/compr/architecture_2.jpg"
           "test/de/nihas101/phash/test_images/compr/architecture_2.jpg")

  (debug-d "test/de/nihas101/phash/test_images/compr/architecture_2.jpg"
           "test/de/nihas101/phash/test_images/compr/architecture_2.jpg")

  (debug-p "test/de/nihas101/phash/test_images/compr/architecture_2.jpg"
           "test/de/nihas101/phash/test_images/compr/architecture_2.jpg"))