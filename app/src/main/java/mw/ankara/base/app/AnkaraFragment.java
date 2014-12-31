package mw.ankara.base.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.Serializable;

/**
 * @author MasaWong
 * @date 14-9-3.
 */
public abstract class AnkaraFragment extends Fragment {

    private View mVFragment;

    /**
     * use {@link #initSelf(android.view.View)} instead
     */
    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        if (mVFragment == null) {
            mVFragment = inflater.inflate(getLayoutResource(), container, false);
            initSelf(mVFragment);
        }
        return mVFragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        ((ViewGroup) mVFragment.getParent()).removeView(mVFragment);
    }

    protected void initSelf(View view) {
    }

    protected Intent buildIntent(Class nextActivity, Object... args) {
        Intent intent = new Intent(getActivity(), nextActivity);
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

    protected abstract int getLayoutResource();

    protected void showBack() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    protected ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

    protected void showToast(int res, int duration) {
        Toast.makeText(getActivity(), res, duration).show();
    }

    protected void showToast(String message, int duration) {
        Toast.makeText(getActivity(), message, duration).show();
    }

    protected void showToast(int res) {
        showToast(res, Toast.LENGTH_SHORT);
    }

    protected void showToast(String message) {
        showToast(message, Toast.LENGTH_SHORT);
    }
}
