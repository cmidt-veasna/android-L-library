package com.thecamtech.android.library.path;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

import com.thecamtech.android.library.R;
import com.thecamtech.android.library.view.DelightfulButton;

/**
 * Created by veasnatemp on 9/11/14.
 */
public class MenuPath extends DelightfulButton.Outline {

    private static final Shape[] MENU_DONE = new Shape[]{
            new Shape(0.00f, 0.25f, 1.00f, 0.25f, /*Second Line*/ 0.00f, 0.50f, 1.00f, 0.50f, /*Third Line*/ 0.00f, 0.75f, 1.00f, 0.75f),
            new Shape(0.08f, 0.21f, 0.95f, 0.32f, /*Second Line*/ 0.05f, 0.50f, 0.95f, 0.50f, /*Third line*/ 0.04f, 0.69f, 0.97f, 0.77f),
            new Shape(0.16f, 0.17f, 0.90f, 0.37f, /*Second Line*/ 0.10f, 0.50f, 0.90f, 0.50f, /*Third line*/ 0.08f, 0.63f, 0.94f, 0.79f),
            new Shape(0.24f, 0.13f, 0.85f, 0.44f, /*Second Line*/ 0.15f, 0.50f, 0.85f, 0.50f, /*Third line*/ 0.12f, 0.57f, 0.91f, 0.81f),
            new Shape(0.32f, 0.09f, 0.80f, 0.51f, /*Second Line*/ 0.20f, 0.50f, 0.80f, 0.50f, /*Third line*/ 0.16f, 0.51f, 0.88f, 0.83f),
            new Shape(0.40f, 0.05f, 0.75f, 0.58f, /*Second Line*/ 0.25f, 0.50f, 0.75f, 0.50f, /*Third line*/ 0.20f, 0.45f, 0.85f, 0.85f),
            new Shape(0.48f, 0.05f, 0.70f, 0.65f, /*Second Line*/ 0.30f, 0.50f, 0.70f, 0.50f, /*Third line*/ 0.24f, 0.39f, 0.82f, 0.87f),
            new Shape(0.56f, 0.05f, 0.65f, 0.72f, /*Second Line*/ 0.35f, 0.50f, 0.65f, 0.50f, /*Third line*/ 0.28f, 0.33f, 0.79f, 0.89f),
            new Shape(0.64f, 0.10f, 0.60f, 0.79f, /*Second Line*/ 0.40f, 0.50f, 0.60f, 0.50f, /*Third line*/ 0.32f, 0.27f, 0.76f, 0.91f),
            new Shape(0.72f, 0.15f, 0.55f, 0.86f, /*Second Line*/ 0.45f, 0.50f, 0.55f, 0.50f, /*Third line*/ 0.36f, 0.21f, 0.73f, 0.93f),
            new Shape(0.80f, 0.20f, 0.50f, 0.93f, /*Second Line*/ 0.50f, 0.50f, 0.50f, 0.50f, /*Third line*/ 0.40f, 0.15f, 0.70f, 0.95f),
            new Shape(0.88f, 0.25f, 0.50f, 1.00f, /*Second Line*/ 0.50f, 0.50f, 0.50f, 0.50f, /*Third line*/ 0.44f, 0.09f, 0.67f, 0.97f),
            new Shape(0.96f, 0.30f, 0.40f, 0.95f, /*Second Line*/ 0.50f, 0.50f, 0.50f, 0.50f, /*Third line*/ 0.48f, 0.03f, 0.64f, 1.00f),
            new Shape(0.88f, 0.35f, 0.37f, 0.90f, /*Second Line*/ 0.50f, 0.50f, 0.50f, 0.50f, /*Third line*/ 0.50f, 0.00f, 0.61f, 0.97f),
            new Shape(0.80f, 0.40f, 0.34f, 0.85f, /*Second Line*/ 0.50f, 0.50f, 0.50f, 0.50f, /*Third line*/ 0.50f, 0.00f, 0.58f, 0.95f),
            new Shape(0.72f, 0.45f, 0.31f, 0.80f, /*Second Line*/ 0.50f, 0.50f, 0.50f, 0.50f, /*Third line*/ 0.55f, 0.03f, 0.55f, 0.93f),
            new Shape(0.64f, 0.50f, 0.28f, 0.75f, /*Second Line*/ 0.50f, 0.50f, 0.50f, 0.50f, /*Third line*/ 0.60f, 0.06f, 0.52f, 0.91f),
            new Shape(0.56f, 0.55f, 0.25f, 0.70f, /*Second Line*/ 0.50f, 0.50f, 0.50f, 0.50f, /*Third line*/ 0.65f, 0.09f, 0.49f, 0.89f),
            new Shape(0.48f, 0.60f, 0.22f, 0.65f, /*Second Line*/ 0.50f, 0.50f, 0.50f, 0.50f, /*Third line*/ 0.70f, 0.12f, 0.46f, 0.87f),
            new Shape(0.40f, 0.65f, 0.19f, 0.60f, /*Second Line*/ 0.50f, 0.50f, 0.50f, 0.50f, /*Third line*/ 0.75f, 0.15f, 0.43f, 0.85f),
            new Shape(0.40f, 0.70f, 0.16f, 0.60f, /*Second Line*/ 0.50f, 0.50f, 0.50f, 0.50f, /*Third line*/ 0.80f, 0.18f, 0.40f, 0.83f),
            new Shape(0.40f, 0.75f, 0.12f, 0.55f, /*Second Line*/ 0.50f, 0.50f, 0.50f, 0.50f, /*Third line*/ 0.85f, 0.20f, 0.40f, 0.80f),
            //
            new Shape(0.40f, 0.80f, 0.12f, 0.55f, /*Second Line*/ 0.50f, 0.50f, 0.50f, 0.50f, /*Third line*/ 0.85f, 0.20f, 0.40f, 0.80f)
    };

