package mw.ankara.punch;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import mw.ankara.base.database.SQLitable;
import mw.ankara.punch.database.PunchDb;
import mw.ankara.punch.database.PunchRecord;

public class MainActivity extends ActionBarActivity {

    private TextView mEtStart;
    private TextView mEtEnd;

    private PunchRecord mRecordToday;
    private PunchRecord mRecordSelected;

    private String mToday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        mEtStart = (TextView) findViewById(R.id.main_tv_start);
        mEtEnd = (TextView) findViewById(R.id.main_tv_end);

        CalendarView calendarView = (CalendarView) findViewById(R.id.main_cv_calendar);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
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

    /**
     * @param view {@link R.id#main_b_punch}的点击响应
     */
    public void onPunchClick(View view) {
        mRecordToday.punch();
        mRecordToday.updateOrInsert();
        if (mRecordToday == mRecordSelected) {
            updateHours(mRecordSelected);
        }
    }

    /**
     * @param view {@link R.id#main_tv_start}的点击响应
     */
    public void onStartClick(View view) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mRecordSelected.startTime);
        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                mRecordSelected.startTime = calendar.getTimeInMillis();

                mEtStart.setText(String.format("%02d:%02d", hourOfDay, minute));
            }
        };
        TimePickerDialog dialog = new TimePickerDialog(this, listener,
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        dialog.show();
    }

    /**
     * @param view {@link R.id#main_tv_end}的点击响应
     */
    public void onEndClick(View view) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mRecordSelected.endTime);
        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                mRecordSelected.endTime = calendar.getTimeInMillis();

                mEtEnd.setText(String.format("%02d:%02d", hourOfDay, minute));
            }
        };
        TimePickerDialog dialog = new TimePickerDialog(this, listener,
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        dialog.show();
    }

    private void updateHours(PunchRecord record) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");

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
