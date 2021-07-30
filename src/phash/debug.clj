(ns phash.debug
  (:require
   [seesaw.core :as sc]
   [mikera.image.core :as im])
  (:import
   [java.awt.image AffineTransformOp]
   [java.awt.geom AffineTransform]))

(defonce ^:private panels
  (atom {:a nil
         :b nil}))

(defonce max-image-size 400)

(defn- scale-image-to-size [image]
  (let [largest-side (max (im/width image) (im/height image))
        factor (/ max-image-size largest-side)
        scaled-image (im/new-image (* factor (im/width image)) (* factor (im/height image)))
        scale-instance (AffineTransform/getScaleInstance factor factor)
        ; Use nearest neighbour so as to not introduce new colors back into the debug images
        scaler (AffineTransformOp. scale-instance AffineTransformOp/TYPE_NEAREST_NEIGHBOR)]
    (.filter ^AffineTransformOp scaler
             ^java.awt.image.BufferedImage image
             ^java.awt.image.BufferedImage scaled-image)))

(defn- add-node-to-display! [id node]
  (sc/invoke-later
   (swap! panels (fn [panels]
                   (update panels id (fn [panel] (sc/config! panel :items (concat (sc/config panel :items) [node]))))))))

(defn add-image-to-display! [id image]
  (let [icon (sc/label :icon
                       (scale-image-to-size image))]
    (add-node-to-display! id icon)))

(def add-text-to-display! add-node-to-display!)

(defn gui! []
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