package com.example.administrator.funread;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.administrator.funread.module.base.BaseActivity;
import com.example.administrator.funread.util.DownloadUtil;
import com.example.administrator.funread.util.ImageLoader;
import com.example.administrator.funread.widget.BottomSheetDialogFixed;
import com.example.administrator.funread.widget.DismissFrameLayout;
import com.pixelcan.inkpageindicator.InkPageIndicator;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.SettingService;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.MaybeOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import uk.co.senab.photoview.DefaultOnDoubleTapListener;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageBrowserActivity extends BaseActivity {

    private static final String TAG="ImageBrowserActivity";
    private static final String EXTRA_URL="url";
    private static final String EXTRA_LIST="list";

    private static final int ALPHA_MAX=0xFF;

    private int mCurrentPosition;
    private ArrayList<String>mImgList;
    private ViewPager mViewPager;
    private InkPageIndicator mIndicator;
    private ColorDrawable mColorDrawable;

    private boolean canHideFlag=true;
    private long mIndicatorHideTime;

    private DismissFrameLayout.OnDismissListener onDisMissListener=new DismissFrameLayout.OnDismissListener() {
        @Override
        public void onScaleProgress(float scale) {
            mColorDrawable.setAlpha(
                    Math.min(ALPHA_MAX,mColorDrawable.getAlpha()-(int)(scale*ALPHA_MAX))
            );
        }

        @Override
        public void onDismiss() {

            finish();
        }

        @Override
        public void onCancel() {

            mColorDrawable.setAlpha(ALPHA_MAX);
        }
    };
    public static void start(Context context,String url,ArrayList<String>imgList){
        Intent starter=new Intent(context,ImageBrowserActivity.class);
        starter.putExtra(EXTRA_URL,url);
        starter.putStringArrayListExtra(EXTRA_LIST,imgList);
        starter.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(starter);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        translucentScreen();
        setContentView(R.layout.activity_image_browser);

        Intent intent=getIntent();
        if(intent==null){
            finish();
            return;
        }

        RelativeLayout container=findViewById(R.id.container);
        mColorDrawable=new ColorDrawable(getResources().getColor(R.color.Black));
        container.setBackground(mColorDrawable);

        mImgList=intent.getStringArrayListExtra(EXTRA_LIST);
        String url=intent.getStringExtra(EXTRA_URL);
        mCurrentPosition=mImgList.indexOf(url);

        mViewPager=findViewById(R.id.viewpager);
        mViewPager.setAdapter(new PhotoAdapter(mImgList,onDisMissListener));
        mViewPager.setCurrentItem(mCurrentPosition);
        mIndicator=findViewById(R.id.indicator);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

                if(slidrInterface!=null){
                    if(i==0){
                        slidrInterface.unlock();
                    }else {
                        slidrInterface.lock();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

                //滑动时指示器显示
                if(i==ViewPager.SCROLL_STATE_IDLE){
                    mIndicatorHideTime=System.currentTimeMillis();
                    canHideFlag=true;
                }else {
                    //显示
                    canHideFlag=false;
                    mIndicator.animate()
                            .translationY(0)
                            .setDuration(200)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    mIndicator.setVisibility(View.VISIBLE);
                                }
                            });
                }
            }
        });
        startIndicatorObserver();

    }

    @Override
    protected void onStart() {
        super.onStart();
        mIndicator.setViewPager(mViewPager);
    }

    private void startIndicatorObserver() {
        //延迟一秒，定时一秒，执行任务
        Flowable.interval(1,1,TimeUnit.SECONDS)
                .filter(aLong -> canHideFlag&&System.currentTimeMillis()-mIndicatorHideTime>1000)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .as(this.bindAutoDispose())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        canHideFlag=false;
                        mIndicator.animate()
                                .translationY(mIndicator.getHeight())
                                .setDuration(400)
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        mIndicator.setVisibility(View.GONE);
                                    }
                                });
                    }
                });
    }

    private void translucentScreen() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
            //窗口标志:请求一个半透明的状态栏，系统提供的背景保护最小。
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //窗口标志:请求一个半透明的导航条，系统提供的背景保护最小。
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }
    private void onLongClick(){
        final BottomSheetDialogFixed dialogFixed=new BottomSheetDialogFixed(context);
        dialogFixed.setOwnerActivity(this);
        View view=getLayoutInflater().inflate(R.layout.item_imageview_action_sheet,null);
        view.findViewById(R.id.layout_dowm_image).setOnClickListener(view12 -> {
            saveImage();
            dialogFixed.dismiss();
        });
        view.findViewById(R.id.layout_share_image).setOnClickListener(view1 -> {
            shareImage();
            dialogFixed.dismiss();
        });
        dialogFixed.setContentView(view);
        dialogFixed.show();
    }

    private void shareImage() {
        if(ContextCompat.checkSelfPermission(context, Permission.WRITE_EXTERNAL_STORAGE)
                !=PackageManager.PERMISSION_GRANTED){
            requestPermision();
        }else {
            Maybe.create((MaybeOnSubscribe<Bitmap>)emitter->{
               final String url=mImgList.get(mViewPager.getCurrentItem()) ;
               Bitmap bitmap= Glide.with(context).asBitmap().load(url)
                       .submit(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL)
                       .get();
               emitter.onSuccess(bitmap);
            })
                    .subscribeOn(Schedulers.io())
                    .filter(Objects::nonNull)
                    .map(bitmap -> {
                        File appDir=new File(Environment.getExternalStorageDirectory(),"funread");
                        if(!appDir.exists()){
                            appDir.mkdir();
                        }
                        String fileName="temporary_file.jpg";
                        File file=new File(appDir,fileName);
                        FileOutputStream fileOutputStream=new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
                        fileOutputStream.flush();
                        fileOutputStream.close();
                        //bitmap转变为file类型
                        return file;
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .as(this.bindAutoDispose())
                    .subscribe(file ->IntentAction.sendImage(context,Uri.fromFile(file)),ErrorAction.error() );
        }
    }

    /**
     * 请求权限
     */
    private void requestPermision() {
        AndPermission.with(this)
                .permission(Permission.WRITE_EXTERNAL_STORAGE)
                .rationale((context,permissions,executor)->new AlertDialog.Builder(context)
                    .setMessage(R.string.permission_write_rationale)
                    .setPositiveButton(R.string.button_allow,(dialog, which) -> executor.execute())
                    .setNegativeButton(R.string.button_deny,(dialog, which) -> executor.execute())
                    .show())
                .onDenied(permissions -> {
                    Snackbar.make(mViewPager,R.string.permission_write_denied,Snackbar.LENGTH_SHORT).show();
                    if(AndPermission.hasAlwaysDeniedPermission(ImageBrowserActivity.this,permissions)){
                        final SettingService settingService=AndPermission.permissionSetting(ImageBrowserActivity.this);
                        new AlertDialog.Builder(ImageBrowserActivity.this)
                                .setMessage(R.string.permission_write_rationale)
                                .setPositiveButton(R.string.button_allow,(dialog, which) -> settingService.execute())
                                .setNegativeButton(R.string.button_deny,(dialog, which) -> settingService.execute())
                                .show();
                    }
                })
                .start();

    }

    /**
     * 保存图片
     */
    private void saveImage() {
        if (ContextCompat.checkSelfPermission(context, Permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermision();
        }else {
            final String url = mImgList.get(mViewPager.getCurrentItem());
            Maybe.create((MaybeOnSubscribe<Boolean>) emitter -> emitter.onSuccess(DownloadUtil.saveImage(url, context)))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .as(this.bindAutoDispose())
                    .subscribe(b -> {
                        String s = b ? getString(R.string.saved) : getString(R.string.error);
                        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                    }, ErrorAction.error());
        }
    }

    public class PhotoAdapter extends PagerAdapter{
        List<String> mList;
        DismissFrameLayout.OnDismissListener mOnDismissListener;
        SparseArray<View>mCacheViewArray;

        PhotoAdapter(List<String>mList,DismissFrameLayout.OnDismissListener onDismissListener){
            this.mList=mList;
            this.mOnDismissListener=onDismissListener;
            this.mCacheViewArray=new SparseArray<>(mList.size());
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view==o;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
           View view=mCacheViewArray.get(position);

           if(view==null){
               Context context=container.getContext();
               view=LayoutInflater.from(context).inflate(R.layout.item_image_browser,container,false);
               view.setTag(position);

               PhotoView imageView=view.findViewById(R.id.photoView);
               PhotoViewAttacher attacher=new PhotoViewAttacher(imageView);
               attacher.setOnDoubleTapListener(new PhoteViewOnDoubleTapListener(attacher));
               attacher.setOnLongClickListener(v->{
                   //
                   ImageBrowserActivity.this.onLongClick();
                   return false;
               });
               ImageLoader.loadNormal(context,mList.get(position),imageView);

               DismissFrameLayout layout=view.findViewById(R.id.dismissContaineer);
               layout.setDismissListener(mOnDismissListener);
               mCacheViewArray.put(position,view);
           }
           container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View)object);
        }
    }

    private class PhoteViewOnDoubleTapListener extends DefaultOnDoubleTapListener{
        private PhotoViewAttacher mPhotoViewAttacher;
        private boolean canZoom=true;

        PhoteViewOnDoubleTapListener(PhotoViewAttacher photoViewAttacher){
            super(photoViewAttacher);
            this.mPhotoViewAttacher=photoViewAttacher;
        }

        @Override
        public boolean onDoubleTap(MotionEvent ev) {
            if(mPhotoViewAttacher==null){
                return false;
            }
            try{
                float x=ev.getX();
                float y=ev.getY();
                if(canZoom){
                    mPhotoViewAttacher.setScale(mPhotoViewAttacher.getMediumScale(),x,y,true);
                }else {
                    mPhotoViewAttacher.setScale(mPhotoViewAttacher.getMinimumScale(),x,y,true);
                }
                canZoom=!canZoom;
            }catch (ArrayIndexOutOfBoundsException e){

            }
            return true;

        }
    }
}
