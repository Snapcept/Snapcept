package local.snapcept.snapchat;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import local.snapcept.utils.FileUtils;
import local.snapcept.utils.LogUtils;

public class SnapInfo {

    private final String id;

    // UNKNOWN = Snap
    // STORIES = Stories
    // SEARCH = Discovery shit
    private final String type;

    private String username;

    private long timestamp;

    private boolean video;

    private boolean zipped;

    public SnapInfo(String id, String type) {
        this.id = id;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public boolean isZipped() {
        return zipped;
    }

    public void setZipped(boolean zipped) {
        this.zipped = zipped;
    }

    // Related to saving the snap.

    public String getFileName() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS", Locale.getDefault());
        StringBuilder fileNameBuilder = new StringBuilder();

        fileNameBuilder.append(username);
        fileNameBuilder.append("_");
        fileNameBuilder.append(dateFormat.format(new Date(timestamp)));
        fileNameBuilder.append(video ? ".mp4" : ".jpg");

        return fileNameBuilder.toString();
    }

    public File getFilePath() {
        String snapTypeStr = type.toLowerCase();

        switch (snapTypeStr) {
            case "unknown":
                snapTypeStr = "snaps";
                break;
            case "search":
                snapTypeStr = "discovery";
                break;
            case "stories":
                break;
            default:
                snapTypeStr = "unknown";
        }

        String baseDir = FileUtils.getFileBasePath().getAbsolutePath();
        String path = baseDir + File.separator + snapTypeStr + File.separator + username;

        File filePath = new File(path);
        if (!filePath.mkdirs() && !filePath.exists()) {
            LogUtils.logWarn(String.format("Base path for category '%s' was not created.", snapTypeStr));
        }

        return filePath;
    }

}
