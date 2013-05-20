package whyq.activity;

import java.util.ArrayList;
import java.util.List;

import whyq.PermpingMain;
import whyq.utils.PermUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.whyq.R;

public class GoogleMapActivity extends MapActivity {
	private ArrayList<Drawable> layers = new ArrayList<Drawable>();
	private List<Overlay> mapOverlays;
	public static String url = "http://www.webappers.com/img/2007/11/vector-icons.png";
	private String url2 = "http://www.permping.com/public/pinimages/1341337327338.jpg";
	private OpItemizedOverlay itemizedoverlay;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_view);
		
		TextView textView = (TextView)findViewById(R.id.permpingTitle);
		Typeface tf = Typeface.createFromAsset(getAssets(), "ufonts.com_franklin-gothic-demi-cond-2.ttf");
		if(textView != null) {
			textView.setTypeface(tf);
		}
		
		Bundle bundle = getIntent().getBundleExtra("locationData");

		final MapView mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		if(bundle != null){
			float lat = bundle.getFloat("lat");
			float lon = bundle.getFloat("lon");
			final String url = bundle.getString("thumbnail");
			final GeoPoint point2 = new GeoPoint((int)(lat * 1E6), (int)(lon * 1E6));
			Thread thread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					addOverlayToMap(mapView, mapOverlays, layers, point2, url);
				}
			});
			thread.start();

		}
	}

	private void addOverlayToMap(MapView mapView, List<Overlay> mapOverlays2,
			ArrayList<Drawable> layers2, GeoPoint point, String url) {
		// TODO Auto-generated method stub
		mapOverlays = mapView.getOverlays();
		Drawable[] layers = new Drawable[2];
		Bitmap bitmap = null;
		BitmapFactory.Options bmOptions;
		bmOptions = new BitmapFactory.Options();
		bmOptions.inSampleSize = 1;
		bitmap = PermUtils.LoadImage(url, bmOptions);
		Bitmap bubleBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.bubble);
		if (bitmap != null)
			bitmap = Bitmap.createScaledBitmap(bitmap, 80, 60, false);
		if (bubleBitmap != null)
			bubleBitmap = Bitmap.createScaledBitmap(bubleBitmap, 30, 60, false);
		layers[0] = new BitmapDrawable(bitmap);
		layers[1] = new BitmapDrawable(bubleBitmap);
		LayerDrawable layerDrawable = new LayerDrawable(layers);
		layerDrawable.setLayerInset(0, -70, -160, -70, 50);
		itemizedoverlay = new OpItemizedOverlay(
				layerDrawable, this);
		OverlayItem overlayitem = new OverlayItem(point,
				"Location!", "Location!");
		itemizedoverlay.addOverlay(overlayitem);
		mapOverlays.add(itemizedoverlay);
	}
	public void addOneOverlayToMap( String url, GeoPoint point, String title, String snipet){
		Drawable[] layers = new Drawable[2];
		Bitmap bitmap = null;
		BitmapFactory.Options bmOptions;
		bmOptions = new BitmapFactory.Options();
		bmOptions.inSampleSize = 1;
		bitmap = PermUtils.LoadImage(url, bmOptions);
		Bitmap bubleBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.bubble);
		if (bitmap != null)
			bitmap = Bitmap.createScaledBitmap(bitmap, 80, 60, false);
		if (bubleBitmap != null)
			bubleBitmap = Bitmap.createScaledBitmap(bubleBitmap, 30, 60, false);
		layers[0] = new BitmapDrawable(bitmap);
		layers[1] = new BitmapDrawable(bubleBitmap);
		LayerDrawable layerDrawable = new LayerDrawable(layers);
		layerDrawable.setLayerInset(0, -70, -160, -70, 50);
		OverlayItem overlayitem = new OverlayItem(point,
				title, snipet);
		itemizedoverlay = new OpItemizedOverlay(
				layerDrawable, this);
		itemizedoverlay.addOverlay(overlayitem);
		mapOverlays.add(itemizedoverlay);
	}
	@Override
	protected boolean isRouteDisplayed() {
		return false;
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