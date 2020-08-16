package com.codedisaster.steamworks.test;

import com.codedisaster.steamworks.*;
import com.codedisaster.steamworks.apps.RegisterActivationCodeResult;
import com.codedisaster.steamworks.apps.SteamApps;
import com.codedisaster.steamworks.apps.SteamAppsCallback;
import com.codedisaster.steamworks.friends.SteamFriends;
import com.codedisaster.steamworks.friends.SteamFriendsCallback;
import com.codedisaster.steamworks.remotestorage.SteamPublishedFileUpdateHandle;
import com.codedisaster.steamworks.remotestorage.SteamRemoteStorage;
import com.codedisaster.steamworks.remotestorage.SteamRemoteStorageCallback;
import com.codedisaster.steamworks.ugc.*;
import com.codedisaster.steamworks.user.SteamUser;
import com.codedisaster.steamworks.user.SteamUserCallback;
import com.codedisaster.steamworks.userstats.*;
import com.codedisaster.steamworks.utils.SteamUtils;
import com.codedisaster.steamworks.utils.SteamUtilsCallback;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collection;

public class SteamClientAPITest extends SteamTestApp {

	private SteamUser user;
	private SteamUserStats userStats;
	private SteamRemoteStorage remoteStorage;
	private SteamUGC ugc;
	private SteamUtils utils;
	private SteamApps apps;
	private SteamFriends friends;

	private SteamLeaderboardHandle currentLeaderboard = null;

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

	private final SteamUserStatsCallback userStatsCallback = new SteamUserStatsCallback() {
		@Override
		public void onUserStatsReceived(final long gameId, final SteamID steamIDUser, final SteamResult result) {
			System.out.println("User stats received: gameId=" + gameId + ", userId=" + steamIDUser.getAccountID() +
					", result=" + result.toString());

			final int numAchievements = userStats.getNumAchievements();
			System.out.println("Num of achievements: " + numAchievements);

			for (int i = 0; i < numAchievements; i++) {
				final String name = userStats.getAchievementName(i);
				final boolean achieved = userStats.isAchieved(name, false);
				System.out.println("# " + i + " : name=" + name + ", achieved=" + (achieved ? "yes" : "no"));
			}
		}

		@Override
		public void onUserStatsStored(final long gameId, final SteamResult result) {
			System.out.println("User stats stored: gameId=" + gameId +
					", result=" + result.toString());
		}

		@Override
		public void onUserStatsUnloaded(final SteamID steamIDUser) {
			System.out.println("User stats unloaded: userId=" + steamIDUser.getAccountID());
		}

		@Override
		public void onUserAchievementStored(final long gameId, final boolean isGroupAchievement, final String achievementName,
											final int curProgress, final int maxProgress) {
			System.out.println("User achievement stored: gameId=" + gameId + ", name=" + achievementName +
					", progress=" + curProgress + "/" + maxProgress);
		}

		@Override
		public void onLeaderboardFindResult(final SteamLeaderboardHandle leaderboard, final boolean found) {
			System.out.println("Leaderboard find result: handle=" + leaderboard.toString() +
					", found=" + (found ? "yes" : "no"));

			if (found) {
				System.out.println("Leaderboard: name=" + userStats.getLeaderboardName(leaderboard) +
						", entries=" + userStats.getLeaderboardEntryCount(leaderboard));

				currentLeaderboard = leaderboard;
			}
		}

		@Override
		public void onLeaderboardScoresDownloaded(final SteamLeaderboardHandle leaderboard,
												  final SteamLeaderboardEntriesHandle entries,
												  final int numEntries) {

			System.out.println("Leaderboard scores downloaded: handle=" + leaderboard.toString() +
					", entries=" + entries.toString() + ", count=" + numEntries);

			final int[] details = new int[16];

			for (int i = 0; i < numEntries; i++) {

				final SteamLeaderboardEntry entry = new SteamLeaderboardEntry();
				if (userStats.getDownloadedLeaderboardEntry(entries, i, entry, details)) {

					final int numDetails = entry.getNumDetails();

					System.out.println("Leaderboard entry #" + i +
							": accountID=" + entry.getSteamIDUser().getAccountID() +
							", globalRank=" + entry.getGlobalRank() +
							", score=" + entry.getScore() +
							", numDetails=" + numDetails);

					for (int detail = 0; detail < numDetails; detail++) {
						System.out.println("  ... detail #" + detail + "=" + details[detail]);
					}

					if (friends.requestUserInformation(entry.getSteamIDUser(), false)) {
						System.out.println("  ... requested user information for entry");
					} else {
						System.out.println("  ... user name is '" +
								friends.getFriendPersonaName(entry.getSteamIDUser()) + "'");

						final int smallAvatar = friends.getSmallFriendAvatar(entry.getSteamIDUser());
						if (smallAvatar != 0) {
							final int w = utils.getImageWidth(smallAvatar);
							final int h = utils.getImageHeight(smallAvatar);
							System.out.println("  ... small avatar size: " + w + "x" + h + " pixels");

							final ByteBuffer image = ByteBuffer.allocateDirect(w * h * 4);

							try {
								if (utils.getImageRGBA(smallAvatar, image)) {
									System.out.println("  ... small avatar retrieve avatar image successful");

									int nonZeroes = w * h;
									for (int y = 0; y < h; y++) {
										for (int x = 0; x < w; x++) {
											//System.out.print(String.format(" %08x", image.getInt(y * w + x)));
											if (image.getInt(y * w + x) == 0) {
												nonZeroes--;
											}
										}
										//System.out.println();
									}

									if (nonZeroes == 0) {
										System.err.println("Something's wrong! Avatar image is empty!");
									}

								} else {
									System.out.println("  ... small avatar retrieve avatar image failed!");
								}
							} catch (final SteamException e) {
								e.printStackTrace();
							}
						} else {
							System.out.println("  ... small avatar image not available!");
						}

					}
				}

			}
		}

		@Override
		public void onLeaderboardScoreUploaded(final boolean success,
											   final SteamLeaderboardHandle leaderboard,
											   final int score,
											   final boolean scoreChanged,
											   final int globalRankNew,
											   final int globalRankPrevious) {

			System.out.println("Leaderboard score uploaded: " + (success ? "yes" : "no") +
					", handle=" + leaderboard.toString() +
					", score=" + score +
					", changed=" + (scoreChanged ? "yes" : "no") +
					", globalRankNew=" + globalRankNew +
					", globalRankPrevious=" + globalRankPrevious);
		}

		@Override
		public void onGlobalStatsReceived(final long gameId, final SteamResult result) {
			System.out.println("Global stats received: gameId=" + gameId + ", result=" + result.toString());
		}
	};

