package whyq.activity;

import java.util.ArrayList;

import whyq.PermpingMain;
import whyq.adapter.CategoryAdapter;
import whyq.controller.CategoryController;
import whyq.interfaces.get_category_delegate;
import whyq.model.Category;
import whyq.utils.API;
import whyq.utils.PermUtils;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.whyq.R;

public class ExplorerActivity extends Activity implements get_category_delegate {
//	public ProgressDialog loadingDialog;
//	private ProgressBar progressBar;
	public Context context;
	public CategoryController catController;
	public ListView categoriesView;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.explorer_layout);
		
		TextView textView = (TextView)findViewById(R.id.permpingTitle);
		Typeface tf = Typeface.createFromAsset(getAssets(), "ufonts.com_franklin-gothic-demi-cond-2.ttf");
		if(textView != null) {
			textView.setTypeface(tf);
		}
//		progressBar = (ProgressBar)findViewById(R.id.progressBar);
		showLoadingDialog("Loading", "Please wait...");
		categoriesView = (ListView) findViewById(R.id.categories);
		final CategoryController catController = new CategoryController(ExplorerActivity.this);
		final ArrayList<Category> categories = catController.getCategoryList();
		context = ExplorerActivity.this;
		PermUtils.clearViewHistory();
		
		/*Button btnAllCategory = (Button)findViewById(R.id.btnAllCategory);
		btnAllCategory.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent myIntent = new Intent(v.getContext(), FollowerActivity.class);
				myIntent.putExtra("allcategory", "allcategory");
				View boardListView = ExplorerActivityGroup.group.getLocalActivityManager() .startActivity("AllCatelogy", myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
				ExplorerActivityGroup.group.replaceView(boardListView);
			}
		});*/
	}
	private void showLoadingDialog(String title, String msg) {
//		loadingDialog = new ProgressDialog(getParent());
//		loadingDialog.setMessage(msg);
//		loadingDialog.setTitle(title);
//		loadingDialog.setCancelable(true );
//		this.loadingDialog.show();
//		progressBar.setVisibility(View.VISIBLE);
	}

	private void dismissLoadingDialog() {
//		if (loadingDialog != null)
//			if(loadingDialog.isShowing())
//			loadingDialog.dismiss();
//		if(progressBar.getVisibility()==View.VISIBLE){
//			progressBar.setVisibility(View.GONE);
//		}
	}

	@Override
	public void onCompletedGetCategory(final ArrayList<Category> categories) {
		// TODO Auto-generated method stub
		CategoryAdapter categoriesAdapter = null;
		if(categories != null)
			categoriesAdapter = new CategoryAdapter(context, R.layout.category_item, categories);
		categoriesView.setAdapter(categoriesAdapter);
		categoriesView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent myIntent = new Intent(view.getContext(), FollowerActivity.class);
				if(position == 0) {
					myIntent.putExtra("allcategory", "allcategory");
					View boardListView = ExplorerActivityGroup.group.getLocalActivityManager() .startActivity("AllCatelogy", myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
					ExplorerActivityGroup.group.replaceView(boardListView);
				} else {				
					
					Category cat = categories.get(position);
					String categoryUrl = API.permListFromCategory + cat.getId();
					myIntent.putExtra("categoryURL", categoryUrl);
					View boardListView = ExplorerActivityGroup.group.getLocalActivityManager() .startActivity("BoardListActivity", myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
					ExplorerActivityGroup.group.replaceView(boardListView);
				}
			}
		});
		dismissLoadingDialog();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{		
	    if ((keyCode == KeyEvent.KEYCODE_BACK))
	    {
	        PermpingMain.back();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}

}
