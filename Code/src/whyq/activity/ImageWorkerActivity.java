package whyq.activity;

import com.whyq.R;

import whyq.WhyqApplication;
import whyq.utils.ImageWorker;
import android.os.Bundle;

public class ImageWorkerActivity extends ConsumeServiceActivity {

	protected ImageWorker mImageWorker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mImageWorker = new ImageWorker(this);
		mImageWorker.initCache(this, WhyqApplication.DISK_CACHE_DIR, 0.25f);
		mImageWorker.setLoadingImage(R.drawable.icon);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mImageWorker.setExitTasksEarly(false);
	}

	@Override
	protected void onStop() {
		super.onStop();
		mImageWorker.setExitTasksEarly(true);
	}

}
