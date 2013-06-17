package whyq.activity;

import whyq.WhyqApplication;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.whyq.R;

public class NavigationActivity extends FragmentActivity {

	private TextView textviewTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_navigation);

		initScreenSize();

		FrameLayout titleContainer = (FrameLayout) findViewById(R.id.titleContainer);
		titleContainer.addView(getTitleView());

		textviewTitle = (TextView) findViewById(R.id.textviewTitle);

		View navigationBar = findViewById(R.id.navigationBar);
		navigationBar.getLayoutParams().height = WhyqApplication.sBaseViewHeight;

		ViewGroup.LayoutParams progressBarLP = findViewById(R.id.progressBar)
				.getLayoutParams();
		progressBarLP.width = WhyqApplication.sBaseViewHeight * 4 / 5;
		progressBarLP.height = WhyqApplication.sBaseViewHeight * 4 / 5;

		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		findViewById(R.id.extraContainer).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						onExtraButtonPressed();
					}
				});

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
		WhyqApplication.sScreenHeight = displaymetrics.heightPixels;
		WhyqApplication.sScreenWidth = displaymetrics.widthPixels;
		WhyqApplication.sBaseViewHeight = displaymetrics.heightPixels / 10;
	}

	protected void setExtraView(View view) {
		FrameLayout extraContainer = (FrameLayout) findViewById(R.id.extraContainer);
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
