package com.jamesfchen.yposedplugin3;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.OverScroller;

/**
 * Copyright ® $ 2017
 * All right reserved.
 *
 * @author: hawks.jamesf
 * @since: May/08/2021  Sat
 */
public class MyViewGroup2 extends LinearLayout {
    public MyViewGroup2(Context context) {
        super(context);
    }

    public MyViewGroup2(Context context, AttributeSet attrs) {
        super(context, attrs);
        initScrollView();
    }

    public MyViewGroup2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initScrollView();

    }
    private OverScroller mScroller;
    private int mTouchSlop;
    private int mMinimumVelocity;
    private int mMaximumVelocity;
    private int mOverscrollDistance;
    private int mOverflingDistance;
    private float mVerticalScrollFactor;
    private void initScrollView() {
        mScroller = new OverScroller(getContext());
        setFocusable(true);
        setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
        setWillNotDraw(false);
        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        mOverscrollDistance = configuration.getScaledOverscrollDistance();
        mOverflingDistance = configuration.getScaledOverflingDistance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mVerticalScrollFactor = configuration.getScaledVerticalScrollFactor();
        }
    }

    private boolean mIsBeingDragged = false;
    private float mLastMotionY;
    private int mActivePointerId;
//    private VelocityTracker mVelocityTracker;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        Log.w("cjf", "MyViewGroup2#onInterceptTouchEvent + "+action);
//        return super.onInterceptTouchEvent(ev);
        //在捕获了滑动事件，通过onInterceptTouchEvent返回ture拦截不向下传递，通过requestDisallowInterceptTouchEvent不让父节点拦截，这样就能只能自己处理滑动事件，不受其他干扰
        if (action == MotionEvent.ACTION_DOWN) {
            final int y = (int) ev.getY();
            mLastMotionY = y;
            mActivePointerId = ev.getPointerId(0);
//            mVelocityTracker.addMovement(ev);
            mIsBeingDragged =  false;
        } else if (action == MotionEvent.ACTION_UP) {
            mIsBeingDragged =  false;
        } else if (action == MotionEvent.ACTION_MOVE) {
            final int activePointerId = mActivePointerId;
            final int pointerIndex = ev.findPointerIndex(activePointerId);
            final int y = (int) ev.getY(pointerIndex);
            final int yDiff = (int) Math.abs(y - mLastMotionY);
            if (yDiff > mTouchSlop && (getNestedScrollAxes() & SCROLL_AXIS_VERTICAL) == 0) {
                mIsBeingDragged = true;
                mLastMotionY = y;
//                initVelocityTrackerIfNotExists();
//                mVelocityTracker.addMovement(ev);
//                mNestedYOffset = 0;
//                if (mScrollStrictSpan == null) {
//                    mScrollStrictSpan = StrictMode.enterCriticalSpan("ScrollView-scroll");
//                }
                final ViewParent parent = getParent();
                if (parent != null) {
                    parent.requestDisallowInterceptTouchEvent(true);
                }
            }

        }
        return mIsBeingDragged;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        Log.w("cjf", "MyViewGroup2#onTouchEvent "+action);
        if (action == MotionEvent.ACTION_DOWN) {
//            getParent().requestDisallowInterceptTouchEvent(true);
        } else if (action == MotionEvent.ACTION_UP) {
        } else if (action == MotionEvent.ACTION_MOVE) {
        } else if (action == MotionEvent.ACTION_CANCEL) {
        }
        return super.onTouchEvent(event);
    }
}
