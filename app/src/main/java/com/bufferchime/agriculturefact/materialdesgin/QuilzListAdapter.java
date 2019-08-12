package com.bufferchime.agriculturefact.materialdesgin;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bufferchime.agriculturefact.R;

import java.util.ArrayList;

class QuilzListAdapter extends BaseAdapter {

	ViewHolder viewHolder;

	private ArrayList<QuizListItem> mItems = new ArrayList<QuizListItem>();
	private Context mContext;
	DataBaseHelper dbhelper;

	public QuilzListAdapter(Context context, ArrayList<QuizListItem> list) {
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
			v = vi.inflate(R.layout.quiz_item_transition, null);

			// well set up the ViewHolder
			viewHolder = new ViewHolder();
			viewHolder.title = (com.bufferchime.agriculturefact.font.RobotoMediumTextView) v.findViewById(R.id.item_title);
			viewHolder.image = (ImageView) v.findViewById(R.id.item_image);
			viewHolder.image_done = (ImageView) v.findViewById(R.id.completed);
			viewHolder.titlefirstletter=(com.bufferchime.agriculturefact.font.RobotoMediumTextView) v.findViewById(R.id.item_letter);
			viewHolder.ll=(LinearLayout) v.findViewById(R.id.ll);

			v.setTag(viewHolder);

		}else{
			// just use the viewHolder
			viewHolder = (ViewHolder) convertView.getTag();
		}

		String desc = mItems.get(position).getQuizTitle();
		int imageid=mItems.get(position).getImageId();
		viewHolder.image.setImageResource(imageid);
		viewHolder.title.setText(desc);
		viewHolder.titlefirstletter.setText(mItems.get(position).getFirstchar());

		dbhelper=new DataBaseHelper(mContext);

		if(dbhelper != null){
			DbUSer dbuser=dbhelper.checkQuizLevelExists(desc);
			if(dbuser !=null)
				viewHolder.image_done.setVisibility(View.VISIBLE);
			else
				viewHolder.image_done.setVisibility(View.GONE);

		}

		String color=mItems.get(position).getColor();

		if(color == null){
			color="#E040FB";
		}
		CustomGradientDrawable drawable = new CustomGradientDrawable(Color.parseColor(color), Color.parseColor(color), Color.parseColor(color),1, Color.BLACK,00);
		viewHolder.ll.setBackgroundDrawable(drawable);

		return v;
	}

	static class ViewHolder {
		com.bufferchime.agriculturefact.font.RobotoMediumTextView title,titlefirstletter;
		ImageView image,image_done;
		LinearLayout ll;
	}

}