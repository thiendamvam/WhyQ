package whyq.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import whyq.WhyqApplication;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whyq.R;

public class Util {

	/**
	 * @Description: check to see whether email address is valid or not
	 * 
	 * @Return boolean
	 */
	public static boolean isValidEmail(String inputMail) {   
		
		if(inputMail == null || inputMail == ""){
			return false;
		}
	    Pattern pattern= Pattern.compile("^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\\.([a-zA-Z])+([a-zA-Z])+");
	    return pattern.matcher(inputMail).matches();
	}
	
	public static String convertStreamToString(InputStream is) {
		/*
		 * To convert the InputStream to String we use the Reader.read(char[]
		 * buffer) method. We iterate until the Reader return -1 which means
		 * there's no more data to read. We use the StringWriter class to
		 * produce the string.
		 */

		if (is != null) {
			Writer writer = new StringWriter();

			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(is,
						"UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} catch (IOException e) {
				return "";
			} finally {
				try {
					is.close();
				} catch (IOException e) {

				}
			}
			return writer.toString();
		} else {
			return "";
		}
	}
	
	public static String getFileName(String path) {
		String imageFileName = path.substring(path.lastIndexOf('/') + 1);
		return imageFileName;
	}
	
	public static void writeToFile(String rawData, String path, String fileName) throws IOException{
		File filePath = new File(path);
		filePath.mkdirs();
		File file = new File(path, fileName);
		BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
		writer.write(rawData);
		writer.flush();
		writer.close();
	}
	public static boolean isNetworkAvailable() {
		WhyqApplication mobionApp = WhyqApplication.Instance();
		android.net.ConnectivityManager connMgr = (android.net.ConnectivityManager) mobionApp
				.getApplicationContext().getSystemService(
						Context.CONNECTIVITY_SERVICE);
		android.net.NetworkInfo info = connMgr.getActiveNetworkInfo();
		return (info != null && info.isConnected());
	}
	public static void showNetworkError(Context context) {
		android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
		builder.setTitle(context.getString(R.string.AlertTitleWebExtLoadError));
		builder.setMessage(context.getString(R.string.AlertMsgWebExtLoadError));
		final android.app.AlertDialog alertError = builder.create();
		alertError.setButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				alertError.dismiss();
			}
		});
		alertError.show();
	}
	static int clearCacheFolder(final File dir, final int numDays) {

	    int deletedFiles = 0;
	    if (dir!= null && dir.isDirectory()) {
	        try {
	            for (File child:dir.listFiles()) {

	                //first delete subdirectories recursively
	                if (child.isDirectory()) {
	                    deletedFiles += clearCacheFolder(child, numDays);
	                }

	                //then delete the files and subdirectories in this dir
	                //only empty directories can be deleted, so subdirs have been done first
	                if (child.lastModified() < new Date().getTime() - numDays * DateUtils.DAY_IN_MILLIS) {
	                    if (child.delete()) {
	                        deletedFiles++;
	                    }
	                }
	            }
	        }
	        catch(Exception e) {
//	            Log.e(TAG, String.format("Failed to clean the cache, error %s", e.getMessage()));
	        }
	    }
	    return deletedFiles;
	}

	static public boolean checkInternetConnection(){
		final ConnectivityManager conMgr =  (ConnectivityManager) WhyqApplication.Instance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
		if (activeNetwork != null && activeNetwork.isConnected()) {
		    //notify user you are online
			return true;
		} else {
		    //notify user you are not online
			return false;
		} 
	}
	public static String generateDeviceId() {
		final String macAddr, androidId;

		WifiManager wifiMan = (WifiManager) WhyqApplication.Instance().getApplicationContext()
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInf = wifiMan.getConnectionInfo();

		macAddr = wifiInf.getMacAddress();
		androidId = ""
				+ android.provider.Settings.Secure.getString(
						 WhyqApplication.Instance().getApplicationContext().getContentResolver(),
						android.provider.Settings.Secure.ANDROID_ID);

		UUID deviceUuid = new UUID(androidId.hashCode(), macAddr.hashCode());
		return deviceUuid.toString();

		// Maybe save this: deviceUuid.toString()); to the preferences.
	}
	
	public static String encryptToken(String token) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		RSA rsa = new RSA();
		return rsa.RSAEncrypt(token);
	}
	
	public static String decryptToken(String token) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		RSA rsa = new RSA();
		return rsa.RSADecrypt(token);
	}
	
	public static void applyTypeface(View view, Typeface tf) {
		if (view instanceof TextView) {
	        ((TextView) view).setTypeface(tf);
		} else if (view instanceof ViewGroup) {
			ViewGroup group = (ViewGroup) view;
			for (int i = 0, count = group.getChildCount(); i < count; i++) {
				applyTypeface(group.getChildAt(i), tf);
			}
		}
	}


}
