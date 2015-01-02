package mw.ankara.base.cache;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 图片缓存，实现了{@link com.android.volley.toolbox.ImageLoader.ImageCache}
 * 获取Bitmap的流程：从内存缓存{@link #mMemoryCache}里获取，获取不到再从文件缓存{@link #mDiskCache}获取
 *
 * @author MasaWong
 * @date 14/12/30.
 */
public class BitmapCache implements ImageLoader.ImageCache {

    private LruCache<String, Bitmap> mMemoryCache;
    private DiskLruCache mDiskCache;

    public BitmapCache(Context context) {
        initMemoryCache();
        initDiskCache(context);
    }

    /**
     * 内存缓存，设置为应用可用内存的1/5，可用内存一般为32Mb，一般为4Mb，如果大于4Mb也设置为4Mb
     */
    protected void initMemoryCache() {
        int cacheSize = (int) (Runtime.getRuntime().maxMemory() / 8);
        if (cacheSize > 4194304) {
            cacheSize = 4194304;
        }

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight();
            }
        };
    }

    /**
     * 文件缓存，设置可用的磁盘大小为100Mb
     */
    protected void initDiskCache(Context context) {
        try {
            // 获取系统提供的缓存路径
            File cacheDir = context.getCacheDir().getAbsoluteFile();
            if (!cacheDir.exists()) {
                if (!cacheDir.mkdirs()) {
                    // 目录创建失败的时候抛出异常，自己接收后打印错误堆栈
                    throw new IOException("make cache directory failed");
                }
            }
            // 创建DiskLruCache实例，初始化缓存数据
            mDiskCache = DiskLruCache.open(cacheDir, getAppVersion(context), 1, 104857600l);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取当前应用程序的版本号。格式为XXX，暂不考虑某个值大于10的情况
     */
    private int getAppVersion(Context context) {
        PackageManager manager = context.getPackageManager();//context为当前Activity上下文
        try {
            PackageInfo packageInfo = manager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return 0;
        }
    }

    private Bitmap getFromMemoryCache(String url) {
        return mMemoryCache.get(url);
    }

    private Bitmap getFromDiskCache(String url) {
        FileDescriptor fileDescriptor = null;
        FileInputStream fileInputStream = null;

        try {
            // 生成图片URL对应的key
            String key = String.valueOf(url.hashCode());

            // 查找key对应的缓存
            DiskLruCache.Snapshot snapShot = mDiskCache.get(key);
            if (snapShot != null) {
                fileInputStream = (FileInputStream) snapShot.getInputStream(0);
                fileDescriptor = fileInputStream.getFD();
            }

            // 将缓存数据解析成Bitmap对象
            Bitmap bitmap = null;
            if (fileDescriptor != null) {
                bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            }

            if (bitmap != null) {
                // 将Bitmap对象添加到内存缓存当中
                putIntoMemoryCache(url, bitmap);
            }
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileDescriptor == null && fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private void putIntoMemoryCache(String url, Bitmap bitmap) {
        mMemoryCache.put(url, bitmap);
    }

    private void putIntoDiskCache(String url, Bitmap bitmap) {
        try {
            // 生成图片URL对应的key
            String key = String.valueOf(url.hashCode());

            DiskLruCache.Editor editor = mDiskCache.edit(key);
            if (editor != null) {
                // 写入文件
                int quality = 1048576 / bitmap.getByteCount();
                bitmap.compress(Bitmap.CompressFormat.PNG, quality, editor.newOutputStream(0));

                editor.commit();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Bitmap getBitmap(String url) {
        synchronized (BitmapCache.class) {
            Bitmap bitmap = getFromMemoryCache(url);
            if (bitmap == null) {
                return getFromDiskCache(url);
            } else {
                return bitmap;
            }
        }
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        synchronized (BitmapCache.class) {
            putIntoMemoryCache(url, bitmap);
            putIntoDiskCache(url, bitmap);
        }
    }
}
