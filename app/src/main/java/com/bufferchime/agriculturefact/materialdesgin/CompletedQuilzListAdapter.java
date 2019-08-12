package com.bufferchime.agriculturefact.materialdesgin;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bufferchime.agriculturefact.font.RobotoItalicTextView;
import com.bufferchime.agriculturefact.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

class CompletedQuilzListAdapter extends BaseAdapter {

	ViewHolder viewHolder;

	private ArrayList<DbUSer> mItems = new ArrayList<DbUSer>();
	private Context mContext;

	public CompletedQuilzListAdapter(Context context, ArrayList<DbUSer> list) {
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
			v = vi.inflate(R.layout.completed_quiz_item, null);

			// well set up the ViewHolder
			viewHolder = new ViewHolder();
			viewHolder.title = (com.bufferchime.agriculturefact.font.RobotoMediumTextView) v.findViewById(R.id.quizname);
			viewHolder.categoryname = (com.bufferchime.agriculturefact.font.RobotoMediumTextView) v.findViewById(R.id.categoryname);

			viewHolder.titlefirstletter=(com.bufferchime.agriculturefact.font.RobotoMediumTextView) v.findViewById(R.id.item_letter);
			viewHolder.noofattempts = (com.bufferchime.agriculturefact.font.RobotoMediumTextView) v.findViewById(R.id.noofattempts);
			viewHolder.score = (com.bufferchime.agriculturefact.font.RobotoMediumTextView) v.findViewById(R.id.score);
			viewHolder.date = (RobotoItalicTextView) v.findViewById(R.id.date);

			viewHolder.ll=(LinearLayout) v.findViewById(R.id.ll);
			//viewHolder.titlefirstletter.setBackgroundResource(R.drawable.square);

			//((GradientDrawable)viewHolder.ll.getBackground()).setColor(Color.RED);


			//			GradientDrawable drawable = (GradientDrawable) viewHolder.titlefirstletter.getBackground();
			//			if (position % 2 == 0) { 
			//				drawable.setColor(Color.RED);
			//			} else { 
			//				drawable.setColor(Color.BLUE);
			//			} 

			v.setTag(viewHolder);

		}else{
			// just use the viewHolder
			viewHolder = (ViewHolder) convertView.getTag();
		}

		String desc = mItems.get(position).getQuizlevelname();
		//int imageid=mItems.get(position).getImageId();
		//viewHolder.image.setImageResource(imageid);
		viewHolder.title.setText(desc);
		viewHolder.titlefirstletter.setText(mItems.get(position).getFirstchar());
		viewHolder.noofattempts.setText("Attempts # "+mItems.get(position).getAttempts());
		viewHolder.score.setText(""+mItems.get(position).getScore() +"/"+mItems.get(position).getTotalNoQuestions());

		String d1=mItems.get(position).getDateofquiz();
		String d2=getDateTime();

		Calendar cal1 = new GregorianCalendar();
		Calendar cal2 = new GregorianCalendar();

		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");

		Date date = null;
		try {
			date = sdf.parse(d1);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cal1.setTime(date);
		try {
			date = sdf.parse(d2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cal2.setTime(date);

		//cal2.set(2008, 9, 31); 
		System.out.println("Days= "+daysBetween(cal1.getTime(),cal2.getTime()));

		if(daysBetween(cal1.getTime(),cal2.getTime()) == 0){
			viewHolder.date.setText("Today");

		}else{
			if(daysBetween(cal1.getTime(),cal2.getTime()) == 1)
				viewHolder.date.setText(daysBetween(cal1.getTime(),cal2.getTime()) +"Day Ago");
			else
				viewHolder.date.setText(daysBetween(cal1.getTime(),cal2.getTime()) +"Days Ago");

		}

		viewHolder.categoryname.setText(mItems.get(position).getQuizName());
		String color=mItems.get(position).getColor();

		if(color == null){
			color="#E040FB";
		}
		CustomGradientDrawable drawable = new CustomGradientDrawable(Color.parseColor(color), Color.parseColor(color), Color.parseColor(color),1, Color.BLACK,00);
		viewHolder.ll.setBackgroundDrawable(drawable);

		return v;
	}
	public int daysBetween(Date d1, Date d2){
		return (int)( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
	}
	public String getTimeDiff(Date dateOne, Date dateTwo) {
		String diff = "";
		long timeDiff = Math.abs(dateOne.getTime() - dateTwo.getTime());
		diff = String.format("%d hour(s) %d min(s)", TimeUnit.MILLISECONDS.toHours(timeDiff),
				TimeUnit.MILLISECONDS.toMinutes(timeDiff) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeDiff)));
		return diff;
	}
	private String getDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"ddMMyyyy", Locale.getDefault());
		Date date = new Date();
		return dateFormat.format(date);
	}
	static class ViewHolder {
		com.bufferchime.agriculturefact.font.RobotoMediumTextView title,titlefirstletter,noofattempts,score,categoryname;
		RobotoItalicTextView date; 
		ImageView image;
		LinearLayout ll;
	}

}