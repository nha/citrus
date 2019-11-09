(ns citrus.resolver
  (:require [manifold.deferred :as d]))

(deftype Resolver [state resolvers path reducer]

  clojure.lang.IDeref
  (deref [_]
    (let [[key & path] path]
      ;;
      ;; async by default - rum/reactive will deref again
      ;; resolver is a function now instead of a map
      ;;
      (deref ;; TODO see if we can teach citrus/rum manifolds
        (d/chain (resolvers key)
                 (fn [data]
                   (when-not data
                     ;; make this an error?
                     (println "resolver returned nil for " key))
                   (when state
                     (swap! state assoc key data))
                   data)
                 (fn [data]
                   (if reducer
                     (reducer (get-in data path))
                     (get-in data path)))))))

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

(defn make-resolver [state resolvers path reducer]
  (Resolver. state resolvers path reducer))
