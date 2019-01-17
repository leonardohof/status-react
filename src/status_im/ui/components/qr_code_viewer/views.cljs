(ns status-im.ui.components.qr-code-viewer.views
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [status-im.react-native.js-dependencies :as rn-dependencies]
            [status-im.ui.components.button.view :as button]
            [status-im.ui.components.list-selection :as list-selection]
            [status-im.ui.components.qr-code-viewer.styles :as styles]
            [status-im.ui.screens.profile.ttt.views :as ttt]
            [status-im.ui.components.react :as react]
            [status-im.i18n :as i18n]))

(defn qr-code [props]
  (reagent/create-element
   rn-dependencies/qr-code
   (clj->js (merge {:inverted true} props))))

(defn qr-code-viewer [{:keys [style hint-style footer-style footer-button value hint legend]}]
  (if value
    (let [{:keys [width]} @(re-frame/subscribe [:dimensions/window])
          snt-amount @(re-frame/subscribe [:get-in [:my-profile/tribute-to-talk :snt-amount]])]
      [react/view {:style (merge styles/qr-code style)}
       (when snt-amount
         [react/view {:style {:margin-horizontal 16}}
          [ttt/ttt-enabled-note]])
       (when width
         (let [size (int (min width styles/qr-code-max-width))]
           [react/view {:style               (styles/qr-code-container size)
                        :accessibility-label :qr-code-image}
            [qr-code {:value value
                      :size  size}]]))
       [react/text {:style (merge styles/qr-code-hint hint-style)}
        hint]
       [react/view styles/footer
        [react/view styles/wallet-info
         [react/text {:style               (merge styles/hash-value-text footer-style)
                      :accessibility-label :address-text
                      :selectable          true}
          legend]]]
       (when footer-button
         [footer-button value])])
    [react/view [react/text "no value"]]))