    private static final Shape[] MENU_BACK = new Shape[]{
            new Shape(0.00f, 0.25f, 1.00f, 0.25f, /*Second Line*/ 0.00f, 0.50f, 1.00f, 0.50f, /*Third Line*/ 0.00f, 0.75f, 1.00f, 0.75f),

            new Shape(0.08f, 0.21f, 0.95f, 0.32f, /*Second Line*/ 0.05f, 0.50f, 0.95f, 0.50f, /*Third line*/ 0.04f, 0.69f, 0.97f, 0.77f),
            new Shape(0.16f, 0.17f, 0.90f, 0.37f, /*Second Line*/ 0.10f, 0.50f, 0.90f, 0.50f, /*Third line*/ 0.08f, 0.63f, 0.94f, 0.79f),
            new Shape(0.24f, 0.13f, 0.85f, 0.44f, /*Second Line*/ 0.15f, 0.50f, 0.85f, 0.50f, /*Third line*/ 0.12f, 0.57f, 0.91f, 0.81f),
            new Shape(0.32f, 0.09f, 0.80f, 0.51f, /*Second Line*/ 0.20f, 0.50f, 0.80f, 0.50f, /*Third line*/ 0.16f, 0.51f, 0.88f, 0.83f),
            new Shape(0.40f, 0.05f, 0.75f, 0.58f, /*Second Line*/ 0.25f, 0.50f, 0.75f, 0.50f, /*Third line*/ 0.20f, 0.45f, 0.85f, 0.85f),
            new Shape(0.48f, 0.05f, 0.70f, 0.65f, /*Second Line*/ 0.30f, 0.50f, 0.70f, 0.50f, /*Third line*/ 0.24f, 0.39f, 0.82f, 0.87f),
            new Shape(0.56f, 0.05f, 0.65f, 0.72f, /*Second Line*/ 0.35f, 0.50f, 0.65f, 0.50f, /*Third line*/ 0.28f, 0.33f, 0.79f, 0.89f),
            new Shape(0.64f, 0.10f, 0.60f, 0.79f, /*Second Line*/ 0.40f, 0.50f, 0.60f, 0.50f, /*Third line*/ 0.32f, 0.27f, 0.76f, 0.91f),
            new Shape(0.72f, 0.15f, 0.55f, 0.86f, /*Second Line*/ 0.45f, 0.50f, 0.55f, 0.50f, /*Third line*/ 0.36f, 0.21f, 0.73f, 0.93f),
            new Shape(0.80f, 0.20f, 0.50f, 0.93f, /*Second Line*/ 0.50f, 0.50f, 0.50f, 0.50f, /*Third line*/ 0.40f, 0.15f, 0.70f, 0.95f),
            new Shape(0.88f, 0.25f, 0.50f, 1.00f, /*Second Line*/ 0.50f, 0.50f, 0.50f, 0.50f, /*Third line*/ 0.44f, 0.09f, 0.67f, 0.97f),
            new Shape(0.96f, 0.30f, 0.40f, 0.95f, /*Second Line*/ 0.50f, 0.50f, 0.50f, 0.50f, /*Third line*/ 0.48f, 0.03f, 0.64f, 1.00f),
            new Shape(0.88f, 0.35f, 0.37f, 0.90f, /*Second Line*/ 0.50f, 0.50f, 0.50f, 0.50f, /*Third line*/ 0.50f, 0.00f, 0.61f, 0.97f),
            new Shape(0.80f, 0.40f, 0.34f, 0.85f, /*Second Line*/ 0.50f, 0.50f, 0.50f, 0.50f, /*Third line*/ 0.50f, 0.00f, 0.58f, 0.95f),
            new Shape(0.72f, 0.45f, 0.31f, 0.80f, /*Second Line*/ 0.50f, 0.50f, 0.50f, 0.50f, /*Third line*/ 0.55f, 0.03f, 0.55f, 0.93f),
            new Shape(0.64f, 0.50f, 0.28f, 0.75f, /*Second Line*/ 0.50f, 0.50f, 0.50f, 0.50f, /*Third line*/ 0.60f, 0.06f, 0.52f, 0.91f),
            new Shape(0.56f, 0.55f, 0.25f, 0.70f, /*Second Line*/ 0.50f, 0.50f, 0.50f, 0.50f, /*Third line*/ 0.65f, 0.09f, 0.49f, 0.89f),
            new Shape(0.48f, 0.60f, 0.22f, 0.65f, /*Second Line*/ 0.50f, 0.50f, 0.50f, 0.50f, /*Third line*/ 0.70f, 0.12f, 0.46f, 0.87f),
            new Shape(0.40f, 0.65f, 0.19f, 0.60f, /*Second Line*/ 0.50f, 0.50f, 0.50f, 0.50f, /*Third line*/ 0.75f, 0.15f, 0.43f, 0.85f),
            new Shape(0.40f, 0.70f, 0.16f, 0.60f, /*Second Line*/ 0.50f, 0.50f, 0.50f, 0.50f, /*Third line*/ 0.80f, 0.18f, 0.40f, 0.83f),
            new Shape(0.40f, 0.75f, 0.12f, 0.55f, /*Second Line*/ 0.50f, 0.50f, 0.50f, 0.50f, /*Third line*/ 0.85f, 0.20f, 0.40f, 0.80f),
            //
            new Shape(0.40f, 0.80f, 0.12f, 0.55f, /*Second Line*/ 0.50f, 0.50f, 0.50f, 0.50f, /*Third line*/ 0.85f, 0.20f, 0.40f, 0.80f)
    };

