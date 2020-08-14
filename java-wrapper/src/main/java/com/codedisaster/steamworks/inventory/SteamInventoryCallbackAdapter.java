package com.codedisaster.steamworks.inventory;

import com.codedisaster.steamworks.SteamCallbackAdapter;
import com.codedisaster.steamworks.SteamID;
import com.codedisaster.steamworks.SteamResult;

@SuppressWarnings("unused")
public class SteamInventoryCallbackAdapter extends SteamCallbackAdapter<SteamInventoryCallback> {
    SteamInventoryCallbackAdapter(final SteamInventoryCallback callback) {
        super(callback);
    }

    void onSteamInventoryResultReady(final int handle, final int result) {
        callback.onSteamInventoryResultReady(new SteamInventoryHandle(handle), SteamResult.byValue(result));
    }

    void onSteamInventoryFullUpdate(final int handle) {
        callback.onSteamInventoryFullUpdate(new SteamInventoryHandle(handle));
    }

    void onSteamInventoryDefinitionUpdate() {
        callback.onSteamInventoryDefinitionUpdate();
    }

    void onSteamInventoryEligiblePromoItemDefIDs(final int result, final long steamID, final int eligiblePromoItemDefs, final boolean cachedData) {
        callback.onSteamInventoryEligiblePromoItemDefIDs(SteamResult.byValue(result), SteamID.createFromNativeHandle(steamID), eligiblePromoItemDefs, cachedData);
    }

    void onSteamInventoryStartPurchaseResult(final int result, final long orderID, final long transactionID) {
        callback.onSteamInventoryStartPurchaseResult(SteamResult.byValue(result), orderID, transactionID);
    }

    void onSteamInventoryRequestPricesResult(final int result, final String currency) {
        callback.onSteamInventoryRequestPricesResult(SteamResult.byValue(result), currency);
    }
}
