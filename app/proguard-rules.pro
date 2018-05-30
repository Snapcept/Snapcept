-allowaccessmodification
-repackageclasses

## XPOSED
-keep class local.snapcept.SnapceptLoader{*;}
-keepnames class local.snapcept.SnapceptLoader
-keep class de.robv.android.xposed.**{*;}
-keepnames class de.robv.android.xposed.**