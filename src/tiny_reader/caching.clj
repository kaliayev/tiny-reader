(ns tiny-reader.caching
  (:require [tiny-reader.tower :as tw]
            [tiny-reader.utils :as ut]
            [tiny-reader.headlines :as hl]))

(defn source-type
  [source]
  (get-in @tw/site-tower [source :type]))

(defn get-all-sources
  []
  (ut/eager-vec (keys @tw/site-tower)))

(defn get-sources-by-type
  [type]
  (reduce-kv (fn [acc k v]
               (if (= (:type v) type)
                 (conj acc k)
                 acc))
             [] @tw/site-tower))

(defn refresh-headline-cache
  "Populate the sources tower with headlines, then refresh every hour"
  []
  (doseq [source (get-all-sources)]
    (swap! tw/site-tower assoc-in [source :headlines] (hl/headline-grabber source))))
