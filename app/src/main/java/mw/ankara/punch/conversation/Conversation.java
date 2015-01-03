package mw.ankara.punch.conversation;

import android.content.ContentValues;
import android.database.Cursor;

import java.text.SimpleDateFormat;
import java.util.Date;

import mw.ankara.base.database.Map;
import mw.ankara.base.database.SQLitable;
import mw.ankara.punch.database.PunchDb;

/**
 * @author MasaWong
 * @date 14/12/31.
 */
public class Conversation implements SQLitable{

    public static final int ROBOT = 0;
    public static final int ME = 1;

    public static String translateTimeToDate(long time) {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm:ss");
        return format.format(new Date(time));
    }

    @Map(key = "role")
    public int role;

    @Map(key = "time")
    public long time;

    @Map(key = "date")
    public String date;

    @Map(key = "content")
    public CharSequence content;

    public Conversation() {
    }

    public Conversation(int role, CharSequence content) {
        this(role, System.currentTimeMillis(), content);
    }

    public Conversation(int role, long time, CharSequence content) {
        this.role = role;
        this.time = time;
        this.date = translateTimeToDate(time);
        this.content = content;
    }

    public void save() {
        PunchDb database = PunchDb.getInstance();
        if (database.update(this, "time=?", new String[]{String.valueOf(time)}) == 0) {
            database.insert(this);
        }
    }

    @Override
    public void readFromSQLite(Cursor cursor) {
        role = cursor.getInt(0);
        time = cursor.getLong(1);
        date = cursor.getString(2);
        content = cursor.getString(3);
    }

    @Override
    public ContentValues writeToSQLite() {
        ContentValues pairs = new ContentValues();
        pairs.put("role", role);
        pairs.put("time", time);
        pairs.put("date", date);
        pairs.put("content", String.valueOf(content));
        return pairs;
    }

    @Override
    public String getSQLiteCreation() {
        return "create table if not exists " + getClass().getSimpleName()
                + " (role integer, time integer, date text, content text)";
    }
}
