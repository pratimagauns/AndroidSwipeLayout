package com.example.pratima.swipeablelayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by pratima on 14/03/16.
 */
public class SwipeLayout extends RelativeLayout implements View.OnTouchListener {

    Context mContext;

    RelativeLayout rightView;
    RelativeLayout leftView;
    RelativeLayout contentView;
    RelativeLayout bottomView;
    RelativeLayout parentView;

    boolean isLeftSwiped = false;
    boolean isRightSwiped = false;
    boolean isBottomViewDisplayed = false;
    boolean touchDown = false;

    float mDownX;
    private int mSwipeSlop = -1;

    public SwipeLayout(Context context) {
        super(context);
        mContext = context;
        init(null, 0);
    }

    public SwipeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init(attrs, 0);
    }

    public SwipeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init(attrs, defStyleAttr);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if (mSwipeSlop < 0) {
            mSwipeSlop = ViewConfiguration.get(mContext).
                    getScaledTouchSlop();
        }
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(isLeftSwiped || isRightSwiped) {
                    resetView();
                    return false;
                }
                mDownX = motionEvent.getX();
                touchDown = true;
                break;
            case MotionEvent.ACTION_CANCEL:
                if(touchDown) {
                    if(isLeftSwiped && contentView.getTranslationX() < 0) {
                        animateViewLeft();
                    }
                    else if(isRightSwiped && contentView.getTranslationX() > 0) {
                        animateViewRight();
                    }
                }
                touchDown = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (motionEvent.getX() < mDownX) {
                    float x = motionEvent.getX() + view.getTranslationX();
                    float deltaX = x - mDownX;
                    float deltaXAbs = Math.abs(deltaX);
                    if (!isLeftSwiped && !isRightSwiped) {
                        if (deltaXAbs > mSwipeSlop) {
                            isLeftSwiped = true;
                        }
                    }
                    if (isLeftSwiped) {
                        if(deltaX < contentView.getTranslationX()) {
                            Log.d("Animation", String.format("TransitionX - %s", deltaX));
                            contentView.setTranslationX(deltaX);
                        }
                    }
                } else {
                    //handle right swipe
                    float x = motionEvent.getX() + view.getTranslationX();
                    float deltaX = x - mDownX;
                    float deltaXAbs = Math.abs(deltaX);
                    if (!isRightSwiped && !isLeftSwiped) {
                        if (deltaXAbs > mSwipeSlop) {
                            isRightSwiped = true;
                        }
                    }
                    if (isRightSwiped) {
                        if(deltaX > contentView.getTranslationX()) {
                            Log.d("Animation", String.format("TransitionX - %s", deltaX));
                            contentView.setTranslationX(deltaX);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                View v = null;
                if (isLeftSwiped) {
                    //swipe left
                    animateViewLeft();
                    touchDown = false;
                } else if (isRightSwiped){
                    //swipe right
                    animateViewRight();
                    touchDown = false;
                }
                else if(touchDown){
                    Log.d("Animation", "TapDetected");
                    if(isBottomViewDisplayed) {
                        collapseBottom();
                    }
                    else {
                        expandBottom();
                    }
                }
            break;
            default:
                return false;
        }
        return true;
    }

    private void init(AttributeSet attrs, int defStyle) {

        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = (RelativeLayout) inflater.inflate(R.layout.layout_swipe, this, true);
        parentView = (RelativeLayout) view.findViewById(R.id.parent_view);
        View gestureView = parentView.findViewById(R.id.genture_view);
        gestureView.setOnTouchListener(this);

        rightView = (RelativeLayout) parentView.findViewById(R.id.right_view);
        leftView = (RelativeLayout) parentView.findViewById(R.id.left_view);
        contentView = (RelativeLayout) parentView.findViewById(R.id.content_view);
        bottomView = (RelativeLayout) parentView.findViewById(R.id.bottom_view);
    }

    private void expandBottom() {
        isBottomViewDisplayed = true;
//        bottomView.setVisibility(VISIBLE);
        final float scale = getContext().getResources().getDisplayMetrics().density;
        int pixels = (int) (180 * scale + 0.5f);

        RelativeLayout.LayoutParams rel_btn = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, pixels);
        rel_btn.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        parentView.setLayoutParams(rel_btn);
        invalidate();
    }

    private void collapseBottom() {
        isBottomViewDisplayed = false;
       // bottomView.setVisibility(GONE);
        final float scale = getContext().getResources().getDisplayMetrics().density;
        int pixels = (int) (100 * scale + 0.5f);

        RelativeLayout.LayoutParams rel_btn = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, pixels);
        rel_btn.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        parentView.setLayoutParams(rel_btn);
        invalidate();
    }

    private void animateViewLeft() {
        isLeftSwiped = true;
        contentView.animate().alpha(0).setDuration(1).setListener(
                new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        // Restore animated values
                        contentView.setAlpha(1);
                        contentView.setTranslationX(-leftView.getWidth());
                    }
                });
    }

    private void animateViewRight() {
        isRightSwiped = true;
        contentView.animate().alpha(0).setDuration(1).setListener(
                new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        // Restore animated values
                        contentView.setAlpha(1);
                        contentView.setTranslationX(rightView.getWidth());
                    }
                });
    }

    private void resetView() {
        isLeftSwiped = false;
        isRightSwiped = false;
        contentView.animate().alpha(0).setDuration(1).setListener(
                new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);

                        contentView.setAlpha(1);
                        contentView.setTranslationX(0);
                    }
                });
    }
}
