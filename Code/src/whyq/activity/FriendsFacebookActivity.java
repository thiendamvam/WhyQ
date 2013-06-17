package whyq.activity;

import java.util.List;

import whyq.WhyqApplication;
import whyq.interfaces.FriendFacebookController;
import whyq.model.FriendFacebook;
import whyq.model.StatusWithFriend;
import whyq.service.DataParser;
import whyq.service.Service;
import whyq.service.ServiceResponse;
import whyq.utils.ImageWorker;
import whyq.utils.WhyqUtils;
import whyq.view.SearchField;
import whyq.view.SearchField.QueryCallback;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
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
					final FriendFacebook friend = (FriendFacebook) item;
					Intent i = new Intent(FriendsFacebookActivity.this,
							UserBoardActivity.class);
					i.putExtra(UserBoardActivity.ARG_USER_ID, friend.getId());
					i.putExtra(UserBoardActivity.ARG_FIRST_NAME,
							friend.getFirstName());
					i.putExtra(UserBoardActivity.ARG_AVATAR, friend.getAvatar());
					startActivity(i);
				}
			}
		});

		final String access_token = getAccessToken();
		if (access_token != null && access_token.length() > 0) {
			getFriends(access_token);
		}

	}

	private String getAccessToken() {
		final WhyqUtils mPermutils = new WhyqUtils();
		return mPermutils.getFacebookToken(this);
	}

	@Override
	public void onQuery(String queryString) {
		if (queryString != null && queryString.length() > 0) {
			searchFriends(getAccessToken(), queryString);
		} else {
			getFriends(getAccessToken());
		}
	}

	@Override
	public void onCompleted(Service service, ServiceResponse result) {
		super.onCompleted(service, result);
		setLoading(false);
		if (result != null) {
			FriendFacebookController handler = DataParser
					.parseFriendFacebook(String.valueOf(result.getData()));
			mAdapter.setController(handler);
		}
	}

	private void searchFriends(String accessToken, String queryString) {
		if (accessToken == null || accessToken.length() == 0) {
			return;
		}

		Service service = getService();
		setLoading(true);
		service.searchFriendsFacebook(getEncryptedToken(), queryString,
				accessToken);
	}

	private void getFriends(String access_token) {
		if (access_token == null || access_token.length() == 0) {
			return;
		}

		Service service = getService();
		setLoading(true);
		service.getFriendsFacebook(getEncryptedToken(), access_token);
	}

	static class FriendsFacebookAdapter extends BaseAdapter {

		private static final int AVATAR_SIZE = WhyqApplication.sBaseViewHeight / 5 * 4;
		private Context mContext;
		private List<FriendFacebook> listWhyq;
		private List<FriendFacebook> listNotJoinWhyq;
		private ImageWorker mImageWorker;
		private static int countListNotJoinWhyq = 0;
		private static int countListWhyq = 0;

		public FriendsFacebookAdapter(Context context, ImageWorker imageWorker) {
			this.mContext = context;
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
				return "You have " + countListWhyq
						+ " facebook friends had joined WHY Q.";
			} else if (position == countListWhyq + 1) {
				return "And "
						+ countListNotJoinWhyq
						+ " facebook friends haven't joined WHY Q. Invite your friend to join this app!";
			} else if (position <= countListWhyq) {
				return listWhyq.get(position - 1);
			} else {
				return listNotJoinWhyq.get(position - countListWhyq - 2);
			}
		}

		private static final String YOU_HAVE = "You have ";
		private static final String AND = "And ";
		private static final String FACE_BOOK_FRIEND = " facebook friends";

		private Spannable stylistText(String input) {
			Spannable result = new SpannableString(input);

			final int start, end;
			if (input.startsWith(YOU_HAVE)) {
				start = YOU_HAVE.length();
			} else {
				start = AND.length();
			}
			end = input.indexOf(FACE_BOOK_FRIEND) + FACE_BOOK_FRIEND.length();
			result.setSpan(new ForegroundColorSpan(0xffff8822), start, end,
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			result.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
					start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			return result;
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
					convertView = LayoutInflater.from(mContext).inflate(
							R.layout.friend_list_item, parent, false);
				} else {
					convertView = LayoutInflater.from(mContext).inflate(
							R.layout.friend_list_item_secondary, parent, false);
				}
			}

			ViewHolder holder = getViewHolder(convertView);

			if (item instanceof FriendFacebook) {
				FriendFacebook friendfacebook = (FriendFacebook) item;
				holder.name.setText(friendfacebook.getFirstName());
				mImageWorker.loadImage(friendfacebook.getAvatar(),
						holder.avatar, AVATAR_SIZE, AVATAR_SIZE);
				if (friendfacebook.getIsFriend() == StatusWithFriend.STATUS_NOT_CONNECT) {
					if (position <= countListWhyq) {
						holder.action
								.setBackgroundResource(R.drawable.add_friend);
					} else {
						holder.action.setText(R.string.invite);
					}
				}

			} else {
				holder.name.setText(stylistText(String.valueOf(item)));
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
