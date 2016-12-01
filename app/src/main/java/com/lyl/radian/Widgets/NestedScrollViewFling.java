package com.lyl.radian.Widgets;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * Created by Yannick on 24.11.2016.
 */

public class NestedScrollViewFling extends NestedScrollView {

    private OnFlingEndReachedTopListener mListener;
    private Boolean isBeingTouched = false;

    @SuppressWarnings("unused")
    private int slop;
    @SuppressWarnings("unused")
    private float mInitialMotionX;
    @SuppressWarnings("unused")
    private float mInitialMotionY;

    private void init(Context context) {

        ViewConfiguration config = ViewConfiguration.get(context);
        slop = config.getScaledEdgeSlop();
    }

    private float xDistance, yDistance, lastX, lastY;
    @SuppressWarnings("unused")
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final float x = ev.getX();
        final float y = ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                lastX = ev.getX();
                lastY = ev.getY();
                // This is very important line that fixes
                computeScroll();
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();
                xDistance += Math.abs(curX - lastX);
                yDistance += Math.abs(curY - lastY);
                lastX = curX;
                lastY = curY;
                if (xDistance > yDistance) {
                    return false;
                }
        }
        return super.onInterceptTouchEvent(ev);
    }
    public interface OnScrollChangedListener {
        void onScrollChanged(NestedScrollView who, int l, int t, int oldl,
                             int oldt);
    }
    private OnScrollChangedListener mOnScrollChangedListener;
    public void setOnScrollChangedListener(OnScrollChangedListener listener) {
        mOnScrollChangedListener = listener;
    }
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mOnScrollChangedListener != null) {
            mOnScrollChangedListener.onScrollChanged(this, l, t, oldl, oldt);
        }
    }

    public NestedScrollViewFling(Context context) {

        super(context);
        init(context);
    }

    public NestedScrollViewFling(Context context, AttributeSet attrs,
                                 int defStyle) {

        super(context, attrs, defStyle);
        init(context);
    }

    public NestedScrollViewFling(Context context, AttributeSet attrs) {

        super(context, attrs);
        init(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        int action = ev.getAction();

        if ((action == MotionEvent.ACTION_DOWN) || (action == MotionEvent.ACTION_MOVE))
            isBeingTouched = true;
        else
            isBeingTouched = false;

        return super.onTouchEvent(ev);
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY)
    {
        if (clampedY && scrollY == 0)
        {
            if (mListener != null)
                mListener.onTopReached(isBeingTouched);
        }

        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
    }

    public OnFlingEndReachedTopListener getOnTopReachedListener()
    {
        return mListener;
    }

    public void setOnTopReachedListener(
            OnFlingEndReachedTopListener onTopReachedListener)
    {
        mListener = onTopReachedListener;
    }

    /**
     * Event listener.
     */
    public interface OnFlingEndReachedTopListener
    {
        public void onTopReached(Boolean isBeingTouch);
    }
}
