(ns status-im.ui.screens.stickers.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 :stickers/packs
 (fn [db]
   (:stickers/packs db)))

(re-frame/reg-sub
 :stickers/installed-packs
 (fn [db]
   (:stickers/packs-installed db)))

(re-frame/reg-sub
 :stickers/installed-packs-vals
 :<- [:stickers/installed-packs]
 (fn [packs]
   (vals packs)))

(re-frame/reg-sub
 :stickers/all-packs
 :<- [:stickers/packs]
 :<- [:stickers/installed-packs]
 (fn [[packs installed]]
   (map #(if (get installed (:id %)) (assoc % :installed true) %) (vals packs))))

(re-frame/reg-sub
 :stickers/get-current-pack
 :<- [:get-screen-params]
 :<- [:stickers/all-packs]
 (fn [[{:keys [id]} packs]]
   (first (filter #(= (:id %) id) packs))))

(defn find-pack-id-for-uri [sticker-uri packs]
  (some (fn [{:keys [stickers uri]}]
          (when (some #(= sticker-uri (:uri %)) stickers)
            uri))
        packs))

(re-frame/reg-sub
 :stickers/recent
 :<- [:account/account]
 :<- [:stickers/installed-packs-vals]
 (fn [[{:keys [recent-stickers]} packs]]
   (map (fn [uri] {:uri uri :pack (find-pack-id-for-uri uri packs)}) recent-stickers)))