package com.thecamtech.librarysample.fragment;

import android.widget.ScrollView;

import com.thecamtech.librarysample.BaseActivityActionBar;
import com.thecamtech.librarysample.R;

/**
 * Created by veasnasreng on 10/7/14.
 */
public class SettingFragment extends BaseDrawFragment {

    @Override
    protected int getContentResourceId() {
        return R.layout.fragment_settings;
    }

    @Override
    protected void onActivityAvailable(BaseActivityActionBar baseActivityActionBar) {
        ScrollView scrollView = (ScrollView) getView().findViewById(R.id.scrollview);
        baseActivityActionBar.setScrollableView(scrollView);
        setScrollableView(scrollView);
    }

}
