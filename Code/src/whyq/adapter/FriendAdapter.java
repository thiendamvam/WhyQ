package whyq.adapter;

import java.util.List;

import whyq.activity.WhyqTagFriendsDialog;
import whyq.model.FriendFacebook;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.whyq.R;

public class FriendAdapter extends BaseAdapter implements OnCheckedChangeListener{

	private List<FriendFacebook> list;
	private Context context;

	public FriendAdapter(Context c, List<FriendFacebook> list) {
		// TODO Auto-generated const-ructor stub
		this.list = list;
		context = c;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return this.list.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		FriendFacebook item = list.get(position);
		Holder holder = null;
		if(convertView!=null)holder = (Holder) convertView.getTag();
		if (holder == null){
			View v = LayoutInflater.from(context).inflate(
					R.layout.friend_facebook_tag_item, parent, false);
			holder = new Holder();
			holder.tvName = (TextView) v.findViewById(R.id.tvName);
			holder.cbxTag = (CheckBox) v.findViewById(R.id.cbxTag);
			holder.fbId = item.getId();
			holder.cbxTag.setOnCheckedChangeListener(this);

			holder.tvName.setText(item.getFirstName());
			holder.cbxTag.setTag(item);
			v.setTag(holder);
			return v;
		}
		
		return convertView;
	}

	public class Holder {
		private TextView tvName;
		private CheckBox cbxTag;
		private String fbId;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		((WhyqTagFriendsDialog)context.getp).onChecked(buttonView);
	}
}
