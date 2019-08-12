package com.bufferchime.agriculturefact.font;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class RobotoItalicTextView extends TextView {
	
	private Context c;
    public RobotoItalicTextView(Context c) {
        super(c);
        this.c = c;
        setTypeface(FontUtil.getRobotoItalic(c));

    }
    public RobotoItalicTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.c = context;
        setTypeface(FontUtil.getRobotoItalic(c));
    }

    public RobotoItalicTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.c = context;
        setTypeface(FontUtil.getRobotoItalic(c));
    }

}
