package mw.ankara.punch.database;

import mw.ankara.base.database.MapKey;
import mw.ankara.base.database.SQLiteRecord;

/**
 * @author MasaWong
 * @date 14/12/6.
 */
public class PunchRecord extends SQLiteRecord {
    @MapKey(key = "hours")
    public int hours;

    @MapKey(key = "time")
    public String time;

    public PunchRecord() {
    }

    public PunchRecord(int hours) {
        this.hours = hours;
    }
}
