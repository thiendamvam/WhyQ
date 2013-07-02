package whyq.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import whyq.WhyqApplication;
import whyq.interfaces.FriendFacebookController;
import whyq.model.FriendFacebook;
import whyq.model.FriendWhyq;
import whyq.model.SearchFriendCriteria;
import whyq.model.StatusWithFriend;
import whyq.service.DataParser;
import whyq.service.Service;
import whyq.service.ServiceAction;
import whyq.service.ServiceResponse;
import whyq.utils.ImageViewHelper;
import whyq.utils.SpannableUtils;
import whyq.utils.Util;
import whyq.utils.WhyqUtils;
import whyq.view.SearchField;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.whyq.R;

public class WhyqFriendsFacebookActivity extends ImageWorkerActivity {

	private FriendsFacebookAdapter mFriendFacebookAdapter = null;
	private String mAccessToken;
	private ListView mListview;
	private static final Map<String, FriendFacebook> INVITED_LIST = new HashMap<String, FriendFacebook>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_facebook_friends);

		// showSearchField(true);
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

		mFriendFacebookAdapter = new FriendsFacebookAdapter(this, mImageWorker);
		mAccessToken = getAccessToken();

		setTitle(R.string.friend_from_facebook);

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
				if (result.getAction() == ServiceAction.ActionGetFriendsFacebook
						|| result.getAction() == ServiceAction.ActionSearchFriendsFacebook) {
					FriendFacebookController handler = DataParser
							.parseFriendFacebook(String.valueOf(result
									.getData()));
					mFriendFacebookAdapter.setController(handler);
				}
			}
		}
	}

	private void searchFriends(String queryString) {
		if (mFriendFacebookAdapter == null) {
			mFriendFacebookAdapter = new FriendsFacebookAdapter(this,
					mImageWorker);
		}
		mListview.setAdapter(mFriendFacebookAdapter);

		Service service = getService();
		setLoading(true);
		if (mAccessToken != null && mAccessToken.length() > 0) {
			service.searchFriends(SearchFriendCriteria.facebook,
					getEncryptedToken(), queryString, mAccessToken, null, null);
		}
	}

	private void getFriends() {
		Service service = getService();
		setLoading(true);
		if (mAccessToken != null && mAccessToken.length() > 0) {
			mListview.setAdapter(mFriendFacebookAdapter);
			service.getFriendsFacebook(getEncryptedToken(), mAccessToken);
		}
	}

	private TextView mInviteMessage;

	void addInviteFriend(FriendFacebook friend) {
		INVITED_LIST.put(friend.getId(), friend);
		if (INVITED_LIST.size() > 1) {
		} else {
			String key = friend.getFirstName();
			String message = "Invite " + key + " to join WHYQ?";
			mInviteMessage.setText(SpannableUtils.stylistText(message, key,
					R.color.orange));
		}
	}

	void inviteFriends(String userId) {
		final String accessToken = getAccessToken();
		Service service = getService();
		setLoading(true);
		service.inviteFriendsFacebook(getEncryptedToken(), userId, accessToken);
	}

	static class FriendsFacebookAdapter extends BaseAdapter {

		private static final int AVATAR_SIZE = WhyqApplication.sBaseViewHeight / 5 * 4;
		private WhyqFriendsFacebookActivity mActivity;
		private List<FriendFacebook> listWhyq;
		private List<FriendFacebook> listNotJoinWhyq;
		private ImageViewHelper mImageWorker;
		private static int countListNotJoinWhyq = 0;
		private static int countListWhyq = 0;

		public FriendsFacebookAdapter(WhyqFriendsFacebookActivity context,
				ImageViewHelper imageWorker) {
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

				Util.applyTypeface(convertView,
						WhyqApplication.sTypefaceRegular);
			}

			final ViewHolder holder = getViewHolder(convertView);

			if (item instanceof FriendFacebook) {
				final FriendFacebook friendfacebook = (FriendFacebook) item;
				holder.name.setText(friendfacebook.getFirstName());
				mImageWorker.downloadImage(friendfacebook.getAvatar(),
						holder.avatar);
				if (friendfacebook.getIsFriend() == StatusWithFriend.STATUS_NOT_CONNECT) {
					if (INVITED_LIST.containsKey(friendfacebook.getId())) {
						holder.action.setText("");
						holder.action
								.setBackgroundResource(R.drawable.icon_checked);
					} else if (position <= countListWhyq) {
						holder.action.setText(R.string.add);
					} else {
						holder.action.setText(R.string.invite);
						holder.actionContainer
								.setOnClickListener(new View.OnClickListener() {

									@Override
									public void onClick(View v) {
										mActivity
												.addInviteFriend(friendfacebook);
									}
								});
					}
				} else if (friendfacebook.getIsFriend() == StatusWithFriend.STATUS_WAITING_FOR_ACCEPT) {

				}

			} else {
				holder.name.setText((Spannable) item);
				if (position == 0 && countListWhyq > 0) {
					holder.action.setText(R.string.friend_all);
					holder.actionContainer.setVisibility(View.VISIBLE);
				} else {
					holder.actionContainer.setVisibility(View.GONE);
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
			FrameLayout actionContainer;
			TextView action;

			public ViewHolder(View view) {
				avatar = (ImageView) view.findViewById(R.id.avatar);
				if (avatar != null) {
					avatar.getLayoutParams().width = AVATAR_SIZE;
					avatar.getLayoutParams().height = AVATAR_SIZE;
				}
				name = (TextView) view.findViewById(R.id.name);
				action = (TextView) view.findViewById(R.id.action);
				actionContainer = (FrameLayout) view
						.findViewById(R.id.actionContainer);
			}
		}

	}

}
