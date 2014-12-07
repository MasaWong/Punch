package mw.ankara.punch.database;

import mw.ankara.base.database.MapKey;
import mw.ankara.base.database.SQLiteRecord;

/**
 * @author MasaWong
 * @date 14/12/6.
 */
public class PunchRecord extends SQLiteRecord {

    private static final int HALF_AN_HOUR = 1800000;

    @MapKey(key = "date", primary = true)
    public String date;

    @MapKey(key = "start_time")
    public long startTime;

    @MapKey(key = "end_time")
    public long endTime;

    @MapKey(key = "hours")
    public int hours;

    public PunchRecord() {
    }

    public PunchRecord(String date) {
        this.date = date;
    }

    public void punch() {
        if (startTime == 0) {
            hours = 0;
            startTime = System.currentTimeMillis();
        } else {
            endTime = System.currentTimeMillis();
            hours = (int) ((startTime - endTime) / HALF_AN_HOUR);
        }
    }
}
