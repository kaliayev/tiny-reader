(ns tiny-reader.utils
  (:require [net.cgrand.enlive-html :as html]))

(defn https-resource
  [url]
  (with-open [inputstream (-> url
                              .openConnection
                              (doto (.setRequestProperty "User-Agent"
                                                         "Mozilla/5.0 ..."))
                              .getContent)]
    (html/html-resource inputstream)))

(defn eager-vec
  [coll]
  (into [] coll))
