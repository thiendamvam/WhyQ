package whyq.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import whyq.WhyqApplication;
import whyq.activity.ListDetailActivity;
import whyq.adapter.ExpandableListAdapter.ViewHolderMitemInfo;
import whyq.model.ExtraItem;
import whyq.model.ExtraItemSet;
import whyq.model.GroupMenu;
import whyq.model.Menu;
import whyq.model.OptionItem;
import whyq.model.SizeItem;
import whyq.utils.Util;
import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.custom.WebImageView;
import com.whyq.R;

public class ExpanMenuAdapter extends BaseExpandableListAdapter implements OnClickListener {

	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		super.notifyDataSetChanged();
	}

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
	private int mFocusColor;
	private int mNormalColor;

	public ExpanMenuAdapter(Context pContext, ExpandableListView pExpandableListView,
			List<GroupMenu> pGroupCollection) {
		mContext = pContext;
		mGroupCollection = pGroupCollection;
		mExpandableListView = pExpandableListView;
		groupStatus = new int[mGroupCollection.size()];

		mFocusColor = mContext.getResources().getColor(R.color.red);
		mNormalColor = mContext.getResources().getColor(R.color.grey);
		setListEvent();
	}

	private class DoubleOnCollapseListener implements ExpandableListView.OnGroupCollapseListener {
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

		mExpandableListView.setOnGroupExpandListener(new OnGroupExpandListener() {

			@Override
			public void onGroupExpand(int arg0) {
				// TODO Auto-generated method stub
				groupStatus[arg0] = 1;
			}
		});

		mExpandableListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

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
		Log.d("getChildrenCount", "size: " + mGroupCollection.get(arg0).getMenuList().size());
		return mGroupCollection.get(arg0).getMenuList().size();
	}

	@Override
	public View getChildView(int arg0, int arg1, boolean arg2, View view, ViewGroup arg4) {
		// TODO Auto-generated method stub
		Log.d("getChildView", "getChildView");
		Menu item = mGroupCollection.get(arg0).getMenuList().get(arg1);
		LayoutInflater inflator = ((ListDetailActivity) mContext).getLayoutInflater();
		ViewHolderMitemInfo viewHolder = new ViewHolderMitemInfo();
		if (view == null) {
			view = inflator.inflate(R.layout.whyq_menu_item_phase2, null);
			viewHolder.tvType = (TextView) view.findViewById(R.id.tvType);
			viewHolder.tvPrice = (TextView) view.findViewById(R.id.tvPrice);
			viewHolder.imgThumb = (ImageView) view.findViewById(R.id.imgThumbnail);
			viewHolder.tvCount = (TextView) view.findViewById(R.id.totalForItem);
			viewHolder.tvType.setTypeface(Util.sTypefaceRegular);
			viewHolder.tvPrice.setTypeface(Util.sTypefaceRegular);
			viewHolder.tvType.setText(Html.fromHtml(item.getNameProduct()));
			viewHolder.tvPrice.setText("$" + Html.fromHtml(item.getValue()));
			viewHolder.storeId = item.getStoreId();
			viewHolder.btnAdd = (Button) view.findViewById(R.id.btnAdd);
			viewHolder.btnDoneSelect = (Button) view.findViewById(R.id.btn_done_select_extra);
			viewHolder.btnRemove = (Button) view.findViewById(R.id.btnRemove);
			viewHolder.menuId = item.getId();
			viewHolder.lnPreview = (LinearLayout) view.findViewById(R.id.ln_preview_extra_selected);
			viewHolder.rlExtraView = (RelativeLayout) view.findViewById(R.id.rl_extra_view);
		} else {
			viewHolder = (ViewHolderMitemInfo) view.getTag();
		}
		if (item.getOptionItemList() != null && item.getOptionItemList().size() > 0) {
			List<OptionItem> list = item.getOptionItemList();

			for (int i = 0; i < list.size(); i++) {
				OptionItem itemDetail = list.get(i);
				if (i == 0) {
					TextView tvItem1Name = (TextView) view.findViewById(R.id.tv_item1_name_option);
					TextView tvItem1Price = (TextView) view
							.findViewById(R.id.tv_item1_price_option);
					setViewVisibility(tvItem1Name, true);
					setViewVisibility(tvItem1Price, true);
					tvItem1Name.setText(itemDetail.getName());
					tvItem1Price.setText("$" + itemDetail.getValue());
					tvItem1Price.setTag(itemDetail);
					((LinearLayout) view.findViewById(R.id.ln_item1_option))
							.setOnClickListener(this);

					view.findViewById(R.id.ln_item1_option).setTag(view);
				} else if (i == 1) {
					TextView tvItem2Name = (TextView) view.findViewById(R.id.tv_item2_name_option);
					TextView tvItem2Price = (TextView) view
							.findViewById(R.id.tv_item2_price_option);
					setViewVisibility(tvItem2Name, true);
					setViewVisibility(tvItem2Price, true);
					tvItem2Name.setText(itemDetail.getName());
					tvItem2Price.setText("$" + itemDetail.getValue());
					tvItem2Price.setTag(itemDetail);

					((LinearLayout) view.findViewById(R.id.ln_item2_option))
							.setOnClickListener(this);
					view.findViewById(R.id.ln_item2_option).setTag(view);
				} else if (i == 2) {
					TextView tvItem3Name = (TextView) view.findViewById(R.id.tv_item3_name_option);
					TextView tvItem3Price = (TextView) view
							.findViewById(R.id.tv_item3_price_option);
					setViewVisibility(tvItem3Name, true);
					setViewVisibility(tvItem3Price, true);
					tvItem3Name.setText(itemDetail.getName());
					tvItem3Price.setText("$" + itemDetail.getValue());
					tvItem3Price.setTag(itemDetail);

					((LinearLayout) view.findViewById(R.id.ln_item3_option))
							.setOnClickListener(this);
					view.findViewById(R.id.ln_item3_option).setTag(view);
				}

			}

		} else {
		}

		if (item.getSizeItemList() != null && item.getSizeItemList().size() > 0) {
			List<SizeItem> list = item.getSizeItemList();

			for (int i = 0; i < list.size(); i++) {
				SizeItem itemDetail = list.get(i);
				if (i == 0) {
					TextView tvItem1Name = (TextView) view.findViewById(R.id.tv_item1_name_size);
					TextView tvItem1Price = (TextView) view.findViewById(R.id.tv_item1_price_size);
					setViewVisibility(tvItem1Name, true);
					setViewVisibility(tvItem1Price, true);
					tvItem1Name.setText(itemDetail.getName());
					tvItem1Price.setText("$" + itemDetail.getValue());
					tvItem1Price.setTag(itemDetail);

					((LinearLayout) view.findViewById(R.id.ln_item1_size)).setOnClickListener(this);

					view.findViewById(R.id.ln_item1_size).setTag(view);
				} else if (i == 1) {
					TextView tvItem2Name = (TextView) view.findViewById(R.id.tv_item2_name_size);
					TextView tvItem2Price = (TextView) view.findViewById(R.id.tv_item2_price_size);
					setViewVisibility(tvItem2Name, true);
					setViewVisibility(tvItem2Price, true);
					tvItem2Name.setText(itemDetail.getName());
					tvItem2Price.setText("$" + itemDetail.getValue());
					tvItem2Price.setTag(itemDetail);

					((LinearLayout) view.findViewById(R.id.ln_item2_size)).setOnClickListener(this);
					view.findViewById(R.id.ln_item2_size).setTag(view);
				} else if (i == 2) {
					TextView tvItem3Name = (TextView) view.findViewById(R.id.tv_item3_name_size);
					TextView tvItem3Price = (TextView) view.findViewById(R.id.tv_item3_price_size);
					setViewVisibility(tvItem3Name, true);
					setViewVisibility(tvItem3Price, true);
					tvItem3Name.setText(itemDetail.getName());
					tvItem3Price.setText("$" + itemDetail.getValue());
					tvItem3Price.setTag(itemDetail);

					((LinearLayout) view.findViewById(R.id.ln_item3_size)).setOnClickListener(this);
					view.findViewById(R.id.ln_item3_size).setTag(view);
				}
			}
		} else {
			// setViewVisibility(lnSize, false);
		}

		if (item.getExtraItemList() != null && item.getExtraItemList().size() > 0) {
			// setViewVisibility(lnExtra, true);
			List<ExtraItem> list = item.getExtraItemList();

			for (int i = 0; i < list.size(); i++) {
				ExtraItem itemDetail = list.get(i);
				if (i == 0) {
					TextView tvItem1Name = (TextView) view.findViewById(R.id.tv_item1_name_extra);
					TextView tvItem1Price = (TextView) view.findViewById(R.id.tv_item1_price_extra);
					setViewVisibility(tvItem1Name, true);
					setViewVisibility(tvItem1Price, true);
					tvItem1Name.setText(itemDetail.getName());
					tvItem1Price.setText("$" + itemDetail.getValue());
					tvItem1Price.setTag(itemDetail);

					((LinearLayout) view.findViewById(R.id.ln_item1_extra))
							.setOnClickListener(this);

					view.findViewById(R.id.ln_item1_extra).setTag(view);
				} else if (i == 1) {
					TextView tvItem2Name = (TextView) view.findViewById(R.id.tv_item2_name_extra);
					TextView tvItem2Price = (TextView) view.findViewById(R.id.tv_item2_price_extra);
					setViewVisibility(tvItem2Name, true);
					setViewVisibility(tvItem2Price, true);
					tvItem2Name.setText(itemDetail.getName());
					tvItem2Price.setText("$" + itemDetail.getValue());
					tvItem2Price.setTag(itemDetail);

					((LinearLayout) view.findViewById(R.id.ln_item2_extra))
							.setOnClickListener(this);

					view.findViewById(R.id.ln_item2_extra).setTag(view);
				} else if (i == 2) {
					TextView tvItem3Name = (TextView) view.findViewById(R.id.tv_item3_name_extra);
					TextView tvItem3Price = (TextView) view.findViewById(R.id.tv_item3_price_extra);
					setViewVisibility(tvItem3Name, true);
					setViewVisibility(tvItem3Price, true);
					tvItem3Name.setText("$" + itemDetail.getName());
					tvItem3Price.setTag(itemDetail);

					((LinearLayout) view.findViewById(R.id.ln_item3_extra))
							.setOnClickListener(this);

					view.findViewById(R.id.ln_item3_extra).setTag(view);
				}
			}
		} else {
		}
		viewHolder.btnRemove.setTag(viewHolder);
		// UrlImageViewHelper.setUrlDrawable(viewHolder.imgThumb,
		// item.getImageThumb());
		WhyqApplication.Instance().getImageLoader()
				.DisplayImage(item.getImageThumb(), viewHolder.imgThumb);
		viewHolder.btnAdd.setTag(viewHolder);
		viewHolder.btnDoneSelect.setTag(viewHolder);
		view.setTag(viewHolder);
		View extraTem = inflator.inflate(R.layout.item_extra_preview, arg4, false);
		return view;
	}

	private void setViewVisibility(View v, boolean b) {
		// TODO Auto-generated method stub
		v.setVisibility(b ? View.VISIBLE : View.GONE);
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
			arg2 = LayoutInflater.from(mContext).inflate(R.layout.list_group, null);
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		Log.d("onClick", "onClick");
		int id = v.getId();
		switch (id) {
		case R.id.ln_item1_option:
			onOptionClicked(1, v);
			break;
		case R.id.ln_item2_option:
			onOptionClicked(2, v);
			break;
		case R.id.ln_item3_option:
			onOptionClicked(3, v);
			break;
		case R.id.ln_item1_size:
			onSizeClicked(1, v);
			break;
		case R.id.ln_item2_size:
			onSizeClicked(2, v);
			break;
		case R.id.ln_item3_size:
			onSizeClicked(3, v);
			break;
		case R.id.ln_item1_extra:
			onExtraClicked(1, v);
			break;
		case R.id.ln_item2_extra:
			onExtraClicked(2, v);
			break;
		case R.id.ln_item3_extra:
			onExtraClicked(3, v);
			break;

		default:
			break;
		}
	}

	private void onExtraClicked(int i, View v0) {
		// TODO Auto-generated method stub
		Log.d("onClick", "onExtraClicked" + i);
		View v = (View) v0.getTag();
		ExtraItem item = null;
		switch (i) {
		case 1:
			((TextView) v.findViewById(R.id.tv_item1_name_extra)).setTextColor(mFocusColor);
			((TextView) v.findViewById(R.id.tv_item1_price_extra)).setTextColor(mFocusColor);
			item = (ExtraItem) ((TextView) v.findViewById(R.id.tv_item1_price_extra)).getTag();
			break;
		case 2:
			((TextView) v.findViewById(R.id.tv_item2_name_extra)).setTextColor(mFocusColor);
			((TextView) v.findViewById(R.id.tv_item2_price_extra)).setTextColor(mFocusColor);
			item = (ExtraItem) ((TextView) v.findViewById(R.id.tv_item2_price_extra)).getTag();
			break;
		case 3:
			((TextView) v.findViewById(R.id.tv_item3_name_extra)).setTextColor(mFocusColor);
			((TextView) v.findViewById(R.id.tv_item3_price_extra)).setTextColor(mFocusColor);
			item = (ExtraItem) ((TextView) v.findViewById(R.id.tv_item3_price_extra)).getTag();
			break;
		default:
			break;
		}

		if (item != null && item.getProductId() != null) {
			if (ListDetailActivity.extraList.get(item.getProductId()) != null) {
				ListDetailActivity.extraList.get(item.getProductId()).getExtraList().add(item);
			} else {
				ExtraItemSet extraItemSet = new ExtraItemSet();
				List<ExtraItem> list = new ArrayList<ExtraItem>();
				list.add(item);
				extraItemSet.setExtraList(list);
				ListDetailActivity.extraList.put(item.getProductId(), extraItemSet);
			}
		}
	}

	private void onSizeClicked(int i, View v0) {
		// TODO Auto-generated method stub
		Log.d("onClick", "onSizeClicked" + i);
		View v = (View) v0.getTag();
		SizeItem item = null;
		switch (i) {
		case 1:
			((TextView) v.findViewById(R.id.tv_item1_name_size)).setTextColor(mFocusColor);
			((TextView) v.findViewById(R.id.tv_item1_price_size)).setTextColor(mFocusColor);
			item = (SizeItem) ((TextView) v.findViewById(R.id.tv_item1_price_size)).getTag();
			break;
		case 2:
			((TextView) v.findViewById(R.id.tv_item2_name_size)).setTextColor(mFocusColor);
			((TextView) v.findViewById(R.id.tv_item2_price_size)).setTextColor(mFocusColor);
			item = (SizeItem) ((TextView) v.findViewById(R.id.tv_item2_price_size)).getTag();
			break;
		case 3:
			((TextView) v.findViewById(R.id.tv_item3_name_size)).setTextColor(mFocusColor);
			((TextView) v.findViewById(R.id.tv_item3_price_size)).setTextColor(mFocusColor);
			item = (SizeItem) ((TextView) v.findViewById(R.id.tv_item3_price_size)).getTag();
			break;
		default:
			break;
		}
		if (item != null && item.getProductId() != null) {
			if (ListDetailActivity.extraList.get(item.getProductId()) != null) {
				ListDetailActivity.extraList.get(item.getProductId()).getSizeList().add(item);
			} else {
				ExtraItemSet extraItemSet = new ExtraItemSet();
				List<SizeItem> list = new ArrayList<SizeItem>();
				list.add(item);
				extraItemSet.setSizeList(list);
				ListDetailActivity.extraList.put(item.getProductId(), extraItemSet);
			}
		}
	}

	private void onOptionClicked(int i, View v0) {
		// TODO Auto-generated method stub
		Log.d("onClick", "onOptionClicked" + i);
		View v = (View) v0.getTag();
		OptionItem item = null;
		switch (i) {
		case 1:
			((TextView) v.findViewById(R.id.tv_item1_name_option)).setTextColor(mFocusColor);
			((TextView) v.findViewById(R.id.tv_item1_price_option)).setTextColor(mFocusColor);
			((TextView) v.findViewById(R.id.tv_item2_name_option)).setTextColor(mNormalColor);
			((TextView) v.findViewById(R.id.tv_item2_price_option)).setTextColor(mNormalColor);
			((TextView) v.findViewById(R.id.tv_item3_name_option)).setTextColor(mNormalColor);
			((TextView) v.findViewById(R.id.tv_item3_price_option)).setTextColor(mNormalColor);
			item = (OptionItem) ((TextView) v.findViewById(R.id.tv_item1_price_option)).getTag();

			break;
		case 2:
			((TextView) v.findViewById(R.id.tv_item1_name_option)).setTextColor(mNormalColor);
			((TextView) v.findViewById(R.id.tv_item1_price_option)).setTextColor(mNormalColor);
			((TextView) v.findViewById(R.id.tv_item2_name_option)).setTextColor(mFocusColor);
			((TextView) v.findViewById(R.id.tv_item2_price_option)).setTextColor(mFocusColor);
			((TextView) v.findViewById(R.id.tv_item3_name_option)).setTextColor(mNormalColor);
			((TextView) v.findViewById(R.id.tv_item3_price_option)).setTextColor(mNormalColor);
			item = (OptionItem) ((TextView) v.findViewById(R.id.tv_item2_price_option)).getTag();

			break;
		case 3:
			((TextView) v.findViewById(R.id.tv_item1_name_option)).setTextColor(mNormalColor);
			((TextView) v.findViewById(R.id.tv_item1_price_option)).setTextColor(mNormalColor);
			((TextView) v.findViewById(R.id.tv_item2_name_option)).setTextColor(mNormalColor);
			((TextView) v.findViewById(R.id.tv_item2_price_option)).setTextColor(mNormalColor);
			((TextView) v.findViewById(R.id.tv_item3_name_option)).setTextColor(mFocusColor);
			((TextView) v.findViewById(R.id.tv_item3_price_option)).setTextColor(mFocusColor);

			item = (OptionItem) ((TextView) v.findViewById(R.id.tv_item3_price_option)).getTag();

			break;
		default:
			break;
		}
		if (item != null && item.getProductId() != null) {
			if (ListDetailActivity.extraList.get(item.getProductId()) != null) {
				ListDetailActivity.extraList.get(item.getProductId()).getOptionList().add(item);
			} else {
				ExtraItemSet extraItemSet = new ExtraItemSet();
				List<OptionItem> list = new ArrayList<OptionItem>();
				list.add(item);
				extraItemSet.setOptionList(list);
				ListDetailActivity.extraList.put(item.getProductId(), extraItemSet);
			}
		}
	}

}
