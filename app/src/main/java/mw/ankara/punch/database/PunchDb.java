package mw.ankara.punch.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;

import mw.ankara.base.database.DbHelper;

/**
 * @author MasaWong
 * @date 14/12/6.
 */
public class PunchDb extends DbHelper {

    private static final String DB_NAME = "mw.ankara.punch.db";
    private static final int DB_VERSION = 1;

    private static PunchDb sInstance;

    public static PunchDb createInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PunchDb(context.getApplicationContext());
        }
        return sInstance;
    }

    public static void destoryInstance() {
        sInstance = null;
    }

    public static PunchDb getInstance() {
        return sInstance;
    }

    protected PunchDb(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    protected PunchDb(Context context, DatabaseErrorHandler errorHandler) {
        super(context, DB_NAME, null, DB_VERSION, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        create(db, new PunchRecord());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
