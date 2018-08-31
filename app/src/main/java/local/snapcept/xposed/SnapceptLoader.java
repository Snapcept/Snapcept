package local.snapcept.xposed;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.net.Uri;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import local.snapcept.xposed.config.SnapceptSettings;
import local.snapcept.xposed.hooks.CbcEncryptionAlgorithmClassHook;
import local.snapcept.xposed.hooks.RootDetectorOverrides;
import local.snapcept.xposed.hooks.RootDetectorStringOverrides;
import local.snapcept.xposed.hooks.SnapEventClassHook;
import local.snapcept.xposed.hooks.StoryEventClassHook;
import local.snapcept.xposed.hooks.StoryVideoDecryptorClassHook;
import local.snapcept.xposed.snapchat.SnapConstants;
import local.snapcept.xposed.utils.LogUtils;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;

public class SnapceptLoader implements IXposedHookLoadPackage {

    private final Set<String> processedIds;

    private Context context;

    private SnapceptSettings settings;

    private Timer cleanTimer;

    public SnapceptLoader() {
        this.processedIds = new HashSet<>();
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        // Check if we are hooking on the correct package.
        if (!lpparam.packageName.equals(SnapConstants.PACKAGE_NAME)) {
            return;
        }

        // Skip if not first.
        if (!lpparam.isFirstApplication) {
            return;
        }

        // Initialize values used by the module.
        initialize();

        // Check if the correct version is used.
        if (!isVersionCorrect()) {
            LogUtils.logTrace("Wrong Snapchat version. Ensure version " + SnapConstants.PACKAGE_VERSION_STRING + " is installed.");
            return;
        }

        // Announce correct version.
        LogUtils.logTrace("Correct Snapchat version, have fun.");

        // Initialize more values used by the module.
        initializePost();

        // Clear processedIds sometimes.
        initializeTimer();

        // Hook root detectors.
        hookRootDetectors(lpparam.classLoader);

        // Hook snap event.
        hookSnapEventClass(lpparam.classLoader);

        // Hook story event.
        hookStoryEventClass(lpparam.classLoader);

        // Hook encryption.
        hookCbcEncryptionAlgorithmClass(lpparam.classLoader);

        // Hook video story decryptor.
        hookStoryVideoDecryptor(lpparam.classLoader);

        // Just to be sure.
        this.settings.reload();
    }

    private void initialize() {
        Object activityThread = XposedHelpers.callStaticMethod(findClass("android.app.ActivityThread", null), "currentActivityThread");
        context = (Context) XposedHelpers.callMethod(activityThread, "getSystemContext");
    }

    private void initializePost() {
        settings = new SnapceptSettings(context);
    }

    private void initializeTimer() {
        if (cleanTimer != null) {
            return;
        }

        cleanTimer = new Timer();
        cleanTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Wipe processedIds sometimes so we don't fill up memory.
                processedIds.clear();
            }
        }, 1000 * 60 * 15, 1000 * 60 * 15);
    }

    private boolean isVersionCorrect() throws Throwable {
        PackageInfo snapchatPackage = context.getPackageManager().getPackageInfo(SnapConstants.PACKAGE_NAME, 0);
        return snapchatPackage.versionCode == SnapConstants.PACKAGE_VERSION;
    }

    private void hookRootDetectors(ClassLoader loader) {
        // Snapchat
        findAndHookMethod(SnapConstants.ROOT_DETECTOR_CLASS, loader, SnapConstants.ROOT_DETECTOR_FIRST, new RootDetectorOverrides());
        findAndHookMethod(SnapConstants.ROOT_DETECTOR_CLASS, loader, SnapConstants.ROOT_DETECTOR_SECOND, new RootDetectorOverrides());
        findAndHookMethod(SnapConstants.ROOT_DETECTOR_CLASS, loader, SnapConstants.ROOT_DETECTOR_THIRD, new RootDetectorOverrides());
        findAndHookMethod(SnapConstants.ROOT_DETECTOR_CLASS, loader, SnapConstants.ROOT_DETECTOR_FORTH, new RootDetectorOverrides());

        // Crashlytics
        findAndHookMethod(SnapConstants.ROOT_DETECTOR_TWO_CLASS, loader, SnapConstants.ROOT_DETECTOR_TWO_FIRST, Context.class, new RootDetectorOverrides());

        // Braintree
        findAndHookMethod(SnapConstants.ROOT_DETECTOR_THREE_CLASS, loader, SnapConstants.ROOT_DETECTOR_THREE_FIRST, new RootDetectorStringOverrides());
    }

    private void hookSnapEventClass(ClassLoader loader) {
        findAndHookMethod(
                SnapConstants.SNAP_PROCESSING_CLASS,
                loader,
                SnapConstants.SNAP_PROCESSING_HANDLE_METHOD,
                SnapConstants.SNAP_EVENT_CLASS,
                String.class,
                new SnapEventClassHook(this.processedIds));
    }

    private void hookStoryEventClass(ClassLoader loader) {
        findAndHookMethod(
                SnapConstants.STORY_EVENT_CLASS,
                loader,
                SnapConstants.STORY_EVENT_METHOD_GET_ENCRYPTION_ALGORITHM,
                new StoryEventClassHook(this.processedIds));
    }

    private void hookCbcEncryptionAlgorithmClass(ClassLoader loader) {
        findAndHookMethod(
                SnapConstants.CBC_ENCRYPTION_ALGORITHM_CLASS,
                loader,
                SnapConstants.CBC_ENCRYPTION_ALGORITHM_DECRYPT,
                InputStream.class,
                new CbcEncryptionAlgorithmClassHook(this.settings, this.context));
    }

    private void hookStoryVideoDecryptor(ClassLoader loader) {
        findAndHookMethod(
                SnapConstants.SNAP_VIDEO_DECRYPTOR_CLASS,
                loader,
                SnapConstants.SNAP_VIDEO_DECRYPTOR_METHOD_DECRYPT,
                String.class,
                InputStream.class,
                SnapConstants.ENCRYPTION_ALGORITHM_INTERFACE,
                boolean.class,
                boolean.class,
                boolean.class,
                Uri.class,
                boolean.class,
                boolean.class,
                boolean.class,
                new StoryVideoDecryptorClassHook(this.settings, this.context));
    }

}
