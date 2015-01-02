package mw.ankara.punch;

import android.app.Application;

import mw.ankara.base.network.WebManager;
import mw.ankara.punch.database.PunchDb;

/**
 * @author MasaWong
 * @date 14/12/7.
 */
public class PunchApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        PunchDb.createInstance(this);
        WebManager.createInstance(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        PunchDb.destoryInstance();
        WebManager.destroyInstance();
    }
}
