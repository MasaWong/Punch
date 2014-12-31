package mw.ankara.base.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * {@link #mRequestQueue} request type :
 * {@link com.android.volley.toolbox.JsonObjectRequest}
 * {@link com.android.volley.toolbox.JsonArrayRequest}
 * {@link com.android.volley.toolbox.StringRequest}
 * {@link com.android.volley.toolbox.ImageRequest}
 * <p/>
 * {@link #mImageLoader} cache image :
 *
 * @author MasaWong
 * @date 14-7-30.
 */
public class WebManager {

    private static WebManager sScheduler;

    private RequestQueue mRequestQueue;

    private ImageLoader mImageLoader;

    private WebManager(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);

        mImageLoader = new ImageLoader(
                mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    public static WebManager getInstance() {
        return sScheduler;
    }

    public static void init(Context context) {
        sScheduler = new WebManager(context);
    }

    public void sendRequest(Request request) {
        mRequestQueue.add(request);
    }

    public void cancelRequest(Object tag) {
        mRequestQueue.cancelAll(tag);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }
}
