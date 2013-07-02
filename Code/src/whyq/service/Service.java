package whyq.service;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import whyq.WhyqApplication;
import whyq.interfaces.IServiceListener;
import whyq.model.SearchFriendCriteria;
import whyq.utils.API;
import whyq.utils.Util;
import whyq.utils.XMLParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class Service implements Runnable {

	private static final String TAG = Service.class.getSimpleName();
	private HttpURLConnection _connection;
	private ServiceAction _action;
	private ArrayList<IServiceListener> _listener;
	private boolean _connecting;
	private Thread _thread;
	private String _actionURI;
	private Map<String, String> _params;
	private boolean _includeHost;
	private boolean _isGet;
	private Service _service;
	private boolean _isBitmap;
	private HttpClient httpclient;

	public void getProductList() {
		_action = ServiceAction.ActionGetRetaurentList;
		Map<String, String> params = new HashMap<String, String>();
		params.put("deviceType", "kindle-fire");
		request("/v1/shop/purchase", params, true, false);
	}

	public void logout() {
		// TODO Auto-generated method stub
		_action = ServiceAction.ActionLogout;
		Map<String, String> params = new HashMap<String, String>();
		params.put("token", XMLParser.getToken(WhyqApplication.Instance()
				.getApplicationContext()));
		request("/m/logout", params, true, false);
	}

	public void getComments(String encryptedToken, String store_id, int page,
			int count) {
		_action = ServiceAction.ActionGetComment;
		Map<String, String> params = new HashMap<String, String>();
		params.put("token", encryptedToken);
		params.put("store_id", store_id);
		params.put("page", String.valueOf(page));
		params.put("count", String.valueOf(count));
		request("/m/member/comment", params, true, false);
	}

	public void getFriends(String token, String user_id) {
		_action = ServiceAction.ActionGetFriends;
		Map<String, String> params = new HashMap<String, String>();
		params.put("token", token);
		if (user_id != null) {
			params.put("user_id", user_id);
		}
		request("/m/member/friend", params, true, false);
	}

	public void getFriendsFacebook(String encryptedToken, String accessToken) {
		_action = ServiceAction.ActionGetFriendsFacebook;
		Map<String, String> params = new HashMap<String, String>();
		params.put("token", encryptedToken);
		params.put("access_token", accessToken);
		request("/m/member/friend/facebook", params, true, false);
	}

	public void searchFriends(SearchFriendCriteria criteria,
			String encryptedToken, String key, String accessToken,
			String oauth_token, String oauth_token_secret) {
		_action = ServiceAction.ActionSearchFriendsFacebook;
		Map<String, String> params = new HashMap<String, String>();
		params.put("token", encryptedToken);
		params.put("key", key);
		params.put("search", criteria.toString());
		switch (criteria) {
		case facebook:
			params.put("access_token", accessToken);
			break;
		case twitter:
			params.put("oauth_token", oauth_token);
			params.put("oauth_token_secret", oauth_token_secret);
			break;
		}
		request("/m/member/search/friend", params, true, false);
	}

	public void inviteFriendsFacebook(String encryptedToken, String userId,
			String accessToken) {
		_action = ServiceAction.ActionInviteFriendsFacebook;
		Map<String, String> params = new HashMap<String, String>();
		params.put("token", encryptedToken);
		params.put("user_id", userId);
		params.put("access_token", accessToken);
		request("/m/member/friend/facebook/invite", params, true, false);
	}

	public void getUserActivities(String encryptedToken, String userId) {
		_action = ServiceAction.ActionGetUserActivities;
		Map<String, String> params = new HashMap<String, String>();
		params.put("token", encryptedToken);
		params.put("user_id", userId);
		request("/m/member/recent/activity", params, true, false);
	}

	public void postComment(String encryptedToken, String content,
			String photoFile) {
		_action = ServiceAction.ActionPostComment;
		Map<String, String> params = new HashMap<String, String>();
		params.put("token", encryptedToken);
		params.put("content", content);
		params.put("photo", photoFile);
		request("/m/member/comment/post", params, true, false);
	}

	final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			for (IServiceListener listener : _listener) {
				if (listener != null)
					listener.onCompleted(_service, (ServiceResponse) msg.obj);
			}

		}
	};

	public Service(IServiceListener... listeners) {
		_action = ServiceAction.ActionNone;
		_listener = new ArrayList<IServiceListener>();
		if (listeners != null)
			for (IServiceListener listener : listeners) {
				_listener.add(listener);
			}
		_connecting = false;
		_includeHost = true;
		_service = this;
		_isBitmap = false;
	}

	public void addListener(IServiceListener listener) {
		_listener.add(listener);
	}

	public boolean isConnecting() {
		return _connecting;
	}

	public void stop() {
		// cleanUp();
		if (httpclient != null)
			httpclient.getConnectionManager().shutdown();
		_action = ServiceAction.ActionNone;
		_connecting = false;
	}

	private boolean request(String uri, Map<String, String> params,
			boolean includeHost, boolean isGet) {
		_isGet = isGet;
		request(uri, params, includeHost);
		return true;
	}

	private boolean request(String uri, Map<String, String> params,
			boolean includeHost) {
		if (_connecting)
			return false;
		_connecting = true;
		_actionURI = uri;
		_params = params;
		_includeHost = includeHost;
		_thread = new Thread(this);
		_thread.start();
		return true;
	}

	private void processError(ResultCode errorCode) {
		// if (_listener == null || _action == ServiceAction.ActionNone
		// || !_connecting)
		// return;
		Message msg = handler.obtainMessage(0, new ServiceResponse(_action,
				null, errorCode));
		handler.sendMessage(msg);
	}

	private void dispatchResult(String result) {
		if (_listener == null || _action == ServiceAction.ActionNone
				|| !_connecting)
			return;

		ServiceAction act = _action;
		Object resObj = null;
		ServiceResponse response = null;
		DataParser parser = new DataParser();
		switch (act) {
		// TODO
		case ActionNone:
			break;
		case ActionGetRetaurentList:
			resObj = parser.parseRetaurentList();
			break;
		case ActionLogout:
			resObj = parser.parseLogout();
			break;
		case ActionLoginFacebook:
			resObj = parser.parserLoginData(result);
		case ActionLoginTwitter:
			resObj = parser.parserLoginData(result);
			break;
		case ActionGetBusinessDetail:
			resObj = parser.parseBusinessDetail(result);
			break;
		case ActionComment:
			resObj = parser.parseCommentResult(result);
			break;
		case ActionGetLocation:
			resObj = parser.parseLCationResult(result);
			break;
		default:
			resObj = result;
			break;
		}
		if (resObj == null)
			response = new ServiceResponse(act, null, ResultCode.Failed);
		else
			response = new ServiceResponse(act, resObj);
		stop();
		Message msg = handler.obtainMessage(0, response);
		handler.sendMessage(msg);
	}

	private void dispatchResult(Bitmap result) {
		if (_listener == null || _action == ServiceAction.ActionNone
				|| !_connecting)
			return;
		ServiceAction act = _action;
		ServiceResponse response = null;
		if (result == null)
			response = new ServiceResponse(act, null, ResultCode.Failed);
		else
			response = new ServiceResponse(act, result);
		stop();
		Message msg = handler.obtainMessage(0, response);
		handler.sendMessage(msg);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		Log.d(_action.toString(), "=========== run ===========");
		httpclient = new DefaultHttpClient();
		// AndroidHttpClient httpclient = AndroidHttpClient
		// .newInstance(_actionURI);
		HttpParams httpParameters = httpclient.getParams();
		// HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, 30000);
		HttpConnectionParams.setSoTimeout(httpParameters, 600000);
		HttpConnectionParams.setTcpNoDelay(httpParameters, true);
		try {
			final String urlString;
			if (_action == ServiceAction.ActionGetLocation) {
				String textSearch = _params.get("text-search");
				textSearch = textSearch.replace(" ", "+");
				urlString = "http://ws.geonames.org/search?q=" + textSearch
						+ "&style=full&maxRows=10";
			} else
				urlString = _includeHost ? API.hostURL + _actionURI
						: _actionURI;
			HttpRequestBase request = null;

			if (_isGet) {
				request = new HttpGet();
				if (_params != null) {
					attachUriWithQuery(request, Uri.parse(urlString), _params);
				}
			} else {
				request = new HttpPost(urlString);
				if (_params != null) {
					UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
							paramsToList(_params), HTTP.UTF_8);
					((HttpPost) request).setEntity(formEntity);
				}
			}

			// Set default headers
			HttpResponse response = httpclient.execute(request);

			InputStream in = null;

			if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK) {
				Header[] header = response.getHeaders("Content-Encoding");
				if (header != null && header.length != 0) {
					for (Header h : header) {
						if (h.getName().trim().equalsIgnoreCase("gzip"))
							in = new GZIPInputStream(response.getEntity()
									.getContent());
					}
				}

				if (in == null)
					in = new BufferedInputStream(response.getEntity()
							.getContent());

				if (_isBitmap) {
					Bitmap bm = BitmapFactory.decodeStream(in);
					dispatchResult(bm);
				} else {
					String temp = Util.convertStreamToString(in);// text.toString();
					Log.d(_action.toString(), "==" + temp + "");
					in.close();
					dispatchResult(temp);
				}

			} else if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_NOT_FOUND)
				processError(ResultCode.Failed);
			else if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_SERVER_ERROR)
				processError(ResultCode.ServerError);
			else
				processError(ResultCode.NetworkError);

		} catch (Exception e) {
			e.printStackTrace();
			processError(ResultCode.NetworkError);
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
	}

	public int getConnectionTimeout() {
		return _connection.getConnectTimeout();
	}

	public void loginFacebook(String accessToken) {
		_action = ServiceAction.ActionLoginFacebook;
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", accessToken);
		request("/m/login/fb", params, true, false);
	}

	public void loginTwitter(String oauthToken, String oauthTokenSecret) {
		// TODO Auto-generated method stub
		_action = ServiceAction.ActionLoginTwitter;
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", oauthToken);
		params.put("access_token", oauthTokenSecret);
		request("/m/login/tw", params, true, false);
	}

	private void attachUriWithQuery(HttpRequestBase request, Uri uri,
			Map<String, String> params) {
		try {
			if (params == null) {
				// No params were given or they have already been
				// attached to the Uri.
				request.setURI(new URI(uri.toString()));
			} else {
				Uri.Builder uriBuilder = uri.buildUpon();

				// Loop through our params and append them to the Uri.
				for (BasicNameValuePair param : paramsToList(params)) {
					uriBuilder.appendQueryParameter(param.getName(),
							param.getValue());
				}

				uri = uriBuilder.build();
				request.setURI(new URI(uri.toString()));
			}
		} catch (URISyntaxException e) {
			Log.e(TAG, "URI syntax was incorrect: " + uri.toString());
		}
	}

	private static List<BasicNameValuePair> paramsToList(
			Map<String, String> params) {
		ArrayList<BasicNameValuePair> formList = new ArrayList<BasicNameValuePair>(
				params.size());

		for (String key : params.keySet()) {
			Object value = params.get(key);

			// We can only put Strings in a form entity, so we call the
			// toString()
			// method to enforce. We also probably don't need to check for null
			// here
			// but we do anyway because Bundle.get() can return null.
			if (value != null)
				formList.add(new BasicNameValuePair(key, value.toString()));
		}

		return formList;
	}

	public void getBusinessDetail(String id) {
		_action = ServiceAction.ActionGetBusinessDetail;
		Map<String, String> params = new HashMap<String, String>();
		params.put("token", WhyqApplication.Instance().getRSAToken());
		params.put("store_id", id);
		request("/m/business/show", params, true, false);
	}

	public void getCheckedBills(String encryptedToken, String store_id) {
		_action = ServiceAction.ActionGetBills;
		Map<String, String> params = new HashMap<String, String>();
		params.put("token", encryptedToken);
		params.put("store_id", store_id);
		request("/m/business/member/check_bill", params, true, false);
	}

	public void getHistories(String encryptedToken, String userId) {
		_action = ServiceAction.ActionGetHistories;
		Map<String, String> params = new HashMap<String, String>();
		params.put("token", encryptedToken);
		params.put("user_id", userId);
		request("/m/member/order", params, true, false);
	}

	public void exeComment(String string, String storeId) {
		// TODO Auto-generated method stub
		_action = ServiceAction.ActionComment;
		Map<String, String> params = new HashMap<String, String>();
		params.put("token", WhyqApplication.Instance().getRSAToken());
		params.put("store_id", storeId);
		request("/m/business/show", params, true, false);

	}

	public void orderDelivery(String storeId, String otherAddress,
			String phoneNumber, String hours, String minutes) {
		// TODO Auto-generated method stub

	}

	public void getPhotos(String encryptedToken, String userId) {
		_action = ServiceAction.ActionGetPhotos;
		Map<String, String> params = new HashMap<String, String>();
		params.put("token", encryptedToken);
		params.put("user_id", userId);
		request("/m/member/profile/photo", params, true, false);
	}

	public void setLocation(String string) {
		// TODO Auto-generated method stub
		_action = ServiceAction.ActionGetLocation;
		Map<String, String> params = new HashMap<String, String>();
		params.put("text-search", string);
		request("getlocation", params, true, false);
	}

}
