(ns citrus.resolver
  (:require [manifold.deferred :as d]))

(deftype Resolver [state resolver path reducer]

  clojure.lang.IDeref
  (deref [_]
    (let [[key & path] path]
      ;;
      ;; async by default - rum/reactive will deref again
      ;; resolver is a function now instead of a map
      ;;
      (if-let [resolver-fn (resolver key)]
        (d/chain (resolver-fn)
                 (fn [data]
                   (when-not data (println "resolver returned nil for " key))
                   (when state
                     (swap! state assoc key data))
                   data)
                 (fn [data]
                   (if reducer
                     (reducer (get-in data path))
                     (get-in data path))))
        (d/error-deferred (ex-info "missing resolver-fn for" {:key key})))))

  clojure.lang.IRef
  (setValidator [this vf]
    (throw (UnsupportedOperationException. "citrus.resolver.Resolver/setValidator")))

  (getValidator [this]
    (throw (UnsupportedOperationException. "citrus.resolver.Resolver/getValidator")))

  (getWatches [this]
    (throw (UnsupportedOperationException. "citrus.resolver.Resolver/getWatches")))

  (addWatch [this key callback]
    this)

  (removeWatch [this key]
    this))

(defn make-resolver [state resolver path reducer]
  (Resolver. state resolver path reducer))
