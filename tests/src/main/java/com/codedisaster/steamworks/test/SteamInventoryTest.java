package com.codedisaster.steamworks.test;

import com.codedisaster.steamworks.*;
import com.codedisaster.steamworks.friends.SteamFriends;
import com.codedisaster.steamworks.friends.SteamFriendsCallback;
import com.codedisaster.steamworks.inventory.*;
import com.codedisaster.steamworks.user.SteamUser;
import com.codedisaster.steamworks.user.SteamUserCallback;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SteamInventoryTest extends SteamTestApp {
    private SteamUser user;
    private SteamFriends friends;
    private SteamInventory inventory;

    private final SteamUserCallback userCallback = new SteamUserCallback() {
        @Override
        public void onAuthSessionTicket(final SteamAuthTicket authTicket, final SteamResult result) {

        }

        @Override
        public void onValidateAuthTicket(final SteamID steamID, final SteamAuth.AuthSessionResponse authSessionResponse, final SteamID ownerSteamID) {

        }

        @Override
        public void onMicroTxnAuthorization(final int appID, final long orderID, final boolean authorized) {

        }

        @Override
        public void onEncryptedAppTicket(final SteamResult result) {

        }
    };

    private final SteamFriendsCallback friendsCallback = new SteamFriendsCallback() {
        @Override
        public void onSetPersonaNameResponse(final boolean success, final boolean localSuccess, final SteamResult result) {

        }

        @Override
        public void onPersonaStateChange(final SteamID steamID, final SteamFriends.PersonaChange change) {

        }

        @Override
        public void onGameOverlayActivated(final boolean active) {

        }

        @Override
        public void onGameLobbyJoinRequested(final SteamID steamIDLobby, final SteamID steamIDFriend) {

        }

        @Override
        public void onAvatarImageLoaded(final SteamID steamID, final int image, final int width, final int height) {

        }

        @Override
        public void onFriendRichPresenceUpdate(final SteamID steamIDFriend, final int appID) {

        }

        @Override
        public void onGameRichPresenceJoinRequested(final SteamID steamIDFriend, final String connect) {

        }

        @Override
        public void onGameServerChangeRequested(final String server, final String password) {

        }
    };

    private final SteamInventoryCallback inventoryCallback = new SteamInventoryCallback() {
        @Override
        public void onSteamInventoryResultReady(final SteamInventoryHandle inventoryHandle, final SteamResult result) {
            System.out.println("Inventory Result ready: " + result + ", " + SteamNativeIntHandle
                    .getNativeHandle(inventoryHandle));
            System.out.println(inventory.getResultStatus(inventoryHandle) + ": result of getResultStatus");
        }

        @Override
        public void onSteamInventoryFullUpdate(final SteamInventoryHandle inventoryHandle) {
            System.out.println("Inventory full update");
            final List<SteamInventory.SteamItemDetails> itemDetails = new ArrayList<>();
            System.out.println(inventory.getResultStatus(inventoryHandle) + ": result of getResultStatus");
            System.out.println(inventory.getResultItemsLength(inventoryHandle) + ": result of getResultItemsLength");
            System.out.println(inventory.getResultItems(inventoryHandle, itemDetails) + ": result of getResultItems, Details: " + itemDetails.get(0));
            System.out.println(inventory.getResultItemPropertyKeys(inventoryHandle, 0) + ": result of getResultItemPropertyKeys for itemIndex 0");
            final List<String> properties = new ArrayList<>(
                    Arrays.asList(inventory.getResultItemPropertyKeys(inventoryHandle, 0).split(",")));
            final List<String> values = new ArrayList<>();
            System.out.println(inventory.getResultItemProperty(inventoryHandle, 0, properties.get(0), values) + ": result of getResultItemProperty for " + properties.get(0) + ": " + values.get(0));
            final Optional<SteamInventory.SteamItemDetails> propertyTestItem = itemDetails.stream().filter(itemDetail -> itemDetail.getItemDefinition() == 1300).findFirst();
            if(propertyTestItem.isPresent()) {
                values.clear();
                System.out.println(inventory.getResultItemProperty(inventoryHandle, itemDetails.indexOf(propertyTestItem.get()), "dynamic_props", values) + ": result of getResultItemProperty for dynamic_props: " + values.get(0));
            }
            System.out.println(inventory.getResultTimestamp(inventoryHandle) + ": result of getResultTimestamp");
            System.out.println(inventory.checkResultSteamID(inventoryHandle, user.getSteamID()) + ": result of checkResultSteamID");
            final int friendsSize = friends.getFriendCount(SteamFriends.FriendFlags.Immediate);
            System.out.println(inventory.checkResultSteamID(inventoryHandle, friends.getFriendByIndex(friendsSize-1, SteamFriends.FriendFlags.Immediate)) + ": result of checkResultSteamID");
            final int bufferSize = inventory.getSizeNeededForResultSerialization(inventoryHandle);
            System.out.println(bufferSize + ": result of getSizeNeededForResultSerialization");
            final ByteBuffer serializedResult = ByteBuffer.allocateDirect(bufferSize);
            final List<SteamInventoryHandle> inventories = new ArrayList<>();
            try {
                System.out.println(inventory.serializeResult(inventoryHandle, serializedResult) + ": result of serializeResult, serializedHandle: " + serializedResult + ", Handle: " + inventoryHandle);
                System.out.println(inventory.deserializeResult(inventories, serializedResult) + ": result of deserializeResult, deserializedHandle: " + inventories.get(0));
            } catch(final SteamException e) {
                e.printStackTrace();
            }
            inventory.destroyResult(inventories.get(0));
            inventories.clear();
        }

        @Override
        public void onSteamInventoryDefinitionUpdate() {
            System.out.println("Inventory definition update");
            final List<Integer> itemDefs = new ArrayList<>();
            System.out.println(inventory.getItemDefinitionIDs(itemDefs) + ": result of getItemDefinitionIDs, itemDefs " + itemDefs);
        }

        @Override
        public void onSteamInventoryEligiblePromoItemDefIDs(final SteamResult result, final SteamID steamID,
                                                            final int eligiblePromoItemDefs, final boolean cachedData) {
            System.out.println(result + " Inventory Eligible Promo Items for user: " + steamID.getAccountID() + ", Count: " + eligiblePromoItemDefs + ", cached: " + cachedData);
            final List<Integer> eligiblePromoItemDefIDs = new ArrayList<>();
            System.out.println(inventory.getEligiblePromoItemDefinitionIDs(user.getSteamID(), eligiblePromoItemDefIDs, eligiblePromoItemDefs) + ": result of getEligiblePromoItemDefinitionIDs, itemIds: " + eligiblePromoItemDefs);
        }

        @Override
        public void onSteamInventoryStartPurchaseResult(final SteamResult result, final long orderID, final long transactionID) {
            System.out.println(result + " Inventory Start Purchase, OrderID: " + orderID + ", transactionID: " + transactionID);
        }

        @Override
        public void onSteamInventoryRequestPricesResult(final SteamResult result, final String currency) {
            System.out.println(result + " Inventory Request Prices: " + currency);
        }
    };

    @Override
    protected void registerInterfaces() throws SteamException {
        System.out.println("Register user ...");
        user = new SteamUser(userCallback);

        System.out.println("Register Friends ...");
        friends = new SteamFriends(friendsCallback);

        System.out.println("Register Inventory ...");
        inventory = new SteamInventory(inventoryCallback);
    }

    @Override
    protected void unregisterInterfaces() throws SteamException {
        user.dispose();
        friends.dispose();
        inventory.dispose();
    }

    @Override
    protected void processUpdate() throws SteamException {

    }

    @Override
    protected void processInput(final String input) throws SteamException {
        if (input.startsWith("inventory getAllItems")) {
            final List<SteamInventoryHandle> inventories = new ArrayList<>();
            System.out.println(inventory.getAllItems(inventories) + ": result of getAllItems, Handle: " + SteamNativeIntHandle.getNativeHandle(inventories.get(0)));
        } else if (input.startsWith("inventory loadItemDefinitions")) {
            System.out.println(inventory.loadItemDefinitions() + ": result of loadItemDefinitions");
        } else if (input.startsWith("inventory requestEligiblePromoItemDefinitionsIDs")) {
            System.out.println(inventory.requestEligiblePromoItemDefinitionsIDs(user.getSteamID()).isValid() + ": result of requestEligiblePromoItemDefinitionsIDs");
        } else if (input.startsWith("inventory addPromoItem ")) {
            final String[] params = input.substring("inventory addPromoItem ".length()).split(" ");
            final List<SteamInventoryHandle> inventories = new ArrayList<>();
            System.out.println(inventory.addPromoItem(inventories, Integer.parseInt(params[0])) + ": result of addPromoItem, Handle: " + inventories.get(0));
        } else if (input.startsWith("inventory addPromoItems ")) {
            final String[] params = input.substring("inventory addPromoItems ".length()).split(" ");
            final int[] ids = Arrays.stream(params).mapToInt(Integer::parseInt).toArray();
            final List<SteamInventoryHandle> inventories = new ArrayList<>();
            System.out.println(inventory.addPromoItems(inventories, ids) + ": result of addPromoItems, Handle: " + inventories.get(0));
        } else if (input.startsWith("inventory generateItems")) {
            final List<SteamInventoryHandle> inventories = new ArrayList<>();
            System.out.println(inventory.generateItems(inventories, new int[]{1000}, new int[]{1}) + ": result of generateItems, Handle: " + inventories.get(0));
            System.out.println(inventory.generateItems(inventories, new int[]{1100}, new int[]{2}) + ": result of generateItems, Handle: " + inventories.get(0));
            System.out.println(inventory.generateItems(inventories, new int[]{1300}, new int[]{1}) + ": result of generateItems, Handle: " + inventories.get(0));
        } else if (input.startsWith("inventory grantPromoItems")) {
            final List<SteamInventoryHandle> inventories = new ArrayList<>();
            System.out.println(inventory.grantPromoItems(inventories) + ": result of grantPromoItems, Handle: " + inventories.get(0));
        } else if (input.startsWith("inventory consumeItem")) {
            final List<SteamInventoryHandle> inventories = new ArrayList<>();
            System.out.println(inventory.consumeItem(inventories, new SteamItemInstanceId(100L), 0) + ": result of consumeItem for ID 100, Handle: " + inventories.get(0));
        } else if (input.startsWith("inventory exchangeItems")) {
            final List<SteamInventoryHandle> inventories = new ArrayList<>();
            final List<SteamInventory.SteamItemDetails> itemDetails = new ArrayList<>();
            System.out.println(inventory.getAllItems(inventories));
            try {
                Thread.sleep(1000);
            } catch(final InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(inventory.getResultItems(inventories.get(0), itemDetails));
            final List<SteamItemInstanceId> itemInstances = new ArrayList<>();
            final List<SteamItemInstanceId> items1 = itemDetails.stream().filter(details -> details.getItemDefinition() == 1000 && details.getQuantity() == 1).map(SteamInventory.SteamItemDetails::getItemId).collect(
                    Collectors.toList());
            final List<SteamItemInstanceId> items2 = itemDetails.stream().filter(details -> details.getItemDefinition() == 1100 && details.getQuantity() == 2).map(SteamInventory.SteamItemDetails::getItemId).collect(Collectors.toList());
            itemInstances.add(items1.get(0));
            itemInstances.add(items2.get(0));
            System.out.println(inventory.exchangeItems(inventories, new int[]{1200}, new int[]{1}, 1,
                                                       itemInstances.toArray(new SteamItemInstanceId[0]), new int[]{1, 2}, itemInstances.size()) + ": result of exchangeItems");
        } else if (input.startsWith("inventory transferItemQuantity stack")) {
            final List<SteamInventoryHandle> inventories = new ArrayList<>();
            final List<SteamInventory.SteamItemDetails> itemDetails = new ArrayList<>();
            System.out.println(inventory.getAllItems(inventories));
            try {
                Thread.sleep(1000);
            } catch(final InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(inventory.getResultItems(inventories.get(0), itemDetails));
            final List<SteamItemInstanceId> items = itemDetails.stream().filter(details -> details.getItemDefinition() == 1100).map(SteamInventory.SteamItemDetails::getItemId).collect(Collectors.toList());
            System.out.println(inventory.transferItemQuantity(inventories, items.get(1), 1, items.get(0)) + ": result of transferItemQuantity");
        } else if (input.startsWith("inventory transferItemQuantity unstack")) {
            final List<SteamInventoryHandle> inventories = new ArrayList<>();
            final List<SteamInventory.SteamItemDetails> itemDetails = new ArrayList<>();
            System.out.println(inventory.getAllItems(inventories));
            try {
                Thread.sleep(1000);
            } catch(final InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(inventory.getResultItems(inventories.get(0), itemDetails));
            final List<SteamItemInstanceId> items = itemDetails.stream().filter(details -> details.getItemDefinition() == 1100).map(SteamInventory.SteamItemDetails::getItemId).collect(Collectors.toList());
            System.out.println(inventory.transferItemQuantity(inventories, items.get(0), 1, SteamItemInstanceId.INVALID) + ": result of transferItemQuantity");
        } else if (input.startsWith("inventory sendItemDropHeartbeat")) {
            inventory.sendItemDropHeartbeat();
        } else if (input.startsWith("inventory triggerItemDrop")) {
            final List<SteamInventoryHandle> inventories = new ArrayList<>();
            System.out.println(inventory.triggerItemDrop(inventories, 100) + ": result of triggerItemDrop, Handle: " + inventories.get(0));
        } else if (input.startsWith("inventory getItemDefinitionProperty")) {
            final List<String> values = new ArrayList<>();
            final List<String> properties = new ArrayList<>(Arrays.asList(inventory.getItemDefinitionPropertyKeys(100).split(",")));
            System.out.println(inventory.getItemDefinitionProperty(100, properties.get(0), values) + ": result of getItemDefinitionProperty, values: " + values);
        } else if (input.startsWith("inventory startPurchase")) {
            System.out.println(inventory.startPurchase(new int[]{1100}, new int[]{5}).isValid() + ": result of startPurchase");
        } else if (input.startsWith("inventory requestPrices")) {
            System.out.println(inventory.requestPrices().isValid() + ": result of requestPrices");
        } else if (input.startsWith("inventory getNumItemsWithPrices")) {
            System.out.println(inventory.getNumItemsWithPrices() + ": result of getNumItemsWithPrices");
        } else if (input.startsWith("inventory getItemsWithPrices")) {
            final int size = inventory.getNumItemsWithPrices();
            final int[] itemDefs = new int[size];
            final long[] itemCurrentPrices = new long[size];
            final long[] itemBasePrices = new long[size];
            Arrays.fill(itemDefs, -1);
            Arrays.fill(itemCurrentPrices, -1);
            Arrays.fill(itemBasePrices, -1);
            System.out.println(inventory.getItemsWithPrices(itemDefs, itemCurrentPrices, itemBasePrices) + ": result of getNumItemsWithPrices");
            System.out.println(Arrays.toString(itemDefs) + ": itemDefs");
            System.out.println(Arrays.toString(itemCurrentPrices) + ": itemCurrentPrices");
            System.out.println(Arrays.toString(itemBasePrices) + ": itemBasePrices");
        } else if (input.startsWith("inventory getItemPrice")) {
            final long[] itemCurrentPrices = new long[1];
            final long[] itemBasePrices = new long[1];
            Arrays.fill(itemCurrentPrices, -1);
            Arrays.fill(itemBasePrices, -1);
            System.out.println(inventory.getItemPrice(1100, itemCurrentPrices, itemBasePrices) + ": result of getItemPrice");
            System.out.println(itemCurrentPrices[0] + ": itemCurrentPrices");
            System.out.println(itemBasePrices[0] + ": itemBasePrices");
        } else if (input.startsWith("inventory UpdateProperties ")) {
            final String[] params = input.substring("inventory UpdateProperties ".length()).split(" ");
            final List<SteamInventoryHandle> inventories = new ArrayList<>();
            final List<SteamInventory.SteamItemDetails> itemDetails = new ArrayList<>();
            System.out.println(inventory.getAllItems(inventories));
            try {
                Thread.sleep(1000);
            } catch(final InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(inventory.getResultItems(inventories.get(0), itemDetails));
            final List<SteamItemInstanceId> items = itemDetails.stream().filter(details -> details.getItemDefinition() == 1300).map(SteamInventory.SteamItemDetails::getItemId).collect(Collectors.toList());
            final SteamInventoryUpdateHandle updateHandle = inventory.startUpdateProperties();
            System.out.println(updateHandle.isValid() + ": result of startUpdateProperties");
            System.out.println(inventory.setProperty(updateHandle, items.get(0), params[0], 10L) + ": result of setProperty for property: " + params[0]);
            System.out.println(inventory.submitUpdateProperties(updateHandle, inventories) + ": result of submitUpdateProperties");
        } else if (input.startsWith("inventory removeProperty ")) {
            final String[] params = input.substring("inventory removeProperty ".length()).split(" ");
            final List<SteamInventoryHandle> inventories = new ArrayList<>();
            final List<SteamInventory.SteamItemDetails> itemDetails = new ArrayList<>();
            System.out.println(inventory.getAllItems(inventories));
            try {
                Thread.sleep(1000);
            } catch(final InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(inventory.getResultItems(inventories.get(0), itemDetails));
            final List<SteamItemInstanceId> items = itemDetails.stream().filter(details -> details.getItemDefinition() == 1300).map(SteamInventory.SteamItemDetails::getItemId).collect(Collectors.toList());
            final SteamInventoryUpdateHandle updateHandle = inventory.startUpdateProperties();
            System.out.println(updateHandle + ": result of startUpdateProperties");
            System.out.println(inventory.removeProperty(updateHandle, items.get(0), params[0]) + ": result of removeProperty for property: " + params[0]);
            System.out.println(inventory.submitUpdateProperties(updateHandle, inventories) + ": result of submitUpdateProperties");
        } else if (input.startsWith("inventory inspectItem ")) {
            final List<SteamInventoryHandle> inventories = new ArrayList<>();
            final String[] params = input.substring("inventory inspectItem ".length()).split(" ");
            System.out.println(inventory.inspectItem(inventories, params[0]));
        } else if (input.startsWith("inventory getItemsByID")) {
            final List<SteamItemInstanceId> itemIds = new ArrayList<>();
            final List<SteamInventoryHandle> inventories = new ArrayList<>();
            itemIds.add(new SteamItemInstanceId(100L));
            System.out.println(inventory.getItemsByID(inventories, itemIds) + ": result of getItemsByID for ID 100");
        }
    }

    public static void main(final String[] arguments) {
        new SteamInventoryTest().clientMain(arguments);
    }
}
