package com.bufferchime.agriculturefact.materialdesgin;

import android.graphics.drawable.GradientDrawable;

public class CustomGradientDrawable extends GradientDrawable {
 
public CustomGradientDrawable(int pStartColor, int pCenterColor, int pEndColor, int pStrokeWidth, int pStrokeColor, float cornerRadius) {
    super(Orientation.BOTTOM_TOP,new int[]{pStartColor,pCenterColor,pEndColor});
    setStroke(pStrokeWidth,pStrokeColor);
    setShape(GradientDrawable.RECTANGLE);
    setCornerRadius(cornerRadius);
    setSize(70, 70);
    
} 
}