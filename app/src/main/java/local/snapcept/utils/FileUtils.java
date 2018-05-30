package local.snapcept.utils;

import android.content.Context;
import android.os.Environment;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {

    public static File getFileBasePath() {
        String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        String path = baseDir + File.separator + "Snapcept";

        File filePath = new File(path);
        if (!filePath.mkdirs() && !filePath.exists()) {
            LogUtils.logWarn("Base path was not created.");
        }

        return filePath;
    }

    public static void copyInputStreamToFile(InputStream stream, File file) {
        OutputStream out = null;

        try {
            out = new FileOutputStream(file);
            IOUtils.copy(stream, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

}