	private final SteamRemoteStorageCallback remoteStorageCallback = new SteamRemoteStorageCallback() {
		@Override
		public void onFileWriteAsyncComplete(final SteamResult result) {

		}

		@Override
		public void onFileReadAsyncComplete(final SteamAPICall fileReadAsync, final SteamResult result, final int offset, final int read) {

		}

		@Override
		public void onFileShareResult(final SteamUGCHandle fileHandle, final String fileName, final SteamResult result) {
			System.out.println("Remote storage file share result: handle='" + fileHandle.toString() +
					", name=" + fileName + "', result=" + result.toString());
		}

		@Override
		public void onDownloadUGCResult(final SteamUGCHandle fileHandle, final SteamResult result) {
			System.out.println("Remote storage download UGC result: handle='" + fileHandle.toString() +
					"', result=" + result.toString());

			final ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
			int offset = 0, bytesRead;

			do {
				bytesRead = remoteStorage.ugcRead(fileHandle, buffer, buffer.limit(), offset,
						SteamRemoteStorage.UGCReadAction.ContinueReadingUntilFinished);
				offset += bytesRead;
			} while (bytesRead > 0);

			System.out.println("Read " + offset + " bytes from handle=" + fileHandle.toString());
		}

		@Override
		public void onPublishFileResult(final SteamPublishedFileID publishedFileID, final boolean needsToAcceptWLA, final SteamResult result) {
			System.out.println("Remote storage publish file result: publishedFileID=" + publishedFileID.toString() +
					", needsToAcceptWLA=" + needsToAcceptWLA + ", result=" + result.toString());
		}

		@Override
		public void onUpdatePublishedFileResult(final SteamPublishedFileID publishedFileID, final boolean needsToAcceptWLA, final SteamResult result) {
			System.out.println("Remote storage update published file result: publishedFileID=" + publishedFileID.toString() +
					", needsToAcceptWLA=" + needsToAcceptWLA + ", result=" + result.toString());
		}

		@Override
		public void onPublishedFileSubscribed(final SteamPublishedFileID publishedFileID, final int appID) {

		}

		@Override
		public void onPublishedFileUnsubscribed(final SteamPublishedFileID publishedFileID, final int appID) {

		}

		@Override
		public void onPublishedFileDeleted(final SteamPublishedFileID publishedFileID, final int appID) {

		}
	};

