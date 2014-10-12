package com.thecamtech.librarysample.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.ScrollView;

import com.thecamtech.librarysample.BaseActivityActionBar;
import com.thecamtech.librarysample.R;
import com.thecamtech.librarysample.ZoomImageActivity;
import com.thecamtech.librarysample.view.Hoverable;
import com.thecamtech.librarysample.view.ImageFrameLayout;

/**
 * Created by veasnasreng on 10/8/14.
 */
public class WhatsNewInAndroid extends BaseDrawFragment {

    @Override
    protected int getContentResourceId() {
        return R.layout.fragment_whatsnew;
    }

    @Override
    protected void onActivityAvailable(BaseActivityActionBar baseActivityActionBar) {
        ScrollView scrollView = (ScrollView) getView().findViewById(R.id.scrollview);
        baseActivityActionBar.setScrollableView(scrollView);
        setScrollableView(scrollView);

        final ImageFrameLayout.OnHoverViewClickListener onHoverViewClickListener = new ImageFrameLayout.OnHoverViewClickListener() {
            @Override
            public void onHoverViewClick(int imageFrame, int id) {
                if (id == R.id.second) {
                    switch (imageFrame) {
                        case R.id.material_drive:
                            runZoomActivityImage(getView().findViewById(R.id.material_drive), R.drawable.material_drive);
                            break;

                        case R.id.material_mail:
                            runZoomActivityImage(getView().findViewById(R.id.material_mail), R.drawable.material_mail);
                            break;

                        case R.id.material_music:
                            runZoomActivityImage(getView().findViewById(R.id.material_music), R.drawable.material_music);
                            break;

                        case R.id.material_ui:
                            runZoomActivityImage(getView().findViewById(R.id.material_ui), R.drawable.material_ui);
                            break;

                        case R.id.material_battery:
                            runZoomActivityImage(getView().findViewById(R.id.material_battery), R.drawable.material_battery);
                    }
                }
            }
        };

        ((ImageFrameLayout) getView().findViewById(R.id.material_drive)).setOnHoverViewClickListener(onHoverViewClickListener);
        ((ImageFrameLayout) getView().findViewById(R.id.material_mail)).setOnHoverViewClickListener(onHoverViewClickListener);
        ((ImageFrameLayout) getView().findViewById(R.id.material_music)).setOnHoverViewClickListener(onHoverViewClickListener);
        ((ImageFrameLayout) getView().findViewById(R.id.material_ui)).setOnHoverViewClickListener(onHoverViewClickListener);
        ((ImageFrameLayout) getView().findViewById(R.id.material_battery)).setOnHoverViewClickListener(onHoverViewClickListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        getBaseActivityActionBar().showActionBar(true);
    }

    private void runZoomActivityImage(View view, int resourceId) {
        int[] screenLocation = new int[2];
        view.getLocationOnScreen(screenLocation);
        final Intent subActivity = new Intent(view.getContext(),
                ZoomImageActivity.class);
        int orientation = getResources().getConfiguration().orientation;
        subActivity.
                putExtra("orientation", orientation).
                putExtra("resourceId", resourceId).
                putExtra("left", screenLocation[0]).
                putExtra("top", screenLocation[1]).
                putExtra("width", view.getWidth()).
                putExtra("height", view.getHeight());

        getBaseActivityActionBar().showActionBar(false);
        ((Hoverable) view).dismiss(true, new Runnable() {
            @Override
            public void run() {
                startActivity(subActivity);

                // Override transitions: we don't want the normal window animation in addition
                // to our custom one
                getActivity().overridePendingTransition(0, 0);
            }
        });
    }

}
