(ns tiny-reader.headlines
  (:require [clojure.string :as st]
            [net.cgrand.enlive-html :as html]
            [tiny-reader.utils :as ut]
            [tiny-reader.tower :as tw])
  (:import [java.net URL]))

;;;;;;;;;;
;;
;; Helper Functions
;;
;;;;;;;;;;

(defn cbc-filter [m]
  (filter #(not (re-find #"commentwrapper" (:url %))) m))

(defn ringer-filter [v]
  (let [fakes #{"Jordan Ritter Conn" "Sign in / Sign up" "The Ringer NBA Show" "About The Ringer"}
        token-count (fn [s] (count (st/split s #" ")))]
    (reduce (fn [acc m] (if (or (contains? fakes (:title m))
                                (<= (token-count (:title m)) 2))
                          acc
                          (conj acc m)))
            [] v)))


;;;;;;;;;;
;;
;; Headline URL Map parses the DOM objects and formats a headline for given source-type.
;;
;;;;;;;;;;

(defmulti headline-url-map
  (fn [html type] type))

(defmethod headline-url-map :hacker-news
  [html type]
  (reduce (fn [coll x]
            (let [k (first (:content x))]
              (cond
                (and (string? k) (not= "More" k)) (conj coll {:title k :url (get-in x [:attrs :href])})
                :else coll)))
          [] html))

(defmethod headline-url-map :default
  [html type]
  (reduce (fn [coll x]
            (let [title (apply str (map html/text (:content x)))
                  url-sel (if (= type :r-nba) [:attrs :data-href-url] [:attrs :href])
                  href (get-in x url-sel)
                  url (cond (= type :cbc) (str "http://www.cbc.ca" href)
                            (and (= type :r-nba) (not= (first href) \h) (not= href nil) ) (str "https://www.reddit.com" href)
                            :else href)]
              (if (and url (not= "" title)) (conj coll {:title title :url url}) coll)))
          [] html))

;;;;;;;;;;
;;
;; Headline Grabber grabs the headlines for a sources based on the headline-url-map multi
;;   for a source-type.
;;
;;;;;;;;;;

(defmulti headline-grabber
  "grabs headlines to a given page passed as a key:
   Something like :3am or :larb or :nyrb"
  (fn [x] (get-in @tw/site-tower [x :protocol])))

(defmethod headline-grabber :http
  [site]
  (let [url (get-in @tw/site-tower [site :url])
        selector (get-in @tw/site-tower [site :title-selector])
        headline-urls (-> url
                          html/html-resource
                          (html/select selector)
                          (headline-url-map site))]
    (cond (= site :cbc) (cbc-filter headline-urls)
          :else headline-urls)))

(defmethod headline-grabber :https
  [site]
  (let [url (get-in @tw/site-tower [site :url])
        selector (get-in @tw/site-tower [site :title-selector])
        headline-urls (-> url
                          ut/https-resource
                          (html/select selector)
                          (headline-url-map site))]
    (cond (= site :the-ringer) (ringer-filter headline-urls)
          :else headline-urls)))

(defmethod headline-grabber :default
  [site]
  "Oops, I couldn't find that source")
