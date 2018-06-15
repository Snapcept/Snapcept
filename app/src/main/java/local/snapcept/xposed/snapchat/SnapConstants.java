package local.snapcept.xposed.snapchat;

public class SnapConstants {

    // Snapchat app related constants, all found in "AndroidManifest.xml".

    public static final String PACKAGE_NAME = "com.snapchat.android";

    public static final int PACKAGE_VERSION = 1735;

    public static final String PACKAGE_VERSION_STRING = "10.34.5.0";

    // Snap received, everything is in the same class.

    public static final String SNAP_EVENT_CLASS = "xnm";

    public static final String SNAP_EVENT_FIELD_ID = "u";

    public static final String SNAP_EVENT_IS_VIDEO = "ar_";

    public static final String SNAP_EVENT_USERNAME_FIELD = "aE";

    public static final String SNAP_EVENT_TIMESTAMP_FIELD = "y";

    public static final String SNAP_EVENT_IS_ZIPPED_FIELD = "aF";

    public static final String SNAP_EVENT_METHOD_ORIGIN = "u";

    // Story received, everything is in the same class.

    public static final String STORY_EVENT_CLASS = "xer";

    public static final String STORY_EVENT_IS_VIDEO = SNAP_EVENT_IS_VIDEO;

    public static final String STORY_EVENT_IS_ZIPPED_FIELD = SNAP_EVENT_IS_ZIPPED_FIELD;

    public static final String STORY_EVENT_METHOD_GET_ENCRYPTION_ALGORITHM = "at";

    public static final String STORY_EVENT_METHOD_GET_USERNAME = "ay";

    public static final String STORY_EVENT_FIELD_TIMESTAMP = "v";

    // Snap video decryptor, everything is in the same class.

    public static final String SNAP_VIDEO_DECRYPTOR_CLASS = "wpb";

    public static final String SNAP_VIDEO_DECRYPTOR_METHOD_DECRYPT = "a";

    // Media cache entry, everything is in the same class.

    public static final String MEDIA_CACHE_ENTRY_CLASS = "xsb";

    public static final String MEDIA_CACHE_ENTRY_FIELD_ENCRYPTION_ALGORITHM = "c";

    // Snap received processing, everything is in the same class.

    public static final String SNAP_PROCESSING_CLASS = "spz";

    public static final String SNAP_PROCESSING_HANDLE_METHOD = "a";

    // Encryption.

    public static final String UNENCRYPTED_ALGORITHM_INTERFACE = "UnencryptedEncryptionAlgorithm";

    public static final String CBC_ENCRYPTION_ALGORITHM_CLASS = "com.snapchat.android.framework.crypto.CbcEncryptionAlgorithm";

    public static final String CBC_ENCRYPTION_ALGORITHM_DECRYPT = "b";

    public static final String ENCRYPTION_ALGORITHM_INTERFACE = "com.snapchat.android.framework.crypto.EncryptionAlgorithm";

    // Root detectors, everything is in the same class.

    public static final String ROOT_DETECTOR_CLASS = "zfw";

    public static final String ROOT_DETECTOR_FIRST = "b";

    public static final String ROOT_DETECTOR_SECOND = "c";

    public static final String ROOT_DETECTOR_THIRD = "d";

    public static final String ROOT_DETECTOR_FORTH = "e";

    public static final String ROOT_DETECTOR_TWO_CLASS = "aizu";

    public static final String ROOT_DETECTOR_TWO_FIRST = "f";

    public static final String ROOT_DETECTOR_THREE_CLASS = "ui";

    public static final String ROOT_DETECTOR_THREE_FIRST = "a";

    // Additional info fields

    public static final String ADDITIONAL_FIELD_ENCRYPTION_ALGORITHM_SNAP_INFO = "SnapInfo";

}
