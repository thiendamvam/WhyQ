package whyq.activity;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import android.util.Log;

import whyq.WhyqApplication;
import whyq.interfaces.IServiceListener;
import whyq.service.Service;
import whyq.service.ServiceResponse;
import whyq.utils.Logger;
import whyq.utils.Util;
import whyq.utils.XMLParser;

public class ConsumeServiceActivity extends NavigationActivity implements IServiceListener {

	@Override
	public void onCompleted(Service service, ServiceResponse result) {
		
	}
	
	protected Service getService() {
		return new Service(this);
	}
	
	protected String getEncryptedToken() {
		try {
			String token = XMLParser.getToken(WhyqApplication._instance
					.getApplicationContext());
			token = Util.encryptToken(token);
			Log.d("token", token);
			return token;
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
