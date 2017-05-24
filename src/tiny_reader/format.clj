(ns tiny-reader.format
  (:require [clojure.string :as st]
            [io.aviso.ansi :as colour]))

(def line-breaks #{\- \space})

(defn trunc
  [s n]
  (subs s 0 (min (count s) n)))

(defn adj-break? [s i]
  (if (or (contains? line-breaks (nth s i))
          (contains? line-breaks (nth s (dec i))))
    true
    false))

(defn line-breaker
  [s]
  (if (< (count s) 95)
    s
    (str (trunc s 94) (if (adj-break? s 94) "\n" "-\n") (line-breaker (subs s 94)))))

(defn str-with-p
  [strs]
  (->> strs
       #_(map line-breaker)
       (interpose "\n\n")
       (apply str)))