	private final SteamUGCCallback ugcCallback = new SteamUGCCallback() {
		@Override
		public void onUGCQueryCompleted(final SteamUGCQuery query, final int numResultsReturned, final int totalMatchingResults,
										final boolean isCachedData, final SteamResult result) {
			System.out.println("UGC query completed: handle=" + query.toString() + ", " + numResultsReturned + " of " +
					totalMatchingResults + " results returned, result=" + result.toString());

			for (int i = 0; i < numResultsReturned; i++) {
				final SteamUGCDetails details = new SteamUGCDetails();
				ugc.getQueryUGCResult(query, i, details);
				printUGCDetails("UGC details #" + i, details);
			}

			ugc.releaseQueryUserUGCRequest(query);
		}

		@Override
		public void onSubscribeItem(final SteamPublishedFileID publishedFileID, final SteamResult result) {
			System.out.println("Subscribe item result: publishedFileID=" + publishedFileID + ", result=" + result);
		}

		@Override
		public void onUnsubscribeItem(final SteamPublishedFileID publishedFileID, final SteamResult result) {
			System.out.println("Unsubscribe item result: publishedFileID=" + publishedFileID + ", result=" + result);
		}

		@Override
		public void onRequestUGCDetails(final SteamUGCDetails details, final SteamResult result) {
			System.out.println("Request details result: result=" + result);
			printUGCDetails("UGC details ", details);
		}

		@Override
		public void onCreateItem(final SteamPublishedFileID publishedFileID, final boolean needsToAcceptWLA, final SteamResult result) {
			System.out.println("Create item result: result=" + result + ", needsToAcceptWLA: " + needsToAcceptWLA);
			System.out.println("publishedFileID: " + publishedFileID);

			final SteamUGCUpdateHandle updateHandle = ugc.startItemUpdate(utils.getAppID(), publishedFileID);
			ugc.setItemTitle(updateHandle, "Test UGC!");
			ugc.setItemDescription(updateHandle, "Dummy UGC file published by test application.");
			ugc.setItemVisibility(updateHandle, SteamRemoteStorage.PublishedFileVisibility.Private);
			ugc.submitItemUpdate(updateHandle, "Dummy UGC file published by test application.");
		}

		@Override
		public void onSubmitItemUpdate(final SteamPublishedFileID publishedFileID, final boolean needsToAcceptWLA, final SteamResult result) {
			System.out.println("Submit itemupdate result: result=" + result + ", needsToAcceptWLA: " + needsToAcceptWLA);
			System.out.println("publishedFileID: " + publishedFileID);
		}

		@Override
		public void onDownloadItemResult(final int appID, final SteamPublishedFileID publishedFileID, final SteamResult result) {

		}

		@Override
		public void onUserFavoriteItemsListChanged(final SteamPublishedFileID publishedFileID, final boolean wasAddRequest, final SteamResult result) {

		}

		@Override
		public void onSetUserItemVote(final SteamPublishedFileID publishedFileID, final boolean voteUp, final SteamResult result) {

		}

		@Override
		public void onGetUserItemVote(final SteamPublishedFileID publishedFileID, final boolean votedUp, final boolean votedDown, final boolean voteSkipped, final SteamResult result) {

		}

		private void printUGCDetails(final String prefix, final SteamUGCDetails details) {
			System.out.println(prefix +
					": publishedFileID=" + details.getPublishedFileID().toString() +
					", result=" + details.getResult().name() +
					", type=" + details.getFileType().name() +
					", title='" + details.getTitle() + "'" +
					", description='" + details.getDescription() + "'" +
					", tags='" + details.getTags() + "'" +
					", fileName=" + details.getFileName() +
					", fileHandle=" + details.getFileHandle().toString() +
					", previewFileHandle=" + details.getPreviewFileHandle().toString() +
					", url=" + details.getURL());
		}

		@Override
		public void onStartPlaytimeTracking(final SteamResult result) {

		}

		@Override
		public void onStopPlaytimeTracking(final SteamResult result) {

		}

		@Override
		public void onStopPlaytimeTrackingForAllItems(final SteamResult result) {

		}

		@Override
		public void onDeleteItem(final SteamPublishedFileID publishedFileID, final SteamResult result) {

		}
	};

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

