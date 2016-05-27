package com.mogujie.videotest;

import android.view.View;
import android.widget.LinearLayout;

import com.mogujie.videotest.debug.DebugActivity;

/**
 * @author zijiao
 * @version 16/5/23
 * @Mark
 */
public class ViewActivity extends DebugActivity {

    private LinearLayout mLayout;
    private View mView;

    @Override
    protected void onLayoutCreate(LinearLayout layout) {
        mLayout = layout;
    }

    @Debug("拿过来")
    public void take() {
        View v = ViewHolder.detach();
        if (v != null) {
            mLayout.addView(mView = v);
        }
    }

    @Debug("送过去")
    public void send() {
        ViewHolder.attachView(mView);
    }

    private void findView() {
        mLayout = (LinearLayout) findViewById(R.id.layout);
    }
}
