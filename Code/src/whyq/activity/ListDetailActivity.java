package whyq.activity;

import whyq.interfaces.IServiceListener;
import whyq.model.Store;
import whyq.service.Service;
import whyq.service.ServiceResponse;
import whyq.utils.UrlImageViewHelper;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.whyq.R;

public class ListDetailActivity extends Activity implements IServiceListener {

	private Service service;
	// ProgressDialog dialog;
	ProgressBar progressBar;
	private String id;
	private Store store;
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
	private ImageView imgBtnAbout;
	private ImageView imgBtnMenu;
	private ImageView imgBtnPromotion;
	private String cateId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.store_detail);
		id = getIntent().getStringExtra("id");
		service = new Service(this);
		tvAddresss = (TextView) findViewById(R.id.tvAddress);
		imgThumbnail = (ImageView) findViewById(R.id.imgThumbnail);
		tvNumberFavourtie = (TextView) findViewById(R.id.tvNumberFavourite);
		imgFavourtieIcon = (ImageView) findViewById(R.id.imgFavourite);
		tvOpeningTime = (TextView) findViewById(R.id.tvOpeningTime);
		tvTelephone = (TextView) findViewById(R.id.tvTelephone);
		tvStoreDes = (TextView) findViewById(R.id.tvStoreDes);
		tvCommendRever = (TextView) findViewById(R.id.tvCommendReview);
		imgFrienAvatar = (ImageView) findViewById(R.id.imgAvatar);
		tvFriendNumber = (TextView) findViewById(R.id.tvFriendCount);
		edSearch = (EditText) findViewById(R.id.tvSearch);
		lvResult = (ListView) findViewById(R.id.lvFindResult);
		progressBar = (ProgressBar) findViewById(R.id.prgBar);
		btnDone = (Button) findViewById(R.id.btn_done);
		imgBtnAbout = (ImageView) findViewById(R.id.lnCutleryTab);
		imgBtnMenu = (ImageView) findViewById(R.id.lnWineTab);
		imgBtnPromotion = (ImageView) findViewById(R.id.lnCoffeTab);
		initTabbar();
		getDetailData();
	}

	private void initTabbar() {
		// TODO Auto-generated method stub
		imgBtnAbout.setImageResource(R.color.transparent);
		imgBtnMenu.setImageResource(R.color.transparent);
		imgBtnPromotion.setImageResource(R.color.transparent);
	}

	private void bindData() {
		// TODO Auto-generated method stub
		try {
			if(store.getLogo() !=null)
				UrlImageViewHelper.setUrlDrawable(imgThumbnail, store.getLogo());
			tvAddresss.setText(store.getAddress());

			tvOpeningTime.setText(store.getCreatedate());
			tvTelephone.setText(store.getPhoneStore());
			tvStoreDes.setText(store.getIntroStore());
			tvNumberFavourtie.setText(""+store.getCountFavaouriteMember());
			tvCommendRever.setText(store.getCountFavaouriteMember() + " from users");
			UrlImageViewHelper.setUrlDrawable(imgFrienAvatar, store.getUserList()
					.get(0).getUrlAvatar());
		} catch (Exception e) {
			// TODO: handle exception
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
		progressBar.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onCompleted(Service service, ServiceResponse result) {
		// TODO Auto-generated method stub
		hideDialog();
		store = (Store) result.getData();
		if(store!=null)
			bindData();
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
	public void onBack(View v){
		finish();
		
	}
}
