package com.paulyung.pyphoto.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by yang on 2016/12/7.
 * paulyung@outlook.com
 * 设一个时间长度为 T，若在 0 时刻 触发 DOWN 事件，
 * 0 ~ T 时刻内，若有 UP 或者 CANCEL 事件，且没有发生过 “滑动”，则触发 Click 事件，
 * 注意这里的 “滑动” 只的并非单纯的 MOVE 事件，而是经过处理的 MOVE 事件，
 * “滑动” 的判定方式：定义一个最小滑动距离 MIN_MOVE_DIS，若小于这个距离，则不算滑动
 * 0 ~ T 时刻内，若发生过滑动事件，则按照 ViewPager 原有事件进行处理
 * 若超过 T 时间，没有抬起手指，或者没有发生过 “滑动”，则停止触发所有事件 （让ViewPager 不往下分发事件）
 */

public class TouchViewPager extends ViewPager {
    private static final int TIME_T = 1000;
    private static final int MIN_MOVE_DIS = 30;
    private OnViewPagerClickListener mOnViewPagerClickListener;
    private long startTime;
    private boolean canMove = false;
    private float startX;

    public TouchViewPager(Context context) {
        super(context);
    }

    public TouchViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                startTime = getCurrentTime();
                startX = ev.getX();
                canMove = false;//clear
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                //小于TIME_T时间，并切在这段时间内没有移动过
                if (getCurrentTime() - startTime < TIME_T && !canMove) {
                    mOnViewPagerClickListener.onClick();
                    startTime = getCurrentTime();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (getCurrentTime() - startTime < TIME_T
                        && Math.abs(startX - ev.getX()) > MIN_MOVE_DIS)
                    canMove = true;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int event = ev.getActionMasked();
        if (!canMove && event == MotionEvent.ACTION_MOVE)
            return true;
        return super.onTouchEvent(ev);
    }

    public interface OnViewPagerClickListener {
        void onClick();
    }

    public void setOnViewPagerClickListener(OnViewPagerClickListener l) {
        mOnViewPagerClickListener = l;
    }

    private long getCurrentTime() {
        return System.currentTimeMillis();
    }
}
