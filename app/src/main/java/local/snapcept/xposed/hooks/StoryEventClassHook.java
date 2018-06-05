package local.snapcept.xposed.hooks;

import java.util.Locale;
import java.util.Set;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import local.snapcept.xposed.snapchat.SnapConstants;
import local.snapcept.xposed.snapchat.SnapInfo;
import local.snapcept.xposed.utils.LogUtils;

public class StoryEventClassHook extends XC_MethodHook {

    private final Set<String> processedIds;

    public StoryEventClassHook(Set<String> processedIds) {
        this.processedIds = processedIds;
    }

    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        Object storyEventObject = param.thisObject;
        Object encryptionObject = param.getResult();

        String snapId = (String) XposedHelpers.getObjectField(storyEventObject, SnapConstants.SNAP_EVENT_FIELD_ID);

        Object snapTypeObject = XposedHelpers.callMethod(storyEventObject, SnapConstants.SNAP_EVENT_METHOD_ORIGIN);
        String snapType = (String) XposedHelpers.callMethod(snapTypeObject, "toString");

        SnapInfo snapInfo = new SnapInfo(snapId, snapType);
        snapInfo.setUsername((String) XposedHelpers.callMethod(storyEventObject, SnapConstants.STORY_EVENT_METHOD_GET_USERNAME));
        snapInfo.setTimestamp(XposedHelpers.getLongField(storyEventObject, SnapConstants.STORY_EVENT_FIELD_TIMESTAMP));
        snapInfo.setVideo((boolean) XposedHelpers.callMethod(storyEventObject, SnapConstants.STORY_EVENT_IS_VIDEO));
        snapInfo.setZipped((boolean) XposedHelpers.getObjectField(storyEventObject, SnapConstants.STORY_EVENT_IS_ZIPPED_FIELD));

        if (snapInfo.getUsername() == null) {
            return;
        }

        if (!this.processedIds.contains(snapId)) {
            LogUtils.logTrace(String.format(Locale.ENGLISH, "Received story from %s, type=%s, id=%s, isVideo=%b, isZipped=%b, timestamp=%d",
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
