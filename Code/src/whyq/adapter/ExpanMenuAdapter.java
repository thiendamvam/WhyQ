package whyq.adapter;



import java.util.HashMap;
import java.util.List;

import whyq.WhyqApplication;
import whyq.activity.ListDetailActivity;
import whyq.adapter.ExpandableListAdapter.ViewHolderMitemInfo;
import whyq.model.GroupMenu;
import whyq.model.Menu;
import whyq.utils.UrlImageViewHelper;
import whyq.utils.Util;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.custom.WebImageView;
import com.whyq.R;

public class ExpanMenuAdapter extends BaseExpandableListAdapter {

	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		super.notifyDataSetChanged();
	}
	private HashMap<String, View> viewList = new HashMap<String, View>();
	private Context mContext;
	private ExpandableListView mExpandableListView;
	private List<GroupMenu> mGroupCollection;
	public List<GroupMenu> getmGroupCollection() {
		return mGroupCollection;
	}

	public void setmGroupCollection(List<GroupMenu> mGroupCollection) {
		this.mGroupCollection = mGroupCollection;
	}

	private int[] groupStatus;

	public ExpanMenuAdapter(Context pContext,
			ExpandableListView pExpandableListView,
			List<GroupMenu> pGroupCollection) {
		mContext = pContext;
		mGroupCollection = pGroupCollection;
		mExpandableListView = pExpandableListView;
		groupStatus = new int[mGroupCollection.size()];

		setListEvent();
	}

	private class DoubleOnCollapseListener implements
			ExpandableListView.OnGroupCollapseListener {
		private View currentView;

		public DoubleOnCollapseListener(View view) {
			currentView = view;
		}

		public void onGroupCollapse(int groupPosition) {
			currentView.getLayoutParams().height = 300;
			currentView.requestLayout();
		}
	}

	private void setListEvent() {

		mExpandableListView
				.setOnGroupExpandListener(new OnGroupExpandListener() {

					@Override
					public void onGroupExpand(int arg0) {
						// TODO Auto-generated method stub
						groupStatus[arg0] = 1;
					}
				});

		mExpandableListView
				.setOnGroupCollapseListener(new OnGroupCollapseListener() {

					@Override
					public void onGroupCollapse(int arg0) {
						// TODO Auto-generated method stub
						groupStatus[arg0] = 0;

					}
				});
	}

	@Override
	public Object getChild(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return mGroupCollection.get(arg0).getMenuList().get(arg1);
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return arg1;
	}
	@Override
	public int getChildrenCount(int arg0) {
		// TODO Auto-generated method stub
		Log.d("getChildrenCount","size: "+mGroupCollection.get(arg0).getMenuList().size());
		return mGroupCollection.get(arg0).getMenuList().size();
	}
	@Override
	public View getChildView(int arg0, int arg1, boolean arg2, View arg3,
			ViewGroup arg4) {
		// TODO Auto-generated method stub
		Log.d("getChildView","getChildView");
		View view = arg3;
		Menu item = mGroupCollection.get(arg0).getMenuList().get(arg1);
		if (arg3 == null) {
			LayoutInflater inflator = ((ListDetailActivity) mContext)
					.getLayoutInflater();
			view = inflator.inflate(R.layout.whyq_menu_item_, null);
			final ViewHolderMitemInfo viewHolder = new ViewHolderMitemInfo();
			viewHolder.tvType = (TextView) view.findViewById(R.id.tvType);
			viewHolder.tvPrice = (TextView) view.findViewById(R.id.tvPrice);
			viewHolder.imgThumb = (ImageView) view.findViewById(R.id.imgThumbnail);
			viewHolder.tvCount = (TextView)view.findViewById(R.id.totalForItem);
			viewHolder.tvType.setTypeface(Util.sTypefaceRegular);
			viewHolder.tvPrice.setTypeface(Util.sTypefaceRegular);
			viewHolder.tvType.setText(Html.fromHtml(item.getNameProduct()));
			viewHolder.tvPrice.setText("$"+Html.fromHtml(item.getValue()));
			viewHolder.storeId = item.getStoreId();
			viewHolder.btnAdd = (Button)view.findViewById(R.id.btnAdd);
			viewHolder.btnRemove = (Button) view.findViewById(R.id.btnRemove);
			viewHolder.menuId = item.getId();
			
			viewHolder.btnRemove.setTag(viewHolder);
//			UrlImageViewHelper.setUrlDrawable(viewHolder.imgThumb, item.getImageThumb());
			WhyqApplication.Instance().getImageLoader().DisplayImage(item.getImageThumb(),viewHolder.imgThumb);
			viewHolder.btnAdd.setTag(viewHolder);
			view.setTag(viewHolder);
			
			viewList.put(String.valueOf(item.getStoreId()), view);
		} else {
			view = arg3;

		}

		return view;
	}



	@Override
	public Object getGroup(int arg0) {
		// TODO Auto-generated method stub
		return mGroupCollection.get(arg0);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return mGroupCollection.size();
	}

	@Override
	public long getGroupId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getGroupView(int arg0, boolean arg1, View arg2, ViewGroup arg3) {

		// TODO Auto-generated method stub
		GroupHolder groupHolder;
		if (arg2 == null) {
			arg2 = LayoutInflater.from(mContext).inflate(R.layout.list_group,
					null);
			groupHolder = new GroupHolder();
			groupHolder.img = (ImageView) arg2.findViewById(R.id.tag_img);
			groupHolder.title = (TextView) arg2.findViewById(R.id.group_title);
			arg2.setTag(groupHolder);
		} else {
			groupHolder = (GroupHolder) arg2.getTag();
		}
		if (groupStatus[arg0] == 0) {
			groupHolder.img.setImageResource(R.drawable.group_down);
		} else {
			groupHolder.img.setImageResource(R.drawable.group_up);
		}

		groupHolder.title.setTypeface(Util.sTypefaceRegular);
		groupHolder.title.setText(Html.fromHtml(mGroupCollection.get(arg0).getName()));
		return arg2;
	
	}

	class GroupHolder {
		ImageView img;
		TextView title;
	}

	class ChildHolder {
		TextView storyName;
		TextView storyIntro;
		WebImageView thumbnal;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return true;
	}

}
