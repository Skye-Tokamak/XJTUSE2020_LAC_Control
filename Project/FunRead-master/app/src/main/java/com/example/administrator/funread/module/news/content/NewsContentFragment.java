package com.example.administrator.funread.module.news.content;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.administrator.funread.Constant;
import com.example.administrator.funread.ErrorAction;
import com.example.administrator.funread.IntentAction;
import com.example.administrator.funread.R;
import com.example.administrator.funread.bean.news.NewsContentBean;
import com.example.administrator.funread.module.base.BaseFragment;
import com.example.administrator.funread.util.SettingUtil;

/**
 * 作者：created by weidiezeng on 2019/8/17 11:17
 * 邮箱：1067875902@qq.com
 * 描述：
 */
public class NewsContentFragment extends BaseFragment<INewsContent.Presenter>implements INewsContent.View {

    private static final String TAG="NewsContentFragment";

    private String shareUrl;
    private String shareTitle;
    private String authorName;
    private NewsContentBean bean;
    private Bundle mBundle;

    private Toolbar toolbar;
    private WebView webView;
    private NestedScrollView scrollView;
    private INewsContent.Presenter presenter;
    private ContentLoadingProgressBar progressBar;
    private SwipeRefreshLayout swipRefreshLayout;

    public static NewsContentFragment newInstance(Parcelable dataBean){
        NewsContentFragment instance=new NewsContentFragment();
        Bundle bundle=new Bundle();
        bundle.putParcelable(TAG,dataBean);
        instance.setArguments(bundle);
        return instance;
    }
    @Override
    protected int attachLayoutId() {

        mBundle=getArguments();

        return R.layout.fragment_news_content;
    }

    @Override
    protected void initData() throws NullPointerException {
        if(mBundle==null){
            return;
        }
        try{
            bean=mBundle.getParcelable(TAG);
            presenter.doLoadData(bean);
            shareUrl=bean.getUrl();
            shareTitle=bean.getTitle();
            authorName=bean.getAuthorName();
        }catch (Exception e){
            ErrorAction.print(e);
        }

        toolbar.setTitle(authorName);
    }

    @Override
    protected void initView(View view) {
        toolbar=view.findViewById(R.id.toolbar);
        initToolBar(toolbar,true,"");
        toolbar.setOnClickListener(view1->{
            //滑到顶部
            scrollView.smoothScrollTo(0,0);
        });
        webView=view.findViewById(R.id.webView);
        initWebClient();
        scrollView=view.findViewById(R.id.scrollView);
        scrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener)(v,scrollX,scrollY,oldScrollX,oldScrollY)->onHideLoading());

        progressBar=view.findViewById(R.id.pb_progress);
        int color=SettingUtil.getInstance().getColor();
        progressBar.getIndeterminateDrawable().setColorFilter(color,PorterDuff.Mode.MULTIPLY);
        progressBar.show();

        swipRefreshLayout=view.findViewById(R.id.refresh_layout);
        swipRefreshLayout.setColorSchemeColors(SettingUtil.getInstance().getColor());
        swipRefreshLayout.setOnRefreshListener(()->{
                    swipRefreshLayout.post(()->swipRefreshLayout.setRefreshing(true));
                    presenter.doLoadData(bean);
                });


        setHasOptionsMenu(true);
    }

    @SuppressLint({"SetJavaScriptEnabled","AddJavascriptInterface","JavascriptInterface"})
    private void initWebClient() {
        WebSettings settings=webView.getSettings();
        settings.setJavaScriptEnabled(true);

        // 缩放,设置为不能缩放可以防止页面上出现放大和缩小的图标
        settings.setBuiltInZoomControls(false);
        // 缓存
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        // 开启DOM storage API功能
        settings.setDomStorageEnabled(true);
        // 开启application Cache功能
        settings.setAppCacheEnabled(true);
        // 判断是否为无图模式
        settings.setBlockNetworkImage(SettingUtil.getInstance().getIsNoPhotoMode());

        //注册本地java接口
        //webView.addJavascriptInterface(new InJavaScriptLocalObj(),"local_obj");
        webView.setWebViewClient(new WebViewClient(){
            /**
             * 拦截 url 跳转,在里边添加点击链接跳转或者操作
             * 在利用shouldOverrideUrlLoading来拦截URL时，
             * 如果return true，则会屏蔽系统默认的显示URL结果的行为，不需要处理的URL也需要调用loadUrl()来加载进WebVIew，不然就会出现白屏；
             * 如果return false，则系统默认的加载URL行为是不会被屏蔽的
             */
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(!TextUtils.isEmpty(url)){
                    view.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                onHideLoading();
                //获取html源码
                //view.loadUrl("javascript:window.local_obj.showSource('<head>'+"
                        //+ "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
                //注入js函数监听
                //webView.loadUrl(Constant.JS_INJECT_IMG);
                super.onPageFinished(view ,url);
            }
        });
        //如果希望浏览的网页回退而不是退出浏览器，
        // 需要处理并消费掉该Back事件。

        webView.setOnKeyListener((view,i,keyEvent)->{
            if((keyEvent.getKeyCode()==KeyEvent.KEYCODE_BACK)&&webView.canGoBack()){
                webView.goBack();
                return true;
            }
            return false;
        });
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if(newProgress>=90){
                    onHideLoading();
                }else {
                    onShowLoading();
                }
            }
        });
    }
   /* final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void showSource(String html) {
            Log.d(TAG,html);
        }
    }*/

    /**
     html* @param url
     * @param flag
     */
    @Override
    public void onSetWebView(String url, boolean flag) {

        if(flag){
            webView.loadUrl(url);
        }
    }

    @Override
    public void onShowLoading() {
        progressBar.show();

    }

    @Override
    public void onHideLoading() {

        progressBar.hide();
        swipRefreshLayout.post(()->swipRefreshLayout.setRefreshing(false));
    }

    @Override
    public void onShowNetError() {
        Snackbar.make(scrollView,R.string.network_error,Snackbar.LENGTH_INDEFINITE).show();

    }

    @Override
    public void setPresenter(INewsContent.Presenter presenter) {

        if(null==presenter){
            this.presenter=new NewsContentPresenter(this);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_browser,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case R.id.action_share:
                IntentAction.send(getActivity(),shareTitle+"\n"+shareUrl);
                break;
            case R.id.action_open_in_browser:
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(shareUrl)));
                break;
            case android.R.id.home:
                getActivity().onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
