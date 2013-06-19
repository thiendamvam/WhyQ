package whyq.activity;

import java.util.List;

import whyq.WhyqApplication;
import whyq.interfaces.FriendFacebookController;
import whyq.model.FriendFacebook;
import whyq.model.SearchFriendCriteria;
import whyq.model.StatusWithFriend;
import whyq.service.DataParser;
import whyq.service.Service;
import whyq.service.ServiceAction;
import whyq.service.ServiceResponse;
import whyq.utils.ImageWorker;
import whyq.utils.SpannableUtils;
import whyq.utils.WhyqUtils;
import whyq.utils.XMLParser;
import whyq.view.SearchField;
import whyq.view.SearchField.QueryCallback;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.whyq.R;

public class FriendsFacebookActivity extends ImageWorkerActivity implements
		QueryCallback {

	private FriendsFacebookAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_facebook_friends);

		setTitle(R.string.friend_from_facebook);

		SearchField searchField = (SearchField) findViewById(R.id.searchField);

		searchField.getLayoutParams().height = WhyqApplication.sBaseViewHeight;

		searchField.setQueryCallback(this);

		mAdapter = new FriendsFacebookAdapter(this, mImageWorker);
		ListView listview = (ListView) findViewById(R.id.listview);
		listview.setAdapter(mAdapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				final Object item = mAdapter.getItem(arg2);
				if (item instanceof FriendFacebook) {
					Intent i = new Intent(FriendsFacebookActivity.this,
							UserBoardActivity.class);
					startActivity(i);
				} 
			}
		});

		getFriends();

	}

	private String getAccessToken() {
		final WhyqUtils mPermutils = new WhyqUtils();
		return mPermutils.getFacebookToken(this);
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
		if (result != null) {
			if (result.getAction() == ServiceAction.ActionInviteFriendsFacebook) {

			} else {
				FriendFacebookController handler = DataParser
						.parseFriendFacebook(String.valueOf(result.getData()));
				mAdapter.setController(handler);
			}
		}
	}

	private void searchFriends(String queryString) {
		Service service = getService();
		setLoading(true);
		final String accessToken = getAccessToken();
		if (accessToken == null || accessToken.length() == 0) {
			service.searchFriends(SearchFriendCriteria.whyq, getEncryptedToken(), queryString, null, null, null);
		} else {
			service.searchFriends(SearchFriendCriteria.facebook, getEncryptedToken(), queryString, accessToken, null, null);
		}

	}

	private void getFriends() {
		Service service = getService();
		setLoading(true);
		final String accessToken = getAccessToken();
		if (accessToken == null || accessToken.length() == 0) {
			service.getFriends(getEncryptedToken(), XMLParser.getValue(this, XMLParser.STORE_USER_ID));
		} else {
			service.getFriendsFacebook(getEncryptedToken(), accessToken);
		}
	}

	void inviteFriend(String userId) {
		final String accessToken = getAccessToken();
		Service service = getService();
		setLoading(true);
		service.inviteFriendsFacebook(getEncryptedToken(), userId, accessToken);
	}

	static class FriendsFacebookAdapter extends BaseAdapter {

		private static final int AVATAR_SIZE = WhyqApplication.sBaseViewHeight / 5 * 4;
		private FriendsFacebookActivity mActivity;
		private List<FriendFacebook> listWhyq;
		private List<FriendFacebook> listNotJoinWhyq;
		private ImageWorker mImageWorker;
		private static int countListNotJoinWhyq = 0;
		private static int countListWhyq = 0;

		public FriendsFacebookAdapter(FriendsFacebookActivity context,
				ImageWorker imageWorker) {
			this.mActivity = context;
			this.mImageWorker = imageWorker;
		}

		public void setController(FriendFacebookController controller) {
			if (controller != null) {
				listWhyq = controller.getListWhyq();
				listNotJoinWhyq = controller.getListNotJoinWhyq();
			} else {
				listWhyq = null;
				listNotJoinWhyq = null;
			}
			if (listWhyq != null) {
				countListWhyq = listWhyq.size();
			} else {
				countListWhyq = 0;
			}
			if (listNotJoinWhyq != null) {
				countListNotJoinWhyq = listNotJoinWhyq.size();
			} else {
				countListNotJoinWhyq = 0;
			}
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return countListWhyq + countListNotJoinWhyq + 2;
		}

		@Override
		public Object getItem(int position) {
			if (position < 0 || position >= getCount()) {
				return null;
			}

			if (position == 0) {
				final String key = countListWhyq + " facebook friends";
				final String result = "You have " + key + " had joined WHY Q.";
				return SpannableUtils.stylistTextBold(result, key, mActivity
						.getResources().getColor(R.color.orange));
			} else if (position == countListWhyq + 1) {
				final String key = countListNotJoinWhyq + " facebook friends";
				final String result = "And "
						+ key
						+ " haven't joined WHY Q. Invite your friend to join this app!";
				return SpannableUtils.stylistTextBold(result, key, mActivity
						.getResources().getColor(R.color.orange));
			} else if (position <= countListWhyq) {
				return listWhyq.get(position - 1);
			} else {
				return listNotJoinWhyq.get(position - countListWhyq - 2);
			}
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public int getItemViewType(int position) {
			if (position == 0 || position == countListWhyq + 1) {
				return 0;
			} else {
				return 1;
			}
		}

		@Override
		public int getViewTypeCount() {
			return 2;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final Object item = getItem(position);
			if (convertView == null) {
				if (item instanceof FriendFacebook) {
					convertView = mActivity.getLayoutInflater().inflate(
							R.layout.friend_list_item, parent, false);
				} else {
					convertView = mActivity.getLayoutInflater().inflate(
							R.layout.friend_list_item_secondary, parent, false);
				}
			}

			final ViewHolder holder = getViewHolder(convertView);

			if (item instanceof FriendFacebook) {
				final FriendFacebook friendfacebook = (FriendFacebook) item;
				holder.name.setText(friendfacebook.getFirstName());
				mImageWorker.loadImage(friendfacebook.getAvatar(),
						holder.avatar, AVATAR_SIZE, AVATAR_SIZE);
				if (friendfacebook.getIsFriend() == StatusWithFriend.STATUS_NOT_CONNECT) {
					if (position <= countListWhyq) {
						holder.action
								.setBackgroundResource(R.drawable.add_friend);
					} else {
						holder.action.setText(R.string.invite);
						holder.action
								.setOnClickListener(new View.OnClickListener() {

									@Override
									public void onClick(View v) {
										mActivity.inviteFriend(friendfacebook
												.getId());
									}
								});
					}
				} else if (friendfacebook.getIsFriend() == StatusWithFriend.STATUS_WAITING_FOR_ACCEPT) {

				}

			} else {
				holder.name.setText((Spannable) item);
				if (position == 0 && countListWhyq > 0) {
					holder.action.setText(R.string.friend_all);
					holder.action.setVisibility(View.VISIBLE);
				} else {
					holder.action.setVisibility(View.GONE);
				}
			}

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
			Button action;

			public ViewHolder(View view) {
				view.getLayoutParams().height = WhyqApplication.sBaseViewHeight;
				avatar = (ImageView) view.findViewById(R.id.avatar);
				if (avatar != null) {
					avatar.getLayoutParams().width = AVATAR_SIZE;
					avatar.getLayoutParams().height = AVATAR_SIZE;
				}
				name = (TextView) view.findViewById(R.id.name);
				action = (Button) view.findViewById(R.id.action);
			}
		}

	}

}
