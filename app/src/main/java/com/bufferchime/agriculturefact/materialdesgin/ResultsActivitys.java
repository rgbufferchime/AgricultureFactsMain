package com.bufferchime.agriculturefact.materialdesgin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app. AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bufferchime.agriculturefact.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;

public class ResultsActivitys extends  AppCompatActivity {

	ListView listView;
	ResultTransitionListAdapter mAdapter;
	ArrayList<ResultsListItem> listData = new ArrayList<ResultsListItem>();
	int userscore=0;
	RelativeLayout sharebar;
	ImageView launchericon;
	public static ArrayList<String> question = new ArrayList<String>();
	public static ArrayList<String> answers = new ArrayList<String>();
	public static ArrayList<String> useranswers = new ArrayList<String>();
	public static ArrayList<String> reasons = new ArrayList<String>();
	public static ArrayList<String> optionas = new ArrayList<String>();
	public static ArrayList<String> optionbs = new ArrayList<String>();
	public static ArrayList<String> optioncs = new ArrayList<String>();
	public static ArrayList<String> optionds = new ArrayList<String>();
	ImageView title_bar_share_menu;
	TextView titleBarText,scorecard;
	

	ImageView im,syncimage;
	//AdView mAdView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_results_activitys);
		listView   = (ListView)findViewById(R.id.listView);
		sharebar=(RelativeLayout)findViewById(R.id.sharebar);
		titleBarText=(TextView) findViewById(R.id.titleBar);
		scorecard=(TextView) findViewById(R.id.scorecard);
//		mAdView = (AdView) findViewById(R.id.adMob);
//		sharebar.setVisibility(View.VISIBLE);
//		mAdView.setVisibility(View.VISIBLE);
//		launcherbar.setVisibility(View.INVISIBLE);
//
//		AdRequest adRequest = new AdRequest.Builder()

		// Add a test device to show Test Ads
		//.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
		//.addTestDevice("AD795F0DA0BB01DEE26A8E6D309DBF76") //Random Text
//		.build();
//
//		// Load ads into Banner Ads
//		mAdView.loadAd(adRequest);
	    overridePendingTransition(R.anim.enter_from_right,R.anim.exit_to_left);


		
		SingleQuestionDetailActivity sqd=new SingleQuestionDetailActivity();

		if(useranswers.size() >0){
			useranswers.clear();
		}if(question.size() >0){
			question.clear();
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
		}if(listData.size() >0 ){
			listData.clear();
		}


		if(sqd.questions.size() >0){
			question.addAll(sqd.questions);
		}
		if(sqd.optionas.size() >0){
			optionas.addAll(sqd.optionas);
		}

		if(sqd.optionbs.size() >0){
			optionbs.addAll(sqd.optionbs);
		}

		if(sqd.optioncs.size() >0){
			optioncs.addAll(sqd.optioncs);
		}

		if(sqd.optionds.size() >0){
			optionds.addAll(sqd.optionds);
		}
		if(sqd.answers.size() >0)
		{
			answers.addAll(sqd.answers);
		}
		if(sqd.reasons.size() >0){
			reasons.addAll(sqd.reasons);	
		}if(sqd.useranswers.size() >0){
			useranswers.addAll(sqd.useranswers);	
		}
		//		question.addAll(sqd.questions);
		//		answers.addAll(sqd.answers);
		//		useranswers.addAll(sqd.useranswers);
		//		reasons.addAll(sqd.reasons);
		//		optionas.addAll(sqd.optionas);
		//		optionbs.addAll(sqd.optionbs);
		//		optioncs.addAll(sqd.optioncs);
		//		optionds.addAll(sqd.optionds);

