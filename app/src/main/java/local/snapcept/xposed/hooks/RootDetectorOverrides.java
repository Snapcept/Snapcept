package local.snapcept.xposed.hooks;

import de.robv.android.xposed.XC_MethodReplacement;

public class RootDetectorOverrides extends XC_MethodReplacement {

    @Override
    protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
        return false;
    }

}
