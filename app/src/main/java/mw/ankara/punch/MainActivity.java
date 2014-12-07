package mw.ankara.punch;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;
import android.widget.CalendarView;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import mw.ankara.base.database.SQLitable;
import mw.ankara.punch.database.PunchDb;
import mw.ankara.punch.database.PunchRecord;

public class MainActivity extends ActionBarActivity {

    private CalendarView mCvCalendar;
    private EditText mEtStart;
    private EditText mEtEnd;

    private PunchRecord mRecordToday;
    private PunchRecord mRecordSelected;

    private String mToday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        mCvCalendar = (CalendarView) findViewById(R.id.main_cv_calendar);
        mEtStart = (EditText) findViewById(R.id.main_et_start);
        mEtEnd = (EditText) findViewById(R.id.main_et_end);

        mCvCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                    int dayOfMonth) {
                if (mRecordSelected != null) {
                    mRecordSelected.updateOrInsert();
                }

                String selectDate = String.format("%4d年%02d月%02d日", year, month + 1, dayOfMonth);
                if (mToday.equals(selectDate)) {
                    mRecordSelected = mRecordToday;
                } else {
                    mRecordSelected = query(selectDate);
                }
                updateHours(mRecordSelected);
            }
        });
        findViewById(R.id.main_b_punch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecordToday.punch();
                mRecordToday.updateOrInsert();
                if (mRecordToday == mRecordSelected) {
                    updateHours(mRecordSelected);
                }
            }
        });

        setToday();
    }

    private void setToday() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        Date now = new Date(System.currentTimeMillis());
        mToday = format.format(now);
        mRecordToday = query(mToday);
        updateHours(mRecordToday);
    }

    private PunchRecord query(String date) {
        ArrayList<SQLitable> records = PunchDb.getInstance().query(PunchRecord.class,
                "date=?", new String[]{date}, null, null, null);
        return records.size() == 0 ? new PunchRecord(date) : (PunchRecord) records.get(0);
    }

    private void updateHours(PunchRecord record) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

        if (record.startTime != 0) {
            Date startTime = new Date(record.startTime);
            mEtStart.setText(format.format(startTime));
        } else {
            mEtStart.setText(null);
        }

        if (record.endTime != 0) {
            Date endTime = new Date(record.endTime);
            mEtEnd.setText(format.format(endTime));
        } else {
            mEtEnd.setText(null);
        }
    }

    private void updateMonth() {
        int hours = PunchDb.getInstance().querySum(PunchRecord.class, "hours", null);
    }
}
