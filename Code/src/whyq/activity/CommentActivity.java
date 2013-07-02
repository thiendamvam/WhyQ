package whyq.activity;

import java.util.ArrayList;
import java.util.List;

import whyq.WhyqApplication;
import whyq.model.Comment;
import whyq.model.Photo;
import whyq.service.DataParser;
import whyq.service.ResultCode;
import whyq.service.Service;
import whyq.service.ServiceAction;
import whyq.service.ServiceResponse;
import whyq.utils.ImageViewHelper;
import whyq.utils.Util;
import whyq.utils.XMLParser;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.whyq.R;

public class CommentActivity extends ImageWorkerActivity {

	private CommentAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_comment);

		setTitle(R.string.comment_title);

		mAdapter = new CommentAdapter(this, mImageWorker);

		ListView listview = (ListView) findViewById(R.id.listview);
		listview.setAdapter(mAdapter);

		Service service = getService();
		setLoading(true);
		service.getComments(getEncryptedToken(), XMLParser.getUserId(this), 1,
				20);
	}

	@Override
	public void onCompleted(Service service, ServiceResponse result) {
		super.onCompleted(service, result);
		setLoading(false);
		if (result != null && result.getCode() == ResultCode.Success
				&& result.getAction() == ServiceAction.ActionGetComment) {
			List<Comment> comments = DataParser.parseComments(String
					.valueOf(result.getData()));
			mAdapter.setItems(comments);
		}
	}

	static class CommentAdapter extends BaseAdapter {

		private static final int AVATAR_SIZE = WhyqApplication.sBaseViewHeight / 5 * 4;
		private static final int THUMB_HEIGHT = WhyqApplication.sBaseViewHeight * 5;
		private Context mContext;
		private List<Comment> mItems;
		private ImageViewHelper mImageWorker;

		public CommentAdapter(Context context, ImageViewHelper imageWorker) {
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
				Util.applyTypeface(convertView,
						WhyqApplication.sTypefaceRegular);
			}

			ViewHolder holder = getViewHolder(convertView);
			Comment item = mItems.get(position);

			holder.name.setText(item.getUser().getFirstName());
			holder.comment.setText(item.getContent());
			holder.like.setText("" + item.getCount_like());
			mImageWorker
					.downloadImage(item.getUser().getUrlAvatar(), holder.avatar);
			Photo photo = item.getPhotos();
			if (photo != null) {
				mImageWorker.downloadImage(item.getPhotos().getThumb(),
						holder.thumb);
			}
			if (item.getUser().getLike() > 0) {
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
