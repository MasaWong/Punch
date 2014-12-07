package mw.ankara.punch;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import mw.ankara.base.database.SQLitable;
import mw.ankara.punch.database.PunchDb;
import mw.ankara.punch.database.PunchRecord;

public class MainActivity extends ActionBarActivity {

    private TextView mTvStart;
    private TextView mTvEnd;
    private TextView mTvMonth;

    private PunchRecord mRecordToday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        mTvStart = (TextView) findViewById(R.id.main_tv_start);
        mTvEnd = (TextView) findViewById(R.id.main_tv_end);
        mTvMonth = (TextView) findViewById(R.id.main_tv_month);

        findViewById(R.id.main_b_punch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecordToday.punch();
                updateHours();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        Date now = new Date(System.currentTimeMillis());
        String today = format.format(now);

        ArrayList<SQLitable> records = PunchDb.getInstance().query(PunchRecord.class,
                "date=?", new String[]{today}, null, null, null);
        mRecordToday = records.size() == 0 ? new PunchRecord(today) : (PunchRecord) records.get(0);

        updateHours();
    }

    @Override
    protected void onPause() {
        super.onPause();

        PunchDb database = PunchDb.getInstance();
        if (database.update(mRecordToday, "date=?", new String[]{mRecordToday.date}) == 0) {
            database.insert(mRecordToday);
        }
    }

    private void updateHours() {
        SimpleDateFormat format = new SimpleDateFormat("MM月dd日 HH:mm:ss");

        if (mRecordToday.startTime != 0) {
            Date startTime = new Date(mRecordToday.startTime);
            mTvStart.setText(format.format(startTime));
        }

        if (mRecordToday.endTime != 0) {
            Date endTime = new Date(mRecordToday.endTime);
            mTvEnd.setText(format.format(endTime));
        }

        int hours = PunchDb.getInstance().querySum(PunchRecord.class, "hours", null);
        mTvMonth.setText(String.valueOf(hours));
    }
}