    private static final int[] MENU = {R.attr.state_menu};
    private static final int[] DONE = {R.attr.state_done};
    private static final int[] BACK = {R.attr.state_back};
    private static final int[] CLOSE = {R.attr.state_close};

    private int[] mState = MENU;
    private ValueAnimator mValueAnimator;

    private Path mPath = new Path();
    private Paint mPaint = new Paint();

    @Override
    public ValueAnimator getValueAnimator(boolean isReverse) {
        return ValueAnimator.ofInt(isReverse ? MENU_DONE.length - 1 : 0, isReverse ? 0 : MENU_DONE.length - 1).setDuration(getDuration());
    }

    private boolean mInReverse;

    @Override
    public void draw(Canvas canvas) {
        int index = mValueAnimator == null ? 0 : (Integer) mValueAnimator.getAnimatedValue();

        Shape shape = MENU_BACK[index];
        boolean isConnect = index == MENU_BACK.length - 1;

        final int width = getBounds().width();
        final int height = getBounds().height();

        mPath.reset();
        mPath.moveTo(width * shape.p1x, height * shape.p1y);
        mPath.lineTo(width * shape.p2x, height * shape.p2y);
        if (isConnect) {
            if (mState == MENU) {
                mPath.lineTo(width * shape.p6x, height * shape.p6y);
                mPath.lineTo(width * shape.p5x, height * shape.p5y);
            }
        }
        canvas.drawPath(mPath, mPaint);

        if (!isConnect) {
            mPath.reset();
            mPath.moveTo(width * shape.p3x, height * shape.p3y);
            mPath.lineTo(width * shape.p4x, height * shape.p4y);
            canvas.drawPath(mPath, mPaint);

            mPath.reset();
            mPath.moveTo(width * shape.p5x, height * shape.p5y);
            mPath.lineTo(width * shape.p6x, height * shape.p6y);
            canvas.drawPath(mPath, mPaint);
        }

    }

    @Override
    public void setAlpha(int i) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int[] getState() {
        return MENU;
    }

    @Override
    public int getOpacity() {
        return 0;
    }

    @Override
    public Paint getPaint() {
        return mPaint;
    }

    @Override
    public void startSwitchAnimation() {
        if (mValueAnimator != null) {
            mValueAnimator.end();
        }

        mValueAnimator = getValueAnimator(mInReverse);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                setValueAnimator(valueAnimator);
                invalidateSelf();
            }
        });
        mValueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // enable click or tap when animation finish
                invalidateSelf();
            }
        });
        mValueAnimator.start();
    }

    @Override
    public void onClick(View view) {
        mDelightfulButton.invalidate();
        startSwitchAnimation();
        mInReverse = !mInReverse;
    }

    private static class Shape {
        float p1x;
        float p1y;
        float p2x;
        float p2y;

        float p3x;
        float p3y;
        float p4x;
        float p4y;

        float p5x;
        float p5y;
        float p6x;
        float p6y;

        public Shape(float p1x, float p1y, float p2x, float p2y, float p3x, float p3y, float p4x, float p4y, float p5x, float p5y, float p6x, float p6y) {
            this.p1x = p1x;
            this.p1y = p1y;
            this.p2x = p2x;
            this.p2y = p2y;
            this.p3x = p3x;
            this.p3y = p3y;
            this.p4x = p4x;
            this.p4y = p4y;
            this.p5x = p5x;
            this.p5y = p5y;
            this.p6x = p6x;
            this.p6y = p6y;
        }
    }

}
