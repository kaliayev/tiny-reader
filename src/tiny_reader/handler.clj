(ns tiny-reader.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [tiny-reader.caching :as $]
            [tiny-reader.tower :as tw]
            [cheshire.core :as json]
            [tiny-reader.read :as read]))

(def cache-refresh-minutes 60)

(future
  (loop []
    (println "I'm updating the cache")
    ($/refresh-headline-cache)
    (Thread/sleep (* cache-refresh-minutes 60 1000))
    (recur)))

(defn get-headlines
  [source]
  (get-in @tw/site-tower [source :headlines]))

(defn handle-get
  [source num]
  (let [source (keyword source)
        num (when num )])
  (cond
    (and source num) (let [num (dec (Integer/parseInt num))]
                       (do (println "Sending Article: " source " and " num)
                           (read/article
                            (:url (nth (get-headlines source) num))
                            source)))
    source (do (println "Sending Headlines: " (get-headlines source))
               (json/encode {source (get-headlines source)}))
    :else (route/not-found "Provide source, or source and article number to read")))

(defn handle-get-sources
  [type]
  (let [sources (if type
                  ($/get-sources-by-type type)
                  ($/get-all-sources))]
    (do (println "Fetching Available Sources of Type " type ": " sources)
        (json/encode {:available-sources sources}))))

(defroutes app-routes
  (GET "/read" {{:strs [source num]} :query-params}
       (handle-get (keyword source) num))
  (GET "/sources" {{:strs [type]} :query-params}
       (handle-get-sources (keyword type)))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