	private final SteamFriendsCallback friendsCallback = new SteamFriendsCallback() {
		@Override
		public void onSetPersonaNameResponse(final boolean success, final boolean localSuccess, final SteamResult result) {

		}

		@Override
		public void onPersonaStateChange(final SteamID steamID, final SteamFriends.PersonaChange change) {
			switch (change) {
				case Name:
					System.out.println("Persona name received: " + "accountID=" + steamID.getAccountID() + ", name='" + friends.getFriendPersonaName(steamID) + "'");
					break;
				default:
					System.out.println("Persona state changed (unhandled): " + "accountID=" + steamID.getAccountID() + ", change=" + change.name());
					break;
			}
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

	private final SteamUtilsCallback utilsCallback = () -> System.out.println("Steam client wants to shut down!");

	@Override
	protected void registerInterfaces() {
		System.out.println("Register user ...");
		user = new SteamUser(userCallback);

		System.out.println("Register user stats callback ...");
		userStats = new SteamUserStats(userStatsCallback);

		System.out.println("Register remote storage ...");
		remoteStorage = new SteamRemoteStorage(remoteStorageCallback);

		System.out.println("Register UGC ...");
		ugc = new SteamUGC(ugcCallback);

		System.out.println("Register Utils ...");
		utils = new SteamUtils(utilsCallback);

		System.out.println("Register Apps ...");
		apps = new SteamApps(appsCallback);

		System.out.println("Register Friends ...");
		friends = new SteamFriends(friendsCallback);

		System.out.println("Local user account ID: " + user.getSteamID().getAccountID());
		System.out.println("Local user steam ID: " + SteamNativeHandle.getNativeHandle(user.getSteamID()));
		System.out.println("Local user friends name: " + friends.getPersonaName());
		System.out.println("App ID: " + utils.getAppID());

		System.out.println("App build ID: " + apps.getAppBuildId());
		System.out.println("App owner: " + apps.getAppOwner().getAccountID());

		System.out.println("Current game language: " + apps.getCurrentGameLanguage());
		System.out.println("Available game languages: " + apps.getAvailableGameLanguages());
	}

	@Override
	protected void unregisterInterfaces() {
		user.dispose();
		userStats.dispose();
		remoteStorage.dispose();
		ugc.dispose();
		utils.dispose();
		apps.dispose();
		friends.dispose();
	}

	@Override
	protected void processUpdate() throws SteamException {

	}

	@Override
	protected void processInput(final String input) throws SteamException {
		if (input.startsWith("stats global ")) {
			final String[] cmd = input.substring("stats global ".length()).split(" ");
			if (cmd.length > 0) {
				if (cmd[0].equals("request")) {
					int days = 0;
					if (cmd.length > 1) {
						days = Integer.parseInt(cmd[1]);
					}
					userStats.requestGlobalStats(days);
				} else if (cmd[0].equals("lget") && cmd.length > 1) {
					int days = 0;
					if (cmd.length > 2) {
						days = Integer.parseInt(cmd[2]);
					}
					if (days == 0) {
						final long value = userStats.getGlobalStat(cmd[1], -1);
						System.out.println("global stat (L) '" + cmd[1] + "' = " + value);
					} else {
						final long[] data = new long[days];
						final int count = userStats.getGlobalStatHistory(cmd[1], data);
						System.out.print("global stat history (L) for " + count + " of " + days + " days:");
						for (int i = 0; i < count; i++) {
							System.out.print(" " + data[i]);
						}
						System.out.println();
					}
				} else if (cmd[0].equals("dget") && cmd.length > 1) {
					int days = 0;
					if (cmd.length > 2) {
						days = Integer.parseInt(cmd[2]);
					}
					if (days == 0) {
						final double value = userStats.getGlobalStat(cmd[1], -1.0);
						System.out.println("global stat (D) '" + cmd[1] + "' = " + value);
					} else {
						final double[] data = new double[days];
						final int count = userStats.getGlobalStatHistory(cmd[1], data);
						System.out.print("global stat history (D) for " + count + " of " + days + " days:");
						for (int i = 0; i < count; i++) {
							System.out.print(" " + data[i]);
						}
						System.out.println();
					}
				}
			}
		} else if (input.equals("stats request")) {
			userStats.requestCurrentStats();
		} else if (input.equals("stats store")) {
			userStats.storeStats();
		} else if (input.startsWith("achievement set ")) {
			final String achievementName = input.substring("achievement set ".length());
			System.out.println("- setting " + achievementName + " to 'achieved'");
			userStats.setAchievement(achievementName);
		} else if (input.startsWith("achievement clear ")) {
			final String achievementName = input.substring("achievement clear ".length());
			System.out.println("- clearing " + achievementName);
			userStats.clearAchievement(achievementName);
		} else if (input.equals("file list")) {
			final int numFiles = remoteStorage.getFileCount();
			System.out.println("Num of files: " + numFiles);

			for (int i = 0; i < numFiles; i++) {
				final int[] sizes = new int[1];
				final String file = remoteStorage.getFileNameAndSize(i, sizes);
				final boolean exists = remoteStorage.fileExists(file);
				System.out.println("# " + i + " : name=" + file + ", size=" + sizes[0] + ", exists=" + (exists ? "yes" : "no"));
			}
		} else if (input.startsWith("file write ")) {
			final String path = input.substring("file write ".length());
			final File file = new File(path);
			try (final FileInputStream in = new FileInputStream(file)) {
				final SteamUGCFileWriteStreamHandle remoteFile = remoteStorage.fileWriteStreamOpen(path);
				if (remoteFile != null) {
					final byte[] bytes = new byte[1024];
					int bytesRead;
					while ((bytesRead = in.read(bytes, 0, bytes.length)) > 0) {
						final ByteBuffer buffer = ByteBuffer.allocateDirect(bytesRead);
						buffer.put(bytes, 0, bytesRead);
						buffer.flip();
						remoteStorage.fileWriteStreamWriteChunk(remoteFile, buffer);
					}
					remoteStorage.fileWriteStreamClose(remoteFile);
				}
			} catch (final IOException e) {
				e.printStackTrace();
			}
		} else if (input.startsWith("file create")) {
			ugc.createItem(utils.getAppID(), SteamRemoteStorage.WorkshopFileType.Community);
		} else if (input.startsWith("file delete ")) {
			final String path = input.substring("file delete ".length());
			if (remoteStorage.fileDelete(path)) {
				System.out.println("deleted file '" + path + "'");
			}
		} else if (input.startsWith("file share ")) {
			remoteStorage.fileShare(input.substring("file share ".length()));
		} else if (input.startsWith("file publish ")) {
			final String[] paths = input.substring("file publish ".length()).split(" ");
			if (paths.length >= 2) {
				System.out.println("publishing file: " + paths[0] + ", preview file: " + paths[1]);
				remoteStorage.publishWorkshopFile(paths[0], paths[1], utils.getAppID(),
												  "Test UGC!", "Dummy UGC file published by test application.",
												  SteamRemoteStorage.PublishedFileVisibility.Private, null,
												  SteamRemoteStorage.WorkshopFileType.Community);
			}
		} else if (input.startsWith("file republish ")) {
			final String[] paths = input.substring("file republish ".length()).split(" ");
			if (paths.length >= 3) {
				System.out.println("republishing id: " + paths[0] + ", file: " + paths[1] + ", preview file: " + paths[2]);

				final SteamPublishedFileID fileID = new SteamPublishedFileID(Long.parseLong(paths[0]));

				final SteamPublishedFileUpdateHandle updateHandle = remoteStorage.createPublishedFileUpdateRequest(fileID);
				if (updateHandle != null) {
					remoteStorage.updatePublishedFileFile(updateHandle, paths[1]);
					remoteStorage.updatePublishedFilePreviewFile(updateHandle, paths[2]);
					remoteStorage.updatePublishedFileTitle(updateHandle, "Updated Test UGC!");
					remoteStorage.updatePublishedFileDescription(updateHandle, "Dummy UGC file *updated* by test application.");
					remoteStorage.commitPublishedFileUpdate(updateHandle);
				}
			}
		} else if (input.equals("ugc query")) {
			final SteamUGCQuery query = ugc.createQueryUserUGCRequest(user.getSteamID().getAccountID(), SteamUGC.UserUGCList.Subscribed,
																	  SteamUGC.MatchingUGCType.UsableInGame, SteamUGC.UserUGCListSortOrder.TitleAsc,
																	  utils.getAppID(), utils.getAppID(), 1);

			if (query.isValid()) {
				System.out.println("sending UGC query: " + query.toString());
				//ugc.setReturnTotalOnly(query, true);
				ugc.sendQueryUGCRequest(query);
			}
		} else if (input.startsWith("ugc download ")) {
			final String name = input.substring("ugc download ".length());
			final SteamUGCHandle handle = new SteamUGCHandle(Long.parseLong(name, 16));
			remoteStorage.ugcDownload(handle, 0);
		} else if (input.startsWith("ugc subscribe ")) {
			final long id = Long.parseLong(input.substring("ugc subscribe ".length()), 16);
			ugc.subscribeItem(new SteamPublishedFileID(id));
		} else if (input.startsWith("ugc unsubscribe ")) {
			final long id = Long.parseLong(input.substring("ugc unsubscribe ".length()), 16);
			ugc.unsubscribeItem(new SteamPublishedFileID(id));
		} else if (input.startsWith("ugc state ")) {
			final long id = Long.parseLong(input.substring("ugc state ".length()), 16);
			final Collection<SteamUGC.ItemState> itemStates = ugc.getItemState(new SteamPublishedFileID(id));
			System.out.println("UGC item states: " + itemStates.size());
			for (final SteamUGC.ItemState itemState : itemStates) {
				System.out.println("  " + itemState.name());
			}
		} else if (input.startsWith("ugc details ")) {
			System.out.println("requesting UGC details (deprecated API call)");
			final long id = Long.parseLong(input.substring("ugc details ".length()), 16);
			ugc.requestUGCDetails(new SteamPublishedFileID(id), 0);

			final SteamUGCQuery query = ugc.createQueryUGCDetailsRequest(new SteamPublishedFileID(id));
			if (query.isValid()) {
				System.out.println("sending UGC details query: " + query.toString());
				ugc.sendQueryUGCRequest(query);
			}
		} else if (input.startsWith("ugc info ")) {
			final long id = Long.parseLong(input.substring("ugc info ".length()), 16);
			final SteamUGC.ItemInstallInfo installInfo = new SteamUGC.ItemInstallInfo();
			if (ugc.getItemInstallInfo(new SteamPublishedFileID(id), installInfo)) {
				System.out.println("  folder: " + installInfo.getFolder());
				System.out.println("  size on disk: " + installInfo.getSizeOnDisk());
			}
			final SteamUGC.ItemDownloadInfo downloadInfo = new SteamUGC.ItemDownloadInfo();
			if (ugc.getItemDownloadInfo(new SteamPublishedFileID(id), downloadInfo)) {
				System.out.println("  bytes downloaded: " + downloadInfo.getBytesDownloaded());
				System.out.println("  bytes total: " + downloadInfo.getBytesTotal());
			}
		} else if (input.startsWith("leaderboard find ")) {
			final String name = input.substring("leaderboard find ".length());
			userStats.findLeaderboard(name);
		} else if (input.startsWith("leaderboard list ")) {
			final String[] params = input.substring("leaderboard list ".length()).split(" ");
			if (currentLeaderboard != null && params.length >= 2) {
				userStats.downloadLeaderboardEntries(currentLeaderboard,
						SteamUserStats.LeaderboardDataRequest.Global,
						Integer.parseInt(params[0]), Integer.parseInt(params[1]));
			}
		} else if (input.startsWith("leaderboard users ")) {
			final String[] params = input.substring("leaderboard users ".length()).split(" ");
			if (currentLeaderboard != null && params.length > 0) {
				final SteamID[] users = new SteamID[params.length];
				for (int i = 0; i < params.length; i++) {
					users[i] = SteamID.createFromNativeHandle(Long.parseLong(params[i]));
				}
				userStats.downloadLeaderboardEntriesForUsers(currentLeaderboard, users);
			}
		} else if (input.startsWith("leaderboard score ")) {
			final String score = input.substring("leaderboard score ".length());
			if (currentLeaderboard != null) {
				System.out.println("uploading score " + score + " to leaderboard " + currentLeaderboard.toString());
				userStats.uploadLeaderboardScore(currentLeaderboard,
						SteamUserStats.LeaderboardUploadScoreMethod.KeepBest, Integer.parseInt(score), new int[] {});
			}
		} else if (input.startsWith("apps subscribed ")) {
			final String appId = input.substring("apps subscribed ".length());
			final boolean subscribed = apps.isSubscribedApp(Integer.parseInt(appId));
			System.out.println("user described to app #" + appId + ": " + (subscribed ? "yes" : "no"));
		}
	}

	public static void main(final String[] arguments) {
		new SteamClientAPITest().clientMain(arguments);
	}

}
