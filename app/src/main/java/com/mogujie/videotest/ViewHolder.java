package com.mogujie.videotest;

import android.view.View;
import android.view.ViewGroup;

/**
 * @author zijiao
 * @version 16/5/23
 * @Mark
 */
public class ViewHolder {

    private static View sView;

    public static void attachView(View view) {
        if (view == null) {
            return;
        }
        if (view.getParent() != null) {
            ((ViewGroup)view.getParent()).removeView(view);
        }
        sView = view;
    }

    public static View detach() {
        View view = sView;
        sView = null;
        return view;
    }

}
