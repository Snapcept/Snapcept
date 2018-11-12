package local.snapcept.xposed.config;

import android.content.Context;
import android.content.SharedPreferences;

import com.crossbowffs.remotepreferences.RemotePreferences;

public class SnapceptSettings {

    public static final String AUTHORITY = "local.snapcept.preferences";

    public static final String PREF_NAME = "local.snapcept_preferences";

    private final SharedPreferences.OnSharedPreferenceChangeListener changeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            switch (key) {
                case "block_screen_detect":
                    blockScreenshotDetection = sharedPreferences.getBoolean("block_screen_detect", true);
                    break;
                case "snaps_enable":
                    snapsEnable = sharedPreferences.getBoolean("snaps_enable", true);
                    break;
                case "snaps_save_image":
                    snapsSaveImage = sharedPreferences.getBoolean("snaps_save_image", true);
                    break;
                case "snaps_save_video":
                    snapsSaveVideo = sharedPreferences.getBoolean("snaps_save_video", true);
                    break;
                case "snaps_save_video_overlay":
                    snapsSaveVideoOverlay = sharedPreferences.getBoolean("snaps_save_video_overlay", true);
                    break;
                case "stories_enable":
                    storiesEnable = sharedPreferences.getBoolean("stories_enable", true);
                    break;
                case "stories_save_image":
                    storiesSaveImage = sharedPreferences.getBoolean("stories_save_image", true);
                    break;
                case "stories_save_video":
                    storiesSaveVideo = sharedPreferences.getBoolean("stories_save_video", true);
                    break;
            }
        }
    };

    private final RemotePreferences preferences;

    private boolean blockScreenshotDetection;

    private boolean snapsEnable;

    private boolean snapsSaveImage;

    private boolean snapsSaveVideo;

    private boolean snapsSaveVideoOverlay;

    private boolean storiesEnable;

    private boolean storiesSaveImage;

    private boolean storiesSaveVideo;

    public SnapceptSettings(Context context) {
        this.preferences = new RemotePreferences(context, AUTHORITY, PREF_NAME);
        this.preferences.registerOnSharedPreferenceChangeListener(changeListener);
        this.reload();
    }

    public void reload() {
        blockScreenshotDetection = this.preferences.getBoolean("block_screen_detect", true);

        snapsEnable = this.preferences.getBoolean("snaps_enable", true);
        snapsSaveImage = this.preferences.getBoolean("snaps_save_image", true);
        snapsSaveVideo = this.preferences.getBoolean("snaps_save_video", true);
        snapsSaveVideoOverlay = this.preferences.getBoolean("snaps_save_video_overlay", true);

        storiesEnable = this.preferences.getBoolean("stories_enable", true);
        storiesSaveImage = this.preferences.getBoolean("stories_save_image", true);
        storiesSaveVideo = this.preferences.getBoolean("stories_save_video", true);
    }

    public boolean isBlockScreenshotDetection() {
        return blockScreenshotDetection;
    }

    public boolean isSnapsEnable() {
        return snapsEnable;
    }

    public boolean isSnapsSaveImage() {
        return snapsSaveImage;
    }

    public boolean isSnapsSaveVideo() {
        return snapsSaveVideo;
    }

    public boolean isSnapsSaveVideoOverlay() {
        return snapsSaveVideoOverlay;
    }

    public boolean isStoriesEnable() {
        return storiesEnable;
    }

    public boolean isStoriesSaveImage() {
        return storiesSaveImage;
    }

    public boolean isStoriesSaveVideo() {
        return storiesSaveVideo;
    }

}
