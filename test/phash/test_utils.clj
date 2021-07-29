(ns phash.test-utils
  (:require
   [phash.utils :as u]))

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