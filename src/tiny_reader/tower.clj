(ns tiny-reader.tower
  (:import [java.net URL]))

(def site-tower
  (atom {:3am {:title-selector [:ul :h3 :a]
               :type :lit
               :url (URL. "http://www.3ammagazine.com/3am/")
               :protocol :http
               :p-selector [:div.entry :p]}
         :paris-review {:title-selector [:main :ul :li :article :h1 :a]
                        :type :lit
                        :url (URL. "https://www.theparisreview.org/blog/")
                        :protocol :https
                        :p-selector [:main :p]}
         :nyrb {:title-selector [:article :h2 :a]
                :type :lit
                :url (URL. "https://www.nybooks.com/daily/")
                :protocol :https
                :p-selector [:article :section :p]}
         :hacker-news {:title-selector [:td.title :a]
                       :type :news
                       :url (URL. "https://news.ycombinator.com/")
                       :protocol :http}
         :lobsters {:title-selector [:body :div :div :ol.stories :li.story :div.story_liner :div.details :span.link :a]
                    :type :news
                    :url (URL. "https://lobste.rs/")
                    :protocol :https}
         :cbc {:title-selector [:ul.moreheadlines-list :li :a]
               :type :news
               :url (URL. "http://www.cbc.ca/world/")
               :protocol :http}
         :r-nba {:title-selector [:p.title :a]
                 :type :ball
                 :url (URL. "https://www.reddit.com/r/nba/")
                 :protocol :https}
         :the-ringer {:title-selector [:a]
                      :type :ball
                      :url (URL. "https://theringer.com/nba/home")
                      :protocol :https}}))
