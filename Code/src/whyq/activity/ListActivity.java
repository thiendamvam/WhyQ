package whyq.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import twitter4j.util.ImageUpload.ImgLyOAuthUploader;
import whyq.WhyqApplication;
import whyq.WhyqMain;
import whyq.adapter.WhyqAdapter;
import whyq.adapter.WhyqAdapter.ViewHolder;
import whyq.controller.WhyqListController;
import whyq.interfaces.Login_delegate;
import whyq.model.User;
import whyq.model.Store;
import whyq.utils.API;
import whyq.utils.RSA;
import whyq.utils.UrlImageViewHelper;
import whyq.utils.WhyqUtils;
import whyq.utils.XMLParser;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.whyq.R;

public class ListActivity extends FragmentActivity implements Login_delegate, OnClickListener{

	
	public static final String DOWNLOAD_COMPLETED = "DOWNLOAD_COMPLETED";
	public static final String COFFE = "";
	public String url = "";
	public Boolean header = true;

	/**
	 * MSA
	 */
	private ArrayList<Store> permListMain = new ArrayList<Store>();

	public static int screenWidth;
	public static int screenHeight;
	public static boolean isLogin = false;
	public static int loginType = 0;
	public static boolean isRefesh = true;
	public static boolean isCalendar = false;
	int nextItem = -1;
//	FragmentManager t = ggetSupportFragmentManager();
//	private ProgressDialog dialog;
	
	ListView whyqListView;
//	Button btnRefesh;
	ProgressBar progressBar;
//	ImageView imageViewBeforRefesh;
//	RelativeLayout headerLayout;
	WhyqAdapter permListAdapter;
	View headerView = null;
	private boolean isFirst = true;
	public static LoadPermList loadPermList;
	public boolean isAddHeader = false;
	private ImageView bntFilter;
	private LinearLayout lnFilter;
	private EditText etTextSearch;
	private TextView cktViewAll;
	private TextView cktFriednVised;
	private TextView cktFriendFavourtie;
	/** Called when the activity is first created. */
	/*
	 * Whyq elements
	 */
	
