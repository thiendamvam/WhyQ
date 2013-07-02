package whyq.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


public class MenuAdapter extends BaseAdapter implements OnClickListener {

	private final ArrayList<Store> list;
	private final Context context;
	private HashMap<String, View> viewList = new HashMap<String, View>();
	private ViewHolder holder;
	private boolean isMainYellowPage;
	public static List<MenuAdapter.ViewHolder> listSectionView = new ArrayList<MenuAdapter.ViewHolder>();

	public MenuAdapter(Context context2, ArrayList<Story> storyList,
			boolean isMainYellowPage) {

		this.context = context2;
		this.list = storyList;
		this.isMainYellowPage = isMainYellowPage;
	}

	public void clearAdapter() {
		list.clear();
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view2 = null;
		Story story = list.get(position);
		String storyIndex = story.getIndex();
		convertView = viewList.get(storyIndex);
		if (convertView == null || true) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.yellow_page_item, null);
			holder = new ViewHolder();
			holder.storyName = (TextView) view.findViewById(R.id.story_name);
			holder.image = (ImageView) view.findViewById(R.id.story_thumb);
			holder.id = storyIndex;
			// holder.image.setBackgroundColor(Color.parseColor("#4777b2"));
			// holder.image.setPadding(5, 5, 5, 5);
			if (storyIndex != null) {
				// holder.storyName.setText(story.getTitle());
				int id = Integer.parseInt(storyIndex);
				switch (id) {
				case 0:
					if (MainActivity.LanguageType == 0) { // Vietnamese
						holder.image
								.setBackgroundResource(R.drawable.retaurent_viet);
					} else if (MainActivity.LanguageType == 1) {// Korean
						holder.image
								.setBackgroundResource(R.drawable.restaurent_kr);
					} else { // English
						holder.image
								.setBackgroundResource(R.drawable.restaurant);
					}

					break;
				case 1:
					if (MainActivity.LanguageType == 0) { // Vietnamese
						holder.image
								.setBackgroundResource(R.drawable.food_mart_vi);
					} else if (MainActivity.LanguageType == 1) {// Korean
						holder.image.setBackgroundResource(R.drawable.foot_kr);
					} else { // English
						holder.image
								.setBackgroundResource(R.drawable.food_mart);
					}

					break;
				case 2:
					if (MainActivity.LanguageType == 0) { // Vietnamese
						holder.image
								.setBackgroundResource(R.drawable.education_vi);
					} else if (MainActivity.LanguageType == 1) {// Korean
						holder.image
								.setBackgroundResource(R.drawable.education_kr);
					} else { // English
						holder.image
								.setBackgroundResource(R.drawable.education_institute);
					}

					break;
				case 3:
					if (MainActivity.LanguageType == 0) { // Vietnamese
						holder.image
								.setBackgroundResource(R.drawable.fincace_money_vi);

					} else if (MainActivity.LanguageType == 1) {// Korean
						holder.image
								.setBackgroundResource(R.drawable.finance_money_kr);
					} else { // English
						holder.image
								.setBackgroundResource(R.drawable.finacil_money);
					}
					break;
				case 4:
					if (MainActivity.LanguageType == 0) { // Vietnamese
						holder.image
								.setBackgroundResource(R.drawable.tour_visa_vi);

					} else if (MainActivity.LanguageType == 1) {// Korean
						holder.image
								.setBackgroundResource(R.drawable.tour_visa_kr);
					} else { // English
						holder.image
								.setBackgroundResource(R.drawable.tour_visa);
					}

					break;
				case 5:
					if (MainActivity.LanguageType == 0) { // Vietnamese
						holder.image.setBackgroundResource(R.drawable.hotel_vi);
					} else if (MainActivity.LanguageType == 1) {// Korean
						holder.image.setBackgroundResource(R.drawable.hotel_kr);
					} else { // English
						holder.image.setBackgroundResource(R.drawable.hotel);
					}

					break;
				case 6:
					if (MainActivity.LanguageType == 0) { // Vietnamese
						holder.image
								.setBackgroundResource(R.drawable.real_estate_vi);
					} else if (MainActivity.LanguageType == 1) {// Korean
						holder.image
								.setBackgroundResource(R.drawable.real_estate_kr);
					} else { // English
						holder.image
								.setBackgroundResource(R.drawable.real_estate);
					}
					break;
				case 7:
					if (MainActivity.LanguageType == 0) { // Vietnamese
						holder.image
								.setBackgroundResource(R.drawable.lenting_vi);
					} else if (MainActivity.LanguageType == 1) {// Korean
						holder.image
								.setBackgroundResource(R.drawable.lenting_kr);
					} else { // English
						holder.image.setBackgroundResource(R.drawable.lenting);
					}
					break;
				case 8:
					if (MainActivity.LanguageType == 0) { // Vietnamese
						holder.image
								.setBackgroundResource(R.drawable.medical_heath_vi);
					} else if (MainActivity.LanguageType == 1) {// Korean
						holder.image
								.setBackgroundResource(R.drawable.medical_heath_kr);
					} else { // English
						holder.image
								.setBackgroundResource(R.drawable.medical_health);
					}

					break;
				case 9:
					if (MainActivity.LanguageType == 0) { // Vietnamese
						holder.image
								.setBackgroundResource(R.drawable.golf_sport_vi);
					} else if (MainActivity.LanguageType == 1) {// Korean
						holder.image.setBackgroundResource(R.drawable.golt_kr);
					} else { // English
						holder.image
								.setBackgroundResource(R.drawable.golf_sport);
					}

					break;
				case 10:
					if (MainActivity.LanguageType == 0) { // Vietnamese
						holder.image
								.setBackgroundResource(R.drawable.message_beatyful_vi);
					} else if (MainActivity.LanguageType == 1) {// Korean
						holder.image
								.setBackgroundResource(R.drawable.massage_kr);
					} else { // English
						holder.image
								.setBackgroundResource(R.drawable.massage_beauty);
					}

					break;
				case 11:
					if (MainActivity.LanguageType == 0) { // Vietnamese
						holder.image
								.setBackgroundResource(R.drawable.service_vi);
					} else if (MainActivity.LanguageType == 1) {// Korean
						holder.image
								.setBackgroundResource(R.drawable.service_kr);
					} else { // English
						holder.image.setBackgroundResource(R.drawable.service);
					}

					break;
				case 12:
					if (MainActivity.LanguageType == 0) { // Vietnamese
						holder.image
								.setBackgroundResource(R.drawable.air_transport_vi);
					} else if (MainActivity.LanguageType == 1) {// Korean
						holder.image
								.setBackgroundResource(R.drawable.air_transport_kr);
					} else { // English
						holder.image
								.setBackgroundResource(R.drawable.air_transport);
					}
					break;
				case 13:
					if (MainActivity.LanguageType == 0) { // Vietnamese
						holder.image
								.setBackgroundResource(R.drawable.mobile_it_vi);
					} else if (MainActivity.LanguageType == 1) {// Korean
						holder.image
								.setBackgroundResource(R.drawable.mobile_kr);
					} else { // English
						holder.image
								.setBackgroundResource(R.drawable.mobile_it);
					}

					break;
				case 14:
					if (MainActivity.LanguageType == 0) { // Vietnamese
						holder.image
								.setBackgroundResource(R.drawable.design_interior_vi);
					} else if (MainActivity.LanguageType == 1) {// Korean
						holder.image
								.setBackgroundResource(R.drawable.design_interior_kr);
					} else { // English
						holder.image
								.setBackgroundResource(R.drawable.design_interior);
					}

					break;
				case 15:
					if (MainActivity.LanguageType == 0) { // Vietnamese
						holder.image.setBackgroundResource(R.drawable.lift_vi);
					} else if (MainActivity.LanguageType == 1) {// Korean
						holder.image.setBackgroundResource(R.drawable.lift_kr);
					} else { // English
						holder.image.setBackgroundResource(R.drawable.life);
					}
					break;
				case 16:
					if (MainActivity.LanguageType == 0) { // Vietnamese
						holder.image
								.setBackgroundResource(R.drawable.entertainment_vi);
					} else if (MainActivity.LanguageType == 1) {// Korean
						holder.image
								.setBackgroundResource(R.drawable.entertainment_kr);
					} else { // English
						holder.image
								.setBackgroundResource(R.drawable.entertain_ment);
					}

					break;
				case 17:
					if (MainActivity.LanguageType == 0) { // Vietnamese
						holder.image
								.setBackgroundResource(R.drawable.production_vi);
					} else if (MainActivity.LanguageType == 1) {// Korean
						holder.image
								.setBackgroundResource(R.drawable.production_kr);
					} else { // English
						holder.image
								.setBackgroundResource(R.drawable.production_manufacture);
					}
					break;
				case 18:
					if (MainActivity.LanguageType == 0) { // Vietnamese
						holder.image
								.setBackgroundResource(R.drawable.public_hotline_vi);
					} else if (MainActivity.LanguageType == 1) {// Korean
						holder.image
								.setBackgroundResource(R.drawable.public_hotline_kr);
					} else { // English
						holder.image
								.setBackgroundResource(R.drawable.public_hotline);
					}
					break;
				case 19:
					if (MainActivity.LanguageType == 0) { // Vietnamese
						holder.image
								.setBackgroundResource(R.drawable.register_edit_vi);
					} else if (MainActivity.LanguageType == 1) {// Korean
						holder.image
								.setBackgroundResource(R.drawable.registration_kr);
					} else { // English
						holder.image
								.setBackgroundResource(R.drawable.register_edit);
					}
					break;
				default:
					break;
				}

			} else {
				if (story.getThumbnail() != null)
					holder.image.setBackgroundResource(R.drawable.noimage);
				else
					holder.image.setBackgroundColor(Color.WHITE);
				holder.image.setPadding(20, 20, 20, 20);
				view.setBackgroundColor(Color.WHITE);

			}
			if (story.getThumbnail() != null) {

				try {
					String path = VKTimesApplication.Instance().getDataPath()
							+ "/" + Config.DATA ;
					String uri = StoreIssueDataController.getFileName(story
							.getThumbnail());
					Bitmap bm = ImageService.getImageFromSDCard(path+ "/" + story.getId() + "/" + uri,
							VKTimesApplication.Instance().deviceHeight() / 6,
							VKTimesApplication.Instance().deviceHeight() / 6);
					
					if(bm!=null){
						holder.image.setImageBitmap(bm);
					}else{
						UrlImageViewHelper.setUrlDrawable(holder.image,
								story.getThumbnail());

						
						DownLoadmachine donwloadMachine = new DownLoadmachine();
						donwloadMachine.downloadImage(story.getId(), story.getThumbnail(), path);
					}

				} catch (Exception e) {
					// TODO: handle exception
					UrlImageViewHelper.setUrlDrawable(holder.image,
							story.getThumbnail());
				}

			}
			// Typeface typeFace =
			// Typeface.createFromAsset(VKTimesApplication
			// .Instance().getAssets(), "font/MillerText-Roman.otf");
			// holder.storyName.setTypeface(typeFace);
			view.setTag(story);
			holder.image.setTag(story);
			viewList.put(storyIndex, view);

