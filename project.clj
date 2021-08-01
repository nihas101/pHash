(defproject phash "0.9.0"
  :description "A library of perceptual hash algorithms (aHash, dHash, pHash)."
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [seesaw/seesaw "1.5.0"]
                 [net.mikera/imagez "0.12.0"]
                 [org.clojure/test.check "1.1.0"]]
  :repl-options {:init-ns phash.core})
