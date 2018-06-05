-allowaccessmodification
-repackageclasses

## XPOSED
-keep class local.snapcept.xposed.SnapceptLoader{*;}
-keepnames class local.snapcept.xposed.SnapceptLoader
-keep class de.robv.android.xposed.**{*;}
-keepnames class de.robv.android.xposed.**