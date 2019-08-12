package com.bufferchime.agriculturefact.materialdesgin;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bufferchime.agriculturefact.R;

import java.util.ArrayList;

class ResultTransitionListAdapter extends BaseAdapter {

	ViewHolder viewHolder;

	private ArrayList<ResultsListItem> mItems = new ArrayList<ResultsListItem>();
	private Context mContext;

	public ResultTransitionListAdapter(Context context, ArrayList<ResultsListItem> list) {
		mContext = context;
		mItems = list;
	}


	public int getCount() {
		return mItems.size();
	}


	public Object getItem(int position) {
		return mItems.get(position);
	}


	public long getItemId(int position) {
		return position;
	}


	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;

		if(convertView==null){

			// inflate the layout
			LayoutInflater vi = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.fragment_resultlist_item_transition, null);

			// well set up the ViewHolder
			viewHolder = new ViewHolder();
			viewHolder.question = (TextView) v.findViewById(R.id.question);
			viewHolder.answeroption = (TextView) v.findViewById(R.id.answeroption);
			viewHolder.description  = (TextView) v.findViewById(R.id.description);
			viewHolder.useroption=(TextView)v.findViewById(R.id.useranswer);
			viewHolder.ques_img=(ImageView)v.findViewById(R.id.ques_img);

			
			// store the holder with the view.
			v.setTag(viewHolder);

		}else{
			// just use the viewHolder
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String que = mItems.get(position).getQuestion();
		String answer = mItems.get(position).getAnswer();
		String useranswer = mItems.get(position).getUseranswer();
		String desc = mItems.get(position).getDescription();


		if(que.contains("img =")){	
			String ss=que.substring(que.indexOf("img ="));
			String imgname=ss.substring(ss.indexOf("="), ss.indexOf("\""));
			StringBuilder sb = new StringBuilder(imgname);
			Log.d("data", ss);
			if(sb.charAt(0) =='='){
				Log.d("datasub", ""+sb.deleteCharAt(0));

			}else
				Log.d("datasub", ""+sb.toString());


			int identifier =((ResultsActivitys)mContext).getResources().getIdentifier(sb.toString(), "drawable",((ResultsActivitys)mContext).getPackageName());

		}else if(que.contains("img=")){
			String ss=que.substring(que.indexOf("img="));
			String imgname=ss.substring(ss.indexOf("="), ss.indexOf("\""));
			StringBuilder sb = new StringBuilder(imgname);
			Log.d("data", ss);
			if(sb.charAt(0) =='='){
				Log.d("datasub", ""+sb.deleteCharAt(0));

			}else
				Log.d("datasub", ""+sb.toString());


			int identifier =((ResultsActivitys)mContext).getResources().getIdentifier(sb.toString(), "drawable",((ResultsActivitys)mContext).getPackageName());


		}else{


		}
		
		if(useranswer.contains("img =")){	

			String ss=useranswer.substring(useranswer.indexOf("img ="));
			String imgname=ss.substring(ss.indexOf("="), ss.indexOf("\""));
			StringBuilder sb = new StringBuilder(imgname);
			Log.d("data", ss);
			if(sb.charAt(0) =='='){
				Log.d("datasub", ""+sb.deleteCharAt(0));

			}else
				Log.d("datasub", ""+sb.toString());


			int identifier =((ResultsActivitys)mContext).getResources().getIdentifier(sb.toString(), "drawable",((ResultsActivitys)mContext).getPackageName());
			viewHolder.user_img.setImageResource(identifier);

		}if(useranswer.contains("img=")){
			String ss=useranswer.substring(useranswer.indexOf("img="));
			String imgname=ss.substring(ss.indexOf("="), ss.indexOf("\""));
			StringBuilder sb = new StringBuilder(imgname);
			Log.d("data", ss);
			if(sb.charAt(0) =='='){
				Log.d("datasub", ""+sb.deleteCharAt(0));

			}else
				Log.d("datasub", ""+sb.toString());


			int identifier =((ResultsActivitys)mContext).getResources().getIdentifier(sb.toString(), "drawable",((ResultsActivitys)mContext).getPackageName());

		}else{

		}
		
		if(answer.contains("img =")){	
			String ss=answer.substring(answer.indexOf("img ="));
			Log.d("data", ss);
			Log.d("index of ",""+ss.indexOf("\""));
			Log.d("index of =",""+ss.indexOf("="));
			
			String imgname=ss.substring(ss.indexOf("="), ss.indexOf("\""));
			Log.d("imgname", imgname);
			
			StringBuilder sb = new StringBuilder(imgname);
			if(sb.charAt(0) =='='){
				Log.d("datasub", ""+sb.deleteCharAt(0));

			}else
				Log.d("datasub", ""+sb.toString());


			int identifier =((ResultsActivitys)mContext).getResources().getIdentifier(sb.toString(), "drawable",((ResultsActivitys)mContext).getPackageName());

		}else if(answer.contains("img=")){
			String ss=answer.substring(answer.indexOf("img="));
			Log.d("data", ss);
			Log.d("index of ",""+ss.indexOf("\""));
			Log.d("index of =",""+ss.indexOf("="));

			String imgname=ss.substring(ss.indexOf("="), ss.indexOf("\""));
			Log.d("imgname", imgname);

			StringBuilder sb = new StringBuilder(imgname);
			if(sb.charAt(0) =='='){
				Log.d("datasub", ""+sb.deleteCharAt(0));

			}else
				Log.d("datasub", ""+sb.toString());


			int identifier =((ResultsActivitys)mContext).getResources().getIdentifier(sb.toString(), "drawable",((ResultsActivitys)mContext).getPackageName());

		}else{

		}

		//viewHolder.question.setText(que);
		//viewHolder.answeroption.setText(":"+answer);
		
			if(que.contains("img =")){
				StringBuffer text = new StringBuffer(que);
				text.replace( que.indexOf("\"img =") ,que.length() ,"");
				viewHolder.question.setText(text);
			}else if(que.contains("img=")){
			StringBuffer text = new StringBuffer(que);
			text.replace( que.indexOf("\"img=") ,que.length() ,"");
			viewHolder.question.setText(text);
		}else{
				viewHolder.question.setText(que);
			}
			
			if(answer.contains("img =")){
				StringBuffer text = new StringBuffer(answer);
				text.replace( answer.indexOf("\"img =") ,answer.length() ,"");
				viewHolder.answeroption.setText(":"+text);
			}else if(answer.contains("img=")){
				StringBuffer text = new StringBuffer(answer);
				text.replace( answer.indexOf("\"img=") ,answer.length() ,"");
				viewHolder.answeroption.setText(":"+text);
			}else{
				viewHolder.answeroption.setText(":"+answer);
			}
			
		
		
		if(useranswer.equalsIgnoreCase("Not answered")){
			viewHolder.useroption.setTextColor(Color.parseColor("#FFBF00"));
		}else if(useranswer.equalsIgnoreCase(answer)){
			viewHolder.useroption.setTextColor(Color.GREEN);

		}else{
			viewHolder.useroption.setTextColor(Color.RED);
		}
		if(useranswer.contains("img =")){
			StringBuffer text = new StringBuffer(useranswer);
			text.replace( useranswer.indexOf("\"img =") ,useranswer.length() ,"");
			viewHolder.useroption.setText(":"+text);
		}else if(useranswer.contains("img=")){
			StringBuffer text = new StringBuffer(useranswer);
			text.replace( useranswer.indexOf("\"img=") ,useranswer.length() ,"");
			viewHolder.useroption.setText(":"+text);
		}else{
			viewHolder.useroption.setText(":"+useranswer);
		}
		
		if(!desc.equalsIgnoreCase(""))
		viewHolder.description.setText(desc);
		else
			viewHolder.description.setVisibility(View.INVISIBLE);
		

		return v;
	}

	static class ViewHolder {
		TextView question;
		TextView answeroption,description,useroption;
		int position;
		ImageView ques_img,ans_img,user_img;
	}

}