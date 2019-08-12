package com.bufferchime.agriculturefact.materialdesgin;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app. AppCompatActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.aspose.cells.Cell;
import com.aspose.cells.FileFormatType;
import com.aspose.cells.LoadOptions;
import com.aspose.cells.Row;
import com.aspose.cells.Workbook;
import com.aspose.cells.Worksheet;
import com.bufferchime.agriculturefact.Contact;
import com.bufferchime.agriculturefact.Home;
import com.bufferchime.agriculturefact.Notes;
import com.bufferchime.agriculturefact.OlderPosts;
import com.bufferchime.agriculturefact.font.RobotoRegularTextView;
import com.bufferchime.agriculturefact.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;


public class MainActivity extends  AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
	protected BottomNavigationView navigationView;
	Boolean doubleBackToExitPressedOnce = false;
	private static String TAG = MainActivity.class.getSimpleName();
	//String url="https://onedrive.live.com/download?resid=2B70A9A9AB59B867%21111";
	String url="https://www.dropbox.com/s/mp9xfv1q203peon/MobiQuiz.xlsx?dl=1";
	
	public static Workbook workbook = null;
	public static Workbook getWorkbook() {
		return workbook;
	}

	public static void setWorkbook(Workbook workbook) {
		MainActivity.workbook = workbook;
	}

	public static ArrayList<String> QuizTypes = new ArrayList<String>();
	Context context;
	Button refresh;
	ListView mDrawerList;
	RelativeLayout mDrawerPane;
	ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();
	Dialog dialog;
	RobotoRegularTextView progressdata;
	ArrayList<String> totalquizes=new ArrayList<String>();

	private int delta_top;
	private int delta_left;
	private float scale_width;
	private float scale_height;
	ProgressBar mProgress;
	RelativeLayout profileBox;

	int imgId;
	DataBaseHelper dbhelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		dbhelper=new DataBaseHelper(MainActivity.this);
		navigationView = (BottomNavigationView) findViewById(R.id.navigation);
		navigationView.setOnNavigationItemSelectedListener(this);

		//mProgress = (ProgressBar) findViewById(R.id.progressbar1);
		//progressdata=(RobotoRegularTextView)findViewById(R.id.progressdata);


		//mNavItems.add(new NavItem("About", "Get to know about us", R.drawable.ic_launcher));

		// DrawerLayout
		//mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

		//profileBox=(RelativeLayout)findViewById(R.id.profileBox);


		// Populate the Navigtion Drawer with options
		//mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
		mDrawerList = (ListView) findViewById(R.id.navList);
		DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
		mDrawerList.setAdapter(adapter);

		// Drawer Item click listeners
		mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
				selectItemFromDrawer(position);

			}
		});


		//mDrawerLayout.setDrawerListener(mDrawerToggle);

		context=MainActivity.this;
		
		if(workbook != null){
			changeFragment(new ShowAllQuizesFragment());

		}else{
			File file = new File(getExternalFilesDir(null), "MobiQuiz.xlsx");
			showCustomDialog();
			if(file.exists()){
				//Creating an Workbook object with the stream object
				try 
				{
					updateArraylistListData();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				new DownloadMusicfromInternet().execute(url);
			}
		}
		
		
	}

	public void openorclose()
	{

	}

	public void downloadfromallquizes()
	{
		showCustomDialog();
		new DownloadMusicfromInternet().execute(url);

	}

	private void onlyupdateQuizes(){

		if(workbook !=null){
			Worksheet worksheet = workbook.getWorksheets().get(0);

			Iterator<Row> rowIterator = worksheet.getCells().getRows().iterator();
			while(rowIterator.hasNext())

			{
				Row r = rowIterator.next();
				Iterator<Cell> cellIterator = r.iterator();
				while(cellIterator.hasNext())
				{
					Cell cell= cellIterator.next();
					QuizTypes.add(cell.getStringValue());
					Log.d("cell" , cell.getStringValue());
					break;
				}

			}	
			if(QuizTypes.size() >0){

				//dialog.cancel();
				changeFragment(new ShowAllQuizesFragment());

			}

		}
		
	}
	

	private void updateArraylistListData() {
		if(dialog!= null && !dialog.isShowing()){
			dialog.show();
		}

		Thread mThread = new Thread() {
			@Override
			public void run() {
				File file = new File(getExternalFilesDir(null), "MyQuiz.xlsx");

				FileInputStream fstream ;
				//LoadOptions loadOptions = new LoadOptions(FileFormatType.XLSX);
				//loadOptions.setPassword("rayala1234");
				FileInputStream myInput = null;
				try {
					fstream = new FileInputStream(file);
					workbook = new Workbook(fstream);

				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(QuizTypes != null && QuizTypes.size() >0)
					QuizTypes.clear();

				if(workbook !=null){
					Worksheet worksheet = workbook.getWorksheets().get(0);

					Iterator<Row> rowIterator = worksheet.getCells().getRows().iterator();
					while(rowIterator.hasNext())

					{
						Row r = rowIterator.next();
						Iterator<Cell> cellIterator = r.iterator();
						while(cellIterator.hasNext())
						{
							Cell cell= cellIterator.next();
							QuizTypes.add(cell.getStringValue());
							Log.d("cell" , cell.getStringValue());
							break;
						}

					}	
					if(QuizTypes.size() >0){

						dialog.cancel();
						changeFragment(new ShowAllQuizesFragment());

					}

				}

			}
		};
		mThread.start();

	}

	public int totalQuizes(){

		Worksheet worksheet = workbook.getWorksheets().get(0);
		int value=0;
		for(int i=0;i<worksheet.getCells().getRows().getCount();i++){
			value++;
			Row clcikedrow=worksheet.getCells().getRows().get(i);
			Iterator<Cell> cellIterator = clcikedrow.iterator();
			while(cellIterator.hasNext())
			{
				Cell cell= cellIterator.next();
				//QuizTypes.add(cell.getStringValue());
				Log.d("cell" , cell.getStringValue());
				totalquizes.add(cell.getStringValue());

			}
		}
		int totalqui=totalquizes.size() - value;
		//removing first element
		//totalquizes.remove(0);

		File database=getApplicationContext().getDatabasePath("userDataManager.db");

		if (!database.exists()) {
			// Database does not exist so copy it from assets here
			Log.i("Database", "Not Found");
		} else {
			Log.i("Database", "Found");
		}
		Resources res = getResources();
		Drawable drawable = res.getDrawable(R.drawable.progress_bar);

		Log.d("sqlite data", ""+totalqui+ "dads"+dbhelper.getToDoCount());

		if(dbhelper != null && dbhelper.getToDoCount() != 0)
			mProgress.setProgress(dbhelper.getToDoCount());   // Main Progress
		else
			mProgress.setProgress(0);
		//mProgress.setSecondaryProgress(50); // Secondary Progress
		mProgress.setMax(totalqui); // Maximum Progress
		progressdata.setText(dbhelper.getToDoCount()+" out of "+totalqui+" quizes Completed");
		mProgress.setProgressDrawable(drawable);

		return totalqui;

	}

	private void changeFragment(Fragment targetFragment){
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.mainContent, targetFragment, "fragment")
		.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
		.commit();
	}

	class DownloadMusicfromInternet extends AsyncTask<String, String, String> {



		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}


		protected String doInBackground(String... f_url) {

			File file = new File(getExternalFilesDir(null), "MyQuiz.xlsx");
			try {
				URL url = new URL(f_url[0]);
				Log.i("FILE_URLLINK", "File URL is "+url);
				URLConnection connection = url.openConnection();
				connection.connect();
				// download the file
				InputStream input = new BufferedInputStream(url.openStream());
				OutputStream output = new FileOutputStream(file);
				byte data[] = new byte[1024];
				long total = 0;
				int count;
				while ((count = input.read(data)) != -1) {
					total += count;
					output.write(data, 0, count);
				}
				output.flush();
				output.close();
				input.close();
			} catch (Exception e) {
				e.printStackTrace();
				Log.i("ERR  DOWNLOADING FILES", "ERROR IS" +e);
			}finally{
				Log.i("Completed", "ERROR IS");

			}
			return null;
		}

		protected void onProgressUpdate(String... progress) {
		}

		protected void onPostExecute(String file_url) {
			updateArraylistListData();

		}

	}


	private void selectItemFromDrawer(int position) {
		android.support.v4.app.Fragment fragment;
		if(position == 0){
			fragment = new ShowAllQuizesFragment();

		}else if(position ==1 ){
			fragment = new CompletedQuizesFragment();
		}else{
			fragment = new ContactFragment();
		}

		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
		transaction.replace(R.id.mainContent,fragment);
		transaction.commit(); 

		mDrawerList.setItemChecked(position, true);
		setTitle(mNavItems.get(position).mTitle);


		// Close the drawer
	//	mDrawerLayout.closeDrawer(mDrawerPane);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Fragment f = getSupportFragmentManager().findFragmentById(R.id.mainContent);
		if (f instanceof ShowAllQuizesFragment)  {
			Intent startMain = new Intent(Intent.ACTION_MAIN);
			startMain.addCategory(Intent.CATEGORY_HOME);
			startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(startMain);
		}else if(f instanceof ShowSubTransitionListFragment || f instanceof ContactFragment){
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
			transaction.replace(R.id.mainContent,new ShowAllQuizesFragment());
			transaction.commit();

		}else {

			// handle by activity
			super.onBackPressed();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		return super.onOptionsItemSelected(item);
	}

	public boolean onPrepareOptionsMenu(Menu menu) {

		return super.onPrepareOptionsMenu(menu);
	}

	protected void showCustomDialog() {

		dialog = new Dialog(context,android.R.style.Theme_Translucent);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		dialog.setCancelable(true);
		dialog.setContentView(R.layout.layout_dialog);

		final ImageView myImage = (ImageView) dialog.findViewById(R.id.loader);
		myImage.startAnimation(AnimationUtils.loadAnimation(context, R.anim.rotate) );

		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0x7f000000));

		dialog.show();
	}











	//Navigationviewstart
	@Override
	protected void onStart() {
		super.onStart();
		updateNavigationBarState();
	}
	// Remove inter-activity transition to avoid screen tossing on tapping bottom navigation items
	@Override
	public void onPause() {
		super.onPause();
		overridePendingTransition(0, 0);
	}
	@Override
	public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
		navigationView.postDelayed(new Runnable() {
			@Override
			public void run() {
				int itemId = item.getItemId();
				if (itemId == R.id.navigation_home) {
					MainActivity.this.startActivity(new Intent(MainActivity.this, Home.class));
				} else if (itemId == R.id.navigation_affairs) {
					MainActivity.this.startActivity(new Intent(MainActivity.this, OlderPosts.class));
				} else if (itemId == R.id.navigation_notes) {
					MainActivity.this.startActivity(new Intent(MainActivity.this, Notes.class));
				} else if (itemId == R.id.navigation_tests) {
					MainActivity.this.startActivity(new Intent(MainActivity.this, MainActivity.class));
				} else if (itemId == R.id.navigation_contact) {
					MainActivity.this.startActivity(new Intent(MainActivity.this, Contact.class));
				}
				MainActivity.this.finish();
			}
		}, 300);
		return true;
	}
	private void updateNavigationBarState(){
		int actionId = R.id.navigation_tests;
		selectBottomNavigationBarItem(actionId);
	}
	void selectBottomNavigationBarItem(int itemId) {
		MenuItem item = navigationView.getMenu().findItem(itemId);
		item.setChecked(true);
	}


//NavigationViewEnd



}
