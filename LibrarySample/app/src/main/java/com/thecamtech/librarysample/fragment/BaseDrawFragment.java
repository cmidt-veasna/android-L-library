package com.thecamtech.librarysample.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thecamtech.librarysample.BaseActivityActionBar;

/**
 * Created by veasnasreng on 10/7/14.
 */
public abstract class BaseDrawFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(getContentResourceId(), container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onActivityAvailable((BaseActivityActionBar) getActivity());
    }

    protected void onActivityAvailable(BaseActivityActionBar baseActivityActionBar) {
    }

    protected abstract int getContentResourceId();

}
