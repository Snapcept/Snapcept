package local.snapcept.xposed;

import android.content.Context;
import android.media.MediaScannerConnection;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import local.snapcept.xposed.config.SnapceptSettings;
import local.snapcept.xposed.snapchat.SnapInfo;
import local.snapcept.xposed.utils.FileUtils;

public class SnapceptSaver {

    private final SnapceptSettings settings;

    private final Context context;

    private final SnapInfo snapInfo;

    private final boolean isZipped;

    private final File file;

    public SnapceptSaver(SnapceptSettings settings, Context context, SnapInfo snapInfo) {
        this.settings = settings;
        this.context = context;
        this.snapInfo = snapInfo;
        this.isZipped = snapInfo.isZipped();
        this.file = new File(snapInfo.getFilePath(), snapInfo.getFileName());
    }

    public SnapceptSaver(SnapceptSettings settings, Context context, boolean isZipped, SnapInfo snapInfo) {
        this.settings = settings;
        this.context = context;
        this.snapInfo = snapInfo;
        this.isZipped = isZipped;
        this.file = new File(snapInfo.getFilePath(), snapInfo.getFileName());
    }

    /**
     * Saves the SnapInfo object and returns a new InputStream.
     */
    public InputStream save(InputStream realStream) throws IOException {
        if (isZipped) {
            return saveZipStream(realStream);
        } else {
            return saveStream(realStream);
        }
    }

    public boolean isSaved() {
        return this.file.exists();
    }

    private File createFile() throws IOException {
        MediaScannerConnection.scanFile(
                context,
                new String[] { this.file.getPath() },
                new String[]{ "image/jpeg" },
                null);

        if (this.file.createNewFile()) {
            return this.file;
        }

        return null;
    }

    private InputStream saveZipStream(InputStream realStream) throws IOException {
        File mediaFile = createFile();

        if (mediaFile != null) {
            // We need to clone the InputStream to memory temporarily so that we can pass on the original.
            ByteArrayOutputStream bufferStream = new ByteArrayOutputStream();
            bufferStream.write(realStream);
            IOUtils.closeQuietly(realStream);

            // Create a new input stream from the buffer.
            InputStream fakeStream = bufferStream.toInputStream();

            // Parse stuff from the zip.
            ZipInputStream zipStream = new ZipInputStream(fakeStream);

            while (true) {
                ZipEntry entry = zipStream.getNextEntry();

                if (entry == null) {
                    break;
                }

                String name = entry.getName();

                if (name.startsWith("media")) {
                    FileUtils.copyInputStreamToFile(zipStream, mediaFile);
                } else if (name.startsWith("overlay")) {

                    // Disable if overlays are disabled for video snaps.
                    if (!this.settings.isSnapsSaveVideoOverlay() && snapInfo.isVideo()) {
                        continue;
                    }

                    String originalPath = mediaFile.getAbsolutePath();
                    String overlayFilePath = originalPath.substring(0, originalPath.length() - 4) + "_overlay.jpg";

                    File overlayFile = new File(overlayFilePath);
                    FileUtils.copyInputStreamToFile(zipStream, overlayFile);
                }
            }

            // Clean up.
            IOUtils.closeQuietly(fakeStream);

            // Return new input stream.
            return bufferStream.toInputStream();
        }

        return realStream;
    }

    private InputStream saveStream(InputStream realStream) throws IOException {
        File mediaFile = createFile();

        if (mediaFile != null) {
            FileUtils.copyInputStreamToFile(realStream, mediaFile);
            IOUtils.closeQuietly(realStream);

            return new BufferedInputStream(new FileInputStream(mediaFile));
        }

        return realStream;
    }
}
