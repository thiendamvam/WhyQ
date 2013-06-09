package whyq.activity;

import java.util.ArrayList;
import java.util.List;

import whyq.WhyqApplication;
import whyq.mockup.MockupDataLoader;
import whyq.model.Comment;
import whyq.utils.ImageWorker;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.whyq.R;

public class CommentActivity extends NavigationActivity {

	private CommentAdapter mAdapter;
	private ImageWorker mImageWorker;
	private LoadDataTask mLoadDataTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_comment);

		setTitle(R.string.comment_title);

		mImageWorker = new ImageWorker(this);
		mImageWorker.initCache(this, WhyqApplication.DISK_CACHE_DIR, 0.25f);
		mAdapter = new CommentAdapter(this, mImageWorker);

		ListView listview = (ListView) findViewById(R.id.listview);
		listview.setAdapter(mAdapter);

		mLoadDataTask = new LoadDataTask();
		mLoadDataTask.execute();

	}

	@Override
	protected void onResume() {
		super.onResume();
		mImageWorker.setExitTasksEarly(false);
	}

	@Override
	protected void onStop() {
		super.onStop();
		mImageWorker.setExitTasksEarly(true);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mImageWorker.clearCache();
	}

	class LoadDataTask extends AsyncTask<Void, Void, List<Comment>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			setLoading(true);
		}

		@Override
		protected List<Comment> doInBackground(Void... params) {
			Log.d("LoadDataTask", "Start loading.. ");
			return MockupDataLoader.loadComments();
		}

		@Override
		protected void onPostExecute(List<Comment> result) {
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

	static class CommentAdapter extends BaseAdapter {

		private static final int AVATAR_SIZE = WhyqApplication.sBaseViewHeight / 5 * 4;
		private static final int THUMB_HEIGHT = WhyqApplication.sBaseViewHeight * 4;
		private Context mContext;
		private List<Comment> mItems;
		private ImageWorker mImageWorker;

		public CommentAdapter(Context context, ImageWorker imageWorker) {
			this.mContext = context;
			this.mItems = new ArrayList<Comment>();
			this.mImageWorker = imageWorker;
		}

		public void setItems(List<Comment> items) {
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
						R.layout.comment_list_item, parent, false);
			}

			ViewHolder holder = getViewHolder(convertView);
			Comment item = mItems.get(position);

			holder.name.setText(item.getAuthor().getName());
			holder.comment.setText(item.getContent());
			holder.like.setText("" + item.getAuthor().getLike());
			mImageWorker.loadImage(item.getAuthor().getAvatar().getUrl(),
					holder.avatar, AVATAR_SIZE, AVATAR_SIZE);
			mImageWorker.loadImage(item.getThumbUrl(), holder.thumb,
					WhyqApplication.sScreenWidth, THUMB_HEIGHT);
			if (item.getAuthor().getLike() > 0) {
				holder.favorite.setImageResource(R.drawable.icon_fav_enable);
			} else {
				holder.favorite.setImageResource(R.drawable.icon_fav_disable);
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
			ImageView favorite;
			ImageView thumb;
			TextView name;
			TextView like;
			TextView comment;

			public ViewHolder(View view) {
				view.findViewById(R.id.infor).getLayoutParams().height = WhyqApplication.sBaseViewHeight;
				avatar = (ImageView) view.findViewById(R.id.avatar);
				avatar.getLayoutParams().width = AVATAR_SIZE;
				avatar.getLayoutParams().height = AVATAR_SIZE;
				thumb = (ImageView) view.findViewById(R.id.imageThumb);

				favorite = (ImageView) view.findViewById(R.id.favorite);
				name = (TextView) view.findViewById(R.id.name);
				like = (TextView) view.findViewById(R.id.like);
				comment = (TextView) view.findViewById(R.id.comment);
			}
		}

	}

}
