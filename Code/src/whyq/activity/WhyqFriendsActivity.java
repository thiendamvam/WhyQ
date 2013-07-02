package whyq.activity;

import java.util.ArrayList;
import java.util.List;

import whyq.WhyqApplication;
import whyq.model.FriendFacebook;
import whyq.model.FriendWhyq;
import whyq.model.SearchFriendCriteria;
import whyq.service.DataParser;
import whyq.service.Service;
import whyq.service.ServiceAction;
import whyq.service.ServiceResponse;
import whyq.utils.ImageViewHelper;
import whyq.utils.Util;
import whyq.utils.XMLParser;
import whyq.view.SearchField;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.whyq.R;

import dialog.AlertFindFriendDialog;
import dialog.AlertFindFriendDialog.OnDialogButtonClickListener;

public class WhyqFriendsActivity extends ImageWorkerActivity implements
		OnDialogButtonClickListener {

	private FriendsWhyqAdapter mFriendWhyqAdapter = null;
	private ListView mListview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_friends);

		SearchField searchField = (SearchField) findViewById(R.id.searchField);
		searchField.setQueryCallback(this);
		searchField.setHint(R.string.find_a_friend);

		mListview = (ListView) findViewById(R.id.listview);
		mListview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				final Object item = arg0.getItemAtPosition(arg2);
				if (item instanceof FriendFacebook) {
					FriendFacebook facebook = (FriendFacebook) item;
					startUserProfileActivity(facebook.getId(),
							facebook.getFirstName(), facebook.getAvatar());
				} else if (item instanceof FriendWhyq) {
					FriendWhyq whyq = (FriendWhyq) item;
					startUserProfileActivity(whyq.getId(),
							whyq.getFirst_name(), whyq.getAvatar());
				}
			}
		});

		mFriendWhyqAdapter = new FriendsWhyqAdapter(this, mImageWorker);
		mListview.setAdapter(mFriendWhyqAdapter);

		setTitle(R.string.friends);

		// Set extra button.
		ImageView imageView = new ImageView(this);
		imageView.setImageResource(R.drawable.icon_add_friend);
		setExtraView(imageView);

		setBackButtonIcon(R.drawable.icon_friend_invite);

		getFriends();

	}

	private void startUserProfileActivity(String userId, String userName,
			String avatar) {
		Intent i = new Intent(this, WhyqUserProfileActivity.class);
		i.putExtra(WhyqUserProfileActivity.ARG_USER_ID, userId);
		i.putExtra(WhyqUserProfileActivity.ARG_USER_NAME, userName);
		i.putExtra(WhyqUserProfileActivity.ARG_AVATAR, avatar);
		startActivity(i);
	}

	@Override
	public void onQuery(String queryString) {
		if (queryString != null && queryString.length() > 0) {
			searchFriends(queryString);
		} else {
			getFriends();
		}
	}

	@Override
	public void onCompleted(Service service, ServiceResponse result) {
		super.onCompleted(service, result);
		setLoading(false);
		if (result != null
				&& result.getAction() == ServiceAction.ActionGetFriends) {
			List<FriendWhyq> friends = DataParser.parseFriendWhyq(String
					.valueOf(result.getData()));
			if (friends.size() == 0) {
				AlertFindFriendDialog dialog = new AlertFindFriendDialog();
				dialog.setOnDialogButtonClickListern(this);
				dialog.show(getSupportFragmentManager(), "Dialog");
			} else {
				mFriendWhyqAdapter.setItems(friends);
			}
		}
	}

	@Override
	public void onPositiveButtonClick() {
		startActivity(new Intent(this, WhyqFindMenuActivity.class));
	}

	@Override
	public void onNegativeButtonClick() {

	}

	private void searchFriends(String queryString) {
		Service service = getService();
		setLoading(true);
		service.searchFriends(SearchFriendCriteria.whyq, getEncryptedToken(),
				queryString, null, null, null);
	}

	private void getFriends() {
		Service service = getService();
		setLoading(true);
		service.getFriends(getEncryptedToken(),
				XMLParser.getValue(this, XMLParser.STORE_USER_ID));
	}

	static class FriendsWhyqAdapter extends BaseAdapter {

		private static final int AVATAR_SIZE = WhyqApplication.sBaseViewHeight / 5 * 4;
		private WhyqFriendsActivity mActivity;
		private List<FriendWhyq> listWhyq;
		private ImageViewHelper mImageWorker;

		public FriendsWhyqAdapter(WhyqFriendsActivity context,
				ImageViewHelper imageWorker) {
			this.mActivity = context;
			this.mImageWorker = imageWorker;
			this.listWhyq = new ArrayList<FriendWhyq>();
		}

		public void setItems(List<FriendWhyq> items) {
			if (items == null || items.size() == 0) {
				listWhyq.clear();
			} else {
				listWhyq = items;
			}
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return listWhyq.size();
		}

		@Override
		public Object getItem(int position) {
			return listWhyq.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = mActivity.getLayoutInflater().inflate(
						R.layout.friend_list_item, parent, false);
				Util.applyTypeface(convertView,
						WhyqApplication.sTypefaceRegular);
			}

			final ViewHolder holder = getViewHolder(convertView);

			final FriendWhyq item = listWhyq.get(position);
			holder.name.setText(item.getFirst_name());
			mImageWorker.downloadImage(item.getAvatar(), holder.avatar);
			return convertView;
		}

		private ViewHolder getViewHolder(View view) {
			ViewHolder holder = (ViewHolder) view.getTag();
			if (holder == null) {
				holder = new ViewHolder(view);
				view.setTag(holder);
			}
			return holder;
		}

		class ViewHolder {
			ImageView avatar;
			TextView name;
			View action;

			public ViewHolder(View view) {
				view.getLayoutParams().height = WhyqApplication.sBaseViewHeight;
				avatar = (ImageView) view.findViewById(R.id.avatar);
				if (avatar != null) {
					avatar.getLayoutParams().width = AVATAR_SIZE;
					avatar.getLayoutParams().height = AVATAR_SIZE;
				}
				name = (TextView) view.findViewById(R.id.name);
				action = view.findViewById(R.id.action);
				action.setBackgroundResource(R.drawable.icon_friend);
			}
		}

	}

}
