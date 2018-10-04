package com.example.franc.shopfashion.SideMenuNavigation;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

public class DesignTextView extends TextView {
    public DesignTextView(Context context) {
        super(context,null);
    }

    public DesignTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs,0);
    }

    public DesignTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeface(FontsApp.canaroExtraBold);

    }
}
