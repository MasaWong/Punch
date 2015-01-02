package mw.ankara.base.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import mw.ankara.base.cache.BitmapCache;

/**
 * 警告: 需要权限android.permission.INTERNET
 * <p/>
 * {@link #mRequestQueue} 请求有4种类型 :
 * {@link com.android.volley.toolbox.JsonObjectRequest}
 * {@link com.android.volley.toolbox.JsonArrayRequest}
 * {@link com.android.volley.toolbox.StringRequest}
 * {@link com.android.volley.toolbox.ImageRequest}
 * <p/>
 * {@link #mImageLoader} 用于缓存Bitmap
 *
 * @author MasaWong
 * @date 14-7-30.
 */
public class WebManager {

    private static WebManager sInstance;

    private RequestQueue mRequestQueue;

    private ImageLoader mImageLoader;

    private WebManager(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
        mRequestQueue.start();

        mImageLoader = new ImageLoader(mRequestQueue, new BitmapCache(context));
    }

    public static WebManager getInstance() {
        return sInstance;
    }

    public static WebManager createInstance(Context context) {
        if (sInstance == null) {
            sInstance = new WebManager(context.getApplicationContext());
        }
        return sInstance;
    }

    public static void destroyInstance() {
        sInstance.mRequestQueue.stop();
        sInstance = null;
    }

    public void sendRequest(Request request) {
        mRequestQueue.add(request);
        Log.e("url", request.getUrl());
    }

    public void cancelRequest(Object tag) {
        mRequestQueue.cancelAll(tag);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }
}
