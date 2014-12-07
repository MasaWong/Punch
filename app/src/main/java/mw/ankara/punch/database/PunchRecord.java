package mw.ankara.punch.database;

import mw.ankara.base.database.SQLField;
import mw.ankara.base.database.SQLiteRecord;

/**
 * @author MasaWong
 * @date 14/12/6.
 */
public class PunchRecord extends SQLiteRecord {

    private static final long A_QUARTER = 900000l;
    private static final int AN_HOUR = 3600000;

    @SQLField(key = "date", primary = true)
    public String date;

    @SQLField(key = "start_time")
    public long startTime;

    @SQLField(key = "end_time")
    public long endTime;

    @SQLField(key = "hours")
    public int hours;

    public boolean saved = true;

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
            hours = (int) ((endTime - startTime + A_QUARTER) / AN_HOUR);
        }

        saved = false;
    }

    public void updateOrInsert() {
        PunchDb database = PunchDb.getInstance();
        if (database.update(this, "date=?", new String[]{this.date}) == 0) {
            database.insert(this);
        }

        saved = true;
    }
}
