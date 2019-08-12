
package com.bufferchime.agriculturefact.materialdesgin;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app. AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bufferchime.agriculturefact.R;

import java.util.ArrayList;
import java.util.List;

public class CompletedQuizesFragment extends Fragment {

	private View parentView;
	private ListView mainListView;
	private CompletedQuilzListAdapter mAdapter;
	Context mActivity;
	public static ArrayList<String> subQuizes = new ArrayList<String>();
	ArrayList<DbUSer> quilzList = new ArrayList<DbUSer>();
	private Toolbar toolbar;
	private Dialog dialog;
	ArrayList<String> colorsal=new ArrayList<String>();
	DataBaseHelper dbhelper;

	@SuppressLint("ResourceAsColor") @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mActivity=getActivity();
		parentView = inflater.inflate(R.layout.fragment_list, container, false);
		mainListView = (ListView)parentView.findViewById( R.id.mainListView );
		toolbar = (Toolbar) parentView.findViewById(R.id.toolbar);
		dbhelper=new DataBaseHelper(mActivity);

		//set toolbar appearance 
		//toolbar.setBackgroundColor(R.color.material_deep_teal_200);

		TextView tv=(TextView)toolbar.findViewById(R.id.toolbar_title);
		tv.setText("Attempted Quizes");
		//for crate home button 
		AppCompatActivity activity = ( AppCompatActivity) getActivity();
		activity.setSupportActionBar(toolbar);

		ImageView image = (ImageView) toolbar.findViewById(R.id.toolbar_image);
		image.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				((MainActivity)getActivity()).openorclose(); 

			}
		});
		image.setVisibility(View.GONE);

		colorsal.add("#F44336");
		colorsal.add("#9C27B0");	
		colorsal.add("#7C4DFF");
		colorsal.add("#2196F3");
		colorsal.add("#00BCD4");
		colorsal.add("#009688");
		colorsal.add("#4CAF50");
		colorsal.add("#8BC34A");
		colorsal.add("#CDDC39");
		colorsal.add("#FFEB3B");
		colorsal.add("#212121");
		colorsal.add("#FFA000");
		colorsal.add("#FF5722");
		colorsal.add("#5D4037");
		colorsal.add("#9E9E9E");

		ImageView image1 = (ImageView) toolbar.findViewById(R.id.scaner);
		image1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				((MainActivity)getActivity()).downloadfromallquizes();
			}
		});
		//image.setVisibility(View.GONE);
		toolbar.setNavigationIcon(R.drawable.ic_action_leftmenu); 

		initView();
		return parentView;
	}

	protected void showCustomDialog() {

		dialog = new Dialog(getActivity(),android.R.style.Theme_Translucent);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		dialog.setCancelable(true);
		dialog.setContentView(R.layout.layout_dialog);

		final ImageView myImage = (ImageView) dialog.findViewById(R.id.loader);
		myImage.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.rotate) );

		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0x7f000000));

		dialog.show();
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	private void initView(){
		List<DbUSer> dataSqlite=dbhelper.getAllData();

		if(dbhelper!=null && dataSqlite != null && dataSqlite.size() >0 )
		{
			for(int i=0;i<dataSqlite.size();i++){
				String firstchar;
				if(dataSqlite.get(i).getQuizlevelname() != null && dataSqlite.get(i).getQuizlevelname().length()>0)
				 firstchar=""+dataSqlite.get(i).getQuizlevelname().charAt(0);
				else
					firstchar=""+"M";
				//int identifier =getResources().getIdentifier("ic_"+firstchar.toLowerCase(), "drawable",mActivity.getPackageName());
				int colorid= i % colorsal.size();
				DbUSer dbuser=new  DbUSer();

				dbuser.setColor(colorsal.get(colorid));
				dbuser.setAttempts(dataSqlite.get(i).getAttempts());
				dbuser.setDateofquiz(dataSqlite.get(i).getDateofquiz());
				dbuser.setFirstchar(firstchar);
				dbuser.setQuizlevelname(dataSqlite.get(i).getQuizlevelname());
				dbuser.setQuizName(dataSqlite.get(i).getQuizName());
				dbuser.setScore(dataSqlite.get(i).getScore());
				dbuser.setTotalNoQuestions(dataSqlite.get(i).getTotalNoQuestions());
				quilzList.add(dbuser);

			}

		}


		mAdapter=new CompletedQuilzListAdapter(mActivity, quilzList);
		mainListView.setAdapter(mAdapter);
		mainListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


				//				if(subQuizes.size()>0)
				//					subQuizes.clear();
				//				Worksheet worksheet = workbook.getWorksheets().get(0);
				//
				//				Row clcikedrow=worksheet.getCells().getRows().get(i);
				//				Iterator<Cell> cellIterator = clcikedrow.iterator();
				//				while(cellIterator.hasNext())
				//				{
				//					Cell cell= cellIterator.next();
				//					//QuizTypes.add(cell.getStringValue());
				//					Log.d("cell" , cell.getStringValue());
				//					subQuizes.add(cell.getStringValue());
				//
				//				}
				//				//removing first element
				//				subQuizes.remove(0);
				//
				//				if(subQuizes != null && subQuizes.size() >0){
				//					//					getActivity().getSupportFragmentManager()
				//					//					.beginTransaction()
				//					//					.replace(R.id.mainContent, new ShowSubTransitionListFragment(), "subquizlistfragment")
				//					//					.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
				//					//					//.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
				//					//					.commit();
				//
				//
				//					FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();        
				//					transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
				//					transaction.replace(R.id.mainContent, new ShowSubTransitionListFragment());
				//					transaction.commit(); 
				//
				//					//startActivity(new Intent(mActivity,ShowSubTransactionActivity.class));
				//				}

			}
		});
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}
	public void onBackPressed() {
		Intent setIntent = new Intent(Intent.ACTION_MAIN);
		setIntent.addCategory(Intent.CATEGORY_HOME);
		setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(setIntent);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		((MainActivity)getActivity()).openorclose(); 
		return super.onOptionsItemSelected(menuItem);
	}

}
