(defproject phash "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [seesaw/seesaw "1.5.0"]
                 [net.mikera/imagez "0.12.0"]]
  :main phash.core ; TODO: Remove this will be a library => no main!
  :repl-options {:init-ns phash.core})
