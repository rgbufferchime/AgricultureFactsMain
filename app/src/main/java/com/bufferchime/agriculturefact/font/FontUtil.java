package com.bufferchime.agriculturefact.font;

import android.content.Context;
import android.graphics.Typeface;

public class FontUtil {
	
	private FontUtil instance;
	
	private static Typeface robotoRegular;
	private static Typeface robotoMedium;
	private static Typeface robotoBold;
	private static Typeface robotoBoldItalic;
	
	private static Typeface robotoItalic;
	private static Typeface robotoThinItalic;
	
	private static Typeface segoePrint;
	
	private FontUtil(){
		
	}
	
	public FontUtil getInstance(){
		if(instance == null){
			instance = new FontUtil();
		}
		return instance;
	}
	
	/*Typeface tf = Typeface.createFromAsset(getAssets(),
            "fonts/BPreplay.otf");*/
	
	public static Typeface getRobotoRegular(Context context){
		if(robotoRegular == null){
			robotoRegular = Typeface.createFromAsset(context.getAssets(),"fonts/Roboto-Regular.ttf");
		}
		return robotoRegular;
	}
	public static Typeface getRobotoMedium(Context context){
		if(robotoMedium == null){
			robotoMedium = Typeface.createFromAsset(context.getAssets(),"fonts/Roboto-Medium.ttf");
		}
		return robotoMedium;
	}
	public static Typeface getRobotoBold(Context context){
		if(robotoBold == null){
			robotoBold = Typeface.createFromAsset(context.getAssets(),"fonts/Roboto-Bold.ttf");
		}
		return robotoBold;
	}
	
	public static Typeface getRobotoItalic(Context context){
		if(robotoItalic == null){
			robotoItalic = Typeface.createFromAsset(context.getAssets(),"fonts/Roboto-Italic.ttf");
		}
		return robotoItalic;
	}
	
	public static Typeface getRobotoBoldItalic(Context context){
		if(robotoBoldItalic == null){
			robotoBoldItalic = Typeface.createFromAsset(context.getAssets(),"fonts/Roboto-BoldItalic.ttf");
		}
		return robotoBoldItalic;
	}
	
	public static Typeface getRobotoThinItalic(Context context){
		if(robotoThinItalic == null){
			robotoThinItalic = Typeface.createFromAsset(context.getAssets(),"fonts/Roboto-ThinItalic.ttf");
		}
		return robotoThinItalic;
	}
	
	public static Typeface getSegoePrint(Context context){
		if(segoePrint == null){
			segoePrint = Typeface.createFromAsset(context.getAssets(),"fonts/Segoe-Print.ttf");
		}
		return segoePrint;
	}

}