			if (isMainYellowPage) {
				view.setLayoutParams(new ListView.LayoutParams(
						ListView.LayoutParams.MATCH_PARENT, VKTimesApplication
								.Instance().deviceHeight() / 8));
			} else {
				view.setLayoutParams(new ListView.LayoutParams(
						ListView.LayoutParams.MATCH_PARENT, (VKTimesApplication
								.Instance().deviceHeight()) / 6));
			}

			view.setOnClickListener(this);
			if (storyIndex != null)
				view.setBackgroundColor(Color.parseColor("#f7c600"));
			// holder.image.setOnClickListener(this);
			return view;
		} else {
			view2 = convertView;
		}
		if (storyIndex != null)
			view2.setBackgroundColor(Color.parseColor("#f7c600"));
		return view2;
	}

	public static class ViewHolder {
		public String id;
		public TextView storyName;
		public ImageView image;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		Story story = (Story) v.getTag();
		String id = story.getId();
		Log.d("onTouch", "" + id);
		if (!isMainYellowPage && !id.equals("")
				&& v.getId() == R.id.story_thumb) {
			// YellowPageSubActivity.gotoStoryDetail(story);
			sendBroadcast(story);
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Story story = (Story) v.getTag();
		String id = story.getId();
		Log.d("onTouch", "" + id);
		if (!isMainYellowPage && id != null && !id.equals("")) {
			// YellowPageSubActivity.gotoStoryDetail(story);
			sendBroadcast(story);
		} else if (isMainYellowPage && id != null) {
			YellowPageScreenFragment.gotoSubSection(story);
		}
	}

	protected void sendBroadcast(Story story) {
		Intent new_intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putString("story_id", story.getId());
		bundle.putString("story_title", story.getTitle());
		new_intent.putExtra("story_data", bundle);
		new_intent.setAction(YellowPageSubActivity.GOTO_STORY_DETAIL);
		VKTimesApplication.Instance().getApplicationContext()
				.sendBroadcast(new_intent);
	}
}