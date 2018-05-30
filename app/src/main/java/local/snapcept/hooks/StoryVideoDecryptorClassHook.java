package local.snapcept.hooks;

import android.content.Context;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.BufferedInputStream;
import java.io.InputStream;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import local.snapcept.SnapceptConstants;
import local.snapcept.SnapceptSaver;
import local.snapcept.snapchat.SnapInfo;

public class StoryVideoDecryptorClassHook extends XC_MethodHook {

    private final Context context;

    public StoryVideoDecryptorClassHook(Context context) {
        this.context = context;
    }

    @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
        InputStream inputStream = (InputStream) param.args[1];
        Object encryptionAlgorithm = param.args[2];
        boolean z = (boolean) param.args[3];
        boolean z6 = (boolean) param.args[9];

        // Find possible attached object.
        SnapInfo snapInfo = (SnapInfo) XposedHelpers.getAdditionalInstanceField(
                encryptionAlgorithm,
                SnapceptConstants.ADDITIONAL_FIELD_ENCRYPTION_ALGORITHM_SNAP_INFO);

        if (snapInfo == null) {
            return;
        }

        if (!snapInfo.getType().equals("STORIES") || !snapInfo.isVideo()) {
            return;
        }

        // Check if saved so we can possibly skip.
        SnapceptSaver saver = new SnapceptSaver(this.context, z, snapInfo);

        if (saver.isSaved()) {
            // Let the original call run.
            return;
        }

        // Copy inputStream for the original call.
        ByteArrayOutputStream bufferStream = new ByteArrayOutputStream();
        bufferStream.write(inputStream);
        IOUtils.closeQuietly(inputStream);
        inputStream = bufferStream.toInputStream();

        // Reimplementation of the decrypt method.
        InputStream decryptedStream;

        if (z6 || encryptionAlgorithm == null || encryptionAlgorithm.getClass().getCanonicalName().contains(SnapceptConstants.UNENCRYPTED_ALGORITHM_INTERFACE)) {
            decryptedStream = inputStream;
        } else {
            decryptedStream = (InputStream) XposedHelpers.callMethod(encryptionAlgorithm, SnapceptConstants.CBC_ENCRYPTION_ALGORITHM_DECRYPT, inputStream);
        }

        // decryptedStream is closed quietly.
        saver.save(decryptedStream);

        // Do the original call with the original data.
        param.setResult(
                XposedHelpers.callMethod(
                        param.thisObject,
                        SnapceptConstants.SNAP_VIDEO_DECRYPTOR_METHOD_DECRYPT,
                        param.args[0],
                        new BufferedInputStream(bufferStream.toInputStream()),
                        param.args[2],
                        param.args[3],
                        param.args[4],
                        param.args[5],
                        param.args[6],
                        param.args[7],
                        param.args[8],
                        param.args[9]));
    }

}
