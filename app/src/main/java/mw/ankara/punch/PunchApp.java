package mw.ankara.punch;

import android.app.Application;

import mw.ankara.punch.database.PunchDb;

/**
 * @author MasaWong
 * @date 14/12/7.
 */
public class PunchApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        PunchDb.createInstance(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        PunchDb.destoryInstance();
    }
}
