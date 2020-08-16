package com.codedisaster.steamworks.test;

import com.codedisaster.steamworks.SteamException;
import com.codedisaster.steamworks.SteamResult;
import com.codedisaster.steamworks.apps.RegisterActivationCodeResult;
import com.codedisaster.steamworks.apps.SteamApps;
import com.codedisaster.steamworks.apps.SteamAppsCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SteamAppsTest extends SteamTestApp {
    private SteamApps apps;

    private final SteamAppsCallback appsCallback = new SteamAppsCallback() {
        @Override
        public void onSteamAppsFileDetailsResult(final SteamResult result, final long fileSize, final byte[] fileSHA, final int flags) {

        }

        @Override
        public void onSteamAppsDlcInstalled(final int appID) {

        }

        @Override
        public void onSteamAppsNewUrlLaunchParameters() {

        }

        @Override
        public void onSteamAppsTimedTrialStatus(final int appID, final boolean isOffline, final int secondsAllowed, final int secondsPlayed) {

        }

        @Override
        public void onSteamAppsRegisterActivationCodeResponse(final RegisterActivationCodeResult result, final int packageRegistered) {

        }

        @Override
        public void onSteamAppsAppProofOfPurchaseKeyResponse(final SteamResult result, final int appID, final int keyLength, final String key) {

        }
    };

    @Override
    protected void registerInterfaces() throws SteamException {
        System.out.println("Register Apps ...");
        apps = new SteamApps(appsCallback);
    }

    @Override
    protected void unregisterInterfaces() throws SteamException {
        apps.dispose();
    }

    @Override
    protected void processUpdate() throws SteamException {

    }

    @Override
    protected void processInput(final String input) throws SteamException {
        if (input.startsWith("getAppBuildId")) {
            System.out.println(apps.getAppBuildId() + ": result of getAppBuildId");
        } else if (input.equals("getAppOwner")) {
            System.out.println(apps.getAppOwner().handle + ": result of getAppOwner");
        } else if (input.equals("getAvailableGameLanguages")) {
            System.out.println(apps.getAvailableGameLanguages() + ": result of getAvailableGameLanguages");
        } else if (input.equals("getAppInstallDir")) {
            final List<String> folders = new ArrayList<>();

            System.out.println(apps.getAppInstallDir(clientUtils.getAppID(), folders) + ": copied Bytes (getAppInstallDir)");
            System.out.println(folders);
        } else if (input.equals("getCurrentBetaName")) {
            final List<String> betaNames = new ArrayList<>();

            System.out.println(apps.getCurrentBetaName(betaNames, 10) + ": result of getCurrentBetaName");
            System.out.println(betaNames);
        } else if (input.equals("getCurrentGameLanguage")) {
            System.out.println(apps.getCurrentGameLanguage() + ": result of getCurrentGameLanguage");
        } else if (input.equals("getDLCCount")) {
            System.out.println(apps.getDLCCount() + ": result of getDLCCount");
        } else if (input.equals("getDLCDataByIndex")) {
            final List<Integer> appIds = new ArrayList<>();
            final List<Boolean> dlcAvailables = new ArrayList<>();
            final List<String> values = new ArrayList<>();

            System.out.println(apps.getDLCDataByIndex(0, appIds, dlcAvailables, values, 10) + ": result of getDLCDataByIndex");
            System.out.println(appIds);
            System.out.println(dlcAvailables);
            System.out.println(values);
        } else if (input.equals("getDlcDownloadProgress")) {
            final long[] bytesDownloaded = new long[1];
            final long[] bytesTotal = new long[1];

            System.out.println(apps.getDlcDownloadProgress(clientUtils.getAppID(), bytesDownloaded, bytesTotal) + ": result of getDlcDownloadProgress");
            System.out.println(Arrays.toString(bytesDownloaded));
            System.out.println(Arrays.toString(bytesTotal));
        } else if (input.equals("getEarliestPurchaseUnixTime")) {
            System.out.println(apps.getEarliestPurchaseUnixTime(clientUtils.getAppID()) + ": result of getEarliestPurchaseUnixTime");
        } else if (input.equals("getFileDetails")) {
            System.out.println(apps.getFileDetails("") + ": result of getFileDetails");
        } else if (input.equals("getInstalledDepots")) {
            final int maxDepots = 10;
            final int[] depotIDs = new int[maxDepots];

            System.out.println(apps.getInstalledDepots(clientUtils.getAppID(), depotIDs, maxDepots) + ": result of getInstalledDepots");
            System.out.println(Arrays.toString(depotIDs));
        } else if (input.equals("getLaunchCommandLine")) {
            final List<String> commandLines = new ArrayList<>();
            System.out.println(apps.getLaunchCommandLine(commandLines) + ": copied Bytes (getLaunchCommandLine)");
            System.out.println(commandLines);
        } else if (input.startsWith("getLaunchQueryParam ")) {
            final String[] params = input.substring("getLaunchQueryParam ".length()).split(" ");

            System.out.println(apps.getLaunchQueryParam(params[0]) + ": result of getLaunchQueryParam");
        } else if (input.equals("isAppInstalled")) {
            System.out.println(apps.isAppInstalled(clientUtils.getAppID()) + ": result of isAppInstalled");
        } else if (input.equals("isCybercafe")) {
            System.out.println(apps.isCybercafe() + ": result of isCybercafe");
        } else if (input.equals("isDlcInstalled")) {
            System.out.println(apps.isDlcInstalled(clientUtils.getAppID()) + ": result of isDlcInstalled");
        } else if (input.equals("isLowViolence")) {
            System.out.println(apps.isLowViolence() + ": result of isLowViolence");
        } else if (input.equals("isSubscribed")) {
            System.out.println(apps.isSubscribed() + ": result of isSubscribed");
        } else if (input.equals("isSubscribedApp")) {
            System.out.println(apps.isSubscribedApp(clientUtils.getAppID()) + ": result of isSubscribedApp");
        } else if (input.equals("isSubscribedFromFamilySharing")) {
            System.out.println(apps.isSubscribedFromFamilySharing() + ": result of isSubscribedFromFamilySharing");
        } else if (input.equals("isSubscribedFromFreeWeekend")) {
            System.out.println(apps.isSubscribedFromFreeWeekend() + ": result of isSubscribedFromFreeWeekend");
        } else if (input.equals("isVACBanned")) {
            System.out.println(apps.isVACBanned() + ": result of isVACBanned");
        } else if (input.equals("isTimedTrial")) {
            final int[] secondsAllowed = new int[1];
            final int[] secondsPlayed = new int[1];

            System.out.println(apps.isTimedTrial(secondsAllowed, secondsPlayed) + ": result of isTimedTrial");
            System.out.println(Arrays.toString(secondsAllowed));
            System.out.println(Arrays.toString(secondsPlayed));
        } else if (input.startsWith("markContentCorrupt ")) {
            final String[] params = input.substring("markContentCorrupt ".length()).split(" ");
            final boolean missingFilesOnly = Boolean.getBoolean(params[0]);

            System.out.println(apps.markContentCorrupt(missingFilesOnly) + ": result of markContentCorrupt");
        }
    }

    public static void main(final String[] arguments) {
        new SteamAppsTest().clientMain(arguments);
    }
}
