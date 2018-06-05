package local.snapcept.xposed.config;

import local.snapcept.xposed.snapchat.SnapInfo;

public class SnapceptSettingsUtil {

    public static boolean maySave(SnapceptSettings settings, SnapInfo snapInfo) {
        // Check if we have settings enabled to save this snap.
        // Overlay is handled in the SnapceptSaver class.

        if (snapInfo.getType().equals("UNKNOWN")) {
            // Snaps are disabled.
            if (!settings.isSnapsEnable()) {
                return false;
            }

            // Images are disabled.
            if (!settings.isSnapsSaveImage() && !snapInfo.isVideo()) {
                return false;
            }

            // Videos are disabled.
            if (!settings.isSnapsSaveVideo() && snapInfo.isVideo()) {
                return false;
            }
        } else if (snapInfo.getType().equals("STORIES")) {
            // Stories are disabled.
            if (!settings.isStoriesEnable()) {
                return false;
            }

            // Images are disabled.
            if (!settings.isStoriesSaveImage() && !snapInfo.isVideo()) {
                return false;
            }

            // Videos are disabled.
            if (!settings.isStoriesSaveVideo() && snapInfo.isVideo()) {
                return false;
            }
        }

        return true;
    }

}
