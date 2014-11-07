package com.thecamtech.librarysample;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.graphics.Palette;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.thecamtech.android.library.view.CircleImageView;
import com.thecamtech.android.library.view.DelightfulButton;
import com.thecamtech.android.library.view.ParallaxScrollView;
import com.thecamtech.android.library.view.SelectableLinearLayout;
import com.thecamtech.librarysample.fragment.BaseDrawFragment;
import com.thecamtech.librarysample.fragment.ComingSoonFragment;
import com.thecamtech.librarysample.fragment.SampleFragment;
import com.thecamtech.librarysample.fragment.SettingFragment;
import com.thecamtech.librarysample.fragment.WhatsNewInAndroid;
import com.thecamtech.librarysample.view.util.ActionBarUtil;
import com.thecamtech.librarysample.view.util.Util;

import java.util.Random;

public class MainActivity extends BaseActivityActionBar {

    private DrawerLayout mDrawerLayout;
    private BaseDrawFragment mCurrentFragment;

    @Override
    protected void onContentViewInflated(View view) {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerLayout.setScrimColor(Color.TRANSPARENT);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, Gravity.START);

        ActionBarUtil.homeAction(mDrawerLayout, getActionBarView(), getMenuView(), findViewById(R.id.removable), new Runnable() {
            @Override
            public void run() {
                addFragment(1);
            }
        });

        DrawerListener drawerListener = new DrawerListener();
        getMenuView().setOnCheckedChangeListener(drawerListener);
        mDrawerLayout.setDrawerListener(drawerListener);

        ((TextView) findViewById(R.id.username)).setSelected(true);
        ((TextView) findViewById(R.id.email)).setSelected(true);
        ((ParallaxScrollView) findViewById(R.id.parallax_scrollview)).setParallaxView(findViewById(R.id.parallax_container));

        ((SelectableLinearLayout) findViewById(R.id.selectable_linearlayout)).setOnItemCheckListener(new SelectableLinearLayout.OnItemCheckListener() {
            @Override
            public void onItemChecked(int index, boolean checked) {
                if (checked) {
                    addFragment(index);
                } else {
                    removeFragment(index);
                }
            }

            @Override
            public void onAnimationDone(int index) {
                mDrawerLayout.closeDrawers();
            }
        });

        enableTransparentShadow(true);
    }

    private void addFragment(int index) {
        if (mCurrentFragment == null) {
            final String tag = String.format("%s-%d", MainActivity.class.getName(), index);
            FragmentManager fragmentManager = getFragmentManager();
            mCurrentFragment = (BaseDrawFragment) fragmentManager.findFragmentByTag(tag);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            if (mCurrentFragment == null) {
                switch (index) {
                    case 1:
                        mCurrentFragment = new WhatsNewInAndroid();
                        break;
                    case 2:
                        mCurrentFragment = new ComingSoonFragment();
                        break;
                    case 4:
                        mCurrentFragment = new SampleFragment();
                        break;
                    case 5:
                        mCurrentFragment = new ComingSoonFragment();
                        break;
                    case 7:
                        mCurrentFragment = new SettingFragment();
                }
                transaction.setCustomAnimations(R.anim.push_up, R.anim.push_down);
                transaction.add(R.id.content_fragment, mCurrentFragment, tag);
                showShadow(false);
            } else {
                transaction.setCustomAnimations(R.anim.push_up, R.anim.push_down);
                transaction.attach(mCurrentFragment);
                mCurrentFragment.onFragmentReAttach();
            }
            transaction.commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }
    }

    private void removeFragment(int index) {
        if (mCurrentFragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.push_up, R.anim.push_down);
            transaction.detach(mCurrentFragment);
            transaction.commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
            mCurrentFragment = null;
        }
    }

    @Override
    protected int getContentResourceId() {
        return R.layout.activity_main;
    }

    @Override
    protected int getActionBarResourceId() {
        return R.layout.include_home_action_bar;
    }

    @Override
    protected int getMenuResourceId() {
        return R.layout.include_home_drawer_v2;
    }

    @Override
    protected int getSplashScreenResourceId() {
        return R.layout.include_splash_screen;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Random r = new Random();

        final int[] listRes = new int[]{R.drawable.flower_banner_v1, R.drawable.flower_banner_v2, R.drawable.flower_banner_v3, R.drawable.flower_banner_v4};
        final int resId = listRes[r.nextInt(4)];

        ((ImageView) findViewById(R.id.banner)).setImageResource(resId);
        Palette palette = Palette.generate(((BitmapDrawable) getResources().getDrawable(resId)).getBitmap());
        if (palette != null && palette.getDarkVibrantColor() != null) {
            final int color = palette.getDarkVibrantColor().getRgb();
            final int withAlpha = Util.getArgbFromRgb(180, color);
            ((CircleImageView) findViewById(R.id.circle_image_view)).setColorEffect(color);
            if (Build.VERSION.SDK_INT >= 16)
                ((GradientDrawable) findViewById(R.id.info_container).getBackground()).setColors(new int[]{withAlpha, Color.TRANSPARENT});
            ((ColorDrawable) findViewById(R.id.line_separate).getBackground()).setColor(color);
        }
    }

    private class DrawerListener extends DrawerLayout.SimpleDrawerListener implements DelightfulButton.OnCheckedChangeListener {

        private boolean mIsDrag;
        private boolean mIsCheck;
        private int mMinLeftMenu = -1;
        private int mMaxLeftMenu = -1;

        @Override
        public void onDrawerSlide(View view, float v) {
            if (mMinLeftMenu == -1) {
                mMinLeftMenu = getMenuView().getLeft();
                mMaxLeftMenu = view.getMeasuredWidth() - getMenuView().getWidth();
            }
            final int oldLeft = getMenuView().getLeft();
            final int newLeft = (int) (view.getMeasuredWidth() * v) - getMenuView().getWidth();
            final int left = Math.max(mMinLeftMenu, Math.min(mMaxLeftMenu, newLeft)) - oldLeft;

            getMenuView().offsetLeftAndRight(left);
            if (mIsDrag || !mIsCheck) {
                getMenuView().setChecked(v > 0.5);
            }
        }

        @Override
        public void onDrawerStateChanged(int i) {
            if (i == DrawerLayout.STATE_DRAGGING) {
                mIsDrag = true;
            } else if (i == DrawerLayout.STATE_IDLE) {
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) getMenuView().getLayoutParams();
                params.leftMargin = getMenuView().getLeft();
                getMenuView().setLayoutParams(params);
                if (mIsDrag || mIsCheck) {
                    mIsDrag = false;
                    mIsCheck = false;
                }
            }
        }

        @Override
        public void onCheckedChanged(DelightfulButton buttonView, boolean isChecked) {
            if (!mIsDrag) {
                mIsCheck = true;
                if (isChecked) {
                    mDrawerLayout.openDrawer(Gravity.START);
                } else {
                    mDrawerLayout.closeDrawer(Gravity.START);
                }
            }
        }
    }

}
