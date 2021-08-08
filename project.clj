(defproject phash "0.10.0"
  :description "A library of perceptual hash algorithms (aHash, dHash, pHash)."
  :url "https://github.com/nihas101/pHash"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [net.mikera/imagez "0.12.0"]
                 [org.clojure/test.check "1.1.0"]]
  :repl-options {:init-ns phash.core})
