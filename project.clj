(defproject org.arachne-framework.template/enterprise-spa "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.9.0-alpha14"]

                 [org.arachne-framework/pedestal-assets "0.1.0-master-0006-46abdf0"]
                 [org.arachne-framework/arachne-figwheel "0.1.0-master-0011-735f99c"]

                 [com.datomic/datomic-free "0.9.5554"
                  :exclusions [org.slf4j/slf4j-api
                               org.slf4j/slf4j-nop
                               org.slf4j/slf4j-log4j12
                               org.slf4j/log4j-over-slf4j
                               org.slf4j/jcl-over-slf4j
                               org.slf4j/jul-to-slf4j
                               org.clojure/clojure
                               commons-codec
                               com.google.guava/guava]]

                 [ch.qos.logback/logback-classic "1.1.3"
                  :exclusions [org.slf4j/slf4j-api]]

                 [rum "0.10.8"]]
  :profiles {:dev {:source-paths ["dev"]
                   :dependencies [[com.cemerick/piggieback "0.2.1"
                                   :exclusions [org.clojure/clojurescript]]
                                  [reloaded.repl "0.2.3"
                                   :exclusions [com.stuartsierra/component]]]
                   :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}}}
  :repositories [["arachne-dev"
                  "http://maven.arachne-framework.org/artifactory/arachne-dev"]])
