(defproject org.arachne-framework.template/enterprise-spa "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.9.0-alpha14"]

                 [org.arachne-framework/pedestal-assets "0.2.0-master-0014-8fd438f"]
                 [org.arachne-framework/arachne-figwheel "0.2.0-master-0025-56f212b"]
                 [org.arachne-framework/arachne-sass "0.2.0-master-0024-51d4bce"]

                 [com.datomic/datomic-free "0.9.5561"
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

                 [rum "0.10.8"]
                 [org.webjars/bootstrap "3.3.7-1"]]
  :source-paths ["src" "config"]
  :profiles {:dev {:source-paths ["dev"]
                   :dependencies [[com.cemerick/piggieback "0.2.1"
                                   :exclusions [org.clojure/clojurescript]]
                                  [com.stuartsierra/component.repl "0.2.0"]
                                  [org.clojure/tools.namespace "0.2.11"]]
                   :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}
                   :main user}}
  :main arachne.run
  :repositories [["arachne-dev"
                  "http://maven.arachne-framework.org/artifactory/arachne-dev"]])
