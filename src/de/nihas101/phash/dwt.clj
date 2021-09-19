(ns de.nihas101.phash.dwt
  "A implementation of the discret wavelet transformation algorithm")

(defn discrete-wavelet-transform
  "Haar wavelet. This function assumes that (count input) = 2^n, n>1.
   
   Based on: https://en.wikipedia.org/wiki/Discrete_wavelet_transform#Code_example
   and https://sundoc.bibliothek.uni-halle.de/diss-online/02/03H033/t4.pdf"
  (^longs [^longs values]
   (discrete-wavelet-transform values values (quot (count values) 2)))
  (^longs [^longs input ^longs output ^long length]
   (if (zero? length)
     output
     (let [pairs (transduce
                  (comp (partition-all 2)
                        (take length))
                  conj [] input)]
       (recur
        ;; new input
        (into (mapv (partial apply +) pairs) (drop length input))
        ;; new output
        (into (mapv (partial apply +) pairs)
              (concat (mapv (partial apply -) pairs)
                      (drop (* length 2) output)))
        (quot length 2))))))

(defn rows->columns ^longs [^longs values ^long row-size]
  (vec (apply mapcat vector (partition-all row-size values))))

(defn discrete-wavelet-transform-2d ^longs [^longs values ^long m ^long n]
  (-> values
      discrete-wavelet-transform
      (rows->columns ,,, m)
      discrete-wavelet-transform
      (rows->columns ,,, n)))