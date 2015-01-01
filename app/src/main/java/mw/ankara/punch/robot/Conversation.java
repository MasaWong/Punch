package mw.ankara.punch.robot;

import java.text.SimpleDateFormat;
import java.util.Date;

import mw.ankara.base.database.Map;
import mw.ankara.base.database.SQLiteRecord;

/**
 * @author MasaWong
 * @date 14/12/31.
 */
public class Conversation extends SQLiteRecord{

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
    public String content;
}
