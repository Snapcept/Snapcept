package local.snapcept.xposed.utils;

import de.robv.android.xposed.XposedBridge;

public class LogUtils {

    public static void logTrace(String text) {
        log("TRACE", text);
    }

    public static void logInfo(String text) {
        log("INFO", text);
    }

    public static void logWarn(String text) {
        log("WARN", text);
    }

    public static void logError(String text) {
        log("ERROR", text);
    }

    private static void log(String type, String text) {
        XposedBridge.log(String.format("[Snapcept] %5s %s", type, text));
    }

}
