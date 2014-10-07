package com.thecamtech.librarysample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.AbsListView;
import android.widget.ScrollView;

import com.thecamtech.librarysample.view.MScrollView;

/**
 * Created by veasnatemp on 10/7/14.
 */
public abstract class BaseActivityActionBar extends Activity {

    private View mActionBar;
    private View mShadow;
    private View mContent;
    private boolean mIsTransparentShadow;
    private boolean mShadowShowed;

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

        onActionBarInflated(mActionBar);
        onContentViewInflated(mContent);
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

    public void enableTransparentShadow(boolean bool) {
        if (mIsTransparentShadow != bool) {
            if (bool) {
                mShadow.setAlpha(0);
            } else {
                mShadow.setAlpha(0.2f);
            }
            mIsTransparentShadow = bool;
        }
    }

    public void setScrollableView(View view) {
        if (mIsTransparentShadow && view != null) {
            if (view instanceof MScrollView) {
                ((MScrollView) view).setOnScrollChangeListener(new MScrollView.OnScrollChangeListener() {
                    @Override
                    public void onScrollStateChange(ScrollView scrollView, int status) {
                        if (scrollView.getScrollY() != 0) {
                            mShadow.animate().alpha(0.6f).setDuration(150);
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
