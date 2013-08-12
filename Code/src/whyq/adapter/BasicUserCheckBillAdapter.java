package whyq.adapter;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import whyq.activity.ListDetailActivity;
import whyq.model.UserCheckBill;
import whyq.utils.UrlImageViewHelper;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.whyq.R;

public class BasicUserCheckBillAdapter extends ArrayAdapter<UserCheckBill> {

	private final List<UserCheckBill> list;
	private Context context;
	private HashMap<String, View> viewList = new HashMap<String, View>();
	public static List<ViewUserCheckBillHolder> listSectionView = new ArrayList<ViewUserCheckBillHolder>();

	public BasicUserCheckBillAdapter(Context context, List<UserCheckBill> UserCheckBillList) {
		super(context, R.layout.user_item, UserCheckBillList);
		this.context = context;
		this.list = UserCheckBillList;
	}
	public List<UserCheckBill> getData(){
		return list;
	}
	public static class ViewUserCheckBillHolder {
		public TextView tvName;
		public ImageView imgThumb, imgFriendThumb;
		public String storeId;
		public ViewUserCheckBillHolder() {

		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		convertView = viewList.get(position);
		UserCheckBill item = list.get(position);
		if (convertView == null) {
			LayoutInflater inflator = ((ListDetailActivity) context)
					.getLayoutInflater();
			view = inflator.inflate(R.layout.user_item, null);
			final ViewUserCheckBillHolder viewHolder = new ViewUserCheckBillHolder();
			viewHolder.imgThumb = (ImageView) view.findViewById(R.id.imgAvatar);
			viewHolder.imgFriendThumb = (ImageView) view.findViewById(R.id.imgFriend);
			viewHolder.tvName = (TextView)view.findViewById(R.id.tvFriendCount);
			UrlImageViewHelper.setUrlDrawable(viewHolder.imgThumb, item.getAvatar());
			viewHolder.tvName.setText(item.getFirstName()+" "+item.getLastName());
//			if(UserCheckBill.isFriend()){
//				viewHolder.imgFriendThumb.setBackgroundResource(R.drawable.icon_friended);
//			}else{
				viewHolder.imgFriendThumb.setVisibility(View.INVISIBLE);
//			}
			view.setTag(viewHolder);
			
			viewList.put(String.valueOf(position), view);
		} else {
			view = convertView;

		}

		return view;
	}

}