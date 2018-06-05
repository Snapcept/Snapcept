package local.snapcept.xposed.config;

import com.crossbowffs.remotepreferences.RemotePreferenceProvider;

public class MyPreferenceProvider extends RemotePreferenceProvider {

    public MyPreferenceProvider() {
        super(SnapceptSettings.AUTHORITY, new String[] {SnapceptSettings.PREF_NAME});
    }

}
