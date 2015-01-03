package mw.ankara.punch.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;

import mw.ankara.base.database.DbHelper;
import mw.ankara.punch.conversation.Conversation;

/**
 * @author MasaWong
 * @date 14/12/6.
 */
public class PunchDb extends DbHelper {

    private static final String DB_NAME = "mw.ankara.punch.db";
    private static final int DB_VERSION = 2;

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
        runCreationV1(db);
        runCreationV2(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1) {
            runCreationV2(db);
        }
    }

    protected void runCreationV1(SQLiteDatabase db) {
        create(db, new PunchRecord());
    }

    protected void runCreationV2(SQLiteDatabase db) {
        create(db, new Conversation());
    }
}
