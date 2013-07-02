package whyq.activity;

import whyq.WhyqApplication;
import whyq.utils.Util;
import whyq.view.SearchField;
import whyq.view.SearchField.QueryCallback;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.whyq.R;

public class NavigationActivity extends FragmentActivity implements
		QueryCallback {

	private TextView textviewTitle;
	private SearchField mSearchField;
	private FrameLayout mTitleContainer;
	private ImageView mButtonBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_navigation);

		initScreenSize();

		mTitleContainer = (FrameLayout) findViewById(R.id.titleContainer);
		mTitleContainer.addView(getTitleView());

		mSearchField = (SearchField) findViewById(R.id.searchFieldHeader);
		mSearchField.getLayoutParams().height = WhyqApplication.sBaseViewHeight;

		mSearchField.setQueryCallback(this);

		textviewTitle = (TextView) findViewById(R.id.tvHeaderTitle);

		View navigationBar = findViewById(R.id.navigationBar);
		navigationBar.getLayoutParams().height = WhyqApplication.sBaseViewHeight;

		ViewGroup.LayoutParams progressBarLP = findViewById(R.id.progressBar)
				.getLayoutParams();
		progressBarLP.width = WhyqApplication.sBaseViewHeight * 3 / 5;
		progressBarLP.height = WhyqApplication.sBaseViewHeight * 3 / 5;

		mButtonBack = (ImageView) findViewById(R.id.buttonBack);
		View buttonBackContainer = findViewById(R.id.buttonBackContainer);
		buttonBackContainer.getLayoutParams().width = WhyqApplication.sBaseViewHeight;
		buttonBackContainer.getLayoutParams().height = WhyqApplication.sBaseViewHeight;
		buttonBackContainer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		findViewById(R.id.buttonExtra).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						onExtraButtonPressed();
					}
				});

	}

	protected void showHeaderSearchField(boolean show) {
		if (show) {
			mSearchField.setVisibility(View.VISIBLE);
			mTitleContainer.setVisibility(View.INVISIBLE);
		} else {
			mTitleContainer.setVisibility(View.VISIBLE);
			mSearchField.setVisibility(View.GONE);
		}
	}

	protected void setBackButtonIcon(int resId) {
		mButtonBack.setImageResource(resId);
	}

	@Override
	public void onQuery(String queryString) {
	}

	@Override
	protected void onResume() {
		super.onResume();
		Util.applyTypeface(findViewById(R.id.navigationContentView),
				WhyqApplication.sTypefaceRegular);
	}

	@Override
	public void setContentView(int layoutResID) {
		if (layoutResID == R.layout.activity_navigation) {
			super.setContentView(layoutResID);
		} else {
			((ViewGroup) findViewById(R.id.contentView))
					.addView(getLayoutInflater().inflate(layoutResID, null));
		}
	}

	@Override
	public void setContentView(View view) {
		((ViewGroup) findViewById(R.id.contentView)).addView(view);
	}

	@Override
	public void setContentView(View view, LayoutParams params) {
		((ViewGroup) findViewById(R.id.contentView)).addView(view, params);
	}

	private void initScreenSize() {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		WhyqApplication.initScreenSize(displaymetrics.widthPixels,
				displaymetrics.heightPixels);
	}

	protected void setExtraView(View view) {
		FrameLayout extraContainer = (FrameLayout) findViewById(R.id.buttonExtra);
		extraContainer.addView(view, new FrameLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}

	protected void setLoading(boolean show) {
		if (show) {
			findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
		}
	}

	protected void showExtraButton() {
		findViewById(R.id.extraContainer).setVisibility(View.VISIBLE);
	}

	protected void hideExtraButton() {
		findViewById(R.id.extraContainer).setVisibility(View.INVISIBLE);
	}

	@Override
	public void setTitle(int titleId) {
		if (textviewTitle != null) {
			textviewTitle.setText(titleId);
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		if (textviewTitle != null) {
			textviewTitle.setText(title);
		}
	}

	protected void onExtraButtonPressed() {

	}

	protected View getTitleView() {
		return getLayoutInflater()
				.inflate(R.layout.navigation_title_text, null);
	}

}
