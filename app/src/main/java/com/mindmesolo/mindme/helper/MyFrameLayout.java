package com.mindmesolo.mindme.helper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.Toast;

/**
 * Created by enest_09 on 1/3/2017.
 */

public class MyFrameLayout extends FrameLayout {

    private static final int mWidth = 500;
    private float mDisplacementX;
    // private float mLastMoveX;
    private float mDisplacementY;
    private float mInitialTx;
    private boolean mTracking;
    private OnTouchListener mTouchListener = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:

                    mDisplacementX = event.getRawX();
                    mDisplacementY = event.getRawY();

                    mInitialTx = getTranslationX();

                    return true;

                case MotionEvent.ACTION_MOVE:
                    // get the delta distance in X and Y direction
                    float deltaX = event.getRawX() - mDisplacementX;
                    float deltaY = event.getRawY() - mDisplacementY;
                    // updatePressedState(false);

                    // set the touch and cancel event
                    if ((Math.abs(deltaX) > ViewConfiguration.get(getContext())
                            .getScaledTouchSlop() * 2 && Math.abs(deltaY) < Math
                            .abs(deltaX) / 2)
                            || mTracking) {

                        mTracking = true;

                        if (getTranslationX() <= mWidth / 2
                                && getTranslationX() >= -(mWidth / 2)) {

                            setTranslationX(mInitialTx + deltaX);
                            return true;
                        }
                    }

                    break;

                case MotionEvent.ACTION_UP:

                    if (mTracking) {
                        mTracking = false;
                        float currentTranslateX = getTranslationX();

                        if (currentTranslateX > mWidth / 4) {
                            rightSwipe();
                        } else if (currentTranslateX < -(mWidth / 4)) {
                            leftSwipe();
                        }

                        // comment this line if you don't want your frame layout to
                        // take its original position after releasing the touch
                        setTranslationX(0);
                        return true;
                    } else {
                        // handle click event
                        setTranslationX(0);
                    }
                    break;
            }
            return false;
        }
    };

    public MyFrameLayout(Context context) {
        super(context);
        initialize(context);
    }

    public MyFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public MyFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    private void initialize(Context context) {
        setOnTouchListener(mTouchListener);
    }

    private void rightSwipe() {
        Toast.makeText(this.getContext(), "Swipe to the right",
                Toast.LENGTH_SHORT).show();
        // write code to remove the data from source and notify change to adapter
        // if you want to change remove the item on swipe.
    }

    private void leftSwipe() {
        Toast.makeText(this.getContext(), "Swipe to the left",
                Toast.LENGTH_SHORT).show();
        // write code to remove the data from source and notify change to adapter
        // if you want to change remove the item on swipe.
    }
}
