package com.bufferchime.agriculturefact.font;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class RobotoBoldTextView extends TextView {
	
	private Context c;
    public RobotoBoldTextView(Context c) {
        super(c);
        this.c = c;
        setTypeface(FontUtil.getRobotoBold(c));

    }
    public RobotoBoldTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.c = context;
        setTypeface(FontUtil.getRobotoBold(c));
    }

    public RobotoBoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.c = context;
        setTypeface(FontUtil.getRobotoBold(c));
    }

}
