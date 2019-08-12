package com.bufferchime.agriculturefact.materialdesgin;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app. AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aspose.cells.Cell;
import com.aspose.cells.Row;
import com.aspose.cells.Workbook;
import com.aspose.cells.Worksheet;
import com.bufferchime.agriculturefact.R;

import com.bufferchime.agriculturefact.font.RobotoBoldTextView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class SingleQuestionDetailActivity extends  AppCompatActivity {
	public Drawable d = null;

	private ProgressBar progressBar;
	private int progressStatus = 1;
	private TextView textViewd;
	private Handler handler = new Handler();
	ImageView imageView11,imageView22,imageView3;

	DataBaseHelper dbhelper;
	//Configuration
	public static final int DURATION = 500; // in ms
	public static final String PACKAGE = "IDENTIFY";
	String clcikedname;
	//UI Elements
	private RelativeLayout mLayoutContainer;
	private RobotoBoldTextView timer;
	private Button btnCancel;
	private TextView textView;
	private long time = 1;
	//Vars
	Dialog dialog;

	private int delta_top;
	private int delta_left;
	private float scale_width;
	private float scale_height;
	String title;
	int imgId;

	private RadioGroup radioGroup;
	private RadioButton radio0, radio1, radio2,radio3;
	private Button nextbutton,prevoiusbutton;
	Context context;
	int index=0;

	int top ;//196
	int left ;//69
	int width ;//202
	int height ;//37   

	int totalquestions;

	static int userscore=0 ;
	CountDownTimer countertimer;
	public static ArrayList<String> questions = new ArrayList<String>();
	public static ArrayList<String> optionas = new ArrayList<String>();
	public static ArrayList<String> optionbs = new ArrayList<String>();
	public static ArrayList<String> optioncs = new ArrayList<String>();
	public static ArrayList<String> optionds = new ArrayList<String>();
	public static ArrayList<String> answers = new ArrayList<String>();
	public static ArrayList<String> useranswers = new ArrayList<String>();
	public static ArrayList<String> reasons = new ArrayList<String>();

	public static int counttimervaluefromresul=0;
	public int mRotation = 0;
	public float mScale = 1.0f;
	public int mScaleNum = 0, mOffsetsNum;

	public float [] mScaleVals = {1.0f, 0.5f, 0.25f};
	public float [] mOffsets = {0f, 0.25f, 0.50f, 0.75f};

	Toolbar toolbar;
	TextView toolbar_title;
	ImageView im,syncimage,imageView1,ques_img;
	Animation fadeIn,fadeOut;
	Context mcontext;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_transition);
		context=SingleQuestionDetailActivity.this;
		initUi();
		dbhelper=new DataBaseHelper(SingleQuestionDetailActivity.this);
		overridePendingTransition(R.anim.enter_from_right,R.anim.exit_to_left);
		fadeIn = new AlphaAnimation(0.0f , 1.0f ) ;
		fadeIn.setDuration(1200);
		fadeIn.setFillAfter(true);	     
		fadeOut = new AlphaAnimation(1.0f , 0.0f);
		fadeOut.setDuration(1200);
		fadeOut.setFillAfter(true);

	}

	private void initUi() {

		mLayoutContainer = (RelativeLayout) findViewById(R.id.bg_layout);
		textView = (TextView) findViewById(R.id.title);

		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		textViewd = (TextView) findViewById(R.id.textView1);

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		if (toolbar != null) {
			im = (ImageView) toolbar.findViewById(R.id.toolbar_image);
			syncimage= (ImageView) toolbar.findViewById(R.id.scaner);
			toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
			im.setVisibility(View.INVISIBLE);
			syncimage.setVisibility(View.INVISIBLE);

			setSupportActionBar(toolbar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setTitle("");
		}
		mcontext=SingleQuestionDetailActivity.this;

		ques_img=(ImageView)findViewById(R.id.ques_img);


		Bundle bundle = getIntent().getExtras();

		String fromactivtyorfragment=bundle.getString("from");
		if(fromactivtyorfragment.equalsIgnoreCase("ShowSubTransactionFragemnt")){
			top = bundle.getInt(PACKAGE + ".top");//196
			left = bundle.getInt(PACKAGE + ".left");//69
			width = bundle.getInt(PACKAGE + ".width");//202
			height = bundle.getInt(PACKAGE + ".height");//37 
		}else{
			top = 196;
			left = 69;
			width = 202;
			height = 37; 
		}

		clcikedname=bundle.getString("CLICKEDNAME");

		radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
		radio0 = (RadioButton) findViewById(R.id.radio0);
		radio1 = (RadioButton) findViewById(R.id.radio1);
		radio2 = (RadioButton) findViewById(R.id.radio2);
		radio3 = (RadioButton) findViewById(R.id.radio3);

		nextbutton = (Button)findViewById(R.id.nextButton);
		prevoiusbutton=(Button)findViewById(R.id.previousButton);
		prevoiusbutton.setEnabled(false);
		timer = (RobotoBoldTextView) findViewById(R.id.timer);
		imageView1=(ImageView)findViewById(R.id.imageView1);

		ViewTreeObserver observer = textView.getViewTreeObserver();
		observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

			public boolean onPreDraw() {

				textView.getViewTreeObserver().removeOnPreDrawListener(this);

				int[] screen_location = new int[2];
				textView.getLocationOnScreen(screen_location);

				delta_left = left - screen_location[0];
				delta_top = top - screen_location[1];

				scale_width = (float) width / textView.getWidth();
				scale_height = (float) height / textView.getHeight();


				return true;
			}
		});


		ShowSubTransitionListFragment readslsx=new ShowSubTransitionListFragment();

		if(useranswers.size() >0){
			useranswers.clear();
		}if(questions.size() >0){
			questions.clear();
		}if(optionas.size() >0){
			optionas.clear();

		}if(optionbs.size() >0){
			optionbs.clear();

		}if(optioncs.size() >0){
			optioncs.clear();

		}if(optionds.size() >0){
			optionds.clear();

		}if(answers.size() >0){
			answers.clear();

		}if(useranswers.size() >0){
			useranswers.clear();
		}if(reasons .size() >0){
			reasons.clear();
		}

		if(fromactivtyorfragment.equalsIgnoreCase("ShowSubTransactionFragemnt")){
			if(readslsx.questions.size() >0){
				questions.addAll(readslsx.questions);
			}
			if(readslsx.optionas.size() >0){
				optionas.addAll(readslsx.optionas);
			}

			if(readslsx.optionbs.size() >0){
				optionbs.addAll(readslsx.optionbs);
			}

			if(readslsx.optioncs.size() >0){
				optioncs.addAll(readslsx.optioncs);
			}

			if(readslsx.optionds.size() >0){
				optionds.addAll(readslsx.optionds);

			}
			if(readslsx.answers.size() >0)
			{
				answers.addAll(readslsx.answers);

			}
			if(readslsx.reasons.size() >0){
				reasons.addAll(readslsx.reasons);	
			}


			if(readslsx.counttimervalue == 0){
				time=1*60000;
			}else
				time=readslsx.counttimervalue * 60000;
		}else{

			Workbook wb=readslsx.workbook;
			int clickedpo=bundle.getInt("clickedpo");

			String ss=readslsx.subQuizes.get(clickedpo);
			clcikedname=ss;
			Worksheet worksheet=null;
			worksheet = wb.getWorksheets().get(ss);

			if(worksheet !=null){
				Iterator<Row> rowIterator = worksheet.getCells().getRows().iterator();
				while(rowIterator.hasNext())

				{
					int i=0;
					Row r = rowIterator.next();
					Iterator<Cell> cellIterator = r.iterator();
					while(cellIterator.hasNext())
					{
						Cell cell= cellIterator.next();
						Log.d("cell" , cell.getStringValue());
						if(i == 0){
							questions.add(cell.getStringValue());
						}else if(i == 1){
							optionas.add(cell.getStringValue());

						}else if(i == 2){
							optionbs.add(cell.getStringValue());

						}else if(i == 3){
							optioncs.add(cell.getStringValue());

						}else if(i == 4){
							optionds.add(cell.getStringValue());

						}else if(i == 5){
							answers.add(cell.getStringValue());

						}else if(i == 6){
							if(cell.getStringValue() != null)
								reasons.add(cell.getStringValue());
							else
								reasons.add("");

						}else if(i == 8){
							counttimervaluefromresul=cell.getIntValue();
						}
						i++;

					}

				}

				//removing the first element from the array list beacuse they are heading (i.e Question,Optiona...)

				if(counttimervaluefromresul == 0){
					time=1*60000;
				}else
					time=counttimervaluefromresul * 60000;

				questions.remove(0);
				optionas.remove(0);
				optionbs.remove(0);
				optioncs.remove(0);
				optionds.remove(0);
				answers.remove(0);
				reasons.remove(0);

				//progressBar.setMax(questions.size());

				Log.d("max size", ""+progressBar.getMax());
			}
		}


		totalquestions=questions.size();

		countertimer=new CountDownTimer(time, 1000) {

			public void onTick(long millisUntilFinished) {
				timer.setText("" + millisUntilFinished / 1000);

				if((millisUntilFinished/1000) <= ((time/1000)/2) ){

					//changing the text color if half time or less the 5 sec
					if((millisUntilFinished/1000) <= ((time/1000)/9))
						timer.setTextColor(Color.RED);
					else
						timer.setTextColor(Color.parseColor("#FFBF00"));
				}
			}

			public void onFinish() {
				timer.setText("done!");
				for(int i=0;i<answers.size();i++)
				{
					Log.d("user answer", useranswers.get(i)+""+answers.get(i));
					if(useranswers.get(i).equalsIgnoreCase(answers.get(i)))
						userscore++;

				}
				Toast.makeText(SingleQuestionDetailActivity.this, "your score is"+userscore, Toast.LENGTH_SHORT).show();
				showCustomDialog("Time Up");


			}
		}.start();

		for(int i=0;i<totalquestions;i++)
		{
			useranswers.add("not");
		}

		if(questions.size() > 0){
			if(questions.get(0).contains("img =")){
				StringBuffer text = new StringBuffer(questions.get(0));
				text.replace( questions.get(0).indexOf("\"img =") ,questions.get(0).length() ,"");
				textView.setText(text);
			}else if(questions.get(0).contains("img=")){
				StringBuffer text = new StringBuffer(questions.get(0));
				text.replace( questions.get(0).indexOf("\"img=") ,questions.get(0).length() ,"");
				textView.setText(text);
			}else{
				textView.setText(Html.fromHtml(questions.get(0), new ImageGetter(), null));
			}
		}

		if(questions.get(0).contains("img =")){	
			ques_img.setVisibility(View.VISIBLE);
			String ss=questions.get(0).substring(questions.get(0).indexOf("img ="));
			String imgname=ss.substring(ss.indexOf("="), ss.indexOf("\""));
			StringBuilder sb = new StringBuilder(imgname);
			Log.d("data", ss);
			if(sb.charAt(0) =='='){
				Log.d("datasub", ""+sb.deleteCharAt(0));

			}else
				Log.d("datasub", ""+sb.toString());


			int identifier =getResources().getIdentifier(sb.toString(), "drawable",mcontext.getPackageName());
			ques_img.setImageResource(identifier);

		}else if(questions.get(0).contains("img=")){
			ques_img.setVisibility(View.VISIBLE);
			String ss=questions.get(0).substring(questions.get(0).indexOf("img="));
			String imgname=ss.substring(ss.indexOf("="), ss.indexOf("\""));
			StringBuilder sb = new StringBuilder(imgname);
			Log.d("data", ss);
			if(sb.charAt(0) =='='){
				Log.d("datasub", ""+sb.deleteCharAt(0));

			}else
				Log.d("datasub", ""+sb.toString());


			int identifier =getResources().getIdentifier(sb.toString(), "drawable",mcontext.getPackageName());
			ques_img.setImageResource(identifier);

		}else{
			ques_img.setVisibility(View.GONE);

		}

		if(optionas.size() > 0){
			if(optionas.get(0).contains("img =")){
				StringBuffer text = new StringBuffer(optionas.get(0));
				text.replace(optionas.get(0).indexOf("\"img =") ,optionas.get(0).length() ,"");
				radio0.setText(text);
			}else if(optionas.get(0).contains("img=")){
				StringBuffer text = new StringBuffer(optionas.get(0));
				text.replace(optionas.get(0).indexOf("\"img=") ,optionas.get(0).length() ,"");
				radio0.setText(text);
			}else{
				radio0.setText(Html.fromHtml(optionas.get(0), new ImageGetter(), null));
			}

		}

		if(optionas.get(0).contains("img =")){	

			String ss=optionas.get(0).substring(optionas.get(0).indexOf("img ="));
			String imgname=ss.substring(ss.indexOf("="), ss.indexOf("\""));
			StringBuilder sb = new StringBuilder(imgname);
			if(sb.charAt(0) =='=')
				sb.deleteCharAt(0);

			int identifier =getResources().getIdentifier(sb.toString(), "drawable", mcontext.getPackageName());


			Log.d("value",""+identifier);

			Drawable dr=getResources().getDrawable(identifier);

			Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
			// Scale it to 50 x 50 
			Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 259, 194, true));

			//radio0.setCompoundDrawablesWithIntrinsicBounds(R.drawable.checkbox, 0, 0, d);
			radio0.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.drawable.checkbox), null, null, d);

		}else if(optionas.get(0).contains("img=")){

			String ss=optionas.get(0).substring(optionas.get(0).indexOf("img="));
			String imgname=ss.substring(ss.indexOf("="), ss.indexOf("\""));
			StringBuilder sb = new StringBuilder(imgname);
			if(sb.charAt(0) =='=')
				sb.deleteCharAt(0);

			int identifier =getResources().getIdentifier(sb.toString(), "drawable", mcontext.getPackageName());


			Log.d("value",""+identifier);

			Drawable dr=getResources().getDrawable(identifier);

			Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
			// Scale it to 50 x 50
			Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 259, 194, true));

			//radio0.setCompoundDrawablesWithIntrinsicBounds(R.drawable.checkbox, 0, 0, d);
			radio0.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.drawable.checkbox), null, null, d);

		}else{
			radio0.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.drawable.checkbox), null, null, null);

		}

		if(optionbs.size() > 0){
			if(optionbs.get(0).contains("img =")){
				StringBuffer text = new StringBuffer(optionbs.get(0));
				text.replace(optionbs.get(0).indexOf("\"img =") ,optionbs.get(0).length() ,"");
				radio1.setText(text);
			}else if(optionbs.get(0).contains("img=")){
				StringBuffer text = new StringBuffer(optionbs.get(0));
				text.replace(optionbs.get(0).indexOf("\"img=") ,optionbs.get(0).length() ,"");
				radio1.setText(text);
			}else{
				radio1.setText(Html.fromHtml(optionbs.get(0), new ImageGetter(), null));
			}
		}
		if(optionbs.get(0).contains("img =")){	

			String ss=optionbs.get(0).substring(optionbs.get(0).indexOf("img ="));
			String imgname=ss.substring(ss.indexOf("="), ss.indexOf("\""));
			StringBuilder sb = new StringBuilder(imgname);
			if(sb.charAt(0) =='=')
				sb.deleteCharAt(0);

			int identifier =getResources().getIdentifier(sb.toString(), "drawable",mcontext.getPackageName());
			Drawable dr=getResources().getDrawable(identifier);

			Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
			// Scale it to 50 x 50 
			Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 259, 194, true));

			//radio0.setCompoundDrawablesWithIntrinsicBounds(R.drawable.checkbox, 0, 0, d);
			radio1.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.drawable.checkbox), null, null, d);
		}else if(optionbs.get(0).contains("img=")){

			String ss=optionbs.get(0).substring(optionbs.get(0).indexOf("img="));
			String imgname=ss.substring(ss.indexOf("="), ss.indexOf("\""));
			StringBuilder sb = new StringBuilder(imgname);
			if(sb.charAt(0) =='=')
				sb.deleteCharAt(0);

			int identifier =getResources().getIdentifier(sb.toString(), "drawable",mcontext.getPackageName());
			Drawable dr=getResources().getDrawable(identifier);

			Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
			// Scale it to 50 x 50
			Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 259, 194, true));

			//radio0.setCompoundDrawablesWithIntrinsicBounds(R.drawable.checkbox, 0, 0, d);
			radio1.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.drawable.checkbox), null, null, d);
		}else{
			radio1.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.drawable.checkbox), null, null, null);

		}

		if(optioncs.size() > 0){
			if(optioncs.get(0).contains("img =")){
				StringBuffer text = new StringBuffer(optioncs.get(0));
				text.replace(optioncs.get(0).indexOf("\"img =") ,optioncs.get(0).length() ,"");
				radio2.setText(text);
			}else if(optioncs.get(0).contains("img=")){
				StringBuffer text = new StringBuffer(optioncs.get(0));
				text.replace(optioncs.get(0).indexOf("\"img=") ,optioncs.get(0).length() ,"");
				radio2.setText(text);
			}else{
				radio2.setText(Html.fromHtml(optioncs.get(0), new ImageGetter(), null));
			}
		}
		if(optioncs.get(0).contains("img =")){	

			String ss=optioncs.get(0).substring(optioncs.get(0).indexOf("img ="));
			String imgname=ss.substring(ss.indexOf("="), ss.indexOf("\""));
			StringBuilder sb = new StringBuilder(imgname);
			if(sb.charAt(0) =='=')
				sb.deleteCharAt(0);

			int identifier =getResources().getIdentifier(sb.toString(), "drawable",mcontext.getPackageName());
			Drawable dr=getResources().getDrawable(identifier);

			Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
			// Scale it to 50 x 50 
			Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 259, 194, true));

			//radio0.setCompoundDrawablesWithIntrinsicBounds(R.drawable.checkbox, 0, 0, d);
			radio2.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.drawable.checkbox), null, null, d);

		}else if(optioncs.get(0).contains("img=")){

			String ss=optioncs.get(0).substring(optioncs.get(0).indexOf("img="));
			String imgname=ss.substring(ss.indexOf("="), ss.indexOf("\""));
			StringBuilder sb = new StringBuilder(imgname);
			if(sb.charAt(0) =='=')
				sb.deleteCharAt(0);

			int identifier =getResources().getIdentifier(sb.toString(), "drawable",mcontext.getPackageName());
			Drawable dr=getResources().getDrawable(identifier);

			Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
			// Scale it to 50 x 50
			Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 259, 194, true));

			//radio0.setCompoundDrawablesWithIntrinsicBounds(R.drawable.checkbox, 0, 0, d);
			radio2.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.drawable.checkbox), null, null, d);

		}else{
			radio2.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.drawable.checkbox), null, null, null);

		}
		if(optionds.size() > 0){
			if(optionds.get(0).contains("img =")){
				StringBuffer text = new StringBuffer(optionds.get(0));
				text.replace(optionds.get(0).indexOf("\"img =") ,optionds.get(0).length() ,"");
				radio3.setText(text);
			}else if(optionds.get(0).contains("img=")){
				StringBuffer text = new StringBuffer(optionds.get(0));
				text.replace(optionds.get(0).indexOf("\"img=") ,optionds.get(0).length() ,"");
				radio3.setText(text);
			}else{
				radio3.setText(Html.fromHtml(optionds.get(0), new ImageGetter(), null));
			}
		}
		if(optionds.get(0).contains("img =")){	

			String ss=optionds.get(0).substring(optionds.get(0).indexOf("img ="));
			String imgname=ss.substring(ss.indexOf("="), ss.indexOf("\""));
			StringBuilder sb = new StringBuilder(imgname);
			if(sb.charAt(0) =='=')
				sb.deleteCharAt(0);

			int identifier =getResources().getIdentifier(sb.toString(), "drawable",mcontext.getPackageName());
			Drawable dr=getResources().getDrawable(identifier);

			Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
			// Scale it to 50 x 50 
			Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 259, 194, true));

			//radio0.setCompoundDrawablesWithIntrinsicBounds(R.drawable.checkbox, 0, 0, d);
			radio3.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.drawable.checkbox), null, null, d);


		}if(optionds.get(0).contains("img=")){

			String ss=optionds.get(0).substring(optionds.get(0).indexOf("img="));
			String imgname=ss.substring(ss.indexOf("="), ss.indexOf("\""));
			StringBuilder sb = new StringBuilder(imgname);
			if(sb.charAt(0) =='=')
				sb.deleteCharAt(0);

			int identifier =getResources().getIdentifier(sb.toString(), "drawable",mcontext.getPackageName());
			Drawable dr=getResources().getDrawable(identifier);

			Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
			// Scale it to 50 x 50
			Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 259, 194, true));

			//radio0.setCompoundDrawablesWithIntrinsicBounds(R.drawable.checkbox, 0, 0, d);
			radio3.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.drawable.checkbox), null, null, d);


		}else{
			radio3.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.drawable.checkbox), null, null, null);

		}

		progressBar.setProgress((progressStatus)*(progressBar.getMax() /questions.size()));
		textViewd.setText((progressStatus)+"/"+questions.size());

		prevoiusbutton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				index--;

				if(index == 0){
					prevoiusbutton.setEnabled(false);
				}

				if(index == totalquestions - 1)
					nextbutton.setText("Submit");
				else
					nextbutton.setText("Next");

				if(index != -1){
					if(index >= 0 && index < totalquestions){
						progressStatus--;

						progressBar.setProgress((progressStatus)*(progressBar.getMax() /questions.size()));
						textViewd.setText((progressStatus)+"/"+questions.size());

						radioGroup.clearCheck();
						if(useranswers.get(index) != null && useranswers.get(index) != "not" ){

							if(useranswers.get(index) == "1"){
								radio0.setChecked(true);

							}else if(useranswers.get(index) == "2"){
								radio1.setChecked(true);

							}else if(useranswers.get(index) == "3"){
								radio2.setChecked(true);

							}else if(useranswers.get(index) == "4"){
								radio3.setChecked(true);

							}
						}

						//						textView.setText(Html.fromHtml(questions.get(index), new ImageGetter(), null));
						//						textView.startAnimation(fadeIn);
						//						radioGroup.startAnimation(fadeIn);
						//						//textView.setText(questionid.get(index));
						//						radio0.setText(Html.fromHtml(optionas.get(index), new ImageGetter(), null));
						//						radio1.setText(Html.fromHtml(optionbs.get(index), new ImageGetter(), null));
						//						radio2.setText(Html.fromHtml(optioncs.get(index), new ImageGetter(), null));
						//						radio3.setText(Html.fromHtml(optionds.get(index), new ImageGetter(), null));


						if(questions.size() > 0){
							if(questions.get(index).contains("img =")){
								StringBuffer text = new StringBuffer(questions.get(index));
								text.replace( questions.get(index).indexOf("\"img =") ,questions.get(index).length() ,"");
								textView.setText(text);
							}else if(questions.get(index).contains("img=")){
								StringBuffer text = new StringBuffer(questions.get(index));
								text.replace( questions.get(index).indexOf("\"img=") ,questions.get(index).length() ,"");
								textView.setText(text);
							}else{
								textView.setText(Html.fromHtml(questions.get(index), new URLImageParser(textView, SingleQuestionDetailActivity.this), null));
							}
						}

						textView.startAnimation(fadeIn);
						radioGroup.startAnimation(fadeIn);

						if(questions.get(index).contains("img =")){	
							ques_img.setVisibility(View.VISIBLE);

							String ss=questions.get(index).substring(questions.get(index).indexOf("img ="));
							String imgname=ss.substring(ss.indexOf("="), ss.indexOf("\""));
							StringBuilder sb = new StringBuilder(imgname);
							Log.d("data", ss);
							if(sb.charAt(0) =='='){
								Log.d("datasub", ""+sb.deleteCharAt(0));

							}else
								Log.d("datasub", ""+sb.toString());


							int identifier =getResources().getIdentifier(sb.toString(), "drawable",mcontext.getPackageName());
							ques_img.setImageResource(identifier);

						}else if(questions.get(index).contains("img=")){
							ques_img.setVisibility(View.VISIBLE);

							String ss=questions.get(index).substring(questions.get(index).indexOf("img="));
							String imgname=ss.substring(ss.indexOf("="), ss.indexOf("\""));
							StringBuilder sb = new StringBuilder(imgname);
							Log.d("data", ss);
							if(sb.charAt(0) =='='){
								Log.d("datasub", ""+sb.deleteCharAt(0));

							}else
								Log.d("datasub", ""+sb.toString());


							int identifier =getResources().getIdentifier(sb.toString(), "drawable",mcontext.getPackageName());
							ques_img.setImageResource(identifier);

						}else{
							ques_img.setVisibility(View.GONE);

						}

						if(optionas.get(index).contains("img =")){
							StringBuffer text = new StringBuffer(optionas.get(index));
							text.replace(optionas.get(index).indexOf("\"img =") ,optionas.get(index).length() ,"");
							radio0.setText(text);
						}else if(optionas.get(index).contains("img=")){
							StringBuffer text = new StringBuffer(optionas.get(index));
							text.replace(optionas.get(index).indexOf("\"img=") ,optionas.get(index).length() ,"");
							radio0.setText(text);
						}else{
							radio0.setText(Html.fromHtml(optionas.get(index), new URLImageParser(radio0, SingleQuestionDetailActivity.this), null));
						}
						//textView.setText(questionid.get(index));
						if(optionas.get(index).contains("img =")){	

							String ss=optionas.get(index).substring(optionas.get(index).indexOf("img ="));
							String imgname=ss.substring(ss.indexOf("="), ss.indexOf("\""));
							StringBuilder sb = new StringBuilder(imgname);
							if(sb.charAt(0) =='=')
								sb.deleteCharAt(0);

							int identifier =getResources().getIdentifier(sb.toString(), "drawable",mcontext.getPackageName());
							Drawable dr=getResources().getDrawable(identifier);

							Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
							// Scale it to 50 x 50 
							Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 259, 194, true));

							//radio0.setCompoundDrawablesWithIntrinsicBounds(R.drawable.checkbox, 0, 0, d);
							radio0.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.drawable.checkbox), null, null, d);


						}else if(optionas.get(index).contains("img=")){

							String ss=optionas.get(index).substring(optionas.get(index).indexOf("img="));
							String imgname=ss.substring(ss.indexOf("="), ss.indexOf("\""));
							StringBuilder sb = new StringBuilder(imgname);
							if(sb.charAt(0) =='=')
								sb.deleteCharAt(0);

							int identifier =getResources().getIdentifier(sb.toString(), "drawable",mcontext.getPackageName());
							Drawable dr=getResources().getDrawable(identifier);

							Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
							// Scale it to 50 x 50
							Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 259, 194, true));

							//radio0.setCompoundDrawablesWithIntrinsicBounds(R.drawable.checkbox, 0, 0, d);
							radio0.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.drawable.checkbox), null, null, d);


						}else{
							radio0.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.drawable.checkbox), null, null, null);

						}

						if(optionbs.get(index).contains("img =")){
							StringBuffer text = new StringBuffer(optionbs.get(index));
							text.replace(optionbs.get(index).indexOf("\"img =") ,optionbs.get(index).length() ,"");
							radio1.setText(text);
						}else if(optionbs.get(index).contains("img=")){
							StringBuffer text = new StringBuffer(optionbs.get(index));
							text.replace(optionbs.get(index).indexOf("\"img=") ,optionbs.get(index).length() ,"");
							radio1.setText(text);
						}else{
							radio1.setText(Html.fromHtml(optionbs.get(index), new URLImageParser(radio1, SingleQuestionDetailActivity.this), null));
						}

						if(optionbs.get(index).contains("img =")){	

							String ss=optionbs.get(index).substring(optionbs.get(index).indexOf("img ="));
							String imgname=ss.substring(ss.indexOf("="), ss.indexOf("\""));
							StringBuilder sb = new StringBuilder(imgname);
							if(sb.charAt(0) =='=')
								sb.deleteCharAt(0);

							int identifier =getResources().getIdentifier(sb.toString(), "drawable",mcontext.getPackageName());
							Drawable dr=getResources().getDrawable(identifier);

							Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
							// Scale it to 50 x 50 
							Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 259, 194, true));

							//radio0.setCompoundDrawablesWithIntrinsicBounds(R.drawable.checkbox, 0, 0, d);
							radio1.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.drawable.checkbox), null, null, d);


						}else if(optionbs.get(index).contains("img=")){

							String ss=optionbs.get(index).substring(optionbs.get(index).indexOf("img="));
							String imgname=ss.substring(ss.indexOf("="), ss.indexOf("\""));
							StringBuilder sb = new StringBuilder(imgname);
							if(sb.charAt(0) =='=')
								sb.deleteCharAt(0);

							int identifier =getResources().getIdentifier(sb.toString(), "drawable",mcontext.getPackageName());
							Drawable dr=getResources().getDrawable(identifier);

							Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
							// Scale it to 50 x 50
							Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 259, 194, true));

							//radio0.setCompoundDrawablesWithIntrinsicBounds(R.drawable.checkbox, 0, 0, d);
							radio1.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.drawable.checkbox), null, null, d);


						}else{
							radio1.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.drawable.checkbox), null, null, null);

						}

						if(optioncs.get(index).contains("img =")){
							StringBuffer text = new StringBuffer(optioncs.get(index));
							text.replace(optioncs.get(index).indexOf("\"img =") ,optioncs.get(index).length() ,"");
							radio2.setText(text);
						}else if(optioncs.get(index).contains("img=")){
							StringBuffer text = new StringBuffer(optioncs.get(index));
							text.replace(optioncs.get(index).indexOf("\"img=") ,optioncs.get(index).length() ,"");
							radio2.setText(text);
						}else{
							radio2.setText(Html.fromHtml(optioncs.get(index),new URLImageParser(radio2, SingleQuestionDetailActivity.this), null));
						}



						if(optioncs.get(index).contains("img =")){	

							String ss=optioncs.get(index).substring(optioncs.get(index).indexOf("img ="));
							String imgname=ss.substring(ss.indexOf("="), ss.indexOf("\""));
							StringBuilder sb = new StringBuilder(imgname);
							if(sb.charAt(0) =='=')
								sb.deleteCharAt(0);

							int identifier =getResources().getIdentifier(sb.toString(), "drawable",mcontext.getPackageName());
							Drawable dr=getResources().getDrawable(identifier);

							Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
							// Scale it to 50 x 50 
							Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 259, 194, true));

							//radio0.setCompoundDrawablesWithIntrinsicBounds(R.drawable.checkbox, 0, 0, d);
							radio2.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.drawable.checkbox), null, null, d);

						}else if(optioncs.get(index).contains("img=")){

							String ss=optioncs.get(index).substring(optioncs.get(index).indexOf("img="));
							String imgname=ss.substring(ss.indexOf("="), ss.indexOf("\""));
							StringBuilder sb = new StringBuilder(imgname);
							if(sb.charAt(0) =='=')
								sb.deleteCharAt(0);

							int identifier =getResources().getIdentifier(sb.toString(), "drawable",mcontext.getPackageName());
							Drawable dr=getResources().getDrawable(identifier);

							Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
							// Scale it to 50 x 50
							Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 259, 194, true));

							//radio0.setCompoundDrawablesWithIntrinsicBounds(R.drawable.checkbox, 0, 0, d);
							radio2.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.drawable.checkbox), null, null, d);

						}else{
							radio2.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.drawable.checkbox), null, null, null);

						}


						if(optionds.get(index).contains("img =")){
							StringBuffer text = new StringBuffer(optionds.get(index));
							text.replace(optionds.get(index).indexOf("\"img =") ,optionds.get(index).length() ,"");
							radio3.setText(text);
						}else if(optionds.get(index).contains("img=")){
							StringBuffer text = new StringBuffer(optionds.get(index));
							text.replace(optionds.get(index).indexOf("\"img=") ,optionds.get(index).length() ,"");
							radio3.setText(text);
						}else{
							radio3.setText(Html.fromHtml(optionds.get(index), new URLImageParser(radio3, SingleQuestionDetailActivity.this), null));
						}

						if(optionds.get(index).contains("img =")){	

							String ss=optionds.get(index).substring(optionds.get(index).indexOf("img ="));
							String imgname=ss.substring(ss.indexOf("="), ss.indexOf("\""));
							StringBuilder sb = new StringBuilder(imgname);
							if(sb.charAt(0) =='=')
								sb.deleteCharAt(0);

							int identifier =getResources().getIdentifier(sb.toString(), "drawable",mcontext.getPackageName());
							Drawable dr=getResources().getDrawable(identifier);

							Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
							// Scale it to 50 x 50 
							Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 259, 194, true));

							//radio0.setCompoundDrawablesWithIntrinsicBounds(R.drawable.checkbox, 0, 0, d);
							radio3.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.drawable.checkbox), null, null, d);


						}else if(optionds.get(index).contains("img=")){

							String ss=optionds.get(index).substring(optionds.get(index).indexOf("img="));
							String imgname=ss.substring(ss.indexOf("="), ss.indexOf("\""));
							StringBuilder sb = new StringBuilder(imgname);
							if(sb.charAt(0) =='=')
								sb.deleteCharAt(0);

							int identifier =getResources().getIdentifier(sb.toString(), "drawable",mcontext.getPackageName());
							Drawable dr=getResources().getDrawable(identifier);

							Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
							// Scale it to 50 x 50
							Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 259, 194, true));

							//radio0.setCompoundDrawablesWithIntrinsicBounds(R.drawable.checkbox, 0, 0, d);
							radio3.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.drawable.checkbox), null, null, d);


						}else{
							radio3.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.drawable.checkbox), null, null, null);

						}
					}
				}

			}
		});

		nextbutton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				if (radioGroup.getCheckedRadioButtonId() != -1)
				{
					if(index >= 0 && index <totalquestions)
					{

						if(radioGroup.getCheckedRadioButtonId() == R.id.radio0){
							useranswers.set(index,"1");	

						}else if(radioGroup.getCheckedRadioButtonId() == R.id.radio1){
							useranswers.set(index,"2");	

						}else if(radioGroup.getCheckedRadioButtonId() == R.id.radio2){
							useranswers.set(index,"3");	

						}else if(radioGroup.getCheckedRadioButtonId() == R.id.radio3){
							useranswers.set(index,"4");	
						}
					}				
				}else{
					useranswers.set(index,"not");	

				}

				index=index+1;

				if(index >= 0 && index < totalquestions){

					Log.d("index totalquestion", index+""+totalquestions);

					progressStatus++;
					progressBar.setProgress((progressStatus)*(progressBar.getMax() /questions.size()));
					textViewd.setText((progressStatus)+"/"+questions.size());

					prevoiusbutton.setEnabled(true);

					if(index == totalquestions - 1)
						nextbutton.setText("Submit");
					else
						nextbutton.setText("Next");

					radioGroup.clearCheck();

					if( useranswers.get(index) != null && useranswers.get(index) != "not" )
					{
						if(useranswers.get(index) == "1"){
							radio0.setChecked(true);

						}else if(useranswers.get(index) == "2"){
							radio1.setChecked(true);

						}else if(useranswers.get(index) == "3"){
							radio2.setChecked(true);

						}else if(useranswers.get(index) == "4"){
							radio3.setChecked(true);

						}

					}

					String s=questions.get(index);

					if(questions.size() > 0){
						if(questions.get(index).contains("img =")){
							StringBuffer text = new StringBuffer(questions.get(index));
							text.replace( questions.get(index).indexOf("\"img =") ,questions.get(index).length() ,"");
							textView.setText(text);
						}else if(questions.get(index).contains("img=")){
							StringBuffer text = new StringBuffer(questions.get(index));
							text.replace( questions.get(index).indexOf("\"img=") ,questions.get(index).length() ,"");
							textView.setText(text);
						}else{
							textView.setText(Html.fromHtml(questions.get(index), new URLImageParser(textView, SingleQuestionDetailActivity.this), null));
						}
					}
					textView.startAnimation(fadeIn);
					radioGroup.startAnimation(fadeIn);

					if(questions.get(index).contains("img =")){	

						ques_img.setVisibility(View.VISIBLE);

						String ss=questions.get(index).substring(questions.get(index).indexOf("img ="));
						String imgname=ss.substring(ss.indexOf("="), ss.indexOf("\""));
						StringBuilder sb = new StringBuilder(imgname);
						Log.d("data", ss);
						if(sb.charAt(0) =='='){
							Log.d("datasub", ""+sb.deleteCharAt(0));

						}else
							Log.d("datasub", ""+sb.toString());

						int identifier =getResources().getIdentifier(sb.toString(), "drawable",mcontext.getPackageName());
						ques_img.setImageResource(identifier);

					}else if(questions.get(index).contains("img=")){

						ques_img.setVisibility(View.VISIBLE);

						String ss=questions.get(index).substring(questions.get(index).indexOf("img="));
						String imgname=ss.substring(ss.indexOf("="), ss.indexOf("\""));
						StringBuilder sb = new StringBuilder(imgname);
						Log.d("data", ss);
						if(sb.charAt(0) =='='){
							Log.d("datasub", ""+sb.deleteCharAt(0));

						}else
							Log.d("datasub", ""+sb.toString());

						int identifier =getResources().getIdentifier(sb.toString(), "drawable",mcontext.getPackageName());
						ques_img.setImageResource(identifier);

					}else{
						ques_img.setVisibility(View.GONE);
					}

					//textView.setText(questionid.get(index));
					if(optionas.get(index).contains("img =")){
						StringBuffer text = new StringBuffer(optionas.get(index));
						text.replace(optionas.get(index).indexOf("\"img =") ,optionas.get(index).length() ,"");
						radio0.setText(text);
					}else if(optionas.get(index).contains("img=")){
						StringBuffer text = new StringBuffer(optionas.get(index));
						text.replace(optionas.get(index).indexOf("\"img=") ,optionas.get(index).length() ,"");
						radio0.setText(text);
					}else{
						radio0.setText(Html.fromHtml(optionas.get(index), new URLImageParser(radio0, SingleQuestionDetailActivity.this), null));
					}
					
					
					if(optionas.get(index).contains("img =")){	

						String ss=optionas.get(index).substring(optionas.get(index).indexOf("img ="));
						String imgname=ss.substring(ss.indexOf("="), ss.indexOf("\""));
						StringBuilder sb = new StringBuilder(imgname);
						if(sb.charAt(0) =='=')
							sb.deleteCharAt(0);

						int identifier =getResources().getIdentifier(sb.toString(), "drawable",mcontext.getPackageName());
						Drawable dr=getResources().getDrawable(identifier);

						Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
						// Scale it to 50 x 50 
						Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 259, 194, true));

						//radio0.setCompoundDrawablesWithIntrinsicBounds(R.drawable.checkbox, 0, 0, d);
						radio0.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.drawable.checkbox), null, null, d);


					}else if(optionas.get(index).contains("img=")){

						String ss=optionas.get(index).substring(optionas.get(index).indexOf("img="));
						String imgname=ss.substring(ss.indexOf("="), ss.indexOf("\""));
						StringBuilder sb = new StringBuilder(imgname);
						if(sb.charAt(0) =='=')
							sb.deleteCharAt(0);

						int identifier =getResources().getIdentifier(sb.toString(), "drawable",mcontext.getPackageName());
						Drawable dr=getResources().getDrawable(identifier);

						Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
						// Scale it to 50 x 50
						Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 259, 194, true));

						//radio0.setCompoundDrawablesWithIntrinsicBounds(R.drawable.checkbox, 0, 0, d);
						radio0.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.drawable.checkbox), null, null, d);


					}else{
						radio0.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.drawable.checkbox), null, null, null);

					}


					if(optionbs.get(index).contains("img =")){
						StringBuffer text = new StringBuffer(optionbs.get(index));
						text.replace(optionbs.get(index).indexOf("\"img =") ,optionbs.get(index).length() ,"");
						radio1.setText(text);
					}else if(optionbs.get(index).contains("img=")){
						StringBuffer text = new StringBuffer(optionbs.get(index));
						text.replace(optionbs.get(index).indexOf("\"img=") ,optionbs.get(index).length() ,"");
						radio1.setText(text);
					}else{
						radio1.setText(Html.fromHtml(optionbs.get(index), new URLImageParser(radio1, SingleQuestionDetailActivity.this), null));
					}					
					
					
					if(optionbs.get(index).contains("img =")){	

						String ss=optionbs.get(index).substring(optionbs.get(index).indexOf("img ="));
						String imgname=ss.substring(ss.indexOf("="), ss.indexOf("\""));
						StringBuilder sb = new StringBuilder(imgname);
						if(sb.charAt(0) =='=')
							sb.deleteCharAt(0);

						int identifier =getResources().getIdentifier(sb.toString(), "drawable",mcontext.getPackageName());
						Drawable dr=getResources().getDrawable(identifier);

						Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
						// Scale it to 50 x 50 
						Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 259, 194, true));

						//radio0.setCompoundDrawablesWithIntrinsicBounds(R.drawable.checkbox, 0, 0, d);
						radio1.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.drawable.checkbox), null, null, d);


					}else if(optionbs.get(index).contains("img=")){

						String ss=optionbs.get(index).substring(optionbs.get(index).indexOf("img="));
						String imgname=ss.substring(ss.indexOf("="), ss.indexOf("\""));
						StringBuilder sb = new StringBuilder(imgname);
						if(sb.charAt(0) =='=')
							sb.deleteCharAt(0);

						int identifier =getResources().getIdentifier(sb.toString(), "drawable",mcontext.getPackageName());
						Drawable dr=getResources().getDrawable(identifier);

						Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
						// Scale it to 50 x 50
						Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 259, 194, true));

						//radio0.setCompoundDrawablesWithIntrinsicBounds(R.drawable.checkbox, 0, 0, d);
						radio1.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.drawable.checkbox), null, null, d);


					}else{
						radio1.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.drawable.checkbox), null, null, null);

					}


					if(optioncs.get(index).contains("img =")){
						StringBuffer text = new StringBuffer(optioncs.get(index));
						text.replace(optioncs.get(index).indexOf("\"img =") ,optioncs.get(index).length() ,"");
						radio2.setText(text);
					}else if(optioncs.get(index).contains("img=")){
						StringBuffer text = new StringBuffer(optioncs.get(index));
						text.replace(optioncs.get(index).indexOf("\"img=") ,optioncs.get(index).length() ,"");
						radio2.setText(text);
					}else{
						radio2.setText(Html.fromHtml(optioncs.get(index),new URLImageParser(radio2, SingleQuestionDetailActivity.this), null));
					}

					if(optioncs.get(index).contains("img =")){	

						String ss=optioncs.get(index).substring(optioncs.get(index).indexOf("img ="));
						String imgname=ss.substring(ss.indexOf("="), ss.indexOf("\""));
						StringBuilder sb = new StringBuilder(imgname);
						if(sb.charAt(0) =='=')
							sb.deleteCharAt(0);

						int identifier =getResources().getIdentifier(sb.toString(), "drawable",mcontext.getPackageName());
						Drawable dr=getResources().getDrawable(identifier);

						Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
						// Scale it to 50 x 50 
						Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 259, 194, true));

						//radio0.setCompoundDrawablesWithIntrinsicBounds(R.drawable.checkbox, 0, 0, d);
						radio2.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.drawable.checkbox), null, null, d);


					}else if(optioncs.get(index).contains("img=")){

						String ss=optioncs.get(index).substring(optioncs.get(index).indexOf("img="));
						String imgname=ss.substring(ss.indexOf("="), ss.indexOf("\""));
						StringBuilder sb = new StringBuilder(imgname);
						if(sb.charAt(0) =='=')
							sb.deleteCharAt(0);

						int identifier =getResources().getIdentifier(sb.toString(), "drawable",mcontext.getPackageName());
						Drawable dr=getResources().getDrawable(identifier);

						Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
						// Scale it to 50 x 50
						Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 259, 194, true));

						//radio0.setCompoundDrawablesWithIntrinsicBounds(R.drawable.checkbox, 0, 0, d);
						radio2.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.drawable.checkbox), null, null, d);


					}else{
						radio2.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.drawable.checkbox), null, null, null);

					}



					if(optionds.get(index).contains("img =")){
						StringBuffer text = new StringBuffer(optionds.get(index));
						text.replace(optionds.get(index).indexOf("\"img =") ,optionds.get(index).length() ,"");
						radio3.setText(text);
					}else if(optionds.get(index).contains("img=")){
						StringBuffer text = new StringBuffer(optionds.get(index));
						text.replace(optionds.get(index).indexOf("\"img=") ,optionds.get(index).length() ,"");
						radio3.setText(text);
					}else{
						radio3.setText(Html.fromHtml(optionds.get(index), new URLImageParser(radio3, SingleQuestionDetailActivity.this), null));
					}
					if(optionds.get(index).contains("img =")){	

						String ss=optionds.get(index).substring(optionds.get(index).indexOf("img ="));
						String imgname=ss.substring(ss.indexOf("="), ss.indexOf("\""));
						StringBuilder sb = new StringBuilder(imgname);
						if(sb.charAt(0) =='=')
							sb.deleteCharAt(0);

						int identifier =getResources().getIdentifier(sb.toString(), "drawable",mcontext.getPackageName());
						Drawable dr=getResources().getDrawable(identifier);

						Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
						// Scale it to 50 x 50 
						Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 259, 194, true));

						//radio0.setCompoundDrawablesWithIntrinsicBounds(R.drawable.checkbox, 0, 0, d);
						radio3.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.drawable.checkbox), null, null, d);


					}else if(optionds.get(index).contains("img=")){

						String ss=optionds.get(index).substring(optionds.get(index).indexOf("img="));
						String imgname=ss.substring(ss.indexOf("="), ss.indexOf("\""));
						StringBuilder sb = new StringBuilder(imgname);
						if(sb.charAt(0) =='=')
							sb.deleteCharAt(0);

						int identifier =getResources().getIdentifier(sb.toString(), "drawable",mcontext.getPackageName());
						Drawable dr=getResources().getDrawable(identifier);

						Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
						// Scale it to 50 x 50
						Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 259, 194, true));

						//radio0.setCompoundDrawablesWithIntrinsicBounds(R.drawable.checkbox, 0, 0, d);
						radio3.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.drawable.checkbox), null, null, d);


					}else{
						radio3.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.drawable.checkbox), null, null, null);

					}


					//					textView.setText(Html.fromHtml(s, new URLImageParser(textView, SingleQuestionDetailActivity.this), null));
					//					textView.startAnimation(fadeIn);
					//					radioGroup.startAnimation(fadeIn);
					//
					//					//textView.setText(questionid.get(index));
					//					radio0.setText(Html.fromHtml(optionas.get(index), new URLImageParser(radio0, SingleQuestionDetailActivity.this), null));
					//					radio1.setText(Html.fromHtml(optionbs.get(index), new URLImageParser(radio1, SingleQuestionDetailActivity.this), null));
					//					radio2.setText(Html.fromHtml(optioncs.get(index),new URLImageParser(radio2, SingleQuestionDetailActivity.this), null));
					//					radio3.setText(Html.fromHtml(optionds.get(index), new URLImageParser(radio3, SingleQuestionDetailActivity.this), null));
				}else{

					countertimer.cancel();
					//countertimer.onFinish();
					showCustomDialog("Thanks for completing the test.Here are the Results.");

				}

			}
		});

		if(toolbar_title != null)
			toolbar_title.setText(clcikedname);
		//
		//		mNavigationBackBtn.setOnClickListener(new OnClickListener(){
		//
		//			public void onClick(View arg0) {
		//				onBackPressed();
		//			}
		//
		//		});		
	}


	public void onBackPressed() {

		//		runExitAnimation(new Runnable() {
		//
		//			public void run() {
		//				countertimer.cancel();
		//				finish();
		//			}
		//		});
		countertimer.cancel();
		Intent i=new Intent(SingleQuestionDetailActivity.this,MainActivity.class);
		startActivity(i);
		super.onBackPressed();

	}


	public void finish() {

		super.finish();
		//overridePendingTransition(0, 0);
	}


	//	private void runEnterAnimation() {
	//
	//		ViewHelper.setPivotX(textView, 0.f);
	//		ViewHelper.setPivotY(textView, 0.f);
	//		ViewHelper.setScaleX(textView, scale_width);
	//		ViewHelper.setScaleY(textView, scale_height);
	//		ViewHelper.setTranslationX(textView, delta_left);
	//		ViewHelper.setTranslationY(textView, delta_top);
	//
	//		animate(textView).
	//		setDuration(DURATION).
	//		scaleX(1.f).
	//		scaleY(1.f).
	//		translationX(0.f).
	//		translationY(0.f).
	//		setInterpolator(new DecelerateInterpolator()).
	//		setListener(new AnimatorListenerAdapter() {  
	//
	//
	//			public void onAnimationEnd(Animator animation) {        	    
	//			}
	//		});
	//
	//		ObjectAnimator bg_anim = ObjectAnimator.ofFloat(mLayoutContainer, "alpha", 0f, 1f);
	//		bg_anim.setDuration(DURATION);
	//		bg_anim.start();
	//
	//	}

	//	private void runExitAnimation(final Runnable end_action) {
	//
	//		ViewHelper.setPivotX(textView, 0.f);
	//		ViewHelper.setPivotY(textView, 0.f);
	//		ViewHelper.setScaleX(textView, 1.f);
	//		ViewHelper.setScaleY(textView, 1.f);
	//		ViewHelper.setTranslationX(textView, 0.f);
	//		ViewHelper.setTranslationY(textView, 0.f);
	//
	//		animate(textView).
	//		setDuration(DURATION).
	//		scaleX(scale_width).
	//		scaleY(scale_height).
	//		translationX(delta_left).
	//		translationY(delta_top).
	//		setInterpolator(new DecelerateInterpolator()).
	//		setListener(new AnimatorListenerAdapter() {  
	//
	//
	//			public void onAnimationEnd(Animator animation) {
	//				end_action.run();
	//			}
	//		});
	//
	//		ObjectAnimator bg_anim = ObjectAnimator.ofFloat(mLayoutContainer, "alpha", 1f, 0f);
	//		bg_anim.setDuration(DURATION);
	//		bg_anim.start();
	//
	//	}
	//
	//	private UIParallaxScroll.OnScrollChangedListener mOnScrollChangedListener = new UIParallaxScroll.OnScrollChangedListener() {
	//		public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
	//			//Difference between the heights, important to not add margin or remove mNavigationTitle.
	//			final float headerHeight = ViewHelper.getY(textView) - (mNavigationTop.getHeight() - textView.getHeight());
	//			final float ratio = (float) Math.min(Math.max(t, 0), headerHeight) / headerHeight;
	//			final int newAlpha = (int) (ratio * 255);
	//			mNavigationTop.getBackground().setAlpha(newAlpha);
	//
	//			Animation animationFadeIn = AnimationUtils.loadAnimation(SingleQuestionDetailActivity.this,R.anim.fadein);
	//			Animation animationFadeOut = AnimationUtils.loadAnimation(SingleQuestionDetailActivity.this,R.anim.fadeout);
	//
	//			if (newAlpha == 255 && mNavigationTitle.getVisibility() != View.VISIBLE && !animationFadeIn.hasStarted()){
	//				mNavigationTitle.setVisibility(View.VISIBLE);
	//				mNavigationTitle.startAnimation(animationFadeIn);
	//			} else if (newAlpha < 255 && !animationFadeOut.hasStarted() && mNavigationTitle.getVisibility() != View.INVISIBLE)  { 	
	//				mNavigationTitle.startAnimation(animationFadeOut);
	//				//mNavigationTitle.setVisibility(View.INVISIBLE);
	//
	//			}
	//
	//		}
	//	};

	protected void onDestroy() {

		super.onDestroy();
		if(countertimer != null)
			countertimer.cancel();
		if(questions != null && questions.size() >0)
			questions.clear();
		if(optionas != null && optionas.size() >0)
			optionas.clear();
		if(optionbs != null && optionbs.size() >0)
			optionbs.clear();
		if(optioncs != null && optioncs.size() >0)
			optioncs.clear();
		if(optionds != null && optionds.size() >0)
			optionds.clear();
		if(answers != null && answers.size() >0)
			answers.clear();
		if(useranswers !=null && useranswers.size() >0 )
			useranswers.clear();
		if(reasons != null && reasons.size() >0)
			reasons.clear();

	};



	protected void showCustomDialog(String text) {

		for(int i=0;i<answers.size();i++)
		{
			Log.d("user answer", useranswers.get(i)+""+answers.get(i));
			if(useranswers.get(i).equalsIgnoreCase(answers.get(i)))
				userscore++;

		}

		if(dbhelper!=null && dbhelper.checkQuizLevelExists(clcikedname) !=null )
		{
			//to read previous attempts count
			DbUSer databasedata=dbhelper.checkQuizLevelExists(clcikedname);
			//already exist
			DbUSer data=new DbUSer();
			data.setQuizName(ShowSubTransitionListFragment.selectedQuiz);
			data.setQuizlevelname(clcikedname);
			data.setTotalNoQuestions(questions.size());
			data.setScore(userscore);
			data.setAttempts(databasedata.getAttempts() +1);

			long todo1_id = dbhelper.updateToDo(data);
			Log.d("Tag Count", "" + dbhelper.getToDoCount());
			Log.d("DAta Storeere",""+ todo1_id);
			Log.d("attempts", ""+databasedata.getAttempts());
			dbhelper.close();

		}else{
			DbUSer data=new DbUSer();
			data.setQuizName(ShowSubTransitionListFragment.selectedQuiz);
			data.setQuizlevelname(clcikedname);
			data.setTotalNoQuestions(questions.size());
			data.setScore(userscore);
			data.setAttempts(1);

			long todo1_id = dbhelper.createToDo(data);
			Log.d("Tag Count", "" + dbhelper.getToDoCount());
			Log.d("DAta Storeere",""+ todo1_id);
			dbhelper.close();
		}

		TextView title,dialog_text;

		dialog = new Dialog(context,
				android.R.style.Theme_Translucent);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		dialog.setCancelable(false);
		dialog.setContentView(R.layout.finallayout_dialog);

		title=(TextView)dialog.findViewById(R.id.dtitle);
		dialog_text=(TextView)dialog.findViewById(R.id.dtext);

		if(text.equalsIgnoreCase("Time Up")){
			title.setText("Oops ...");

		}
		dialog_text.setText(text);

		btnCancel = (Button) dialog.findViewById(R.id.btncancel);
		btnCancel.setText("OK");

		btnCancel.setOnClickListener(new OnClickListener() {

			public void onClick(View view) {
				dialog.cancel();
				startActivity(new Intent(SingleQuestionDetailActivity.this,ResultsActivitys.class));

			}

		});

		//		final ImageView myImage = (ImageView) dialog.findViewById(R.id.loader);
		//		myImage.startAnimation(AnimationUtils.loadAnimation(SingleQuestionDetailActivity.this, R.anim.rotate) );
		//
		//		myImage.setVisibility(View.GONE);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0x7f000000));

		dialog.show();
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		countertimer.cancel();
		Intent i2 = new Intent(SingleQuestionDetailActivity.this, MainActivity.class);
		startActivity(i2);
		//overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
		return super.onOptionsItemSelected(menuItem);
	}

	private class ImageGetter implements Html.ImageGetter {

		public Drawable getDrawable(final String source) {

			//final String source1 =source.substring(1,source.length()-2);
			String url1=source.replace("\\", "").trim();
			final String url=url1.replace("\"", " ").trim();
			Log.d("dddd", ""+url);

			new DownloadImageTask((ImageView) findViewById(R.id.imageView1))
			.execute(url); 

			//			new Thread(new Runnable(){ 
			//				@Override 
			//				public void run() { 
			//					InputStream is;
			//					Drawable d1 = null;
			//					try {
			//						is = (InputStream) new URL(url).getContent();
			//						d1 = Drawable.createFromStream(is, "src name");
			//						//d1.setBounds(0,0,d.getIntrinsicWidth(),d.getIntrinsicHeight());
			//						d=d1;
			//						runOnUiThread(new Runnable() {
			//							@Override
			//							public void run() {
			//								// TODO Auto-generated method stub
			//								//textView.setText(Html.fromHtml(questions.get(index), new ImageGetter(), null));								
			//								//radio0.setText(Html.fromHtml(optionas.get(index), new ImageGetter(), null));
			//								//radio1.setText(Html.fromHtml(optionbs.get(index), new ImageGetter(), null));
			//								//radio2.setText(Html.fromHtml(optioncs.get(index), new ImageGetter(), null));
			//								//radio3.setText(Html.fromHtml(optionds.get(index), new ImageGetter(), null));
			//								imageView1.setImageDrawable(d);
			//							}
			//						});
			//					} catch (MalformedURLException e) {
			//						// TODO Auto-generated catch block
			//						e.printStackTrace();
			//					} catch (IOException e) {
			//						// TODO Auto-generated catch block
			//						e.printStackTrace();
			//					}
			//					finally{
			//					}
			//				} 
			//
			//			}).start();
			return d; 

		}
	};


	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public DownloadImageTask(ImageView bmImage) {
			this.bmImage = bmImage;
		} 

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			try { 
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			} 
			return mIcon11;
		} 

		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
		} 
	} 

}