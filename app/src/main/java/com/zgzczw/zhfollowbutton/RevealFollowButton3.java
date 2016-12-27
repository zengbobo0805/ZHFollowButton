package com.zgzczw.zhfollowbutton;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.TextView;

public class RevealFollowButton3 extends FrameLayout {
    protected boolean mIsFollowed;
    private TextView mFollowTv;
    private TextView mUnFollowTv;
    float mCenterX;
    float mCenterY;
    float mRevealRadius = 0;
    float radius = 0;
    private Path mPath = new Path();
    private Path mPath1 = new Path();
    private Path mPath2 = new Path();
    private boolean isLoadingRipple = false;

    public RevealFollowButton3(Context paramContext) {
        super(paramContext);
        init();
    }

    public RevealFollowButton3(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        init();
    }

    public RevealFollowButton3(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
        init();
    }

    private boolean isValidClick(float x, float y) {
        if (x >= 0 && x <= getWidth() && y >= 0 && y <= getHeight()) {
            return true;
        }
        return false;
    }

    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_UP:
                return true;
        }
        return false;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (isLoadingRipple) {
            return false;

        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if (!isValidClick(event.getX(), event.getY())) {
                    return false;
                }
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    mIsFollowed = !mIsFollowed;
                }
                mCenterX = event.getX();
                mCenterY = event.getY();
                float r11 = (float) Math.hypot(getMeasuredWidth() - mCenterX, getMeasuredHeight() - mCenterY);
                float r21 = (float) Math.hypot(mCenterX, mCenterY);
                float r31 = (float) Math.hypot(getMeasuredWidth() - mCenterX, mCenterY);
                float r41 = (float) Math.hypot(mCenterX, getMeasuredHeight() - mCenterY);
                radius = Math.max(Math.max(r11, r21), Math.max(r31, r41));
                System.out.println("RevealFollowButton init radius:" + radius);
                mRevealRadius = radius - ViewConfiguration.get(getContext()).getScaledTouchSlop();

                mFollowTv.setVisibility(View.VISIBLE);
                mUnFollowTv.setVisibility(View.VISIBLE);
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
                if (!isValidClick(event.getX(), event.getY())) {
                    return false;
                }
                mCenterX = event.getX();
                mCenterY = event.getY();
                mRevealRadius = ViewConfiguration.get(getContext()).getScaledTouchSlop();
                float r1 = (float) Math.hypot(getMeasuredWidth() - mCenterX, getMeasuredHeight() - mCenterY);
                float r2 = (float) Math.hypot(mCenterX, mCenterY);
                float r3 = (float) Math.hypot(getMeasuredWidth() - mCenterX, mCenterY);
                float r4 = (float) Math.hypot(mCenterX, getMeasuredHeight() - mCenterY);
                radius = Math.max(Math.max(r1, r2), Math.max(r3, r4));
                System.out.println("RevealFollowButton init radius:" + radius);


                mFollowTv.setVisibility(View.VISIBLE);
                mUnFollowTv.setVisibility(View.VISIBLE);
                setFollowed(mIsFollowed, true);
                return true;
        }
        return false;
    }

    private void init() {

        mFollowTv = new TextView(getContext());
        mFollowTv.setText("关注");
        mFollowTv.setGravity(17);
        mFollowTv.setSingleLine();
        mFollowTv.setBackgroundColor(Color.WHITE);
        mFollowTv.setTextColor(Color.BLACK);
        addView(this.mFollowTv);
        mUnFollowTv = new TextView(getContext());
        mUnFollowTv.setText("未关注");
        mUnFollowTv.setGravity(17);
        mUnFollowTv.setSingleLine();
        mUnFollowTv.setBackgroundColor(Color.RED);
        mUnFollowTv.setTextColor(Color.WHITE);
        addView(this.mUnFollowTv);

        mFollowTv.setPadding(40, 40, 40, 40);
        mUnFollowTv.setPadding(40, 40, 40, 40);
        setFollowed(false, false);
    }

    protected void setFollowed(boolean isFollowed, boolean needAnimate) {
        mIsFollowed = isFollowed;
        if (isFollowed) {
            mUnFollowTv.setVisibility(View.VISIBLE);
            mFollowTv.setVisibility(View.VISIBLE);
//            mFollowTv.bringToFront();
        } else {
            mUnFollowTv.setVisibility(View.VISIBLE);
            mFollowTv.setVisibility(View.VISIBLE);
//            mUnFollowTv.bringToFront();
        }
        if (needAnimate) {
            ValueAnimator animator = ObjectAnimator.ofFloat(mFollowTv, "empty"
                    , radius-ViewConfiguration.get(getContext()).getScaledTouchSlop()
                    , 0.0F);
            animator.setDuration(2000L);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    isLoadingRipple = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    isLoadingRipple = false;
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    isLoadingRipple = false;

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mRevealRadius = (Float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            animator.start();
        }
    }

    private boolean drawBackground(View paramView) {
//        if (mIsFollowed && paramView == mUnFollowTv) {
//            return true;
//        } else if (!mIsFollowed && paramView == mFollowTv) {
//            return true;
//        }
//        return false;
        if (paramView == mFollowTv) {
            return true;
        }
        return false;
    }

    protected boolean drawChild(Canvas canvas, View paramView, long paramLong) {
        System.out.println("RevealFollowButton mIsFollowed:" + mIsFollowed + ",mRevealRadius:" + mRevealRadius);
        if (radius <= 0) {
            radius = (float) Math.hypot(getMeasuredWidth(), getMeasuredHeight());
        }
        if (drawBackground(paramView)) {
            return super.drawChild(canvas, paramView, paramLong);
        }
        int i = canvas.save();
        mPath.reset();
        mPath1.reset();
        mPath2.reset();
        if (mIsFollowed) {
            mPath.addCircle(mCenterX, mCenterY, radius - mRevealRadius, Path.Direction.CW);
        } else {
            mPath1.addCircle(mCenterX, mCenterY, radius, Path.Direction.CW);
            mPath2.addCircle(mCenterX, mCenterY, radius - mRevealRadius, Path.Direction.CW);
//            DIFFERENCE(0), //最终区域为region1 与 region2不同的区域
//                    INTERSECT(1), // 最终区域为region1 与 region2相交的区域
//                    UNION(2),      //最终区域为region1 与 region2组合一起的区域
//                    XOR(3),        //最终区域为region1 与 region2相交之外的区域
//                    REVERSE_DIFFERENCE(4), //最终区域为region2 与 region1不同的区域
//                    REPLACE(5); //最终区域为为region2的区域
            this.mPath.op(mPath1, mPath2, Path.Op.XOR);
        }
        canvas.clipPath(this.mPath);
        boolean bool2 = super.drawChild(canvas, paramView, paramLong);
        canvas.restoreToCount(i);
        return bool2;
    }

    private void drawChildDraw(Canvas canvas, View paramView, long paramLong) {

    }

}