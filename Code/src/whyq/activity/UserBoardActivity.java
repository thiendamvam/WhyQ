package whyq.activity;

import java.util.ArrayList;
import java.util.List;

import whyq.WhyqApplication;
import whyq.mockup.MockupDataLoader;
import whyq.model.ActivityItem;
import whyq.model.ActivityItem.ActivityType;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.whyq.R;

public class UserBoardActivity extends NavigationActivity {

	private ActivitiesAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_user_board);
		initCategory();

		mAdapter = new ActivitiesAdapter(this);
		((ListView) findViewById(R.id.listview)).setAdapter(mAdapter);

		new LoadDataTask().execute();
	}

	private void initCategory() {
		bindCategory(R.id.check_bill, R.drawable.icon_cat_coffee, "2 places",
				"checked bills", new OnClickListener() {

					@Override
					public void onClick(View v) {
						startActivity(new Intent(UserBoardActivity.this,
								CheckedBillActivity.class));
					}
				});
		bindCategory(R.id.history, R.drawable.icon_cat_cutlery, "15",
				"History", new OnClickListener() {

					@Override
					public void onClick(View v) {

					}
				});
		bindCategory(R.id.saving, R.drawable.icon_cat_wine, "0", "Saving",
				new OnClickListener() {

					@Override
					public void onClick(View v) {

					}
				});
		bindCategory(R.id.comment, R.drawable.icon_cat_wine, "2", "Comments",
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						startActivity(new Intent(UserBoardActivity.this,
								CommentActivity.class));
					}
				});
	}

	private void bindCategory(int id, int iconRes, String textTop,
			String textBottom, OnClickListener listener) {
		View categoryView = findViewById(id);
		categoryView.getLayoutParams().height = WhyqApplication.sBaseViewHeight;
		categoryView.setOnClickListener(listener);
		ImageView icon = (ImageView) categoryView.findViewById(R.id.icon);
		icon.getLayoutParams().height = WhyqApplication.sBaseViewHeight;
		icon.setImageResource(iconRes);
		TextView textviewTop = (TextView) categoryView
				.findViewById(R.id.textviewTop);
		textviewTop.setText(textTop);
		TextView textviewBottom = (TextView) categoryView
				.findViewById(R.id.textviewBottom);
		textviewBottom.setText(textBottom);
	}

	class LoadDataTask extends AsyncTask<Void, Void, List<ActivityItem>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			setLoading(true);
		}

		@Override
		protected List<ActivityItem> doInBackground(Void... params) {
			Log.d("LoadDataTask", "Start loading.. ");
			return MockupDataLoader.loadActivities();
		}

		@Override
		protected void onPostExecute(List<ActivityItem> result) {
			super.onPostExecute(result);
			setLoading(false);
			if (isCancelled()) {
				return;
			}

			if (mAdapter != null) {
				Log.d("LoadDataTask", "Result loaded size: " + result.size());
				mAdapter.setItems(result);
			}

		}

	}

	static class ActivitiesAdapter extends BaseAdapter {

		private Context mContext;
		private List<ActivityItem> mItems;

		public ActivitiesAdapter(Context context) {
			this.mContext = context;
			this.mItems = new ArrayList<ActivityItem>();
		}

		public void setItems(List<ActivityItem> items) {
			if (items == null || items.size() == 0) {
				mItems.clear();
			} else {
				mItems = items;
			}
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return mItems.size();
		}

		@Override
		public Object getItem(int position) {
			return mItems.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.activities_list_item, parent, false);
			}

			ViewHolder holder = getViewHolder(convertView);
			ActivityItem item = mItems.get(position);

			holder.activity.setText(item.getName());
			if (item.getType() == ActivityType.comment) {
				holder.activity.setCompoundDrawablesWithIntrinsicBounds(
						R.drawable.icon_quote, 0, 0, 0);
				holder.comment.setVisibility(View.VISIBLE);
			} else {
				holder.activity.setCompoundDrawablesWithIntrinsicBounds(
						R.drawable.followers, 0, 0, 0);
				holder.comment.setVisibility(View.GONE);
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
			TextView activity;
			View comment;

			public ViewHolder(View view) {
				activity = (TextView) view.findViewById(R.id.activity);
				comment = view.findViewById(R.id.comment);
			}
		}

	}
}
