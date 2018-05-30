package local.snapcept;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.net.Uri;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import local.snapcept.hooks.CbcEncryptionAlgorithmClassHook;
import local.snapcept.hooks.RootDetectorOverrides;
import local.snapcept.hooks.SnapEventClassHook;
import local.snapcept.hooks.StoryEventClassHook;
import local.snapcept.hooks.StoryVideoDecryptorClassHook;
import local.snapcept.utils.LogUtils;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;

public class SnapceptLoader implements IXposedHookLoadPackage {

    private final Set<String> processedIds;

    private Context context;

    public SnapceptLoader() {
        this.processedIds = new HashSet<>();
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        // Check if we are hooking on the correct package.
        if (!lpparam.packageName.equals(SnapceptConstants.PACKAGE_NAME)) {
            return;
        }

        // Initialize values used by the module.
        initialize();

        // Check if the correct version is used.
        if (!isVersionCorrect()) {
            LogUtils.logTrace("Wrong Snapchat version. Ensure version " + SnapceptConstants.PACKAGE_VERSION_STRING + " is installed.");
            return;
        }

        // Announce correct version.
        LogUtils.logTrace("Correct Snapchat version, have fun.");

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
    }

    private void initialize() {
        Object activityThread = XposedHelpers.callStaticMethod(findClass("android.app.ActivityThread", null), "currentActivityThread");
        context = (Context) XposedHelpers.callMethod(activityThread, "getSystemContext");
    }

    private boolean isVersionCorrect() throws Throwable {
        PackageInfo snapchatPackage = context.getPackageManager().getPackageInfo(SnapceptConstants.PACKAGE_NAME, 0);
        return snapchatPackage.versionCode == SnapceptConstants.PACKAGE_VERSION;
    }

    private void hookRootDetectors(ClassLoader loader) {
        findAndHookMethod(SnapceptConstants.ROOT_DETECTOR_CLASS, loader, SnapceptConstants.ROOT_DETECTOR_FIRST, new RootDetectorOverrides());
        findAndHookMethod(SnapceptConstants.ROOT_DETECTOR_CLASS, loader, SnapceptConstants.ROOT_DETECTOR_SECOND, new RootDetectorOverrides());
        findAndHookMethod(SnapceptConstants.ROOT_DETECTOR_CLASS, loader, SnapceptConstants.ROOT_DETECTOR_THIRD, new RootDetectorOverrides());
        findAndHookMethod(SnapceptConstants.ROOT_DETECTOR_CLASS, loader, SnapceptConstants.ROOT_DETECTOR_FORTH, new RootDetectorOverrides());
    }

    private void hookSnapEventClass(ClassLoader loader) {
        findAndHookMethod(
                SnapceptConstants.SNAP_PROCESSING_CLASS,
                loader,
                SnapceptConstants.SNAP_PROCESSING_HANDLE_METHOD,
                SnapceptConstants.SNAP_EVENT_CLASS,
                String.class,
                new SnapEventClassHook(this.processedIds));
    }

    private void hookStoryEventClass(ClassLoader loader) {
        findAndHookMethod(
                SnapceptConstants.STORY_EVENT_CLASS,
                loader,
                SnapceptConstants.STORY_EVENT_METHOD_GET_ENCRYPTION_ALGORITHM,
                new StoryEventClassHook(this.processedIds));
    }

    private void hookCbcEncryptionAlgorithmClass(ClassLoader loader) {
        findAndHookMethod(
                SnapceptConstants.CBC_ENCRYPTION_ALGORITHM_CLASS,
                loader,
                SnapceptConstants.CBC_ENCRYPTION_ALGORITHM_DECRYPT,
                InputStream.class,
                new CbcEncryptionAlgorithmClassHook(this.context));
    }

    private void hookStoryVideoDecryptor(ClassLoader loader) {
        findAndHookMethod(
                SnapceptConstants.SNAP_VIDEO_DECRYPTOR_CLASS,
                loader,
                SnapceptConstants.SNAP_VIDEO_DECRYPTOR_METHOD_DECRYPT,
                String.class,
                InputStream.class,
                SnapceptConstants.ENCRYPTION_ALGORITHM_INTERFACE,
                boolean.class,
                boolean.class,
                boolean.class,
                Uri.class,
                boolean.class,
                boolean.class,
                boolean.class,
                new StoryVideoDecryptorClassHook(this.context));
    }

}
