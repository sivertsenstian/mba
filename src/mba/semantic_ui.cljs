(ns mba.semantic-ui)

;; (ns mba.semantic-ui
;;   (:require cljsjs.semantic-ui-react
;;             goog.object
;;             [reagent.core :as c]))

;; ;; https://github.com/cljsjs/packages/tree/master/semantic-ui-react
;; (def semantic-ui js/semanticUIReact)
;; (defn component
;;   "Get a component from sematic-ui-react:
;;     (component \"Button\")
;;     (component \"Menu\" \"Item\")"
;;   [k & ks]
;;   (if (seq ks)
;;     (apply goog.object/getValueByKeys semantic-ui k ks)
;;     (goog.object/get semantic-ui k)))

;; (def container      (component "Container"))
;; (def dimmer         (component "Dimmer"))
;; (def loader         (component "Loader"))
;; (def message        (component "Message"))
;; (def message-header (component "Message" "Header"))
;; (def segment        (component "Segment"))
