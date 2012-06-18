(ns 'mixi.io)

(defn- ^String encoding [options]
  (or (:encoding options) "UTF-8"))

(defn slurp
  "Reads the file named by f and returns it.
  this manages resources better than clojure.core/slurp."
  [f & options]
  (let [sb (StringBuilder.)]
    (with-open [fis (java.io.FileInputStream. f)
                isr (apply java.io.InputStreamReader. fis (encoding options))
                    ]
      (loop [c (.read isr)]
        (if (neg? c)
          (str sb)
          (do
            (.append sb (char c))
            (recur (.read isr))))))))

(defn spit
  "Opposite of slurp. Opens f with writer, writes content, then
  closes f. this manages resources better than clojure.core/spit."
  [f content & options]
  (with-open [fos (java.io.FileOutputStream. f)
              osw (apply java.io.OutputStreamWriter. fos (encoding options))
              ]
    (.write osw (str content))))
