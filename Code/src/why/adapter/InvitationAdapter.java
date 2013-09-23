package why.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import whyq.activity.InvitationActivity;
import whyq.model.User;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.whyq.R;

public class InvitationAdapter extends ArrayAdapter<User> {

	private final List<User> list;
	private Context context;
	private HashMap<String, View> viewList = new HashMap<String, View>();
	private InvitationHolder holder;
	public static List<InvitationAdapter.InvitationHolder> listSectionView = new ArrayList<InvitationAdapter.InvitationHolder>();

	public InvitationAdapter(Context context, List<User> sectionList) {
		super(context, R.layout.invitation_screen, sectionList);
		this.context = context;
		this.list = sectionList;
	}

	public static class InvitationHolder {
		public TextView tvName;
		public ImageView imgAvatar, imgWhyq;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		convertView = viewList.get(position);
		User item = list.get(position);
		if (convertView == null) {
			LayoutInflater inflator = ((InvitationActivity) context).getLayoutInflater();
			view = inflator.inflate(R.layout.invitation_item, null);
			holder = new InvitationHolder();

			view.setTag(holder);
			listSectionView.add(holder);
			viewList.put(String.valueOf(position), view);
		} else {
			view = convertView;
			holder = (InvitationHolder) view.getTag();
		}
		return view;
	}


}