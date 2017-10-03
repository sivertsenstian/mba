(defproject mba "1.0.0"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.946"]
                 [cljsjs/c3 "0.4.14-0"]
                 [reagent "0.7.0"]
                 [re-frame "0.10.1"]
                 [secretary "1.2.3"]
                 [cljs-ajax "0.7.2"]]

  :plugins [[lein-cljsbuild "1.1.7"]
            [lein-sassy "1.0.8"]]

  :min-lein-version "2.5.3"

  :clean-targets ^{:protect false} ["resources/public/Scripts" "target"]

  :figwheel {:css-dirs ["resources/public/Content"]}

  :sass {:src "sass"
         :dst "resources/public/Content/"}

  :profiles
  {:dev
   {:dependencies [[binaryage/devtools "0.9.4"]]
    :plugins      [[lein-figwheel "0.5.13"]]}}

  :cljsbuild
  {:builds
   [{:id           "dev"
     :source-paths ["src"]
     :figwheel     {:on-jsload "mba.core/mount-root"}
     :compiler     {:main                 mba.core
                    :parallel-build       true
                    :output-to            "resources/public/Scripts/mba.js"
                    :output-dir           "resources/public/Scripts/out"
                    :asset-path           "Scripts/out"
                    :source-map-timestamp true
                    :preloads             [devtools.preload]
                    :external-config      {:devtools/config {:features-to-install :all}}}} {:id           "min"
                                                                                            :source-paths ["src"]
                                                                                            :compiler     {:main            mba.core
                                                                                                           :parallel-build  true
                                                                                                           :output-to       "docs/Scripts/mba.min.js"
                                                                                                           :optimizations   :advanced
                                                                                                           :closure-defines {goog.DEBUG false}
                                                                                                           :pretty-print    false}}]})

