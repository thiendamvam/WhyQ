package whyq.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import whyq.adapter.BasicImageListAdapter;
import whyq.adapter.ExpanMenuAdapter;
import whyq.adapter.ExpandableListAdapter.ViewHolderMitemInfo;
import whyq.adapter.WhyqMenuAdapter;
import whyq.interfaces.IServiceListener;
import whyq.model.Bill;
import whyq.model.GroupMenu;
import whyq.model.Menu;
import whyq.model.Photo;
import whyq.model.Promotion;
import whyq.model.Store;
import whyq.model.UserCheckBill;
import whyq.service.Service;
import whyq.service.ServiceResponse;
import whyq.utils.UrlImageViewHelper;
import whyq.view.CustomViewPager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.whyq.R;

public class ListDetailActivity extends FragmentActivity implements IServiceListener {

	private Service service;
	// ProgressDialog dialog;
	ProgressBar progressBar;
	private String id;
	public static Store store;
	private TextView tvAddresss;
	private ImageView imgThumbnail;
	private TextView tvNumberFavourtie;
	private TextView tvOpeningTime;
	private ImageView imgFavourtieIcon;
	private TextView tvTelephone;
	private TextView tvStoreDes;
	private TextView tvCommendRever;
	private ImageView imgUserAvatar;
	private TextView tvFriendNumber;
	private EditText edSearch;
	private ListView lvResult;
	private ImageView imgFrienAvatar;
	private Button btnDone;
	private RadioButton imgBtnAbout;
	private RadioButton imgBtnMenu;
	private RadioButton imgBtnPromotion;
	private String cateId;
	private TextView tvHeaderTitle;
	private TextView tvFromUsr;
	private ImageView imgHeader;
	private int storeType;
	private RadioGroup radioGroup;
	private LinearLayout lnAboutContent;
	private LinearLayout lnMenuContent;
	private LinearLayout lnPromotionContent;
	private ExpandableListView lvMenu;
	private TextView tvNumberDiscount;
	private TextView tvDate;
	private TextView tvDes;
	private ImageView imgView;
	private WhyqMenuAdapter menuAdapter;
	private Button btnTotalValue;
	private ArrayList<Menu> menuList;
	private CustomViewPager vpPhotoList;
	public static HashMap<String,Bill> billList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.store_detail);
		id = getIntent().getStringExtra("id");
		service = new Service(this);
		tvAddresss = (TextView) findViewById(R.id.tvAddress);
		imgThumbnail = (ImageView) findViewById(R.id.imgThumbnail);
		tvNumberFavourtie = (TextView) findViewById(R.id.tvNumberOfFavourite);
		imgFavourtieIcon = (ImageView) findViewById(R.id.imgFavourite);
		tvOpeningTime = (TextView) findViewById(R.id.tvOpeningTime);
		tvTelephone = (TextView) findViewById(R.id.tvTelephone);
		tvStoreDes = (TextView) findViewById(R.id.tvStoreDes);
		tvCommendRever = (TextView) findViewById(R.id.tvCommendReview);
		imgFrienAvatar = (ImageView) findViewById(R.id.imgAvatar);
		tvFriendNumber = (TextView) findViewById(R.id.tvFriendCount);
		tvFromUsr = (TextView)findViewById(R.id.tvFromUser);
		edSearch = (EditText) findViewById(R.id.tvSearch);
		lvResult = (ListView) findViewById(R.id.lvFindResult);
		progressBar = (ProgressBar) findViewById(R.id.prgBar);
		btnDone = (Button) findViewById(R.id.btn_done);
		imgBtnAbout = (RadioButton) findViewById(R.id.rdoAbout);
		imgBtnMenu = (RadioButton) findViewById(R.id.rdoMenu);
		imgBtnPromotion = (RadioButton) findViewById(R.id.rdoPromotion);
		tvHeaderTitle = (TextView) findViewById(R.id.tvHeaderTitle);
		imgHeader = (ImageView)findViewById(R.id.imgHeader);
		storeType = getIntent().getIntExtra("store_type", 0);
		lnAboutContent = (LinearLayout)findViewById(R.id.lnAboutContent);
		lnMenuContent = (LinearLayout)findViewById(R.id.lnMenuContent);
		lnPromotionContent = (LinearLayout)findViewById(R.id.lnStoreDetailPromotion);
		imgView = (ImageView)findViewById(R.id.imgView);
		tvNumberDiscount = (TextView)findViewById(R.id.tvNumberDiscount);
		tvDate = (TextView)findViewById(R.id.tvDate);
		tvDes = (TextView)findViewById(R.id.tvDescription);
		btnTotalValue = (Button) findViewById(R.id.btnTotalValue);
		btnTotalValue.setText("0");
		lvMenu = (ExpandableListView)findViewById(R.id.lvMenu);
		billList = new HashMap<String, Bill>();
		vpPhotoList = (CustomViewPager)findViewById(R.id.vpStorephoto);
