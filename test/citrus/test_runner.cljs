(ns citrus.test-runner
  (:require-macros [doo.runner :refer [doo-tests]])
  (:require [citrus.core-test]))

(doo-tests 'citrus.core-test)
