package com.thecamtech.librarysample.view.util;

import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateInterpolator;

import com.thecamtech.android.library.view.PathAnimatorView;
import com.thecamtech.librarysample.R;

/**
 * Created by veasnasreng on 9/27/14.
 */
public class ActionBarUtil {

    public static void homeAction(final DrawerLayout drawerLayout, final View actionBar, final View menuView, final View removable, final Runnable finish) {

        actionBar.setEnabled(false);
        menuView.setEnabled(false);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.START);

        final View logo = removable.findViewById(R.id.logo);
        final View text = removable.findViewById(R.id.text);

        final PathAnimatorView pathAnimatorView = (PathAnimatorView) removable.findViewById(R.id.path_animator_view);
        pathAnimatorView.setAnimationListener(new PathAnimatorView.AnimationListener() {

            private boolean mLogoAnimateStarted;

            @Override
            public void onAnimationEnd() {
                pathAnimatorView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationUpdate(int frame) {
                if (frame >= 100 && logo.getVisibility() != View.VISIBLE) {
                    logo.setVisibility(View.VISIBLE);
                } else if (frame >= 300 && !mLogoAnimateStarted) {
                    mLogoAnimateStarted = true;
                    ViewCompat.animate(logo).translationY(-logo.getHeight() / 2).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            ViewCompat.animate(text).alpha(1f).translationY(text.getHeight()).withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    removable.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            final int top = removable.getHeight() + (logo.getHeight() / 2);
                                            ViewCompat.animate(logo).translationY(-top).setDuration(360).setInterpolator(new AnticipateInterpolator());
                                            ViewCompat.animate(text).translationY(-top).setDuration(360).setInterpolator(new AnticipateInterpolator());
                                            ViewCompat.animate(removable).alpha(0).setDuration(480).withEndAction(new Runnable() {
                                                @Override
                                                public void run() {
                                                    ((ViewGroup) removable.getParent()).removeView(removable);
                                                    actionBar.setTranslationY(-actionBar.getHeight());
                                                    actionBar.setVisibility(View.VISIBLE);
                                                    menuView.setTranslationY(-actionBar.getHeight());
                                                    menuView.setVisibility(View.VISIBLE);
                                                    ViewCompat.animate(actionBar).translationY(0).setDuration(300);
                                                    ViewCompat.animate(menuView).translationY(0).setDuration(300).withEndAction(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            menuView.setEnabled(true);
                                                            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, Gravity.START);
                                                            if (finish != null) {
                                                                finish.run();
                                                            }
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    }, 1500);
                                }
                            });
                        }
                    });
                }
            }
        });

    }

}
