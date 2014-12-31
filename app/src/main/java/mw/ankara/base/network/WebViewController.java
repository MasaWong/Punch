package mw.ankara.base.network;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * 用于控制WebView的Controller
 * 由于使用WebView的类型太多，如Activity，Fragment，DialogFragment等
 * 因此将WebView的功能独立出来作为一个Controller
 *
 * @author MasaWong
 * @date 14/12/29.
 */
public class WebViewController {

    public static final String K_URL = "url";

    private WebView mWebView;

    private PageFinishedListener mPageFinishedListener;
    private ReceivedErrorListener mReceivedErrorListener;
    private UrlLoadingListener mUrlLoadingListener;
    private JsAlertListener mJsAlertListener;

    /**
     * 构造函数，配置WebView
     *
     * @param webView 需要在xml中引入WebView，然后将这个WebView传入
     */
    public WebViewController(WebView webView) {
        mWebView = webView;

        // clearView() is deprecated, but onBackPressed returns to about:blank
        mWebView.clearView();
        mWebView.setHorizontalScrollBarEnabled(false);

        setWebViewClient();
        setWebChromeClient();
        setWebViewSettings();
    }

    /**
     * 配置WebView参数
     */
    @SuppressLint("SetJavaScriptEnabled")
    protected void setWebViewSettings() {
        WebSettings settings = mWebView.getSettings();
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSaveFormData(true);
        settings.setJavaScriptEnabled(true);
        settings.setBlockNetworkImage(true);
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        settings.setUseWideViewPort(false);
        settings.setDomStorageEnabled(true);
    }

    /**
     * 配置WebViewClient来处理网页加载的各种状态
     */
    protected void setWebViewClient() {
        // open external url should setWebViewClient
        WebViewClient webClient = new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                if (mPageFinishedListener != null) {
                    mPageFinishedListener.overridePageFinished(view, url);
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description,
                    String failingUrl) {
                if (mReceivedErrorListener != null) {
                    mReceivedErrorListener.overrideReceivedError(view, errorCode,
                            description, failingUrl);
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return mUrlLoadingListener != null &&
                        mUrlLoadingListener.overrideUrlLoading(view, url);
            }
        };
        mWebView.setWebViewClient(webClient);
    }

    /**
     * 配置WebChromeClient来处理JsAlert，用于从网页取得一些复杂的数据
     */
    protected void setWebChromeClient() {
        WebChromeClient webChromeClient = new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return mJsAlertListener != null &&
                        mJsAlertListener.overrideJsAlert(view, url, message, result);
            }
        };
        mWebView.setWebChromeClient(webChromeClient);
    }

    /**
     * 加载Url
     *
     * @param url 需要加载的url
     */
    public void loadUrl(String url) {
        mWebView.loadUrl(url);
    }

    public void setPageFinishedListener(PageFinishedListener pageFinishedListener) {
        mPageFinishedListener = pageFinishedListener;
    }

    public void setReceivedErrorListener(ReceivedErrorListener receivedErrorListener) {
        mReceivedErrorListener = receivedErrorListener;
    }

    public void setUrlLoadingListener(UrlLoadingListener urlLoadingListener) {
        mUrlLoadingListener = urlLoadingListener;
    }

    public void setJsAlertListener(JsAlertListener jsAlertListener) {
        mJsAlertListener = jsAlertListener;
    }

    public static interface PageFinishedListener {
        public void overridePageFinished(WebView view, String url);
    }

    public static interface ReceivedErrorListener {
        public void overrideReceivedError(WebView view, int errorCode, String description,
                String failingUrl);
    }

    public static interface UrlLoadingListener {
        public boolean overrideUrlLoading(WebView view, String url);
    }

    public static interface JsAlertListener {
        public boolean overrideJsAlert(WebView view, String url, String message, JsResult result);
    }
}
