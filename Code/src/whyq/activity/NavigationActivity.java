package whyq.activity;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import whyq.WhyqApplication;
import whyq.utils.RSA;
import whyq.utils.XMLParser;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
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

	private void initScreenSize() {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		WhyqApplication.sScreenHeight = displaymetrics.heightPixels;
		WhyqApplication.sScreenWidth = displaymetrics.widthPixels;
		WhyqApplication.sBaseViewHeight = displaymetrics.heightPixels / 10;
	}

	protected String getToken() {
		String token = XMLParser.getToken(this);
		RSA rsa = new RSA();
		try {
			Log.d("NavigationActivity", "token: " + token);
			return rsa.RSAEncrypt(token);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
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

	protected void setTitleView(View view) {
		FrameLayout titleContainer = (FrameLayout) findViewById(R.id.titleContainer);
		titleContainer.removeViewAt(0);
		if (view != null) {
			titleContainer.addView(view);
		}
	}

	protected void onExtraButtonPressed() {

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

}
