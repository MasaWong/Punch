package mw.ankara.punch;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import mw.ankara.base.database.SQLitable;
import mw.ankara.punch.database.PunchDb;
import mw.ankara.punch.database.PunchRecord;

public class MainActivity extends ActionBarActivity {

    private Button mBPaste;
    private Button mBStart;
    private Button mBEnd;

    private PunchRecord mRecordToday;
    private PunchRecord mRecordSelected;
    private PunchRecord mRecordCopied;

    private Calendar mCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        mBPaste = (Button) findViewById(R.id.main_b_paste);
        mBStart = (Button) findViewById(R.id.main_tv_start);
        mBEnd = (Button) findViewById(R.id.main_tv_end);

        CalendarView calendarView = (CalendarView) findViewById(R.id.main_cv_calendar);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                    int dayOfMonth) {
                if (mRecordSelected != null) {
                    mRecordSelected.updateOrInsert();
                }

                mCalendar.set(year, month, dayOfMonth);
                if (mCalendar.getTimeInMillis() == mRecordToday.baseTime) {
                    mRecordSelected = mRecordToday;
                } else {
                    mRecordSelected = query(mCalendar.getTimeInMillis());
                }

                updateHours(mRecordSelected);
            }
        });

        setToday();
    }

    @Override
    protected void onPause() {
        super.onPause();

        saveRecord();
    }

    private void setToday() {
        mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.HOUR_OF_DAY, 0);
        mCalendar.set(Calendar.MINUTE, 0);
        mCalendar.set(Calendar.SECOND, 0);
        mCalendar.set(Calendar.MILLISECOND, 0);

        mRecordToday = query(mCalendar.getTimeInMillis());
        mRecordSelected = mRecordToday;
        updateHours(mRecordToday);
    }

    private void delete(String date) {
        PunchDb.getInstance().delete(PunchRecord.class, "time=?", new String[]{date});
    }

    private PunchRecord query(long baseTime) {
        ArrayList<SQLitable> records = PunchDb.getInstance().query(PunchRecord.class,
                "base_time=?", new String[]{String.valueOf(baseTime)}, null, null, null);
        return records.size() == 0 ? new PunchRecord(baseTime) : (PunchRecord) records.get(0);
    }

    private void saveRecord() {
        if (mRecordSelected != null) {
            mRecordSelected.updateOrInsert();
        }
    }

    /**
     * @param view {@link R.id#main_b_punch}的点击响应
     */
    public void onPunchClick(View view) {
        if (mRecordToday == mRecordSelected) {
            mRecordToday.punch();
            mRecordToday.updateOrInsert();
            updateHours(mRecordSelected);
        } else {
            new AlertDialog.Builder(this).setTitle(R.string.warning_modify)
                    .setNegativeButton(R.string.no, null)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mRecordToday.punch();
                            mRecordToday.updateOrInsert();
                        }
                    }).create().show();
        }
    }

    /**
     * @param view {@link R.id#main_b_copy}的点击响应
     */
    public void onCopyClick(View view) {
        mRecordCopied = mRecordSelected;
        mBPaste.setEnabled(true);
    }

    /**
     * @param view {@link R.id#main_b_paste}的点击响应
     */
    public void onPasteClick(View view) {
        long startTime = mRecordCopied.startTime - mRecordCopied.baseTime;
        mRecordSelected.startTime = mRecordSelected.baseTime + startTime;

        long endTime = mRecordCopied.endTime - mRecordCopied.baseTime;
        mRecordSelected.endTime = mRecordSelected.baseTime + endTime;

        updateHours(mRecordSelected);
    }

    /**
     * @param view {@link R.id#main_tv_start}的点击响应
     */
    public void onStartClick(View view) {
        final Calendar calendar = Calendar.getInstance();

        if (mRecordSelected.startTime == 0) {
            calendar.setTimeInMillis(mCalendar.getTimeInMillis());
        } else {
            calendar.setTimeInMillis(mRecordSelected.startTime);
        }

        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                mRecordSelected.startTime = calendar.getTimeInMillis();

                mBStart.setText(String.format("%02d:%02d", hourOfDay, minute));
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

        if (mRecordSelected.endTime == 0) {
            calendar.setTimeInMillis(mCalendar.getTimeInMillis());
        } else {
            calendar.setTimeInMillis(mRecordSelected.endTime);
        }

        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                mRecordSelected.endTime = calendar.getTimeInMillis();

                mBEnd.setText(String.format("%02d:%02d", hourOfDay, minute));
            }
        };
        TimePickerDialog dialog = new TimePickerDialog(this, listener,
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        dialog.show();
    }

    /**
     * @param view 点击响应
     */
    public void onMonthClick(View view) {
        saveRecord();

        int hours = PunchDb.getInstance().querySum(PunchRecord.class, "hours", null);
        Toast.makeText(this, String.valueOf(hours), Toast.LENGTH_LONG).show();
    }

    private void updateHours(PunchRecord record) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");

        if (record.startTime != 0) {
            Date startTime = new Date(record.startTime);
            mBStart.setText(format.format(startTime));
        } else {
            mBStart.setText(null);
        }

        if (record.endTime != 0) {
            Date endTime = new Date(record.endTime);
            mBEnd.setText(format.format(endTime));
        } else {
            mBEnd.setText(null);
        }
    }
}
