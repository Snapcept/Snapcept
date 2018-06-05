package local.snapcept.xposed.hooks;

import java.util.Locale;
import java.util.Set;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import local.snapcept.xposed.snapchat.SnapConstants;
import local.snapcept.xposed.snapchat.SnapInfo;
import local.snapcept.xposed.utils.LogUtils;

public class SnapEventClassHook extends XC_MethodHook {

    private final Set<String> processedIds;

    public SnapEventClassHook(Set<String> processedIds) {
        this.processedIds = processedIds;
    }

    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        Object snapEventObject = param.args[0];
        Object encryptionObject = XposedHelpers.getObjectField(param.getResult(), SnapConstants.MEDIA_CACHE_ENTRY_FIELD_ENCRYPTION_ALGORITHM);

        String snapId = (String) XposedHelpers.getObjectField(snapEventObject, SnapConstants.SNAP_EVENT_FIELD_ID);
        SnapInfo snapInfo;

        Object snapTypeObject = XposedHelpers.callMethod(snapEventObject, SnapConstants.SNAP_EVENT_METHOD_ORIGIN);
        String snapType = (String) XposedHelpers.callMethod(snapTypeObject, "toString");

        snapInfo = new SnapInfo(snapId, snapType);
        snapInfo.setUsername((String) XposedHelpers.getObjectField(snapEventObject, SnapConstants.SNAP_EVENT_USERNAME_FIELD));
        snapInfo.setTimestamp(XposedHelpers.getLongField(snapEventObject, SnapConstants.SNAP_EVENT_TIMESTAMP_FIELD));
        snapInfo.setVideo((boolean) XposedHelpers.callMethod(snapEventObject, SnapConstants.SNAP_EVENT_IS_VIDEO));
        snapInfo.setZipped((boolean) XposedHelpers.getObjectField(snapEventObject, SnapConstants.SNAP_EVENT_IS_ZIPPED_FIELD));

        if (snapInfo.getUsername() == null) {
            return;
        }

        if (!this.processedIds.contains(snapId)) {
            LogUtils.logTrace(String.format(Locale.ENGLISH, "Received Snap from %s, type=%s, id=%s, isVideo=%b, isZipped=%b, timestamp=%d",
                    snapInfo.getUsername(),
                    snapType,
                    snapInfo.getId(),
                    snapInfo.isVideo(),
                    snapInfo.isZipped(),
                    snapInfo.getTimestamp()));

            this.processedIds.add(snapId);
        }

        XposedHelpers.setAdditionalInstanceField(
                encryptionObject,
                SnapConstants.ADDITIONAL_FIELD_ENCRYPTION_ALGORITHM_SNAP_INFO,
                snapInfo);
    }

}
