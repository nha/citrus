(defproject nha/citrus "3.2.3-1"
  :description "State management library for Rum"
  :url "https://github.com/nha/citrus"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.1" :scope "provided"]
                 [org.clojure/clojurescript "1.10.520" :scope "provided"]
                 [manifold "0.1.8"]
                 [rum "0.11.4"]]

  :plugins [[lein-cljsbuild "1.1.7" :exclusions [[org.clojure/clojure]]]
            [lein-figwheel "0.5.19" :exclusions [org.clojure/clojure]]
            [lein-doo "0.1.11"]]

  :profiles {:dev {:dependencies [[cider/piggieback "0.4.2"]
                                  [nrepl "0.6.0"]
                                  [binaryage/devtools "0.9.10"]
                                  [figwheel-sidecar "0.5.19"]
                                  [expound "0.7.1"]]
                   :repl-options {:nrepl-middleware [cider.piggieback/wrap-cljs-repl]}
                   :source-paths ["src" "dev"]}}

  :aliases {"cljs-test" ["do"
                         ["clean"]
                         ["doo" "chrome" "test"]]}

  :cljsbuild {:builds
              [{:id           "dev"
                :source-paths ["src" "example"]
                :figwheel     true
                :compiler     {:main           counter.core
                               :asset-path     "js/compiled/out"
                               :output-to      "resources/public/js/compiled/main.js"
                               :output-dir     "resources/public/js/compiled/out"
                               :compiler-stats true
                               :parallel-build true}}

               {:id           "min"
                :source-paths ["src" "example"]
                :compiler     {:main            counter.core
                               :output-to       "resources/public/js/compiled-min/main.js"
                               :output-dir      "resources/public/js/compiled-min/out"
                               :optimizations   :advanced
                               :closure-defines {"goog.DEBUG" false}
                               :static-fns      true
                               :elide-asserts   true
                               :output-wrapper  true
                               :compiler-stats  true
                               :parallel-build  true}}
               {:id           "test"
                :source-paths ["src" "test"]
                :compiler     {:output-to     "target/test.js"
                               :main          citrus.test-runner
                               :optimizations :none}}]}

  :doo {:paths {:karma "./node_modules/karma/bin/karma"}})
