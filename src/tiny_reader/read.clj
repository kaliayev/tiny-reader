(ns tiny-reader.read
  (:require [tiny-reader.format :as f]
            [tiny-reader.tower :as tw]
            [tiny-reader.utils :as ut]
            [net.cgrand.enlive-html :as html])
  (:import [java.net URL]))

(defn get-article-parags
  [url-str source]
  (let [url (URL. url-str)
        p-selector (get-in @tw/site-tower [source :p-selector])]
    (-> url
        ut/https-resource
        (html/select p-selector))))

(defn article
  [url-str source]
  (->> (get-article-parags url-str source)
       (map html/text)
       (map #(str "    " %))
       (f/str-with-p)))
