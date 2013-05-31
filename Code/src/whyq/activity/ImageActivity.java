package whyq.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import whyq.WhyqMain;
import whyq.model.User;
import whyq.utils.WhyqUtils;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whyq.R;

public class ImageActivity extends Activity {
	private int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1224;
	
	private int SELECT_PICTURE = 1;
	public static String imagePath="";
	public static String strCurDate="";
	public static Uri mCapturedImageURI;
	//private ProgressDialog dialog;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_layout);
		
		TextView textView = (TextView)findViewById(R.id.permpingTitle);
		Typeface tf = Typeface.createFromAsset(getAssets(), "ufonts.com_franklin-gothic-demi-cond-2.ttf");
		if(textView != null) {
			textView.setTypeface(tf);
		}
		
		final LinearLayout takePhoto = (LinearLayout) findViewById(R.id.takePhoto);
		takePhoto.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				User user = WhyqUtils.isAuthenticated(getApplicationContext());
				if (user != null) {
					// define the file-name to save photo taken by Camera activity
					showCamera();
				} else {

					WhyqMain.showLogin();
				}
			}
		});
		
		
		
		final LinearLayout openGalerry = (LinearLayout) findViewById(R.id.gallery);
		openGalerry.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				   // select a file
				User user = WhyqUtils.isAuthenticated(getApplicationContext());
				if (user != null) {
					Intent intent = new Intent();
	                intent.setType("image/*");
	                intent.setAction(Intent.ACTION_GET_CONTENT);
	                getParent().startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
				} else {
					// Go to login screen
//					Intent i = new Intent(v.getContext(), LoginPermActivity.class);
//					v.getContext().startActivity(i);
					WhyqMain.showLogin();
				}
                
			}
		});
		
		final LinearLayout createBoard = (LinearLayout) findViewById(R.id.createBoard);
		createBoard.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				User user = WhyqUtils.isAuthenticated(getApplicationContext());
				if (user != null) {
					// Go to the Create Board screen.
					Intent i = new Intent(v.getContext(), CreateBoardActivity.class);
					View view = ImageActivityGroup.group.getLocalActivityManager().startActivity("CreateBoardActivity", i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
					ImageActivityGroup.group.replaceView(view);
				} else {
					// Go to login screen
//					Intent i = new Intent(v.getContext(), LoginPermActivity.class);
//					v.getContext().startActivity(i);
					WhyqMain.showLogin();
				}
				
			}
		});
		
		WhyqUtils.clearViewHistory();

	}

	public void showCamera(){
//		try {
//
//	        String fileName = "temp.jpg";  
//	        ContentValues values = new ContentValues();  
////	        values.put(MediaStore.Images.Media.TITLE, fileName);  
//	        mCapturedImageURI = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);  
//
//	        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  
//	        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);  
//			Context context = ImageActivity.this;
//			PackageManager packageManager = context.getPackageManager();
//	 
//			// if device support camera?
//			if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
//				 getParent().startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
//			}
//	       
//	  
//	        
//		} catch (Exception e) {
//			// TODO: handle exception
//			Logger.appendLog(e.toString(), "takePhotoLog");
//		}
		try {
			FileOutputStream fos = openFileOutput("MyFile.jpg", Context.MODE_WORLD_WRITEABLE);
			fos.close();
			File f = new File(getFilesDir() + File.separator + "MyFile.jpg");
			if(f != null){
				imagePath = f.getPath();
				Uri uri = Uri.fromFile(f);
				if(imagePath != null && uri != null){
					getParent().startActivityForResult(
					        new Intent(MediaStore.ACTION_IMAGE_CAPTURE)
					            .putExtra(MediaStore.EXTRA_OUTPUT, uri)
					        , CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
				}
			}
			}
			catch(IOException e) {

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
}
