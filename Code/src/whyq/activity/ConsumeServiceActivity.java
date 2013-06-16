package whyq.activity;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import whyq.WhyqApplication;
import whyq.interfaces.IServiceListener;
import whyq.service.Service;
import whyq.service.ServiceResponse;
import whyq.utils.Util;
import whyq.utils.XMLParser;
import android.util.Log;

public class ConsumeServiceActivity extends NavigationActivity implements IServiceListener {

	private static final String TAG = ConsumeServiceActivity.class.getSimpleName();

	@Override
	public void onCompleted(Service service, ServiceResponse result) {
		
	}
	
	protected Service getService() {
		return new Service(this);
	}
	
	protected String getEncryptedToken() {
		try {
			final String token = XMLParser.getToken(WhyqApplication._instance
					.getApplicationContext());
			Log.d(TAG, "token: " + token);
			return Util.encryptToken(token);
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
		return "";
	}

}
