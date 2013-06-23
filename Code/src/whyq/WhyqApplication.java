/**
 * 
 */
package whyq;

import whyq.model.User;
import whyq.utils.RSA;
import whyq.utils.Util;
import whyq.utils.XMLParser;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;


/**
 * @author Linh Nguyen
 * This class is to store the global information (data objects, variable)
 * during the Permping application is running
 */
public class WhyqApplication extends Application {
	/**
	 * The perm user stored in application context
	 */
	private User user;
	public static final float RATIO_BASE_HEIGHT = 0.08f;
	public static final String ACCESS_TOKEN = "access_token";
	public static final String REFESH_TOKEN = "refresh_token";
	public static final String TOKEN_EXPIRED_TIME = "expires_in";
	public static String DEVICE_ID = "noID";
	public static final String DISK_CACHE_DIR = "images";
	public static int sScreenWidth = 0;
	public static int sScreenHeight;
	public static int sBaseViewHeight;
	public static int sBaseViewPadding;
	public static Typeface sTypeface;

	/**
	 * The current login type 
	 */
	private String loginType;

	public static WhyqApplication _instance;
	
	public WhyqApplication() {
		super();
		_instance = this;

	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		sTypeface = Typeface.createFromAsset(getAssets(), "ufonts.com_franklin-gothic-demi-cond-2.ttf");
	}
	
	public static void initScreenSize(int width, int height) {
		if (sScreenWidth == 0) {
			sScreenHeight = height;
			sScreenWidth = width;
			sBaseViewHeight = (int) (sScreenHeight * RATIO_BASE_HEIGHT);
		}
	}

	public static WhyqApplication Instance() {
		return _instance;
	}
	/*
	 * Current screen info
	 */
	public static String getRSAToken(){
		
		try {
			RSA rsa = new RSA();
			String token = rsa.RSAEncrypt(XMLParser.getToken(Instance().getApplicationContext()));
			return token;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
	public String getToken() {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		long lastSaved = pref.getLong("token_save_at", 0);
		int expireTime = pref.getInt(TOKEN_EXPIRED_TIME, 3600);
		if ((System.currentTimeMillis() - lastSaved) / 1000 < expireTime) {
			return pref.getString(ACCESS_TOKEN, null);
		} else {
			Editor editor = pref.edit();
			editor.remove(ACCESS_TOKEN);
			editor.remove(TOKEN_EXPIRED_TIME);
			editor.remove("token_save_at");
			editor.commit();
			return null;
		}
	}
	
	private DisplayMetrics metrics;
	
	public void setDisplayMetrics( DisplayMetrics metrics ){
		this.metrics = metrics;
	}
	
	public DisplayMetrics getDisplayMetrics(){
		return this.metrics;
	}
	
	/**
	 * @return the user
	 */
	public User getUser() {
		//return new User("121");
		return user;
		
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the loginType
	 */
	public String getLoginType() {
		return loginType;
	}

	/**
	 * @param loginType the loginType to set
	 */
	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}
	public String getDeviceID() {
		// TelephonyManager tManager = (TelephonyManager)
		// this.getSystemService(Context.TELEPHONY_SERVICE);
		if (Util.generateDeviceId() != null)
			DEVICE_ID = Util.generateDeviceId();
		return DEVICE_ID;
	}
	/*
	
	private static Context context;
	public void onCreate(){
		PermpingApplication.context = getApplicationContext();
	}
	
	public static Context getAppContext(){
		return PermpingApplication.context;
	}
	
	*/
}