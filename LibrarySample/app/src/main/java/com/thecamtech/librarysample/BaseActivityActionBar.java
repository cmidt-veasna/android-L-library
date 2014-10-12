package com.thecamtech.librarysample;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.AbsListView;
import android.widget.ScrollView;

import com.thecamtech.android.library.view.DelightfulButton;
import com.thecamtech.librarysample.view.MScrollView;

/**
 * Created by veasnatemp on 10/7/14.
 */
public abstract class BaseActivityActionBar extends Activity {

    private View mActionBar;
    private View mShadow;
    private View mContent;
    private DelightfulButton mMenu;
    private boolean mIsTransparentShadow;
    private boolean mShadowShowed;
    private boolean mActionBarShow;
    private ValueAnimator mValueAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_action_bar);
        ViewStub viewStub = (ViewStub) findViewById(R.id.splash_screen_viewstub);
        viewStub.setLayoutResource(getSplashScreenResourceId());
        viewStub.inflate();

        viewStub = (ViewStub) findViewById(R.id.action_bar_viewstub);
        viewStub.setLayoutResource(getActionBarResourceId());
        mActionBar = viewStub.inflate();

        viewStub = (ViewStub) findViewById(R.id.content_viewstub);
        viewStub.setLayoutResource(getContentResourceId());
        mContent = viewStub.inflate();

        viewStub = (ViewStub) findViewById(R.id.menu_viewstub);
        viewStub.setLayoutResource(getMenuResourceId());
        viewStub.inflate();

        mShadow = findViewById(R.id.shadow);
        mMenu = (DelightfulButton) findViewById(R.id.menu_icon);

        onActionBarInflated(mActionBar);
        onContentViewInflated(mContent);
        mActionBarShow = true;
    }

    protected DelightfulButton getMenuView() {
        return mMenu;
    }

    protected View getContentView() {
        return mContent;
    }

    protected View getActionBarView() {
        return mActionBar;
    }

    protected void onContentViewInflated(View view) {
    }

    protected void onActionBarInflated(View view) {
    }

    public void showActionBar(boolean bool) {
        if (bool != mActionBarShow) {
            if (mValueAnimator != null) {
                mValueAnimator.cancel();
            }

            if (bool) {

                mValueAnimator = ValueAnimator.ofInt(-(mActionBar.getHeight() + mShadow.getHeight()), 0);

                mMenu.setVisibility(View.VISIBLE);
                mActionBar.setVisibility(View.VISIBLE);

            } else {
                mValueAnimator = ValueAnimator.ofInt(0, -(mActionBar.getHeight() + mShadow.getHeight()));
            }

            mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    final int tranY = (Integer) animation.getAnimatedValue();
                    mMenu.setTranslationY(tranY);
                    mActionBar.setTranslationY(tranY);
                    mShadow.setTranslationY(tranY);
                }
            });
            mValueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mValueAnimator.removeAllUpdateListeners();
                    mValueAnimator.removeAllListeners();
                    mValueAnimator = null;
                    if (!mActionBarShow) {
                        mMenu.setVisibility(View.GONE);
                        mActionBar.setVisibility(View.GONE);
                    }
                }
            });
            mValueAnimator.start();
            mActionBarShow = bool;
        }
    }

    public void enableTransparentShadow(boolean bool) {
        if (mIsTransparentShadow != bool) {
            mShadow.setAlpha(bool ? 0 : 0.5f);
            mIsTransparentShadow = bool;
        }
    }

    public void showShadow(boolean bool) {
        mShadow.setAlpha(bool ? 0.5f : 0);
    }

    public void setScrollableView(View view) {
        if (mIsTransparentShadow && view != null) {
            if (view instanceof MScrollView) {
                ((MScrollView) view).setOnScrollChangeListener(new MScrollView.OnScrollChangeListener() {
                    @Override
                    public void onScrollStateChange(ScrollView scrollView, int status) {
                        if (scrollView.getScrollY() != 0) {
                            mShadow.animate().alpha(0.5f).setDuration(150);
                            mActionBar.animate().scaleX(1.02f).scaleY(1.02f).setDuration(150);
                            mShadowShowed = true;
                        } else if (mShadowShowed) {
                            mShadow.animate().alpha(0f).setDuration(150);
                            mActionBar.animate().scaleX(1f).scaleY(1f).setDuration(150);
                            mShadowShowed = false;
                        }
                    }
                });
            } else if (view instanceof AbsListView) {
                ((AbsListView) view).setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView absListView, int i) {
                    }

                    @Override
                    public void onScroll(AbsListView absListView, int i, int i2, int i3) {
                    }
                });
            }
        }
    }

    protected abstract int getContentResourceId();

    protected abstract int getActionBarResourceId();

    protected abstract int getSplashScreenResourceId();

    protected abstract int getMenuResourceId();

}
