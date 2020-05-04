package com.example.administrator.funread.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 作者：created by weidiezeng on 2019/8/18 16:41
 * 邮箱：1067875902@qq.com
 * 描述：
 */
public class DismissFrameLayout extends FrameLayout {

    private static final String TAG="DismissFrameLayout";
    private SwipeGestureDetector swipeGestureDetector;
    private OnDismissListener dismissListener;
    private int initHeight;
    private int initLeft=0;
    private int initWidth;
    private int initTop=0;

    public DismissFrameLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public DismissFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DismissFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DismissFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        swipeGestureDetector=new SwipeGestureDetector(getContext(),
                new SwipeGestureDetector.OnSwipeGestureListener() {
                    @Override
                    public void onSwipeLeft(float deltaX, float deltaY) {

                    }

                    @Override
                    public void onSwipeRight(float deltaX, float deltaY) {

                    }

                    @Override
                    public void onSwipeTop(float deltaX, float deltaY) {

                        dragChildView(deltaX,deltaY,SwipeGestureDetector.DIRECTION_TOP);
                    }

                    @Override
                    public void onSwipeBottom(float deltaX, float deltaY) {

                        dragChildView(deltaX,deltaY,SwipeGestureDetector.DIRECTION_BOTTOM);
                    }

                    @Override
                    public void onFinish(@SwipeGestureDetector.Direction int direction, float distanceX, float distanceY) {

                        if(dismissListener!=null
                                && direction==SwipeGestureDetector.DIRECTION_BOTTOM||direction==SwipeGestureDetector.DIRECTION_TOP){
                            if(Math.abs(distanceY)>initHeight/10){
                                dismissListener.onDismiss();
                            }else {
                                dismissListener.onCancel();
                                reset();
                            }
                        }
                    }
                });

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return swipeGestureDetector.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return swipeGestureDetector.onTouchEvent(event);
    }

    private void dragChildView(float deltaX, float deltaY, @SwipeGestureDetector.Direction int direction) {
        int count=getChildCount();
        if(count>0){
            View view=getChildAt(0);
            scaleAndMove(view,deltaX,deltaY,direction);
        }
    }

    /**
     * 最小缩小到1/2
     * @param view
     * @param deltaX
     * @param deltaY
     * @param direction
     */
    private void scaleAndMove(View view, float deltaX, float deltaY, @SwipeGestureDetector.Direction int direction) {

        MarginLayoutParams params=(MarginLayoutParams)view.getLayoutParams();
        if(params==null){
            params=new MarginLayoutParams(view.getWidth(),view.getHeight());

        }
        if(params.width<=0&&params.height<=0){
            params.width=view.getWidth();
            params.height=view.getHeight();
        }
        if(initHeight<=0){
            initHeight=view.getHeight();
            initWidth=view.getWidth();
            initLeft=params.leftMargin;
            initTop=params.topMargin;
        }
        //百分比
        float percent=0;
        if(direction==SwipeGestureDetector.DIRECTION_BOTTOM){
            percent=deltaY/getHeight();
        }else if(direction==SwipeGestureDetector.DIRECTION_TOP){
            percent=-deltaY/getHeight();
        }

        int scaleX=(int)(initWidth*percent);
        int scaleY=(int)(initHeight*percent);
        params.width=params.width-scaleX;
        params.height=params.height-scaleY;
        Log.d("scaleDown", params.width + "-" + params.height);
        params.leftMargin+=(deltaX+scaleX/2);
        params.topMargin+=(deltaY+scaleY/2);
        view.setLayoutParams(params);
        if(dismissListener!=null){
            dismissListener.onScaleProgress(percent);
        }
    }

    private void reset(){

        int count=getChildCount();
        if(count>0){
            View view=getChildAt(0);
            MarginLayoutParams params=(MarginLayoutParams)view.getLayoutParams();
            params.width=initWidth;
            params.height=initHeight;
            params.leftMargin=initLeft;
            params.topMargin=initTop;
            view.setLayoutParams(params);
        }
    }
    public void setDismissListener(OnDismissListener dismissListener){
        this.dismissListener=dismissListener;
    }

    public interface OnDismissListener {
        void onScaleProgress(float scale);

        void onDismiss();

        void onCancel();
    }
}
