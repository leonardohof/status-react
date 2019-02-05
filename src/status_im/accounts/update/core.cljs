(ns status-im.accounts.update.core
  (:require
   [status-im.contact.device-info :as device-info]
   [status-im.data-store.accounts :as accounts-store]
   [status-im.transport.message.protocol :as protocol]
   [status-im.transport.message.contact :as message.contact]
   [status-im.utils.fx :as fx]))

(fx/defn send-account-update [cofx]
  (let [fcm-token       (get-in cofx [:db :notifications :fcm-token])
        {:keys [name photo-path address]} (get-in cofx [:db :account/account])]
    (protocol/send
     (message.contact/ContactUpdate.
      name
      photo-path
      address
      fcm-token
      (device-info/all cofx))
     nil
     cofx)))

(fx/defn account-update
  "Takes effects (containing :db) + new account fields, adds all effects necessary for account update.
  Optionally, one can specify a success-event to be dispatched after fields are persisted."
  [{:keys [db] :as cofx} new-account-fields {:keys [success-event]}]
  (let [current-account (:account/account db)
        new-account     (merge current-account new-account-fields)
        fx              {:db                 (assoc db :account/account new-account)
                         :data-store/base-tx [{:transaction (accounts-store/save-account-tx new-account)
                                               :success-event success-event}]}
        {:keys [name photo-path address]} new-account]
    (if (or (:name new-account-fields) (:photo-path new-account-fields))
      (fx/merge cofx
                fx
                (send-account-update cofx))
      fx)))

(fx/defn clean-seed-phrase
  "A helper function that removes seed phrase from storage."
  [cofx]
  (account-update cofx
                  {:seed-backed-up? true
                   :mnemonic        nil}
                  {}))

(fx/defn update-sign-in-time
  [{db :db now :now :as cofx}]
  (account-update cofx {:last-sign-in now} {}))

(fx/defn update-settings
  [{{:keys [account/account] :as db} :db :as cofx} settings {:keys [success-event]}]
  (let [new-account (assoc account :settings settings)]
    {:db                 (assoc db :account/account new-account)
     :data-store/base-tx [{:transaction   (accounts-store/save-account-tx new-account)
                           :success-event success-event}]}))
