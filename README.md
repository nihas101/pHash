# pHash [![Clojure CI](https://github.com/nihas101/pHash/actions/workflows/clojure.yml/badge.svg)](https://github.com/nihas101/pHash/actions/workflows/clojure.yml)

A Clojure library for computing perceptual hashes. Currently aHash, dHash and pHash are supported.

# Requirements
Java 1.8 is required to run this program. Leiningen is required for building the library.

## Usage

1. Deploy the library to your local repository (`~/.m2`) via `lein install -h`
2. Include `[de.nihas101/phash "0.9.0"]` in the dependencies of your `project.clj`
3. Require the necessary namespaces:
```
(ns example
  (:require
   [de.nihas101.phash.core :as core]
   [de.nihas101.phash.a-hash :as ah]    ; If you plan to use aHash
   [de.nihas101.phash.d-hash :as dh]    ; If you plan to use dHash
   [de.nihas101.phash.p-hash :as ph]))  ; If you plan to use pHash
```
4. Use the desired functionality:
```
;; Calculate the perceptual hash of image img using aHash
(core/perceptual-hash (ah/a-hash) img)

;; Calculate the hamming distance of the perceptual hash (computed using dHash) between images img-a and img-b
(core/image-distance (dh/d-hash) img-a img-b)

;; Returns true if img-a's and img-b's perceptual hashes (computed using pHash) differ in less than 5 bits
(core/eq-images? (ph/p-hash) img-a img-b 5)
```
Images can be loaded from a file-system with libraries such as [imagez](https://github.com/mikera/imagez).

## License

Copyright © 2021 nihas101

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
