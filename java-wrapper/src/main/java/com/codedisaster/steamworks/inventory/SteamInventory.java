package com.codedisaster.steamworks.inventory;

import com.codedisaster.steamworks.*;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SteamInventory extends SteamInterface {
    public enum SteamItemFlags {
        NoTrade(1 << 0),
        Removed(1 << 8),
        Consumed(1 << 9);

        private final int bits;

        SteamItemFlags(final int bits) {
            this.bits = bits;
        }

        public static boolean isSet(final SteamInventory.SteamItemFlags value, final int bitMask) {
            return (value.bits & bitMask) == value.bits;
        }
    }

    public static class SteamItemDetails {
        private long itemId;
        private int itemDefinition;
        private short quantity;
        private short flags;

        public SteamItemInstanceId getItemId() {
            return new SteamItemInstanceId(itemId);
        }

        public int getItemDefinition() {
            return itemDefinition;
        }

        public short getQuantity() {
            return quantity;
        }

        public short getFlags() {
            return flags;
        }
    }

    public static class SteamInventoryValue {
        private String value;

        public String getValue() {
            return value;
        }
    }

    public SteamInventory(final SteamInventoryCallback callback) {
        super(SteamAPI.getSteamInventoryPointer(), createCallback(new SteamInventoryCallbackAdapter(callback)));
    }

    public SteamResult getResultStatus(final SteamInventoryHandle inventory) {
        return SteamResult.byValue(getResultStatus(pointer, inventory.handle));
    }

    public int getResultItemsLength(final SteamInventoryHandle inventory) {
        return getResultItemsLength(pointer, inventory.handle);
    }

    public boolean getResultItems(final SteamInventoryHandle inventory, final List<SteamItemDetails> itemDetails) {
        final int itemCount = getResultItemsLength(pointer, inventory.handle);
        if(itemCount > 0) {
            final SteamItemDetails[] steamItemDetailsArray = new SteamItemDetails[itemCount];

            for(int i = 0; i < itemCount; i++) {
                steamItemDetailsArray[i] = new SteamItemDetails();
            }

            final boolean result = getResultItems(pointer, inventory.handle, steamItemDetailsArray);

            if(result) {
                itemDetails.addAll(Arrays.stream(steamItemDetailsArray).collect(Collectors.toList()));
            }

            return result;
        }

        return false;
    }

    public String getResultItemPropertyKeys(final SteamInventoryHandle inventory, final int itemIndex) {
        return getResultItemPropertyKeys(pointer, inventory.handle, itemIndex);
    }

    public boolean getResultItemProperty(final SteamInventoryHandle inventory, final int itemIndex, final String propertyName, final List<String> values) {
        final SteamInventoryValue steamValue = new SteamInventoryValue();

        final boolean result = getResultItemProperty(pointer, inventory.handle, itemIndex, propertyName, steamValue);

        values.add(steamValue.getValue());

        return result;
    }

    public int getResultTimestamp(final SteamInventoryHandle inventory) {
        return getResultTimestamp(pointer, inventory.handle);
    }

    public boolean checkResultSteamID(final SteamInventoryHandle inventory, final SteamID steamIDExpected) {
        return checkResultSteamID(pointer, inventory.handle, steamIDExpected.handle);
    }

    public void destroyResult(final SteamInventoryHandle inventory) {
        destroyResult(pointer, inventory.handle);
    }

    public boolean getAllItems(final List<SteamInventoryHandle> inventories) {
        final int[] tempIntArray = new int[1];

        final boolean result = getAllItems(pointer, tempIntArray);

        if(result) {
            inventories.addAll(SteamInventoryHandle.mapToHandles(tempIntArray));
        }

        return result;
    }

    public boolean getItemsByID(final List<SteamInventoryHandle> inventories, final List<SteamItemInstanceId> instanceIDs) {
        final int[] tempIntArray = new int[1];

        final boolean result = getItemsByID(pointer, tempIntArray, instanceIDs.stream().mapToLong(steamItemInstanceId -> steamItemInstanceId.handle).toArray(), instanceIDs.size());

        if(result) {
            inventories.addAll(SteamInventoryHandle.mapToHandles(tempIntArray));
        }

        return result;
    }

    public int getSizeNeededForResultSerialization(final SteamInventoryHandle inventory) {
        return serializeResultSize(pointer, inventory.handle);
    }

    public boolean serializeResult(final SteamInventoryHandle inventory, final ByteBuffer outBuffer) throws SteamException {
        if (!outBuffer.isDirect()) {
            throw new SteamException("Direct buffer required!");
        }

        return serializeResult(pointer, inventory.handle, outBuffer, outBuffer.position(), outBuffer.remaining());
    }

    // STEAM_BUFFER_COUNT(punOutBufferSize) void *pBuffer
    public boolean deserializeResult(final List<SteamInventoryHandle> inventories, final ByteBuffer buffer) {
        final int[] tempIntArray = new int[1];

        final boolean result = deserializeResult(pointer, tempIntArray, buffer, buffer.position(), buffer.remaining(), false);

        if(result) {
            inventories.addAll(SteamInventoryHandle.mapToHandles(tempIntArray));
        }

        return result;
    }

    // STEAM_ARRAY_COUNT(unArrayLength) int[] pArrayItemDefs, STEAM_ARRAY_COUNT(unArrayLength)
    public boolean generateItems(final List<SteamInventoryHandle> inventories, final int[] arrayItemDefs, final int[] arrayQuantity) {
        if(arrayItemDefs.length != arrayQuantity.length) {
            throw new IllegalArgumentException("The length of arrayItemDefs and arrayQuantity must match!");
        }

        final int[] tempIntArray = new int[1];

        final boolean result = generateItems(pointer, tempIntArray, arrayItemDefs, arrayQuantity, arrayItemDefs.length);

        if(result) {
            inventories.addAll(SteamInventoryHandle.mapToHandles(tempIntArray));
        }

        return result;
    }

    public boolean grantPromoItems(final List<SteamInventoryHandle> inventories) {
        final int[] tempIntArray = new int[1];

        final boolean result = grantPromoItems(pointer, tempIntArray);

        if(result) {
            inventories.addAll(SteamInventoryHandle.mapToHandles(tempIntArray));
        }

        return result;
    }

    public boolean addPromoItem(final List<SteamInventoryHandle> inventories, final int itemDef) {
        final int[] tempIntArray = new int[1];

        final boolean result = addPromoItem(pointer, tempIntArray, itemDef);

        if(result) {
            inventories.addAll(SteamInventoryHandle.mapToHandles(tempIntArray));
        }

        return result;
    }

    // STEAM_ARRAY_COUNT(unArrayLength) int[] pArrayItemDefs
    public boolean addPromoItems(final List<SteamInventoryHandle> inventories, final int[] arrayItemDefs) {
        final int[] tempIntArray = new int[1];

        final boolean result = addPromoItems(pointer, tempIntArray, arrayItemDefs, arrayItemDefs.length);

        if(result) {
            inventories.addAll(SteamInventoryHandle.mapToHandles(tempIntArray));
        }

        return result;
    }

    public boolean consumeItem(final List<SteamInventoryHandle> inventories, final SteamItemInstanceId itemConsume, final int quantity) {
        final int[] tempIntArray = new int[1];

        final boolean result = consumeItem(pointer, tempIntArray, itemConsume.handle, quantity);

        if(result) {
            inventories.addAll(SteamInventoryHandle.mapToHandles(tempIntArray));
        }

        return result;
    }

    // STEAM_ARRAY_COUNT(unArrayGenerateLength) int[] pArrayGenerate, STEAM_ARRAY_COUNT(unArrayGenerateLength) int[] punArrayGenerateQuantity
    // STEAM_ARRAY_COUNT(unArrayDestroyLength) SteamItemInstanceId[] pArrayDestroy, STEAM_ARRAY_COUNT(unArrayDestroyLength) int[] punArrayDestroyQuantity
    public boolean exchangeItems(final List<SteamInventoryHandle> inventories, final int[] arrayGenerate, final int[] arrayGenerateQuantity, final int arrayGenerateLength,
                                 final SteamItemInstanceId[] arrayDestroy, final int[] arrayDestroyQuantity, final int arrayDestroyLength) {
        final int[] tempIntArray = new int[1];

        final boolean result = exchangeItems(pointer, tempIntArray, arrayGenerate, arrayGenerateQuantity, arrayGenerateLength,
                                             Arrays.stream(arrayDestroy).mapToLong(steamItemInstanceId -> steamItemInstanceId.handle).toArray(), arrayDestroyQuantity, arrayDestroyLength);

        if(result) {
            inventories.addAll(SteamInventoryHandle.mapToHandles(tempIntArray));
        }

        return result;
    }

    public boolean transferItemQuantity(final List<SteamInventoryHandle> inventories, final SteamItemInstanceId itemIdSource, final int quantity, final SteamItemInstanceId itemIdDest) {
        final int[] tempIntArray = new int[1];

        final boolean result = transferItemQuantity(pointer, tempIntArray, itemIdSource.handle, quantity, itemIdDest.handle);

        if(result) {
            inventories.addAll(SteamInventoryHandle.mapToHandles(tempIntArray));
        }

        return result;
    }

    @Deprecated
    public void sendItemDropHeartbeat() {
        sendItemDropHeartbeat(pointer);
    }

    public boolean triggerItemDrop(final List<SteamInventoryHandle> inventories, final int dropListDefinition) {
        final int[] tempIntArray = new int[1];

        final boolean result = triggerItemDrop(pointer, tempIntArray, dropListDefinition);

        if(result) {
            inventories.addAll(SteamInventoryHandle.mapToHandles(tempIntArray));
        }

        return result;
    }

    public boolean loadItemDefinitions() {
        return loadItemDefinitions(pointer);
    }

    public boolean getItemDefinitionIDs(final List<Integer> itemDefIDs) {
        final int size = getItemDefinitionIDSize(pointer);
        final int[] tempIntArray = new int[size];

        final boolean result = getItemDefinitionIDs(pointer, tempIntArray, size);

        if(result) {
            itemDefIDs.addAll(Arrays.stream(tempIntArray).boxed().collect(Collectors.toList()));
        }

        return result;
    }

    public String getItemDefinitionPropertyKeys(final int itemDefinition) {
        return getItemDefinitionPropertyKeys(pointer, itemDefinition);
    }

    public boolean getItemDefinitionProperty(final int itemDefinition, final String propertyName, final List<String> values) {
        final SteamInventoryValue steamValue = new SteamInventoryValue();

        final boolean result = getItemDefinitionProperty(pointer, itemDefinition, propertyName, steamValue);

        values.add(steamValue.getValue());

        return result;
    }

    public SteamAPICall requestEligiblePromoItemDefinitionsIDs(final SteamID steamID) {
        return new SteamAPICall(requestEligiblePromoItemDefinitionsIDs(pointer, callback, steamID.handle));
    }

    public boolean getEligiblePromoItemDefinitionIDs(final SteamID steamID, final List<Integer> itemDefIDs, final int size) {
        final int[] tempIntArray = new int[size];
        Arrays.fill(tempIntArray, -1);

        final boolean result = getEligiblePromoItemDefinitionIDs(pointer, steamID.handle, tempIntArray, size);

        if(result) {
            itemDefIDs.addAll(Arrays.stream(tempIntArray).boxed().collect(Collectors.toList()));
        }

        return result;
    }

    public SteamAPICall startPurchase(final int[] arrayItemDefs, final int[] arrayQuantity) {
        return new SteamAPICall(startPurchase(pointer, callback, arrayItemDefs, arrayQuantity, arrayItemDefs.length));
    }

    public SteamAPICall requestPrices() {
        return new SteamAPICall(requestPrices(pointer, callback));
    }

    public int getNumItemsWithPrices() {
        return getNumItemsWithPrices(pointer);
    }

    public boolean getItemsWithPrices(final int[] arrayItemDefs, final long[] currentPrices, final long[] basePrices) {
        return getItemsWithPrices(pointer, arrayItemDefs, currentPrices, basePrices, arrayItemDefs.length);
    }

    public boolean getItemPrice(final int itemDefinition, final long[] currentPrice, final long[] basePrice) {
        return getItemPrice(pointer, itemDefinition, currentPrice, basePrice);
    }

    public SteamInventoryUpdateHandle startUpdateProperties() {
        return new SteamInventoryUpdateHandle(startUpdateProperties(pointer));
    }

    public boolean removeProperty(final SteamInventoryUpdateHandle updateHandle, final SteamItemInstanceId itemID, final String propertyName) {
        return removeProperty(pointer, updateHandle.handle, itemID.handle, propertyName);
    }

    public boolean setProperty(final SteamInventoryUpdateHandle updateHandle, final SteamItemInstanceId itemID, final String propertyName, final String value) {
        return setProperty(pointer, updateHandle.handle, itemID.handle, propertyName, value);
    }

    public boolean setProperty(final SteamInventoryUpdateHandle updateHandle, final SteamItemInstanceId itemID, final String propertyName, final boolean value) {
        return setProperty(pointer, updateHandle.handle, itemID.handle, propertyName, value);
    }

    public boolean setProperty(final SteamInventoryUpdateHandle updateHandle, final SteamItemInstanceId itemID, final String propertyName, final long value) {
        return setProperty(pointer, updateHandle.handle, itemID.handle, propertyName, value);
    }

    public boolean setProperty(final SteamInventoryUpdateHandle updateHandle, final SteamItemInstanceId itemID, final String propertyName, final float value) {
        return setProperty(pointer, updateHandle.handle, itemID.handle, propertyName, value);
    }

    public boolean submitUpdateProperties(final SteamInventoryUpdateHandle updateHandle, final List<SteamInventoryHandle> inventories) {
        final int[] tempIntArray = new int[1];

        final boolean result = submitUpdateProperties(pointer, updateHandle.handle, tempIntArray);

        if(result) {
            inventories.addAll(SteamInventoryHandle.mapToHandles(tempIntArray));
        }

        return result;
    }

    public boolean inspectItem(final List<SteamInventoryHandle> inventories, final String itemToken) {
        final int[] tempIntArray = new int[1];

        final boolean result = inspectItem(pointer, tempIntArray, itemToken);

        if(result) {
            inventories.addAll(SteamInventoryHandle.mapToHandles(tempIntArray));
        }

        return result;
    }

    // @off

	/*JNI
		#include "SteamInventoryCallback.h"
		#include <vector>
	*/

    private static native long createCallback(SteamInventoryCallbackAdapter javaCallback); /*
		return (intp) new SteamInventoryCallback(env, javaCallback);
	*/

    private static native int getResultStatus(long pointer, int resultHandle); /*
		ISteamInventory* inventory = (ISteamInventory*) pointer;
		return inventory->GetResultStatus((SteamInventoryResult_t) resultHandle);
	*/

    private static native int getResultItemsLength(long pointer, int resultHandle); /*
		ISteamInventory* inventory = (ISteamInventory*) pointer;

        uint32 count = 0;
        bool success = inventory->GetResultItems((SteamInventoryResult_t) resultHandle, NULL, &count);

        if(success) {
            return count;
        }

		return -1;
	*/

    private static native boolean getResultItems(long pointer, int resultHandle, SteamItemDetails[] itemDetails); /*
		ISteamInventory* inventory = (ISteamInventory*) pointer;

        uint32 count = 0;
        bool success = false;
        if(inventory->GetResultItems((SteamInventoryResult_t) resultHandle, NULL, &count)) {
            std::vector<SteamItemDetails_t> results;
	        results.resize(count);

            success = inventory->GetResultItems((SteamInventoryResult_t) resultHandle, results.data(), &count);

            if (success) {
                for(unsigned int a = 0; a < count; a = a + 1) {
                    jclass clazz = env->GetObjectClass(env->GetObjectArrayElement(itemDetails, a));

                    jfieldID field = env->GetFieldID(clazz, "itemId", "J");
                    env->SetLongField(env->GetObjectArrayElement(itemDetails, a), field, (jlong) results[a].m_itemId);

                    field = env->GetFieldID(clazz, "itemDefinition", "I");
                    env->SetIntField(env->GetObjectArrayElement(itemDetails, a), field, (jint) results[a].m_iDefinition);

                    field = env->GetFieldID(clazz, "quantity", "S");
                    env->SetShortField(env->GetObjectArrayElement(itemDetails, a), field, (jshort) results[a].m_unQuantity);

                    field = env->GetFieldID(clazz, "flags", "S");
                    env->SetShortField(env->GetObjectArrayElement(itemDetails, a), field, (jshort) results[a].m_unFlags);
                }
            }
        }

		return success;
	*/

    private static native String getResultItemPropertyKeys(long pointer, int resultHandle, int itemIndex); /*
		ISteamInventory* inventory = (ISteamInventory*) pointer;
		char *valueBuffer = (char*) malloc(1);
		uint32 valueBufferSizeOut = 0;

		inventory->GetResultItemProperty((SteamInventoryResult_t) resultHandle, itemIndex, NULL, valueBuffer, &valueBufferSizeOut);
		valueBuffer = (char*) malloc(valueBufferSizeOut);
        inventory->GetResultItemProperty((SteamInventoryResult_t) resultHandle, itemIndex, NULL, valueBuffer, &valueBufferSizeOut);

		return env->NewStringUTF(valueBuffer);
	*/

    private static native boolean getResultItemProperty(long pointer, int resultHandle, int itemIndex, String propertyName, SteamInventoryValue value); /*
		ISteamInventory* inventory = (ISteamInventory*) pointer;
		char *valueBuffer = (char*) malloc(1);
		uint32 valueBufferSizeOut = 0;

		inventory->GetResultItemProperty((SteamInventoryResult_t) resultHandle, itemIndex, propertyName, valueBuffer, &valueBufferSizeOut);
		valueBuffer = (char*) malloc(valueBufferSizeOut);
        bool success = inventory->GetResultItemProperty((SteamInventoryResult_t) resultHandle, itemIndex, propertyName, valueBuffer, &valueBufferSizeOut);

        jclass valueClazz = env->GetObjectClass(value);

        jfieldID field = env->GetFieldID(valueClazz, "value", "Ljava/lang/String;");
	    env->SetObjectField(value, field, env->NewStringUTF(valueBuffer));

		return success;
	*/

    private static native int getResultTimestamp(long pointer, int resultHandle); /*
		ISteamInventory* inventory = (ISteamInventory*) pointer;
		return inventory->GetResultTimestamp((SteamInventoryResult_t) resultHandle);
	*/

    private static native boolean checkResultSteamID(long pointer, int resultHandle, long steamIDExpected); /*
		ISteamInventory* inventory = (ISteamInventory*) pointer;
		return inventory->CheckResultSteamID((SteamInventoryResult_t) resultHandle, (uint64) steamIDExpected);
	*/

    private static native void destroyResult(long pointer, int resultHandle); /*
		ISteamInventory* inventory = (ISteamInventory*) pointer;
		return inventory->DestroyResult((SteamInventoryResult_t) resultHandle);
	*/

    private static native boolean getAllItems(long pointer, int[] resultHandles); /*
		ISteamInventory* inventory = (ISteamInventory*) pointer;
		return inventory->GetAllItems((SteamInventoryResult_t*) resultHandles);
	*/

    private static native boolean getItemsByID(long pointer, int[] resultHandles, long[] instanceIDs, int countInstanceIDs); /*
		ISteamInventory* inventory = (ISteamInventory*) pointer;
		return inventory->GetItemsByID((SteamInventoryResult_t*) resultHandles, (SteamItemInstanceID_t*) instanceIDs, countInstanceIDs);
	*/

    private static native int serializeResultSize(long pointer, int resultHandle); /*
		ISteamInventory* inventory = (ISteamInventory*) pointer;
		uint32 outBufferSize = 0;

		bool success = inventory->SerializeResult((SteamInventoryResult_t) resultHandle, NULL, &outBufferSize);

		if(success) {
		    return outBufferSize;
		} else {
		    return -1;
		}
	*/

    private static native boolean serializeResult(long pointer, int resultHandle, ByteBuffer outBuffer, int offset, int outBufferSize); /*
		ISteamInventory* inventory = (ISteamInventory*) pointer;
		return inventory->SerializeResult((SteamInventoryResult_t) resultHandle, &outBuffer[offset], (uint32*) &outBufferSize);
	*/

    private static native boolean deserializeResult(long pointer, int[] resultHandles, ByteBuffer buffer, int offset, int bufferSize, boolean reserved); /*
		ISteamInventory* inventory = (ISteamInventory*) pointer;
		return inventory->DeserializeResult((SteamInventoryResult_t*) resultHandles, &buffer[offset], bufferSize, reserved);
	*/

    private static native boolean generateItems(long pointer, int[] resultHandles, int[] arrayItemDefs, int[] arrayQuantity, int arrayLength); /*
		ISteamInventory* inventory = (ISteamInventory*) pointer;
		return inventory->GenerateItems((SteamInventoryResult_t*) resultHandles, (SteamItemDef_t*) arrayItemDefs, (uint32*) arrayQuantity, arrayLength);
	*/

    private static native boolean grantPromoItems(long pointer, int[] resultHandles); /*
		ISteamInventory* inventory = (ISteamInventory*) pointer;
		return inventory->GrantPromoItems((SteamInventoryResult_t*) resultHandles);
	*/

    private static native boolean addPromoItem(long pointer, int[] resultHandles, int itemDef); /*
		ISteamInventory* inventory = (ISteamInventory*) pointer;
		return inventory->AddPromoItem((SteamInventoryResult_t*) resultHandles, itemDef);
	*/

    private static native boolean addPromoItems(long pointer, int[] resultHandles, int[] arrayItemDefs, int arrayLength); /*
		ISteamInventory* inventory = (ISteamInventory*) pointer;
		return inventory->AddPromoItems((SteamInventoryResult_t*) resultHandles, (SteamItemDef_t*) arrayItemDefs, arrayLength);
	*/

    private static native boolean consumeItem(long pointer, int[] resultHandles, long itemConsume, int quantity); /*
		ISteamInventory* inventory = (ISteamInventory*) pointer;
		return inventory->ConsumeItem((SteamInventoryResult_t*) resultHandles, (SteamItemInstanceID_t) itemConsume, quantity);
	*/

    private static native boolean exchangeItems(long pointer, int[] resultHandles, int[] arrayGenerate, int[] arrayGenerateQuantity, int arrayGenerateLength,
                                                long[] arrayDestroy, int[] arrayDestroyQuantity, int arrayDestroyLength); /*
		ISteamInventory* inventory = (ISteamInventory*) pointer;
		return inventory->ExchangeItems((SteamInventoryResult_t*) resultHandles, (SteamItemDef_t*) arrayGenerate, (uint32*) arrayGenerateQuantity, arrayGenerateLength, (SteamItemInstanceID_t*) arrayDestroy, (uint32*) arrayDestroyQuantity, arrayDestroyLength);
	*/

    private static native boolean transferItemQuantity(long pointer, int[] resultHandles, long itemIdSource, int quantity, long itemIdDest); /*
		ISteamInventory* inventory = (ISteamInventory*) pointer;
		return inventory->TransferItemQuantity((SteamInventoryResult_t*) resultHandles, (SteamItemInstanceID_t) itemIdSource, quantity, (SteamItemInstanceID_t) itemIdDest);
	*/

    private static native void sendItemDropHeartbeat(long pointer); /*
		ISteamInventory* inventory = (ISteamInventory*) pointer;
		return inventory->SendItemDropHeartbeat();
	*/

    private static native boolean triggerItemDrop(long pointer, int[] resultHandles, int dropListDefinition); /*
		ISteamInventory* inventory = (ISteamInventory*) pointer;
		return inventory->TriggerItemDrop((SteamInventoryResult_t*) resultHandles, dropListDefinition);
	*/

    private static native boolean loadItemDefinitions(long pointer); /*
		ISteamInventory* inventory = (ISteamInventory*) pointer;
		return inventory->LoadItemDefinitions();
	*/

    private static native int getItemDefinitionIDSize(long pointer); /*
		ISteamInventory* inventory = (ISteamInventory*) pointer;
        uint32 count = 0;

        bool success = inventory->GetItemDefinitionIDs(NULL, &count);

        if(success) {
            return count;
        }

		return -1;
	*/

    private static native boolean getItemDefinitionIDs(long pointer, int[] itemDefIDs, int itemDefIDsArraySize); /*
		ISteamInventory* inventory = (ISteamInventory*) pointer;
		return inventory->GetItemDefinitionIDs((SteamItemDef_t*) itemDefIDs, (uint32*) &itemDefIDsArraySize);
	*/

    private static native String getItemDefinitionPropertyKeys(long pointer, int itemDefinition); /*
		ISteamInventory* inventory = (ISteamInventory*) pointer;
		char *valueBuffer = (char*) malloc(1);
		uint32 valueBufferSizeOut = 0;

		inventory->GetItemDefinitionProperty((SteamItemDef_t) itemDefinition, NULL, valueBuffer, &valueBufferSizeOut);
		valueBuffer = (char*) malloc(valueBufferSizeOut);
        inventory->GetItemDefinitionProperty((SteamItemDef_t) itemDefinition, NULL, valueBuffer, &valueBufferSizeOut);

		return env->NewStringUTF(valueBuffer);
	*/

    private static native boolean getItemDefinitionProperty(long pointer, int itemDefinition, String propertyName, SteamInventoryValue value); /*
		ISteamInventory* inventory = (ISteamInventory*) pointer;
		char *valueBuffer = (char*) malloc(1);
		uint32 valueBufferSizeOut = 0;

		inventory->GetItemDefinitionProperty((SteamItemDef_t) itemDefinition, propertyName, valueBuffer, &valueBufferSizeOut);
		valueBuffer = (char*) malloc(valueBufferSizeOut);
        bool success = inventory->GetItemDefinitionProperty((SteamItemDef_t) itemDefinition, propertyName, valueBuffer, &valueBufferSizeOut);

        jclass valueClazz = env->GetObjectClass(value);

        jfieldID field = env->GetFieldID(valueClazz, "value", "Ljava/lang/String;");
	    env->SetObjectField(value, field, env->NewStringUTF(valueBuffer));

		return success;
	*/

    private static native long requestEligiblePromoItemDefinitionsIDs(long pointer, long callback, long steamID); /*
		ISteamInventory* inventory = (ISteamInventory*) pointer;
		SteamInventoryCallback* cb = (SteamInventoryCallback*) callback;
		SteamAPICall_t handle = inventory->RequestEligiblePromoItemDefinitionsIDs(CSteamID((uint64) steamID));
		cb->onSteamInventoryEligiblePromoItemDefIDsCall.Set(handle, cb, &SteamInventoryCallback::onSteamInventoryEligiblePromoItemDefIDs);
		return handle;
	*/

    private static native boolean getEligiblePromoItemDefinitionIDs(long pointer, long steamID, int[] itemDefIDs, int itemDefIDsArraySize); /*
		ISteamInventory* inventory = (ISteamInventory*) pointer;
		return inventory->GetEligiblePromoItemDefinitionIDs((uint64) steamID, (SteamItemDef_t*) itemDefIDs, (uint32*) &itemDefIDsArraySize);
	*/

    private static native long startPurchase(long pointer, long callback, int[] arrayItemDefs, int[] arrayQuantity, int arrayLength); /*
		ISteamInventory* inventory = (ISteamInventory*) pointer;
		SteamInventoryCallback* cb = (SteamInventoryCallback*) callback;
		SteamAPICall_t handle = inventory->StartPurchase((SteamItemDef_t*) arrayItemDefs, (uint32*) arrayQuantity, arrayLength);
		cb->onSteamInventoryStartPurchaseResultCall.Set(handle, cb, &SteamInventoryCallback::onSteamInventoryStartPurchaseResult);
		return handle;
	*/

    private static native long requestPrices(long pointer, long callback); /*
		ISteamInventory* inventory = (ISteamInventory*) pointer;
		SteamInventoryCallback* cb = (SteamInventoryCallback*) callback;
		SteamAPICall_t handle = inventory->RequestPrices();
		cb->onSteamInventoryRequestPricesResultCall.Set(handle, cb, &SteamInventoryCallback::onSteamInventoryRequestPricesResult);
		return handle;
	*/

    private static native int getNumItemsWithPrices(long pointer); /*
		ISteamInventory* inventory = (ISteamInventory*) pointer;
		return inventory->GetNumItemsWithPrices();
	*/

    private static native boolean getItemsWithPrices(long pointer, int[] arrayItemDefs, long[] currentPrices, long[] basePrices, int arrayLength); /*
		ISteamInventory* inventory = (ISteamInventory*) pointer;
		return inventory->GetItemsWithPrices((SteamItemDef_t*) arrayItemDefs, (uint64*) currentPrices, (uint64*) basePrices, arrayLength);
	*/

    private static native boolean getItemPrice(long pointer, int itemDefinition, long[] currentPrice, long[] basePrice); /*
		ISteamInventory* inventory = (ISteamInventory*) pointer;
		return inventory->GetItemPrice((SteamItemDef_t) itemDefinition, (uint64*) currentPrice, (uint64*) basePrice);
	*/

    private static native long startUpdateProperties(long pointer); /*
		ISteamInventory* inventory = (ISteamInventory*) pointer;
		return inventory->StartUpdateProperties();
	*/

    private static native boolean removeProperty(long pointer, long updateHandle, long itemID, String propertyName); /*
		ISteamInventory* inventory = (ISteamInventory*) pointer;
		return inventory->RemoveProperty((SteamInventoryUpdateHandle_t) updateHandle, (SteamItemInstanceID_t) itemID, propertyName);
	*/

    private static native boolean setProperty(long pointer, long updateHandle, long itemID, String propertyName, String value); /*
		ISteamInventory* inventory = (ISteamInventory*) pointer;
		return inventory->SetProperty((SteamInventoryUpdateHandle_t) updateHandle, (SteamItemInstanceID_t) itemID, propertyName, (char*) value);
	*/

    private static native boolean setProperty(long pointer, long updateHandle, long itemID, String propertyName, boolean value); /*
		ISteamInventory* inventory = (ISteamInventory*) pointer;
		return inventory->SetProperty((SteamInventoryUpdateHandle_t) updateHandle, (SteamItemInstanceID_t) itemID, propertyName, (bool) value);
	*/

    private static native boolean setProperty(long pointer, long updateHandle, long itemID, String propertyName, long value); /*
		ISteamInventory* inventory = (ISteamInventory*) pointer;
		return inventory->SetProperty((SteamInventoryUpdateHandle_t) updateHandle, (SteamItemInstanceID_t) itemID, propertyName, (int64) value);
	*/

    private static native boolean setProperty(long pointer, long updateHandle, long itemID, String propertyName, float value); /*
		ISteamInventory* inventory = (ISteamInventory*) pointer;
		return inventory->SetProperty((SteamInventoryUpdateHandle_t) updateHandle, (SteamItemInstanceID_t) itemID, propertyName, (float) value);
	*/

    private static native boolean submitUpdateProperties(long pointer, long updateHandle, int[] resultHandles); /*
		ISteamInventory* inventory = (ISteamInventory*) pointer;
		return inventory->SubmitUpdateProperties((SteamInventoryUpdateHandle_t) updateHandle, (SteamInventoryResult_t*) resultHandles);
	*/

    private static native boolean inspectItem(long pointer, int[] resultHandles, String itemToken); /*
		ISteamInventory* inventory = (ISteamInventory*) pointer;
		return inventory->InspectItem((SteamInventoryResult_t*) resultHandles, itemToken);
	*/
}
