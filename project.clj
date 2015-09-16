(defproject pinger "1.0.0"
  :description "Get a weekly email that reminds you to reconnect with people"
  :url "http://github.com/elobato/pinger"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [com.draines/postal "1.11.4"]]
  :main ^:skip-aot pinger.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
