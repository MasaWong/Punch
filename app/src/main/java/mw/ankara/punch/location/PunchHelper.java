package mw.ankara.punch.location;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Calendar;

import mw.ankara.base.database.SQLitable;
import mw.ankara.punch.database.PunchDb;
import mw.ankara.punch.database.PunchRecord;

public class PunchHelper extends BroadcastReceiver {

    public static final String PK_LATITUDE = "work_latitude";
    public static final String PK_LONGITUDE = "work_longitude";
    public static final String PK_RADIUS = "work_radius";

    public static final int RC_PROXY = 0x9788;

    public static final long INTERVAL = 300000l;

    public static boolean hasLocation(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.contains(PK_LATITUDE) && preferences.contains(PK_LONGITUDE)
                && preferences.contains(PK_RADIUS);
    }

    public static void setLocation(Context context, float latitude, float longitude, float radius) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putFloat(PK_LATITUDE, latitude).putFloat(PK_LONGITUDE, longitude)
                .putFloat(PK_RADIUS, radius).apply();
    }

    public static void removeLocation(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().remove(PK_LATITUDE).remove(PK_LONGITUDE).remove(PK_RADIUS).apply();
    }

    public static void addProximityAlert(Context context) {
        LocationManager manager = (LocationManager) context.getSystemService(
                Context.LOCATION_SERVICE);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        float latitude = preferences.getFloat(PK_LATITUDE, 0);
        float longitude = preferences.getFloat(PK_LONGITUDE, 0);

        float radius = preferences.getFloat(PK_RADIUS, 0);

        Intent intent = new Intent(context, PunchHelper.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, RC_PROXY, intent, 0);

        manager.addProximityAlert(latitude, longitude, radius, INTERVAL, pendingIntent);
    }

    public static void removeProximityAlert(Context context) {
        LocationManager manager = (LocationManager) context.getSystemService(
                Context.LOCATION_SERVICE);

        Intent intent = new Intent(context, PunchHelper.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, RC_PROXY, intent, 0);

        manager.removeProximityAlert(pendingIntent);

    }

    public PunchHelper() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Calendar calendar = Calendar.getInstance();
        long now = calendar.getTimeInMillis();

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long baseTime = calendar.getTimeInMillis();

        ArrayList<SQLitable> records = PunchDb.getInstance().query(PunchRecord.class,
                "base_time=?", new String[]{String.valueOf(baseTime)}, null, null, null);
        PunchRecord record = records.size() == 0 ? new PunchRecord(baseTime)
                : (PunchRecord) records.get(0);

        record.punch(now);
        record.updateOrInsert();
    }
}
