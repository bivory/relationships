(defproject relationships "0.0.1-SNAPSHOT"
  :description "Model crazy family relationships."
  :dependencies [[org.clojure/clojure "1.4.0"]]
  :profiles {:dev {:dependencies [[midje "1.5.0"]]
                   :plugins [[lein-midje "3.0.1"]
                             [lein-cloverage "1.0.2"]]}})
