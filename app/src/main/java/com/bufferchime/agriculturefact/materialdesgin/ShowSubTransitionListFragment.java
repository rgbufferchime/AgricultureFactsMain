package com.bufferchime.agriculturefact.materialdesgin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app. AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aspose.cells.Cell;
import com.aspose.cells.Row;
import com.aspose.cells.Workbook;
import com.aspose.cells.Worksheet;
import com.bufferchime.agriculturefact.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ShowSubTransitionListFragment extends Fragment {

	//Views & Widgets
	private View parentView;
	private ListView listView;
	public static List<String> questions = new ArrayList<String>();
	public static List<String> optionas = new ArrayList<String>();
	public static List<String> optionbs = new ArrayList<String>();
	public static List<String> optioncs = new ArrayList<String>();
	public static List<String> optionds = new ArrayList<String>();
	public static List<String> answers = new ArrayList<String>();
	public static List<String> reasons = new ArrayList<String>();
	public static int totalsubquizes=0 ; 
	ArrayList<String> colorsal=new ArrayList<String>();
	public static String selectedQuiz=null;

	public static int clickedposition ; 

	public static void setTotalsubquizes(int totalsubquizes) {
		ShowSubTransitionListFragment.totalsubquizes = 0;
	}

	public static int getClickedposition() {
		return clickedposition;
	}

	public static void setClickedposition(int clickedposition) {
		ShowSubTransitionListFragment.clickedposition = clickedposition;
	}

	public static int counttimervalue=0;

	//Vars
	public static ArrayList<String> subQuizes = new ArrayList<String>();

	private String PACKAGE = "IDENTIFY";
	ArrayAdapter listAdapter;
	public static Workbook workbook = null;
	Context mActivity;
	LinearLayout topview;
	TextView titleview;
	private QuilzListAdapter mAdapter;

	ArrayList<QuizListItem> quilzList = new ArrayList<QuizListItem>();
	private Toolbar toolbar;
	ImageView im,scaner;
	TextView toolbar_title, productName, productRate, productCompany,nutrion,contains;


	@SuppressLint("ResourceAsColor") public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		parentView = inflater.inflate(R.layout.fragment_list_transition, container, false);
		mActivity=getActivity();
		topview = (LinearLayout) parentView.findViewById(R.id.layout_top);
		toolbar = (Toolbar) parentView.findViewById(R.id.toolbar);

		 AppCompatActivity activity = ( AppCompatActivity) getActivity();

		topview.getBackground().setAlpha(0);
		//titleBar.setVisibility(View.INVISIBLE);
		selectedQuiz=ShowAllQuizesFragment.selectedQuiz;
		//loading colors into array
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
		


		
		

		listView   = (ListView) parentView.findViewById(R.id.listView);
		initView();
		return parentView;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);


	}
	@Override
	public void onActivityCreated( Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
//		getActivity().getSupportFragmentManager()
//		.beginTransaction()
//		.replace(R.id.mainContent, new ShowAllQuizesFragment(), "alllistfragment")
//		.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
//		.commit();
		
		FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
		transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
		transaction.replace(R.id.mainContent, new ShowAllQuizesFragment());
		transaction.commit(); 
		
		return super.onOptionsItemSelected(item);
	} 
	private void initView(){

		ShowAllQuizesFragment showallobject=new ShowAllQuizesFragment();

		workbook=showallobject.workbook;
		if(subQuizes!=null && subQuizes.size() >0)
			subQuizes.clear();

		subQuizes.addAll(showallobject.subQuizes);
		if(subQuizes != null && subQuizes.size() >0){
			if(totalsubquizes != 0){
				totalsubquizes=0;
			}
			totalsubquizes=subQuizes.size();
		}

		for(int i=0;i<subQuizes.size();i++){
			//retriving the first charcater
			if(!TextUtils.isEmpty(subQuizes.get(i))){
				String firstchar=""+subQuizes.get(i).charAt(0);
				if(firstchar != null && !TextUtils.isEmpty(firstchar)){
					int identifier =getResources().getIdentifier("ic_"+firstchar.toLowerCase(), "drawable",mActivity.getPackageName());
					
					int colorid= i % colorsal.size();
					QuizListItem qlistitem=new QuizListItem(identifier, subQuizes.get(i),colorsal.get(colorid),firstchar);
					quilzList.add(qlistitem);
				}
			}

		}
		mAdapter=new QuilzListAdapter(mActivity, quilzList);

		listView.setAdapter(mAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> adapterView, View viewa, int position, long l) {

				clickedposition=position;


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
				if(reasons != null && reasons.size() >0)
					reasons.clear();
				String ss=subQuizes.get(position);
				Worksheet worksheet=null;
				worksheet = workbook.getWorksheets().get(ss);

				if(worksheet !=null){
					Iterator<Row> rowIterator = worksheet.getCells().getRows().iterator();
					
					while(rowIterator.hasNext())

					{
						int i=0;
						Row r = rowIterator.next();
						Iterator<Cell> cellIterator = r.iterator();
						if(r.getFirstCell() != null )
						{

						for(int col=0;col<=8;col++){
							//Cell cell= cellIterator.next();
							Cell cell = r.getCellOrNull(col);

							if(col == 0){
								if(cell != null &&cell.getStringValue() != null && !TextUtils.isEmpty(cell.getStringValue()))
								questions.add(cell.getStringValue());
								else
									//questions.add("N");
								break;
							}else if(col == 1){
								if(cell != null &&cell.getStringValue() != null && !TextUtils.isEmpty(cell.getStringValue()))
								optionas.add(cell.getStringValue());
								else
									optionas.add("N/A");

							}else if(col == 2){
								if(cell != null &&cell.getStringValue() != null && !TextUtils.isEmpty(cell.getStringValue()))
								optionbs.add(cell.getStringValue());
								else
									optionbs.add("N/A");

							}else if(col == 3){
								if(cell != null &&cell.getStringValue() != null && !TextUtils.isEmpty(cell.getStringValue()))
								optioncs.add(cell.getStringValue());
								else
									optioncs.add("N/A");

							}else if(col == 4){
								if(cell != null &&cell.getStringValue() != null && !TextUtils.isEmpty(cell.getStringValue()))
								optionds.add(cell.getStringValue());
								else
									optionds.add("N/A");

							}else if(col == 5){
								if(cell != null &&cell.getStringValue() != null && !TextUtils.isEmpty(cell.getStringValue()))
								answers.add(cell.getStringValue());
								else
									reasons.add("N/A");

							}else if(col == 6){
								if(cell != null && cell.getStringValue() != null && !TextUtils.isEmpty(cell.getStringValue()))
									reasons.add(cell.getStringValue());
								else
									reasons.add("N/A");


							}else if(col == 8){
								if(cell != null && cell.getStringValue() != null && !TextUtils.isEmpty(cell.getStringValue()))
								counttimervalue=cell.getIntValue();
							}
						}
						}
						
//						Iterator<Cell> cellIterator = r.iterator();
//						while(cellIterator.hasNext())
//						{
//							Cell cell= cellIterator.next();
//							Log.d("cell" , cell.getStringValue());
//							if( cell.getStringValue() != null && !TextUtils.isEmpty(cell.getStringValue())){
//								Log.d("cell" , cell.getStringValue());
//								if(i == 0){
//									questions.add(cell.getStringValue());
//								}else if(i == 1){
//									optionas.add(cell.getStringValue());
//
//								}else if(i == 2){
//									optionbs.add(cell.getStringValue());
//
//								}else if(i == 3){
//									optioncs.add(cell.getStringValue());
//
//								}else if(i == 4){
//									optionds.add(cell.getStringValue());
//
//								}else if(i == 5){
//									answers.add(cell.getStringValue());
//
//								}else if(i == 6){
//									if(cell.getStringValue() != null)
//										reasons.add(cell.getStringValue());
//									else
//										reasons.add("");
//
//
//								}else if(i == 8){
//									counttimervalue=cell.getIntValue();
//								}
//								i++;
//							}
//
//						}

					}
					//removing the first element from the array list beacuse they are heading (i.e Question,Optiona...)
					questions.remove(0);
					optionas.remove(0);
					optionbs.remove(0);
					optioncs.remove(0);
					optionds.remove(0);
					answers.remove(0);
					reasons.remove(0);

					Log.d("questions", ""+questions.size());

					Intent intent = new Intent(getActivity(), SingleQuestionDetailActivity.class);

					Bundle bundle = new Bundle();
					//bundle.putString("title", item.getTitle());

					int[] screen_location = new int[2];
					View view = viewa.findViewById(R.id.item_title);
					view.getLocationOnScreen(screen_location);

					bundle.putInt(PACKAGE + ".left", screen_location[0]);
					bundle.putInt(PACKAGE + ".top", screen_location[1]);
					bundle.putInt(PACKAGE + ".width", view.getWidth());
					bundle.putInt(PACKAGE + ".height", view.getHeight());
					bundle.putString("CLICKEDNAME", ss);
					bundle.putString("from", "ShowSubTransactionFragemnt");

					intent.putExtras(bundle);

					startActivity(intent);
					//getActivity().overridePendingTransition(0, 0);
				}
			}
		});

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(subQuizes.size() > 0)
			subQuizes.clear();
	}

//	public void onBackPressed() { 
//		getActivity().getSupportFragmentManager()
//		.beginTransaction()
//		.replace(R.id.mainContent, new ShowAllQuizesFragment(), "alllistfragment")
//		.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
//		.commit();	
//
//	} 

}
