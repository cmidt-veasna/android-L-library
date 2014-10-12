package com.thecamtech.librarysample.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.thecamtech.librarysample.BaseActivityActionBar;

/**
 * Created by veasnasreng on 10/7/14.
 */
public abstract class BaseDrawFragment extends Fragment {

    private static final String SCROLL = "SCROLL";

    private int mScrollY;
    private View mScrollableView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(getContentResourceId(), container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onActivityAvailable((BaseActivityActionBar) getActivity());

        if (savedInstanceState != null) {
            mScrollY = savedInstanceState.getInt(SCROLL);
        }

        if (mScrollY == 0) {
            getBaseActivityActionBar().showShadow(false);
        } else {
            getBaseActivityActionBar().showShadow(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        saveScrollPosition();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        saveScrollPosition();
        outState.putInt(SCROLL, mScrollY);
        super.onSaveInstanceState(outState);
    }

    private void saveScrollPosition() {
        if (mScrollableView instanceof ScrollView) {
            mScrollY = ((ScrollView) mScrollableView).getScrollY();
        }
    }

    public void onFragmentReAttach() {
    }

    protected void setScrollableView(View scrollableView) {
        mScrollableView = scrollableView;
    }

    protected void onActivityAvailable(BaseActivityActionBar baseActivityActionBar) {
    }

    protected BaseActivityActionBar getBaseActivityActionBar() {
        return (BaseActivityActionBar) getActivity();
    }

    protected abstract int getContentResourceId();

}