		for(int i=0;i<question.size();i++){
			String answe = null;
			String userAns = null;

			if(answers.get(i).equalsIgnoreCase("1") ){
				answe=optionas.get(i);
			}else if(answers.get(i).equalsIgnoreCase("2") ){
				answe=optionbs.get(i);

			}else if(answers.get(i).equalsIgnoreCase("3") ){
				answe=optioncs.get(i);

			}else if(answers.get(i).equalsIgnoreCase("4") ){
				answe=optionds.get(i);

			}


			if(useranswers.get(i).equalsIgnoreCase("1")){
				userAns=optionas.get(i);

			}else if(useranswers.get(i).equalsIgnoreCase("2") ){
				userAns=optionbs.get(i);

			}else if(useranswers.get(i).equalsIgnoreCase("3") ){
				userAns=optioncs.get(i);

			}else if(useranswers.get(i).equalsIgnoreCase("4") ){
				userAns=optionds.get(i);

			}else if(useranswers.get(i).equalsIgnoreCase("not") ){
				userAns="Not answered";

			}
			String reasondes = "";
			if(i<reasons.size())
				reasondes=reasons.get(i);

			ResultsListItem rli=new ResultsListItem(question.get(i),answe, userAns,reasondes);
			listData.add(rli);	
		}
		mAdapter = new ResultTransitionListAdapter(ResultsActivitys.this,listData);
		listView.setAdapter(mAdapter);

		final int fi=ShowSubTransitionListFragment.clickedposition +1;
		int si=ShowSubTransitionListFragment.totalsubquizes;

		for(int i=0;i<answers.size();i++)
		{
			Log.d("user answer", useranswers.get(i)+"/"+answers.get(i));
			if(useranswers.get(i).equalsIgnoreCase(answers.get(i)))
				userscore++;

		}

		titleBarText.setText("Your Score");
		scorecard.setText(userscore+"/"+question.size());
		//Log.d("clciked post", ""+fi+si);
		if(fi == si)
		{
			syncimage.setEnabled(false);
			syncimage.setVisibility(View.INVISIBLE);
			ShowSubTransitionListFragment.setTotalsubquizes(0);
		}



		Button btnback = (Button) findViewById(R.id.title_bar_left_menu);
		btnback.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				userbackpressed();
			}

			private void userbackpressed() {

				Intent i=new Intent(ResultsActivitys.this,MainActivity.class);
				startActivity(i);

				//getSupportFragmentManager().popBackStack();
			}

		});



	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(question != null && question.size() >0)
			question.clear();
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


	}
	@Override
	public void onBackPressed() {
		Intent i=new Intent(ResultsActivitys.this,MainActivity.class);
		startActivity(i);
		super.onBackPressed();

	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	private void shareTextUrl() {


		File file = new File(getExternalFilesDir(null), "screen.jpg");
		//String file1=Environment.getExternalStorageDirectory().toString()+"/"+ "screen.jpg";
		View v = getWindow().getDecorView().getRootView();
		v.setDrawingCacheEnabled(true);
		Bitmap bmp = Bitmap.createBitmap(v.getDrawingCache());
		v.setDrawingCacheEnabled(false);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			bmp.compress(CompressFormat.PNG, 100, fos);
			Uri imageUri = Uri.fromFile(file);
			Intent share = new Intent(android.content.Intent.ACTION_SEND);
			share.setType("text/plain");
			share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
			share.putExtra(Intent.EXTRA_SUBJECT, "Your Projected Score in Mobi Quiz");
			share.putExtra(Intent.EXTRA_TEXT, "Your Score is"+userscore+"/"+question.size());
			share.putExtra(Intent.EXTRA_STREAM, imageUri);
			share.setType("image/*");
			//share.setType("multipart/mixed");
			share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			startActivity(Intent.createChooser(share, "Share link!"));

			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			sharebar.setVisibility(View.VISIBLE);
			//mAdView.setVisibility(View.VISIBLE);
			//launcherbar.setVisibility(View.GONE);

		}

	}

	private File savebitmap(Bitmap bmp) {
		String temp="SplashItShare";
		String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
		OutputStream outStream = null;
		String path = Environment.getExternalStorageDirectory()
				.toString();
		new File(path + "/SplashItTemp").mkdirs();
		File file = new File(path+"/SplashItTemp", temp + ".png");
		if (file.exists()) {
			file.delete();
			file = new File(path+"/SplashItTemp", temp + ".png");
		}

		try {
			outStream = new FileOutputStream(file);
			bmp.compress(Bitmap.CompressFormat.PNG, 100, outStream);
			outStream.flush();
			outStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return file;
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
	        Intent i2 = new Intent(ResultsActivitys.this, MainActivity.class);
	        startActivity(i2);
	        //overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
	        return super.onOptionsItemSelected(menuItem);
	    }

}
