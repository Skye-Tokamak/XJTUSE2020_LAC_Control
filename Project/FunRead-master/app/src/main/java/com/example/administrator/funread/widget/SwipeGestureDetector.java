package com.example.administrator.funread.widget;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import com.example.administrator.funread.BuildConfig;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 作者：created by weidiezeng on 2019/8/18 15:37
 * 邮箱：1067875902@qq.com
 * 描述：滑动手势探测器
 */
public class SwipeGestureDetector {
    public static final int DIRECTION_LEFT = 0x00;
    public static final int DIRECTION_RIGHT = 0x01;
    public static final int DIRECTION_TOP = 0x02;
    public static final int DIRECTION_BOTTOM = 0x03;
    private static final String TAG="SwipGestureDetector";
    private static final boolean DEBUG=BuildConfig.DEBUG;
    private OnSwipeGestureListener listener;
    private int touchSlop;//
    private float initialMotionX,initialMotionY;//初始坐标
    private boolean isBeginDragged;//是否开始拖动标志
    private float lastMotionX,lastMotioY;//上一个位置坐标
    @Direction
    private int direction;

    public SwipeGestureDetector(Context context, @NonNull OnSwipeGestureListener listener){
        this.listener=listener;
        //getSceleTouchSlop()距离(以像素为单位)在我们认为用户在滚动之前就可以移动
        touchSlop=ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public boolean onInterceptTouchEvent(MotionEvent event){
        int action=event.getAction();

        //两指
        int pointerCount=event.getPointerCount();
        if(pointerCount>1){
            return true;
        }
        float x=event.getRawX();
        float y=event.getRawY();

        if(DEBUG)
            Log.d(TAG,"onInterceptTouchEvent:"+x+"-"+y);
        if(action==MotionEvent.ACTION_CANCEL||action==MotionEvent.ACTION_UP){
            //释放
            reset(initialMotionX,initialMotionY);
            return false;
        }
        if(action!=MotionEvent.ACTION_DOWN){
            if(isBeginDragged){
                return true;
            }
        }

        switch (action){
            case MotionEvent.ACTION_DOWN:
                initialMotionX=lastMotionX=x;
                initialMotionY=lastMotioY=y;
                break;
            case MotionEvent.ACTION_MOVE:
                //x轴移动距离
                final float xDiff=Math.abs(x-initialMotionX);
                //y轴移动距离
                final float yDiff=Math.abs(y-initialMotionY);
                if(xDiff>touchSlop&&xDiff>yDiff){
                    isBeginDragged=true;
                    if(x-initialMotionX>0){
                        direction=DIRECTION_RIGHT;
                        if(DEBUG)
                            Log.d(TAG,"OnInterceptTouchEvent: RIGHT");
                    }else {
                        direction=DIRECTION_LEFT;
                        if(DEBUG)
                            Log.d(TAG,"onInterceptTouchEvent: LEFT");
                    }
                }else if(yDiff>touchSlop&&yDiff>xDiff){
                    isBeginDragged=true;
                    if(y-initialMotionY>0){
                        direction=DIRECTION_BOTTOM;
                        if(DEBUG)
                            Log.d(TAG,"OnInterceptTouchEvent: BOTTOM");
                    }else {
                        direction=DIRECTION_TOP;
                        if(DEBUG)
                            Log.d(TAG,"OnInterceptTouchEvent: TOP");
                    }
                }
                break;
        }
        return isBeginDragged;
    }

    public boolean onTouchEvent(MotionEvent event){
        int action=event.getAction();
        int pointerCount=event.getPointerCount();
        if(pointerCount>1){
            return true;
        }
        float x=event.getRawX();
        float y=event.getRawY();
        if(DEBUG)
            Log.d(TAG,"onTouchEvent:"+x+"-"+y);

        switch (action){
            case MotionEvent.ACTION_DOWN:
                initialMotionX=lastMotionX=x;
                initialMotionY=lastMotioY=y;
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaX=x-lastMotionX;
                final float deltaY=y-lastMotioY;
                lastMotionX=x;
                lastMotioY=y;
                if(isBeginDragged){
                    if(direction==DIRECTION_LEFT){
                        listener.onSwipeLeft(deltaX,deltaY);
                    }else if(direction==DIRECTION_RIGHT){
                        listener.onSwipeRight(deltaX,deltaY);
                    }else if(direction==DIRECTION_TOP){
                        listener.onSwipeTop(deltaX,deltaY);
                    }else if(direction==DIRECTION_BOTTOM){
                        listener.onSwipeBottom(deltaX,deltaY);
                    }
                }else{
                    final float xDiff=Math.abs(x-initialMotionX);
                    final float yDiff=Math.abs(y-initialMotionY);
                    if(xDiff>touchSlop&&xDiff>yDiff){
                        isBeginDragged=true;
                        if(x-initialMotionX>0){
                            direction=DIRECTION_RIGHT;
                            if(DEBUG)
                                Log.d(TAG,"OnTouchEvent: RIGHT");
                        }else {
                            direction=DIRECTION_LEFT;
                            if(DEBUG)
                                Log.d(TAG,"onTouchEvent: LEFT");
                        }
                    }else if(yDiff>touchSlop&&yDiff>xDiff){
                        isBeginDragged=true;
                        if(y-initialMotionY>0){
                            direction=DIRECTION_BOTTOM;
                            if(DEBUG)
                                Log.d(TAG,"OnTouchEvent: BOTTOM");
                        }else {
                            direction=DIRECTION_TOP;
                            if(DEBUG)
                                Log.d(TAG,"OnTouchEvent: TOP");
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                reset(x,y);
                break;
            case MotionEvent.ACTION_CANCEL:
                reset(x,y);
                break;
        }
        return true;
    }
    private void reset(float x, float y) {
        if(isBeginDragged){
            listener.onFinish(direction,x-initialMotionX,y-initialMotionY);

        }
        isBeginDragged=false;
    }

    @IntDef({DIRECTION_LEFT,DIRECTION_RIGHT,DIRECTION_TOP,DIRECTION_BOTTOM})
    @Retention(RetentionPolicy.SOURCE)
    @interface Direction{

    }

    //接口供SwipFramlayout回调
    public interface OnSwipeGestureListener {
        void onSwipeLeft(float deltaX, float deltaY);

        void onSwipeRight(float deltaX, float deltaY);

        void onSwipeTop(float deltaX, float deltaY);

        void onSwipeBottom(float deltaX, float deltaY);

        void onFinish(@Direction int direction, float distanceX, float distanceY);
    }

}