	ImageButton lnCutlery;
	ImageButton lnWine;
	private ImageButton lnCoffe;
	
	
	private String searchKey="";
	private String longitude="";
	private String latgitude="";
	private String filter="0"; 
	private String friendFavourite;
	private String friendVisited;
	private String cateId="0";
	public static boolean isSearch = false;
	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			if (intent.getAction().equals(DOWNLOAD_COMPLETED)) {
				exeListActivity(false);
			} 
		}
	};
	
	
	private OnItemClickListener onStoreItemListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> adapter, View view,
				int position, long id) {
			try {
				ViewHolder store = ((ViewHolder) view.getTag());
				if(store !=null){
					String storeId = store.id;
					if(storeId !=null)
						gotoStoreDetail(storeId);
				}
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	};
	private ImageView imgCheckedAll;
	private ImageView imgCheckedFavorite;
	private ImageView imgCheckedVisited;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		createUI();
		IntentFilter intentFilter = new IntentFilter(DOWNLOAD_COMPLETED);
		registerReceiver(receiver, intentFilter);
		WhyqUtils.clearViewHistory();
		WhyqUtils utils= new WhyqUtils();
		utils.writeLogFile(ListActivity.this.getIntent());
//    	dialog = ProgressDialog.show(getParent(), "", "progressing...",
//    			true);
    	showProgress();
//    	Intent intent = new Intent(ListActivity.this, WhyqLogout.class);
//    	startActivity(intent);
	}
	
	protected void gotoStoreDetail(String storeId) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(ListActivity.this, ListDetailActivity.class);
		startActivity(intent);
	}

	public void createUI() {
		setContentView(R.layout.list_screen);//
		
		whyqListView = (ListView) findViewById(R.id.lvWhyqList);
		lnCutlery = (ImageButton) findViewById(R.id.lnCutleryTab);
		lnWine = (ImageButton) findViewById(R.id.lnWineTab);
		lnCoffe = (ImageButton) findViewById(R.id.lnCoffeTab);
		bntFilter = (ImageView)findViewById(R.id.btnFilters);
		lnFilter = (LinearLayout)findViewById(R.id.lnFilterView);
		loadPermList = new LoadPermList(false);
		progressBar = (ProgressBar)findViewById(R.id.prgBar);
		etTextSearch =(EditText) findViewById(R.id.etTextSearch);
		isAddHeader = true;
		cktViewAll = (TextView) findViewById(R.id.cktViewAll);
		cktFriednVised = (TextView)findViewById(R.id.cktViewVisited);
		cktFriendFavourtie = (TextView) findViewById(R.id.cktViewFavourite);
		imgCheckedAll = (ImageView) findViewById(R.id.imgCbAll);
		imgCheckedFavorite = (ImageView)findViewById(R.id.imgCbFavourite);
		imgCheckedVisited = (ImageView) findViewById(R.id.imgCbVisited);
		
		whyqListView.setOnItemClickListener(onStoreItemListener);
		etTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(etTextSearch
							.getApplicationWindowToken(), 0);
					try {
						String text = etTextSearch.getText().toString();
						if(text.equals(""))
						{
							isSearch = false;
						}else{
							isSearch = true;
						}
						exeSearch(text);
					} catch (Exception e) {
						// TODO: handle exception
					}
					return true;
				}
				return false;
			}
		});
		etTextSearch.addTextChangedListener(mTextEditorWatcher);

	}
	
	protected void exeSearch(String string) {
		// TODO Auto-generated method stub
		searchKey = string;
		isSearch = true;
		exeListActivity(true);
	}

	@Override
	protected void onDestroy(){
		super.onDestroy();
		isCalendar =false;
		isRefesh = true;
	}
	@Override
	protected void onResume() {
		super.onResume();
		if(isLogin && WhyqMain.getCurrentTab() == 3){
			User user2 = WhyqUtils.isAuthenticated(getApplicationContext());
			if(user2 != null){
				String id = user2.getId();
				if(id != null)
					WhyqMain.gotoDiaryTab(id);
			}
			isLogin = false;
		}else if(WhyqMain.getCurrentTab() == 0 && isRefesh){
			// Get the screen's size.
			exeListActivity(false);
		}else if(WhyqMain.getCurrentTab() == 1 || WhyqMain.getCurrentTab() == 4){
			if(isRefesh)
				exeListActivity(false);
		}else if(WhyqMain.getCurrentTab() == 3) { 
			isCalendar = true;
			exeListActivity(false);
		}else if(!isRefesh){
			isRefesh = true;
		}
	}
	
	protected void onPause () {
    	super.onPause();
    	isFirst = true;
    	nextItem = -1;
    	showProgress();
    	
    }



	public void exeListActivity(boolean isSearch) {
		// TODO Auto-generated method stub
		showProgress();
		if(isFirst){
//	    	clearData();
	    	if(permListMain !=null)
	    		permListMain.clear();
	    	isFirst = false;
		}
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		
		
		//Set to application
		WhyqApplication state = (WhyqApplication) this.getApplication();
		if (state != null) {
			state.setDisplayMetrics(metrics);
		}
		
		
		screenHeight = metrics.heightPixels;
		screenWidth = metrics.widthPixels;

		User user = WhyqUtils.isAuthenticated(getApplicationContext());
		Bundle extras = getIntent().getExtras();
		if(extras != null && extras.containsKey("allcategory")){
			this.url = API.getNewPerm;
			this.header = false;
		}else if( extras != null && extras.containsKey("permByDate")){
			this.url = (String)extras.getString("permByDate");
			this.header = false;
		}else if (extras != null) {
			this.url = (String) extras.get("categoryURL");
			this.header = false;
		} else if (user != null) {
//			this.url = API.followingPerm + String.valueOf(user.getId());

		}
		if(isSearch){
			this.url = API.searchBusinessListURL;
			this.header = false;
		}else{
			this.url = API.popularBusinessListURL;
			this.header = false;
		}
		clearData();
//		btnRefesh.setVisibility(View.GONE);
//		progressBar.setVisibility(View.VISIBLE);
		showProgress();

		loadPermList = new LoadPermList(isSearch);
		loadPermList.execute();
	}
	
	private void timeoutDialog() {
		// TODO Auto-generated method stub

		hideProgress();
	}
	public void loadPreviousItems() {
		if(nextItem > -1) {
			nextItem = nextItem - 1;
			clearData();

//			btnRefesh.setVisibility(View.GONE);
//			progressBar.setVisibility(View.VISIBLE);
			showProgress();
			
			loadPermList = new LoadPermList(isSearch);
			loadPermList.execute();
		}
		
	}

	public void loadNextItems() {
		if(permListAdapter != null) {
			nextItem = permListAdapter.getNextItems();
			//clearData();

//			btnRefesh.setVisibility(View.GONE);
//			progressBar.setVisibility(View.VISIBLE);
			showProgress();
	    	loadPermList = new LoadPermList(isSearch);
			loadPermList.execute();
		}		
	}
	
	public void cancelLoadPermList() {
		if(loadPermList != null) {
			loadPermList.cancel(true);
		}
	}
	
	private void loadPerms() {
		User user = WhyqUtils.isAuthenticated(getApplicationContext());		
		if(permListMain != null && !permListMain.isEmpty()){
//			clearData();
			//createUI();
			if(this.permListAdapter == null) {
				this.permListAdapter = new WhyqAdapter(ListActivityGroup.context,
					getSupportFragmentManager(),R.layout.whyq_item_1, permListMain, this, screenWidth, screenHeight, header, user);
			} else {
				for(int i = 0; i < permListMain.size(); i++) {
					Store store = permListMain.get(i);
					if(!permListAdapter.isPermDuplicate(store)) {
						permListAdapter.add(store);
					}
				}
			}

			
			whyqListView.setAdapter(permListAdapter);

//			int selected = permListAdapter.getCount() - permListMain.size() - 2;
//			if(selected >= 0) {
//				whyqListView.setSelection(selected);
//			} else {
//				whyqListView.setSelection(0);
//			}

		}else{
			
			
		}

	}
	
	public void clearData() {
		if(permListAdapter != null && !permListAdapter.isEmpty()) {
			permListAdapter.clear();
			UrlImageViewHelper.clearAllImageView();				
		}
		if(whyqListView != null && headerView != null) {
			whyqListView.removeHeaderView(headerView);
		}
	}
	
	public void removeAllData() {
		if(permListAdapter != null && !permListAdapter.isEmpty()) {
			permListAdapter.clear();						
		}
		clearData();
	}

	
	// AsyncTask task for upload file

	public class LoadPermList extends AsyncTask<ArrayList<Store>, Void, ArrayList<Store>> {

		public boolean isSearch;

		public LoadPermList(boolean isSearch){
			this.isSearch = isSearch; 
		}
		
		@Override
		protected ArrayList<Store> doInBackground(ArrayList<Store>... params) {
			// TODO Auto-generated method stub
			WhyqListController whyqListController = new WhyqListController();
			ArrayList<Store> permList = null;
			try {				
				if (nextItem != -1) {
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
					nameValuePairs.add(new BasicNameValuePair("nextItem", String.valueOf(nextItem)));

					if(isCalendar){
						nameValuePairs.add(new BasicNameValuePair("uid", WhyqMain.UID));
//						isCalendar =false;
					}

					permList = whyqListController.getBusinessList(url, nameValuePairs);
				} else {
					if(isCalendar){
						List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
						nameValuePairs.add(new BasicNameValuePair("uid", WhyqMain.UID));
						permList = whyqListController.getBusinessList(url,nameValuePairs);
						
					}else{
						RSA rsa = new RSA();
						String enToken = rsa.RSAEncrypt(XMLParser.getToken(WhyqApplication.Instance().getApplicationContext()));
						List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
						nameValuePairs.add(new BasicNameValuePair("token",enToken));
						if(isSearch){
							nameValuePairs.add(new BasicNameValuePair("key", searchKey));
							nameValuePairs.add(new BasicNameValuePair("search_longitude", longitude));
							nameValuePairs.add(new BasicNameValuePair("search_latitude", latgitude));
							nameValuePairs.add(new BasicNameValuePair("cate_id", cateId));
							
							
						}else{
//							nameValuePairs.add(new BasicNameValuePair("key", searchKey));
							nameValuePairs.add(new BasicNameValuePair("cate_id", cateId));
							if(friendVisited !=null){
								nameValuePairs.add(new BasicNameValuePair("friend_visit", friendVisited));
							}else if(friendFavourite !=null){
								nameValuePairs.add(new BasicNameValuePair("friend_favourite", friendFavourite));
							}else{
								
							}
						}
						permList = whyqListController.getBusinessList(url, nameValuePairs);	
					}
					
				}
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
//				if (dialog != null && dialog.isShowing()) {
//					dialog.dismiss();
//				}
				/*if(progressBar.getVisibility()==View.VISIBLE){
					progressBar.setVisibility(View.GONE);
					btnRefesh.setVisibility(View.VISIBLE);
				}*/
				hideProgress();
			}
			/*if(permList != null){
				for(int i = 0; i < permList.size(); i++) {
					if(!permListMain.contains(permList.get(i))) {
						permListMain.add(permList.get(i));
					}
				}
				
			}*/
			permListMain = permList;
			/**
			 * MSA
			 */
			//permListMain.addAll(permList);
			//permListMain = permList;
						
			return permListMain;
		}

		@Override
		protected void onProgressUpdate(Void... unsued) {

		}

		@Override
		protected void onPostExecute(ArrayList< Store> sResponse) {
			/**
			 * MSA
			 */
			loadPerms();
			WhyqListController.isLoading = false;
			
			//permListMain.size();
//			if (dialog != null && dialog.isShowing()) {
//				dialog.dismiss();
//			}
			hideProgress();
			if(permListAdapter != null) {
				permListAdapter.notifyDataSetChanged();
			}
		}

	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
	    if ((keyCode == KeyEvent.KEYCODE_BACK))
	    {
	        WhyqMain.back();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}

	@Override
	public void on_success() {
		// TODO Auto-generated method stub
		WhyqMain.refeshFollowerActivity();
	}

	@Override
	public void on_error() {
		// TODO Auto-generated method stub
		//Logger.appendLog("test log", "loginerror");
		Intent intent = new Intent(getApplicationContext(), JoinWhyqActivity.class);
		this.startActivity(intent);		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.btnRefesh:
			isFirst = true;
	    	nextItem = -1;
			exeListActivity(false);
			break;

		default:
			break;
		}
	}
	
	public void resetTabBarFocus(int index){
		
		switch (index) {
		case 1:
			
			lnWine.setBackgroundResource(R.drawable.bg_tab_normal);
			lnCoffe.setBackgroundResource(R.drawable.bg_tab_normal);
			lnCutlery.setBackgroundResource(R.drawable.bg_tab_active);
//			lnCoffe.setBackgroundResource(R.drawable.icon_cat_coffee);
//			lnCutlery.setBackgroundResource(R.drawable.bg_tab_active);
			cateId = "0";
			exeSearch(etTextSearch.getText().toString());
			break;
		case 2:
			lnWine.setBackgroundResource(R.drawable.bg_tab_active);
			lnCutlery.setBackgroundResource(R.drawable.bg_tab_normal);
			lnCoffe.setBackgroundResource(R.drawable.bg_tab_normal);
//			lnCoffe.setBackgroundResource(R.drawable.icon_cat_coffee);
//			lnCutlery.setBackgroundResource(R.drawable.icon_cat_cutlery);
			cateId = "1";
			exeSearch(etTextSearch.getText().toString());
			break;
		case 3:
			lnCoffe.setBackgroundResource(R.drawable.bg_tab_active);
			lnWine.setBackgroundResource(R.drawable.bg_tab_normal);
			lnCutlery.setBackgroundResource(R.drawable.bg_tab_normal);
//			lnWine.setBackgroundResource(R.drawable.icon_cat_wine);
//			lnCutlery.setBackgroundResource(R.drawable.icon_cat_cutlery);
			cateId = "2";
			exeSearch(etTextSearch.getText().toString());
			break;

		default:
			break;
		}
		
	}