//		showHeaderImage();
		initTabbar();
		getDetailData();
		radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				int selectedBtn = radioGroup.getCheckedRadioButtonId();
				Log.d("setOnCheckedChangeListener", "selectedBtn "+selectedBtn);
				if(selectedBtn == R.id.rdoAbout){
					exeAboutFocus();
				}else if(selectedBtn == R.id.rdoMenu){
					exeMenuFocus();
				}else if(selectedBtn == R.id.rdoPromotion){
					exePromotionFocus();
				}
			}
		});
	}

	public void onDoneClicked( View v){
		
	}
	protected void exeAboutFocus() {
		// TODO Auto-generated method stub
		Log.d("exeAboutFocus","exeAboutFocus");
		setViewContent(1);
	}

	protected void exeMenuFocus() {
		// TODO Auto-generated method stub
		Log.d("exeMenuFocus","exeMenuFocus");
		setViewContent(2);
	}

	protected void exePromotionFocus() {
		// TODO Auto-generated method stub
		Log.d("exePromotionFocus","exePromotionFocus");
		setViewContent(3);
	}

	private void setViewContent(int i) {
		// TODO Auto-generated method stub
		if(i==1){
			lnAboutContent.setVisibility(View.VISIBLE);
			lnMenuContent.setVisibility(View.GONE);
			lnPromotionContent.setVisibility(View.GONE);
		}else if(i==2){
			lnAboutContent.setVisibility(View.GONE);
			lnMenuContent.setVisibility(View.VISIBLE);
			lnPromotionContent.setVisibility(View.GONE);
		}else if(i==3){
			lnAboutContent.setVisibility(View.GONE);
			lnMenuContent.setVisibility(View.GONE);
			lnPromotionContent.setVisibility(View.VISIBLE);
		}
	}

	private void showHeaderImage() {
		// TODO Auto-generated method stub
		if(storeType == 1){
			imgHeader.setImageResource(R.drawable.icon_result_cutlery);
		}else if(storeType==2){
			imgHeader.setImageResource(R.drawable.icon_result_wine);
		}else if(storeType==3){
			imgHeader.setImageResource(R.drawable.icon_result_coffee);
		}else if(storeType==4){
			imgHeader.setImageResource(R.drawable.icon_result_hotel);
		}
	}

	private void initTabbar() {
		// TODO Auto-generated method stub
//		imgBtnAbout.setImageResource(R.color.transparent);
//		imgBtnMenu.setImageResource(R.color.transparent);
//		imgBtnPromotion.setImageResource(R.color.transparent);
		tvHeaderTitle.setText("");
	}

	private void bindData() {
		// TODO Auto-generated method stub
		try {
			bindUserChecked();
			if(store.getLogo() !=null)
				UrlImageViewHelper.setUrlDrawable(imgThumbnail, store.getLogo());
			tvAddresss.setText(store.getAddress());
			tvNumberFavourtie.setText(""+store.getCountFavaouriteMember());
			
			tvOpeningTime.setText(store.getStartTime()+" - "+store.getEndTime());
			tvTelephone.setText(store.getPhoneStore());
			tvStoreDes.setText(store.getIntroStore());
			tvHeaderTitle.setText(store.getNameStore());
			
//			UrlImageViewHelper.setUrlDrawable(imgView, store.getPhotos());
			if(!store.getCountFavaouriteMember().equals("0")){
				tvCommendRever.setText(store.getCountFavaouriteMember()+" comments");
				tvCommendRever.setTextColor(getResources().getColor(R.color.profifle_blue));
				tvFromUsr.setVisibility(View.VISIBLE);
			}else{
				tvFromUsr.setVisibility(View.VISIBLE);
			}

//			UrlImageViewHelper.setUrlDrawable(imgFrienAvatar, store.getUserList()
//					.get(0).getUrlAvatar());
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	private void bindUserChecked() {
		// TODO Auto-generated method stub
		try {
			UserCheckBill userCheckBill = store.getUserCheckBill();
			if(userCheckBill !=null){
				if(userCheckBill.getTotalMember()!=null && !userCheckBill.getTotalMember().equals("")){
					if(userCheckBill.getAvatar()!=null && !userCheckBill.getAvatar().equals("")){
						if(Integer.parseInt(userCheckBill.getTotalMember()) > 0){
							tvFriendNumber.setText(userCheckBill.getFirstName()+" "+userCheckBill.getLastName()+ " & "+userCheckBill.getTotalMember()+" others visited");	
						}else{
							tvFriendNumber.setText(userCheckBill.getFirstName()+" "+userCheckBill.getLastName()+ " & "+userCheckBill.getTotalMember()+" other visited");
						}
						
						imgFrienAvatar.setVisibility(View.VISIBLE);
						UrlImageViewHelper.setUrlDrawable(imgFrienAvatar, userCheckBill.getAvatar());	
					}else{
						imgFrienAvatar.setVisibility(View.GONE);
						if(Integer.parseInt(userCheckBill.getTotalMember()) > 0){
							tvFriendNumber.setText(userCheckBill.getTotalMember()+" others visited");
						}else{
							tvFriendNumber.setText(userCheckBill.getTotalMember()+" other visited");
						}
						
					}

				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private void getDetailData() {
		// TODO Auto-generated method stub
		showDialog();
		service.getBusinessDetail(id);
	}

	private void showDialog() {
		// dialog.show();
		progressBar.setVisibility(View.VISIBLE);
	}

	private void hideDialog() {
		// dialog.dismiss();
		progressBar.setVisibility(View.GONE);
	}

	@Override
	public void onCompleted(Service service, ServiceResponse result) {
		// TODO Auto-generated method stub
		hideDialog();
		store = (Store) result.getData();
		if(store!=null){
			storeType = Integer.valueOf(store.getCateid());
			showHeaderImage();
			bindData();
			bindMenuData();
			bindPromotionData();
			bindImageList();
		}
	}
	private void bindImageList() {
		// TODO Auto-generated method stub
		ArrayList<Photo> photoList = store.getPhotos();
		if(photoList.size()>0){
			exePutPhotoListData();
		}
	}
	private void exePutPhotoListData() {
		// TODO Auto-generated method stub

		List<Fragment> fragments = getFragments(store.getPhotos());
		whyq.adapter.CoverStoryAdapter coverStoryAdapter = new whyq.adapter.CoverStoryAdapter(
				getSupportFragmentManager(), fragments);
		vpPhotoList.setAdapter(coverStoryAdapter);
		coverStoryAdapter.notifyDataSetChanged();
		vpPhotoList.requestLayout();
		
	}
	private List<Fragment> getFragments(ArrayList<Photo> photoList) {
		// TODO Auto-generated method stub
		List<Fragment> fList = new ArrayList<Fragment>();


		for (int i= 0; i< photoList.size(); i++) {
			try {
				Photo photo = photoList.get(i);
				fList.add(BasicImageListAdapter.newInstance(photo));
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		return fList;
	}
	private void bindPromotionData() {
		// TODO Auto-generated method stub
		try {
			if(store.getPromotionList()!=null){
				if(store.getPromotionList().size()>0){
					Promotion promotion = store.getPromotionList().get(0);
					tvNumberDiscount.setText(promotion.getValuePromotion()+""+promotion.getTypeValue()); 
					tvDes.setText(promotion.getDescriptionPromotion());
					tvDate.setText(promotion.getStartDate()+" - "+promotion.getEndDate());
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private void bindMenuData() {
		// TODO Auto-generated method stub
		try {
			menuList = store.getMenuList();
			
			int size = menuList.size();
			if(size > 0 ){
//				menuAdapter = new WhyqMenuAdapter(ListDetailActivity.this, menuList);
//				lvMenu.setAdapter(menuAdapter);
//				ExampleAdapter adapter = new ExampleAdapter(ListDetailActivity.this);
//				lvMenu.setAdapter(adapter);
				ArrayList<GroupMenu> mGroupCollection = new ArrayList<GroupMenu>();
				ArrayList<String> idList = getProductTypeIdList(menuList);
				int length = idList.size();
				for (int i = 0; i < length; i++) {
					try {

						String id = idList.get(i);
						GroupMenu group= getGroupFromId(
								menuList, id);
						
						if (group != null)
							mGroupCollection.add(group);

					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
				ExpanMenuAdapter adapter = new ExpanMenuAdapter(
						ListDetailActivity.this,
						lvMenu,
						mGroupCollection);

				lvMenu.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				
				lvMenu.setAdapter(adapter);
				for (int i = 0; i < mGroupCollection.size(); i++) {
					lvMenu.expandGroup(i);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	private GroupMenu getGroupFromId(List<Menu> storyList, String id) {
		// TODO Auto-generated method stub
		GroupMenu ge = new GroupMenu();
		List<Menu> storiesList = new ArrayList<Menu>();
		int storiesLength = storyList.size();
		for (int j = 0; j < storiesLength; j++) {
			Menu story = storyList.get(j);
			if (story.getTypeProductId().equals(id)) {
				storiesList.add(story);
			}
		}
		if (storiesList.size() > 0) {
			ge.setMenuList(storiesList);

//			ge.setName(storiesList.get(0).getTypeProductId());
			ge.setName(storiesList.get(0).getProductTypeInfoList().get(0).getNameProductType());

			ge.setColor("ffffff");
			return ge;
		} else {
			return null;
		}

	}
	private ArrayList<String> getProductTypeIdList(List<Menu> menuList) {
		// TODO Auto-generated method stub
		ArrayList<String> listId = new ArrayList<String>();
		int length = menuList.size();
		for (int i = 0; i < length; i++) {
			Menu menu = menuList.get(i);
			if (!listId.contains(menu.getTypeProductId())) {
				listId.add(menu.getTypeProductId());
			}
		}
		return listId;
	}
	public void onCutleryTabCliked(View v){
		resetTabBarFocus(1);
	}
	public void onWineTabCliked(View v){
		resetTabBarFocus(2);
	}
	public void onCoffeTabClicked(View v){
		resetTabBarFocus(3);
	}
	public void resetTabBarFocus(int index){
		
		switch (index) {
		case 3:
			
			imgBtnAbout.setBackgroundResource(R.drawable.bg_tab_normal);
			imgBtnMenu.setBackgroundResource(R.drawable.bg_tab_normal);
			imgBtnPromotion.setBackgroundResource(R.drawable.bg_tab_active);
//			imgBtnMenu.setBackgroundResource(R.drawable.icon_cat_coffee);
//			imgBtnPromotion.setBackgroundResource(R.drawable.bg_tab_active);
			cateId = "0";
			break;
		case 1:
			imgBtnAbout.setBackgroundResource(R.drawable.bg_tab_active);
			imgBtnPromotion.setBackgroundResource(R.drawable.bg_tab_normal);
			imgBtnMenu.setBackgroundResource(R.drawable.bg_tab_normal);
//			imgBtnMenu.setBackgroundResource(R.drawable.icon_cat_coffee);
//			imgBtnPromotion.setBackgroundResource(R.drawable.icon_cat_cutlery);
			cateId = "1";
			break;
		case 2:
			imgBtnMenu.setBackgroundResource(R.drawable.bg_tab_active);
			imgBtnAbout.setBackgroundResource(R.drawable.bg_tab_normal);
			imgBtnPromotion.setBackgroundResource(R.drawable.bg_tab_normal);
//			imgBtnAbout.setBackgroundResource(R.drawable.icon_cat_wine);
//			imgBtnPromotion.setBackgroundResource(R.drawable.icon_cat_cutlery);
			cateId = "2";
			break;

		default:
			break;
		}
		
	}
	
//	public void onAddClicked(View v){
//		Log.d("onAddClicked","id ="+v.getId());
//		Menu item = (Menu)v.getTag();
//		if(item!=null){
//			if(billList.containsKey(item.getId())){
//				int value = Integer.parseInt(billList.get(item.getId()).getUnit())+1;
//				billList.get(item.getId()).setUnit(""+value);
//			}else{
//				Bill bill = new Bill();
//				bill.setId(item.getId());
//				bill.setPrice(item.getValue());
//				bill.setUnit("1");
//				billList.put(item.getId(),bill);
//			}
//
//			updateCount(item,true);
//		}
//	}
//	
//	public void onRemoveClicked(View v){
//		Log.d("onRemoveClicked","id ="+v.getId());
//		Menu item = (Menu)v.getTag();
//		updateCount(item,false);
//		if(billList.containsKey(item.getId())){
//			int value = Integer.parseInt(billList.get(item.getId()).getUnit())-1;
//
//			billList.get(item.getId()).setUnit(""+value);
//			if(value < 0)
//				billList.remove(item.getId());
//		}else{
//			Bill bill = new Bill();
//			bill.setId(item.getId());
//			bill.setPrice(item.getValue());
//			bill.setThumb(item.getImageThumb());
//			bill.setUnit("1");
//			billList.put(item.getId(),bill);
//		}
//	}
	public void onAddClicked(View v){
		Log.d("onAddClicked","id ="+v.getId());
		ViewHolderMitemInfo holder = (ViewHolderMitemInfo)v.getTag();
		Menu item = getMenuById(holder.menuId);
		if(item!=null){
			if(billList.containsKey(item.getId())){
				int value = Integer.parseInt(billList.get(item.getId()).getUnit())+1;
				billList.get(item.getId()).setUnit(""+value);
			}else{
				Bill bill = new Bill();
				bill.setId(item.getId());
				bill.setPrice(item.getValue());
				bill.setUnit("1");
				billList.put(item.getId(),bill);
			}
	
//			updateCount(holder,true);
			updateCountInExpandListview(holder, true);
		}
	}
	private Menu getMenuById(String menuId) {
		// TODO Auto-generated method stub
		int size = menuList.size();
		for(Menu menu: menuList){
			if(menuId.equals(menu.getId())){
				return menu;
			}
		}
		return null;
	}
	public void onRemoveClicked(View v){
		Log.d("onRemoveClicked","id ="+v.getId());
		ViewHolderMitemInfo holder = (ViewHolderMitemInfo)v.getTag();
		Menu item = getMenuById(holder.menuId);
//		updateCount(holder,false);
		updateCountInExpandListview(holder, false);
		if(item!=null){
			if(billList.containsKey(item.getId())){
				int value = Integer.parseInt(billList.get(item.getId()).getUnit())-1;

				billList.get(item.getId()).setUnit(""+value);
				if(value < 0)
					billList.remove(item.getId());
			}else{
				Bill bill = new Bill();
				bill.setId(item.getId());
				bill.setPrice(item.getValue());
				bill.setThumb(item.getImageThumb());
				bill.setUnit("1");
				billList.put(item.getId(),bill);
			}
		}
	}
	public void onViewBillClicked(View v){
		Intent intent = new Intent(ListDetailActivity.this, WhyQBillScreen.class);
		startActivity(intent);
	}
//	private void updateCount(Menu item, boolean b) {
//		// TODO Auto-generated method stub
//		int size = lvMenu.getChildCount();
//		float value,totalValue = Float.parseFloat(btnTotalValue.getText().toString());
//		Menu item2;
//		ViewHolderMitemInfo holder;
//		for(int i=0;i< size;i++){
//			try {
//
//				item2 = store.getMenuList().get(i);
//				if(item2.getId().equals(item.getId()) && !item.getValue().equals("")){
//					holder = (ViewHolderMitemInfo)lvMenu.getChildAt(i).getTag();
//					if(b){
//						value = Float.parseFloat(holder.tvCount.getText().toString())+Float.parseFloat("1");
//						totalValue+=Float.parseFloat(item.getValue());
//					}else{
//						value = Float.parseFloat(holder.tvCount.getText().toString())-Float.parseFloat("1");
//						totalValue-=Float.parseFloat(item.getValue());
//					}
//					if(value < 0 )
//						value= 0;
//					if(totalValue < 0)
//						totalValue = 0;
//					holder.tvCount.setText(""+value);
//					lvMenu.getChildAt(i).requestLayout();
//					btnTotalValue.setText(""+totalValue);
//				}
//			
//			} catch (Exception e) {
//				// TODO: handle exception
//				e.printStackTrace();
//			}
//		}
//	}
	private void updateCountInExpandListview(ViewHolderMitemInfo holder, boolean b) {
		int size = lvMenu.getChildCount();
		float value,totalValue = Float.parseFloat(btnTotalValue.getText().toString());
		Menu item2;
		Menu item = getMenuById(holder.menuId);
		if(b){
			value = Float.parseFloat(holder.tvCount.getText().toString())+Float.parseFloat("1");
			totalValue+=Float.parseFloat(item.getValue());
		}else{
			value = Float.parseFloat(holder.tvCount.getText().toString())-Float.parseFloat("1");
			totalValue-=Float.parseFloat(item.getValue());
		}
		if(value < 0 )
			value= 0;
		if(totalValue < 0)
			totalValue = 0;
		holder.tvCount.setText(""+value);
		holder.tvCount.requestLayout();
		btnTotalValue.setText(""+totalValue);
	}
	private void updateCount(ViewHolderMitemInfo holder, boolean b) {
		// TODO Auto-generated method stub
		int size = lvMenu.getChildCount();
		float value,totalValue = Float.parseFloat(btnTotalValue.getText().toString());
		Menu item2;
		Menu item = getMenuById(holder.menuId);
		for(int i=0;i< size;i++){
			try {

				item2 = store.getMenuList().get(i);
				if(item2.getId().equals(item.getId()) && !item.getValue().equals("")){
//					holder = (ViewHolderMitemInfo)lvMenu.getChildAt(i).getTag();
					if(b){
						value = Float.parseFloat(holder.tvCount.getText().toString())+Float.parseFloat("1");
						totalValue+=Float.parseFloat(item.getValue());
					}else{
						value = Float.parseFloat(holder.tvCount.getText().toString())-Float.parseFloat("1");
						totalValue-=Float.parseFloat(item.getValue());
					}
					if(value < 0 )
						value= 0;
					if(totalValue < 0)
						totalValue = 0;
					holder.tvCount.setText(""+value);
					lvMenu.getChildAt(i).requestLayout();
					btnTotalValue.setText(""+totalValue);
				}
			
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
	public void gotoCommentScreen(View v){
		if(store !=null){
			if(store.getCountFavaouriteMember() !=null){
				if(store.getCountFavaouriteMember().equals("0")){
					Intent intent = new Intent(ListDetailActivity.this, WhyQCommentActivity.class);
					intent.putExtra("store_id", store.getId());
					startActivity(intent);
				}else{
					Intent intent = new Intent(ListDetailActivity.this, CommentActivity.class);
					intent.putExtra("store_id", store.getId());
					startActivity(intent);
				}
				
			}else{
				Intent intent = new Intent(ListDetailActivity.this, WhyQCommentActivity.class);
				intent.putExtra("store_id", store.getId());
				startActivity(intent);
			}
		}
	} 
	public void onBack(View v){
		finish();
		
	}
}
