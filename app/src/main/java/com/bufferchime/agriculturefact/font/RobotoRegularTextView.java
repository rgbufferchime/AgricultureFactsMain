package com.bufferchime.agriculturefact.font;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class RobotoRegularTextView extends TextView {
	
	private Context c;
    public RobotoRegularTextView(Context c) {
        super(c);
        this.c = c;
        setTypeface(FontUtil.getRobotoRegular(c));

    }
    public RobotoRegularTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.c = context;
        setTypeface(FontUtil.getRobotoRegular(c));
    }

    public RobotoRegularTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.c = context;
        setTypeface(FontUtil.getRobotoRegular(c));
    }

}