/*
 * Clicked Listener
 * 
 */
	public void onCutleryTabCliked(View v){
		resetTabBarFocus(1);
	}
	
	public void onWineTabCliked(View v){
		resetTabBarFocus(2);
	}
	
	public void onCoffeTabClicked(View v){
		resetTabBarFocus(3);
	}
	private final TextWatcher mTextEditorWatcher = new TextWatcher() {
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// This sets a textview to the current length

			try {
				String text = s.toString();
				Log.d("Text serch","Text "+text);
				if(text.equals(""))
				{
					isSearch = false;
					exeListActivity(false);
				}else{
					isSearch = true;
					exeSearch(text);
				}
			
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		public void afterTextChanged(Editable s) {
		}
	};
	
	private void hideProgress() {
		// TODO Auto-generated method stub
    	if(progressBar.getVisibility() == View.VISIBLE){
    		progressBar.setVisibility(View.GONE);
    	}
//		if (dialog != null && dialog.isShowing()) {
//			dialog.dismiss();
//		}
	}
	private void showProgress() {
		// TODO Auto-generated method stub
    	if(progressBar.getVisibility() != View.VISIBLE){
    		progressBar.setVisibility(View.VISIBLE);
    	}
//		if (dialog != null && !dialog.isShowing()) {
//			dialog.show();
//		}
	}
	public void onFilterClicked(View v){
		if(lnFilter.getVisibility()==View.VISIBLE){
			hideFilterView();
		}else{
			showFilterView();
		}
		
	}
	public void toggle(View v)
	{
		int id =v.getId();
		switch (id) {
		case R.id.rlViewAll:
			initCheckAll();
			break;
		case R.id.rlFavourite:
			initCheckFavourite();
			break;
		case R.id.rlVisited:
			initCheckVisited();
			break;
			
		default:
			break;
		}

	}
	private void initCheckAll() {
		// TODO Auto-generated method stub
		cateId = "0";
		cktViewAll.setTextColor(Color.parseColor("#805504"));	
		cktFriendFavourtie.setTextColor(getResources().getColor(R.color.white));
		cktFriednVised.setTextColor(getResources().getColor(R.color.white));
		imgCheckedFavorite.setVisibility(View.INVISIBLE);
		imgCheckedVisited.setVisibility(View.INVISIBLE);
		imgCheckedAll.setVisibility(View.VISIBLE);
	}

	private void initCheckFavourite() {
		// TODO Auto-generated method stub
		cateId = "1";
		cktFriendFavourtie.setTextColor(Color.parseColor("#805504"));
		cktViewAll.setTextColor(getResources().getColor(R.color.white));
		cktFriednVised.setTextColor(getResources().getColor(R.color.white));
		
		imgCheckedAll.setVisibility(View.INVISIBLE);
		imgCheckedVisited.setVisibility(View.INVISIBLE);
		imgCheckedFavorite.setVisibility(View.VISIBLE);
	}

	private void initCheckVisited() {
		// TODO Auto-generated method stub
		cateId = "2";
		cktFriednVised.setTextColor(Color.parseColor("#805504"));
		cktViewAll.setTextColor(getResources().getColor(R.color.white));
		cktFriendFavourtie.setTextColor(getResources().getColor(R.color.white));
		
		imgCheckedAll.setVisibility(View.INVISIBLE);
		imgCheckedFavorite.setVisibility(View.INVISIBLE);
		imgCheckedVisited.setVisibility(View.VISIBLE);
	}

	private void showFilterView() {
		// TODO Auto-generated method stub
		lnFilter.setVisibility(View.VISIBLE);
	}

	private void hideFilterView() {
		// TODO Auto-generated method stub
		lnFilter.setVisibility(View.GONE);
	}
}
