package local.snapcept.hooks;

import de.robv.android.xposed.XC_MethodReplacement;

public class RootDetectorOverrides extends XC_MethodReplacement {

    @Override
    protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
        return false;
    }

}
