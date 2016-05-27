package com.mogujie.videotest.debug;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author zijiao
 * @version 2016/1/14
 * @Mark
 */
public abstract class DebugActivity extends Activity {

    protected Context mContext;
    protected static final int MP;
    protected static final int WC;
    protected static final ViewGroup.LayoutParams ww;

    static {
        MP = ViewGroup.LayoutParams.MATCH_PARENT;
        WC = ViewGroup.LayoutParams.WRAP_CONTENT;
        ww = new ViewGroup.LayoutParams(MP, WC);
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD})
    public @interface Debug {
        String value() default "";
    }

    private final String TAG = getClass().getName();

    protected LinearLayout layout;
    private ViewGroup.LayoutParams mw, mm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView();
    }

    protected void setContentView() {
        final int MP = ViewGroup.LayoutParams.MATCH_PARENT;
        final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
        mm = new ViewGroup.LayoutParams(MP, MP);
        mw = new ViewGroup.LayoutParams(MP, WC);
        ScrollView scroll = new ScrollView(this);
        layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        onLayoutCreate(layout);
        scroll.addView(layout, mm);

        Item.Builder builder = getItems(new Item.Builder());
        List<Item> items;
        if (builder != null && (items = builder.items) != null && items.size() > 0) {
            for (final Item item : items) {
                addView(item);
            }
        } else {
            try {
                Method[] methods = getClass().getMethods();
                for (final Method method : methods) {
                    if (method.isAnnotationPresent(Debug.class)) {
                        String value = method.getAnnotation(Debug.class).value();
                        if (TextUtils.isEmpty(value)) {
                            value = method.getName();
                        }
                        method.setAccessible(true);
                        Item item = new Item(value, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    method.invoke(mContext);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        addView(item);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        setContentView(scroll, mm);
    }

    private void addView(final Item item) {
        if (item == null) {
            return;
        }
        Button button = new Button(this);
        button.setText(item.name);
        button.setOnClickListener(item.listener == null ? new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.target != null && Activity.class.isAssignableFrom(item.target)) {
                    startActivity(new Intent(DebugActivity.this, item.target));
                }
            }
        } : item.listener);
        layout.addView(button, mw);
    }

    protected void onLayoutCreate(LinearLayout layout) {

    }

    protected Item.Builder getItems(Item.Builder builder) {
        return null;
    }

    public void L(Object s) {
        Log.i(TAG, s + "");
    }

    public static class Item {
        public String name;
        public View.OnClickListener listener;
        public Class target;

        public Item(String name, Class target) {
            this.target = target;
            this.name = name;
        }

        public Item(String name, View.OnClickListener listener) {

            this.name = name;
            this.listener = listener;
        }

        public static class Builder {
            private List<Item> items = new ArrayList<>();

            public Builder add(Item... items) {
                if (items != null) {
                    this.items.addAll(Arrays.asList(items));
                }
                return this;
            }
        }
    }

    public void T(Object message) {
        Toast.makeText(mContext, String.valueOf(message), Toast.LENGTH_SHORT).show();
    }

    public void to(Class<? extends Activity> target) {
        startActivity(new Intent(this, target));
    }

    public void T(String format, Object... values) {
        T(String.format(format, values));
    }

}
