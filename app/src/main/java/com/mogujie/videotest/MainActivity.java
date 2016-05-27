package com.mogujie.videotest;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.mogujie.videotest.debug.DebugActivity;

import java.lang.reflect.Field;

public class MainActivity extends DebugActivity {

    private LinearLayout mLayout, mLayout1, mLayout2;
    private Button mButton;

    @Override
    protected void onLayoutCreate(LinearLayout layout) {
        mLayout1 = new LinearLayout(this);
        mLayout1.setBackgroundColor(Color.RED);

        mLayout2 = new LinearLayout(this);
        mLayout2.setBackgroundColor(Color.GREEN);

        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, height);
        lp.weight = 1;
        mLayout = new LinearLayout(this);
        mLayout.setOrientation(LinearLayout.HORIZONTAL);
        mLayout.addView(mLayout1, lp);
        mLayout.addView(mLayout2, lp);

        mButton = new Button(this);
        mButton.setText("测试View");
        mButton.setTextColor(Color.RED);
        mLayout1.addView(mButton);

        layout.addView(mLayout);
    }


    @Debug("添加到Layout1")
    public void addLayout1() {
        replace(mButton, mLayout1);
    }

    @Debug("添加到Layout2")
    public void addLayout2() {
        replace(mButton, mLayout2);
    }

    @Debug("反射替换ApplicationContext")
    public void replaceApplicationContext() {
        replaceContext(mButton, getApplicationContext());
    }

    @Debug("反射替换ActivityContext")
    public void replaceActivityContext() {
        replaceContext(mButton, this);
    }

    @Debug("走你")
    public void toViewActivity() {
        ViewHolder.attachView(mButton);
        to(ViewActivity.class);
    }

    @Debug("来来来")
    public void come() {
        View v = ViewHolder.detach();
        if (v != null) {
            mLayout1.addView(mButton = (Button) v);
        }
    }

    private static void replaceContext(View view, Context context) {
        if (view == null || context == null || view.getContext() == context) {
            return;
        }
        try {
            Field field = View.class.getDeclaredField("mContext");
            field.setAccessible(true);
            field.set(view, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void replace(View view, ViewGroup parent) {
        if (view == null) {
            return;
        }
        if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
        if (parent != null) {
            parent.addView(view);
        }
    }

}
