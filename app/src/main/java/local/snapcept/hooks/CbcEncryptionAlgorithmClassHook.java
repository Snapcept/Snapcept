package local.snapcept.hooks;

import android.content.Context;

import java.io.InputStream;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import local.snapcept.SnapceptConstants;
import local.snapcept.SnapceptSaver;
import local.snapcept.snapchat.SnapInfo;

public class CbcEncryptionAlgorithmClassHook extends XC_MethodHook {

    private final Context context;

    public CbcEncryptionAlgorithmClassHook(Context context) {
        this.context = context;
    }

    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        // Check if the stream is valid.
        InputStream realStream = (InputStream) param.getResult();

        if (realStream == null) {
            return;
        }

        // Check if a SnapInfo object is attached.
        SnapInfo snapInfo = (SnapInfo) XposedHelpers.getAdditionalInstanceField(
                param.thisObject,
                SnapceptConstants.ADDITIONAL_FIELD_ENCRYPTION_ALGORITHM_SNAP_INFO);

        if (snapInfo == null) {
            return;
        }

        if (snapInfo.getType().equals("UNKNOWN") || (snapInfo.getType().equals("STORIES") && !snapInfo.isVideo())) {
            // Save it.
            SnapceptSaver saver = new SnapceptSaver(context, snapInfo);

            if (!saver.isSaved()) {
                param.setResult(saver.save(realStream));
            }
        }
    }

}
