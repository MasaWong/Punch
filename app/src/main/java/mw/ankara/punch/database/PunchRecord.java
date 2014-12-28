package mw.ankara.punch.database;

import mw.ankara.base.database.Map;
import mw.ankara.base.database.SQLiteRecord;

/**
 * @author MasaWong
 * @date 14/12/6.
 */
public class PunchRecord extends SQLiteRecord {

    private static final long A_QUARTER = 900000l;
    private static final int AN_HOUR = 3600000;

    @Map(key = "base_time", primary = true)
    public long baseTime;

    @Map(key = "start_time")
    public long startTime;

    @Map(key = "end_time")
    public long endTime;

    @Map(key = "hours")
    public int hours;

    public PunchRecord() {
    }

    public PunchRecord(long baseTime) {
        this.baseTime = baseTime;
    }

    public void punch() {
        punch(System.currentTimeMillis());
    }

    public void punch(long time) {
        if (startTime == 0) {
            startTime = time;
        } else {
            endTime = time;
        }
    }

    public void updateOrInsert() {
        if (endTime != 0) {
            hours = (int) ((endTime - startTime + A_QUARTER) / AN_HOUR);
        }

        PunchDb database = PunchDb.getInstance();
        if (database.update(this, "base_time=?", new String[]{String.valueOf(baseTime)}) == 0) {
            database.insert(this);
        }
    }
}
