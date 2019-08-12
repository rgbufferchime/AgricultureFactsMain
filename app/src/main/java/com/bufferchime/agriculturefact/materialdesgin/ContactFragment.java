package com.bufferchime.agriculturefact.materialdesgin;


import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app. AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bufferchime.agriculturefact.R;

public class ContactFragment extends Fragment {
	
	//Layouts
 	Button btn, btnCancel;
 	Dialog dialog;
 	TextView telephone,email,buylink;
 	private Toolbar toolbar;
	ImageView im,scaner;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	final ScrollView v =  (ScrollView) inflater.inflate(R.layout.fragment_contact, container, false);
    	
    	MainActivity parentActivity = (MainActivity) getActivity();
    	email=(TextView)v.findViewById(R.id.emailid);
    	telephone=(TextView)v.findViewById(R.id.telephone);
    	buylink=(TextView)v.findViewById(R.id.buylink);
    	
    	toolbar = (Toolbar) v.findViewById(R.id.toolbar);

		 AppCompatActivity activity = ( AppCompatActivity) getActivity();
		activity.setSupportActionBar(toolbar);
		//activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		//titleBar.setVisibility(View.INVISIBLE);
		toolbar.setNavigationIcon(R.drawable.ic_action_leftmenu); 

		ImageView image = (ImageView) toolbar.findViewById(R.id.toolbar_image);
		image.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
                ((MainActivity)getActivity()).openorclose();

			}
		});
		image.setVisibility(View.VISIBLE);

		ImageView image1 = (ImageView) toolbar.findViewById(R.id.scaner);
		image1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		image1.setVisibility(View.GONE);
		
    	
    	email.setText("support@kloudportal.com");
    	telephone.setText("https://www.kloudportal.com");
       
    	String htmlString1="Buy now and Monetize the App";

    	//buylink.setText(Html.fromHtml(htmlString1));
    	SpannableString content = new SpannableString(htmlString1);
    	content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
    	buylink.setText(content);
    	
    	buylink.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

		    	startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://codecanyon.net/user/kolluru81/portfolio?ref=kolluru81")));
				
			}
		});
    	
        return v;
    }
    
    
//    @Override 
//	public boolean onOptionsItemSelected(MenuItem item) {
//		getActivity().getSupportFragmentManager()
//		.beginTransaction()
//		.replace(R.id.mainContent, new ShowAllQuizesFragment(), "alllistfragment")
//		.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
//		.commit();
//		return super.onOptionsItemSelected(item);
//	} 
    
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
    	  ((MainActivity)getActivity()).openorclose(); 
        return super.onOptionsItemSelected(menuItem);
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

        
    }
    public void onBackPressed() { 


	} 
    
}
