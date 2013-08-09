package whyq.adapter;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import whyq.activity.ListDetailActivity;
import whyq.activity.WhyqProflleFAQActivity;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.whyq.R;

public class WhyqProfileFAQItemAdapter extends ArrayAdapter<String> {

	private final List<String> list;
	private Context context;
	private HashMap<String, View> viewList = new HashMap<String, View>();
	public static List<FAQViewHolder> listSectionView = new ArrayList<FAQViewHolder>();

	public WhyqProfileFAQItemAdapter(Context context, List<String> menuList) {
		super(context, R.layout.whyq_profile_faq_item, menuList);
		this.context = context;
		this.list = menuList;
	}
	public List<String> getData(){
		return list;
	}
	public static class FAQViewHolder implements Parcelable{
		public TextView tvName; 
		public FAQViewHolder() {

		}
		@Override
		public int describeContents() {
			// TODO Auto-generated method stub
			return 0;
		}
		@Override
		public void writeToParcel(Parcel dest, int flags) {
			// TODO Auto-generated method stub
			
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		convertView = viewList.get(position);
		String item = list.get(position);
		if (convertView == null) {
			LayoutInflater inflator = ((WhyqProflleFAQActivity) context)
					.getLayoutInflater();
			view = inflator.inflate(R.layout.whyq_profile_faq_item, null);
			final FAQViewHolder viewHolder = new FAQViewHolder();
			viewHolder.tvName = (TextView) view.findViewById(R.id.tvName);
			
			if(item!=null){
				viewHolder.tvName.setText(item);
			}
			view.setTag(viewHolder);
			
			viewList.put(item, view);
		} else {
			view = convertView;

		}

		return view;
	}

}