(ns phash.test-utils
  (:require
   [mikera.image.filters :as filt]
   [mikera.image.core :as im]
   [phash.utils :as u]
   [clojure.test.check.generators :as gen]))

; TODO: Remove after done
(set! *warn-on-reflection* true)
(set! *unchecked-math* true)

(defonce blur-filter (filt/blur))
(defonce noise-filter (filt/noise))

(defn- blank-image-gen [min-size max-size]
  (gen/fmap (fn [[w h]] (u/new-image w h))
            (gen/tuple (gen/choose min-size max-size)
                       (gen/choose min-size max-size))))

(defn- color+image-gen [min-size max-size]
  (gen/bind (blank-image-gen min-size max-size)
            (fn [blank-image]
              (gen/tuple (gen/return blank-image)
                         (gen/vector (gen/choose 1 Long/MAX_VALUE)
                                     (count (u/get-pixels blank-image)))))))

; TODO: Create a gen that changes an image slightly (some pixels)

(defn image-gen [min-size max-size]
  (gen/fmap (fn [[image colors]]
              (let [pixels ^ints (u/get-pixels image)]
                (doseq [[idx color] (mapv vector (range) colors)]
                  (aset pixels ^long idx ^long color))
                (im/set-pixels image pixels))
              image)
            (color+image-gen min-size max-size)))

(defonce ^:private compr-path-prefix "test/phash/test_images/compr/")
(defonce ^:private blur-path-prefix "test/phash/test_images/blur/")
(defonce ^:private misc-path-prefix "test/phash/test_images/misc/")
(defonce ^:private rotd-path-prefix "test/phash/test_images/rotd/")

(defonce ^:private file-names ["architecture_2"
                               "architecture1"
                               "bamarket115"
                               "butterflywallpaper"
                               "Chang_PermanentMidnightintheChairofJasperJohns_large"
                               "damien_hirst_does_fashion_week"
                               "damien_hirst_virgin_mother"
                               "damien_hirst"
                               "damienhirst"
                               "dhirst_a3b9ddea"
                               "diamondskull"
                               "doodle"
                               "england"
                               "englandpath"
                               "Hhirst_BGE"
                               "jasper_johns"
                               "johns_portrait_380x311"
                               "latrobe"
                               "Scotland_castle_wedding"
                               "targetjasperjohns"
                               "Tower-Bridge-at-night--London--England_web"
                               "uk-golf-scotland"
                               "wallacestevens"])

(defonce compr (mapv #(u/load-image (str compr-path-prefix % ".jpg")) file-names))
(defonce blur (mapv #(u/load-image (str blur-path-prefix % ".bmp")) file-names))
(defonce misc (mapv #(u/load-image (str misc-path-prefix % ".bmp")) file-names))
(defonce rotd (mapv #(u/load-image (str rotd-path-prefix % ".bmp")) file-names))

(defonce test-image (first compr))

(defonce tiny-test-image (u/resize test-image 8 8))