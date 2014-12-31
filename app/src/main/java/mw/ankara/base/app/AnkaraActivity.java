package mw.ankara.base.app;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import java.io.Serializable;

/**
 * @author MasaWong
 * @date 14-8-28.
 */
public class AnkaraActivity extends ActionBarActivity {

    protected Intent buildIntent(Class nextActivity, Object... args) {
        Intent intent = new Intent(this, nextActivity);
        try {
            for (int i = 0; i < args.length; i += 2) {
                String key = (String) args[i];
                Object value = args[i + 1];
                if (value instanceof Integer) {
                    intent.putExtra(key, (Integer) value);
                } else if (value instanceof Float) {
                    intent.putExtra(key, (Float) value);
                } else if (value instanceof Double) {
                    intent.putExtra(key, (Double) value);
                } else if (value instanceof Long) {
                    intent.putExtra(key, (Long) value);
                } else if (value instanceof Boolean) {
                    intent.putExtra(key, (Boolean) value);
                } else if (value instanceof String) {
                    intent.putExtra(key, (String) value);
                } else if (Serializable.class.isAssignableFrom(value.getClass())) {
                    intent.putExtra(key, (Serializable) value);
                }
            }
        } catch (Exception ignored) {
        }
        return intent;
    }

    protected void launchActivity(Class nextActivity, Object... args) {
        startActivity(buildIntent(nextActivity, args));
    }

    protected void listenActivity(int requestCode, Class nextActivity, Object... args) {
        startActivityForResult(buildIntent(nextActivity, args), requestCode);
    }

    protected void showBack() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    protected void showToast(int res, int duration) {
        Toast.makeText(this, res, duration).show();
    }

    protected void showToast(String message, int duration) {
        Toast.makeText(this, message, duration).show();
    }

    protected void showToast(int res) {
        showToast(res, Toast.LENGTH_SHORT);
    }

    protected void showToast(String message) {
        showToast(message, Toast.LENGTH_SHORT);
    }
}
