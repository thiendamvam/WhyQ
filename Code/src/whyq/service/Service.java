package whyq.service;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
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
import whyq.activity.ListDetailActivity;
import whyq.interfaces.IServiceListener;
import whyq.model.SearchFriendCriteria;
import whyq.utils.API;
import whyq.utils.Constants;
import whyq.utils.Logger;
import whyq.utils.Util;
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
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		request("/v1/shop/purchase", params, true, false);
	}

	public void logout() {
		// TODO Auto-generated method stub
		_action = ServiceAction.ActionLogout;
		Map<String, String> params = new HashMap<String, String>();
		params.put("token", WhyqApplication.Instance().getRSAToken());
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		request("/m/logout", params, true, false);
	}

	public void postFavorite(String storeId) {
		// TODO Auto-generated method stub
		_action = ServiceAction.ActionPostFavorite;
		Map<String, String> params = new HashMap<String, String>();
		params.put("token", WhyqApplication.Instance().getRSAToken());
		params.put("store_id", storeId);
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		request("/m/member/favourite/business", params, true, false);
	}

	public void removeFavorite(String storeId) {
		// TODO Auto-generated method stub
		_action = ServiceAction.ActionRemoveFavorite;
		Map<String, String> params = new HashMap<String, String>();
		params.put("token", WhyqApplication.Instance().getRSAToken());
		params.put("store_id", storeId);
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		request("/m/member/favourite/business", params, true, false);
	}

	public void getComments(String encryptedToken, String store_id, int page,
			int count, boolean onlyFriend) {
		_action = ServiceAction.ActionGetComment;
		Map<String, String> params = new HashMap<String, String>();
		params.put("token", encryptedToken);
		params.put("store_id", store_id);
		params.put("page", String.valueOf(page));
		params.put("count", String.valueOf(count));
		params.put("only_friend", onlyFriend ? "1" : "");
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		request("/m/member/comment", params, true, false);
	}

	public void getFriends(String token, String user_id) {
		// �Nedd add page and key = if search

		_action = ServiceAction.ActionGetFriends;
		Map<String, String> params = new HashMap<String, String>();
		params.put("token", token);
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		if (user_id != null) {
			params.put("user_id", user_id);
		}
		request("/m/member/friend", params, true, false);
	}

	public void getInvitation(String token, String listInvited) {
		// �Nedd add page and key = if search

		_action = ServiceAction.ActionGetInvitations;
		Map<String, String> params = new HashMap<String, String>();
		params.put("token", token);
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		if (listInvited != null) {
			params.put("list_invited", listInvited);
		}
		request("/m/member/friend", params, true, false);
	}

	public void getInvitationNotification(String token, String listInvited) {
		// �Nedd add page and key = if search

		_action = ServiceAction.ActionGetInvitationsNotification;
		Map<String, String> params = new HashMap<String, String>();
		params.put("token", token);
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		params.put("list_invited", "");
		params.put("get_total", "");
		request("/m/member/friend", params, true, false);
	}

	public void getFriendsFacebook(String encryptedToken, String accessToken) {
		_action = ServiceAction.ActionGetFriendsFacebook;
		Map<String, String> params = new HashMap<String, String>();
		params.put("token", encryptedToken);
		params.put("access_token", accessToken);
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		request("/m/member/friend/facebook", params, true, false);
	}

	public void searchFriends(SearchFriendCriteria criteria,
			String encryptedToken, String key, String accessToken,
			String oauth_token, String oauth_token_secret) {
		_action = ServiceAction.ActionSearchFriendsFacebook;
		Map<String, String> params = new HashMap<String, String>();
		params.put("token", encryptedToken);
		params.put("key", key);
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
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
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		request("/m/member/friend/facebook/invite", params, true, false);
	}

	public void getUserActivities(String encryptedToken, String userId) {
		_action = ServiceAction.ActionGetUserActivities;
		Map<String, String> params = new HashMap<String, String>();
		params.put("token", encryptedToken);
		params.put("user_id", userId);
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		request("/m/member/recent/activity", params, true, false);
	}

	public void postComment(String encryptedToken, String content,
			String photoFile) {
		_action = ServiceAction.ActionPostComment;
		Map<String, String> params = new HashMap<String, String>();
		params.put("token", encryptedToken);
		params.put("content", content);
		params.put("photo", photoFile);
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
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
			result = result.replace("<0></0>", "");
			resObj = parser.parserLoginData(result);
			break;
		case ActionLoginWhyq:
			result = result.replace("<0></0>", "");
			resObj = parser.parserLoginData(result);
			break;
		case ActionSigup:
			resObj = parser.parserSignupResult(result);
			break;
		case ActionEditProfile:
			resObj = parser.parserLoginData(result);
			break;
		case ActionGetInvitations:
			resObj = parser.parseLUserCheckedResult(result);// Same structor
															// data
			break;
		case ActionGetInvitationsNotification:
			resObj = parser.parseInvitationNotification(result);// Same structor
																// data
			break;

		case ActionDeleteFriend:
			resObj = parser.parserDeleteFriend(result);
			break;
		case ActionOrderSend:
			resObj = parser.parserOrderSend(result);
			break;
		case ActionGetBillDetail:
			resObj = parser.parserBillDetailResultSend(result);
			break;
		case ActionGetDistance:
			resObj = parser.parserDistanceResultSend(result);
			break;
		case ActionInviteFriendsWhyQ:
			resObj = parser.parseFriendWhyq(result);
			break;
		case ActionLoginTwitter:
			resObj = parser.parserLoginData(result);
			break;
		case ActionGetBusinessDetail:
			resObj = parser.parseBusinessDetail(result);
			break;
		case ActionGetFaqs:
			resObj = parser.parseFaqsResult(result);
			break;
		case ActionGetBusinessList:
			resObj = parser.parseBusinessList(result);
			break;
		case ActionComment:
			resObj = parser.parseCommentResult(result);
			break;
		case ActionGetLocation:
			resObj = parser.parseLCationResult(result);
			break;
		case ActionPostFavorite:
			resObj = parser.parseLFavouriteResult(result);
			break;
		case ActionRemoveFavorite:
			resObj = parser.parseLFavouriteResult(result);
			break;
		case ActionForgotPassword:
			resObj = parser.parseLResetPasswordResult(result);
			break;
		case ActionGetUserChecked:
			resObj = parser.parseLUserCheckedResult(result);
			break;
		case ActionCheckbill:
			resObj = parser.parserCheckBillResult(result);
			break;
		default:
			resObj = result;
			Logger.appendLog(result);
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
		Log.d(_action.toString(), "=========== run ===========\n" + _actionURI);
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
			} else if (_action == ServiceAction.ActionGetDistance) {
				urlString = _actionURI;
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
					String temp = convertStreamToString(in);// text.toString();
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

	private String convertStreamToString(InputStream is) {
		// TODO Auto-generated method stub

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

	public int getConnectionTimeout() {
		return _connection.getConnectTimeout();
	}

	public void loginFacebook(String accessToken) {
		_action = ServiceAction.ActionLoginFacebook;
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", accessToken);
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		request("/m/login/fb", params, true, false);
	}

	public void loginTwitter(String oauthToken, String oauthTokenSecret) {
		// TODO Auto-generated method stub
		Log.d("loginTwitter", "loginTwitter");
		_action = ServiceAction.ActionLoginTwitter;
		Map<String, String> params = new HashMap<String, String>();
		params.put("oauth_token", oauthToken);
		params.put("oauth_token_secret", oauthTokenSecret);
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		request("/m/login/tw", params, true, false);
	}

	public void inviteFriendsTwitter(String encryptedToken, String oauth_token,
			String oauth_token_secret, String twitter_id) {
		_action = ServiceAction.ActionInviteFriendsTwitter;
		Map<String, String> params = new HashMap<String, String>();
		params.put("token", encryptedToken);
		params.put("oauth_token", oauth_token);
		params.put("oauth_token_secret", oauth_token_secret);
		params.put("twitter_id", twitter_id);
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		request("/m/member/friend/twitter/invite", params, true, false);
	}

	public void inviteFriendsWhyq(String encryptedToken, String userId) {
		_action = ServiceAction.ActionInviteFriendsWhyQ;
		Map<String, String> params = new HashMap<String, String>();
		params.put("token", encryptedToken);
		params.put("user_id", userId);
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		request("/m/member/friend/invite", params, true, false);
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

	public void getBusinessList(Map<String, String> params, String url) {
		_action = ServiceAction.ActionGetBusinessList;
		request(url, params, true, false);
	}

	public void getBusinessDetail(String id) {
		_action = ServiceAction.ActionGetBusinessDetail;
		Map<String, String> params = new HashMap<String, String>();
		params.put("token", WhyqApplication.Instance().getRSAToken());
		params.put("store_id", id);
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		request("/m/business/show", params, true, false);
	}

	public void getFaqs(String id) {
		_action = ServiceAction.ActionGetFaqs;
		Map<String, String> params = new HashMap<String, String>();
		params.put("token", WhyqApplication.Instance().getRSAToken());
		request("/m/faqs", params, true, false);
	}

	public void getCheckedBills(String encryptedToken, String store_id) {
		_action = ServiceAction.ActionGetBills;
		Map<String, String> params = new HashMap<String, String>();
		params.put("token", encryptedToken);
		params.put("store_id", store_id);
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		request("/m/business/member/check_bill", params, true, false);
	}

	public void getUserCheckedBills(String encryptedToken, String store_id,
			String text) {
		_action = ServiceAction.ActionGetUserChecked;
		Map<String, String> params = new HashMap<String, String>();
		params.put("token", encryptedToken);
		params.put("store_id", store_id);
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		if (text != null) {
			params.put("key", text);
			request("/m/business/member/check_bill/search", params, true, false);
		} else {
			request("/m/business/member/check_bill", params, true, false);
		}

	}

	public void getOrder(String encryptedToken, String userId) {
		_action = ServiceAction.ActionGetOrder;
		Map<String, String> params = new HashMap<String, String>();
		params.put("token", encryptedToken);
		params.put("user_id", userId);
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		request("/m/member/profile/order", params, true, false);
	}

	public void getHistories(String encryptedToken, String userId) {
		_action = ServiceAction.ActionGetHistories;
		Map<String, String> params = new HashMap<String, String>();
		params.put("token", encryptedToken);
		params.put("user_id", userId);
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		request("/m/member/order", params, true, false);
	}

	public void exeComment(String string, String storeId) {
		// TODO Auto-generated method stub
		_action = ServiceAction.ActionComment;
		Map<String, String> params = new HashMap<String, String>();
		params.put("token", WhyqApplication.Instance().getRSAToken());
		params.put("store_id", storeId);
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
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
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		request("/m/member/profile/photo", params, true, false);
	}

	public void setLocation(String string) {
		// TODO Auto-generated method stub
		_action = ServiceAction.ActionGetLocation;
		Map<String, String> params = new HashMap<String, String>();
		params.put("text-search", string);
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		request("getlocation", params, true, false);
	}

	public void getProfiles(String encryptedToken, String userId) {
		_action = ServiceAction.ActionGetProfiles;
		Map<String, String> params = new HashMap<String, String>();
		params.put("token", encryptedToken);
		params.put("user_id", userId);
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		request("/m/member/profile", params, true, false);
	}

	public void exeResetPass(String string) {
		// TODO Auto-generated method stub
		_action = ServiceAction.ActionForgotPassword;
		Map<String, String> params = new HashMap<String, String>();
		params.put("email", string);
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		request("/m/forget_password", params, true, false);
	}

	public void loginWhyq(String email, String pass) {
		// TODO Auto-generated method stub
		_action = ServiceAction.ActionLoginWhyq;
		Map<String, String> params = new HashMap<String, String>();
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		request("/m/login", params, true, false);
		params.put("email", email);
		params.put("password", pass);
	}

	public void register(HashMap<String, String> params) {
		// TODO Auto-generated method stub
		_action = ServiceAction.ActionSigup;
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		request("/m/register", params, true, false);
	}

	public void editProfile(HashMap<String, String> params) {
		// TODO Auto-generated method stub
		// first_name, las_name,token,new_password,old_password
		_action = ServiceAction.ActionEditProfile;
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		request("/m/member/profile/edit", params, true, false);
	}
	public void pushNotification(String isReceiveNotify, String isShowFriend) {
		// TODO Auto-generated method stub
		// value is 0  or 1 for isReceivedNotify and isShowFriend
		HashMap<String, String> params = new HashMap<String, String>();
		_action = ServiceAction.ActionEditProfile;
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		params.put("is_receive_notification", isReceiveNotify);
		params.put("is_show_friend", isReceiveNotify);
		request("/m/member/setting/edit", params, true, false);
	}
	public void searchOnlyFriend(SearchFriendCriteria criteria,
			String encryptedToken, String key, String accessToken,
			String oauth_token, String oauth_token_secret) {
		_action = ServiceAction.ActionSearchOnlyFriend;
		Map<String, String> params = new HashMap<String, String>();
		params.put("token", encryptedToken);
		params.put("key", key);
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		params.put("search", criteria.toString());
		request("/m/member/search/friend", params, true, false);
	}

	public void deleteFriend(String userId, String encryptedToken) {
		// TODO Auto-generated method stub
		_action = ServiceAction.ActionDeleteFriend;
		Map<String, String> params = new HashMap<String, String>();
		params.put("token", encryptedToken);
		params.put("user_id", userId);
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		request("/m/member/profile", params, true, false);
	}

	public void orderSend(HashMap<String, String> params) {
		// TODO Auto-generated method stub
		// store_id, deliver_type(1: tike away, 4: dinein),
		// list_items,deliver_to(empty for typle=1 and 0 if type =4),
		// time_zone,time_deliver(if not empty, oly for type =1),note(commend),
		// token
		_action = ServiceAction.ActionOrderSend;
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		if (ListDetailActivity.commentContent != null) {
			params.put("note", ListDetailActivity.commentContent);
		}
		request("/m/member/order/send", params, true, false);
	}

	public void getBillDetail(String billId, String encryptedToken) {
		// TODO Auto-generated method stub
		_action = ServiceAction.ActionGetBillDetail;
		Map<String, String> params = new HashMap<String, String>();
		params.put("token", encryptedToken);
		params.put("bill_id", billId);
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		request("/m/member/order/show", params, true, false);
	}

	public void getDistance(HashMap<String, String> params) {
		// TODO Auto-generated method stub
		_action = ServiceAction.ActionGetDistance;
		request(Constants.GET_DISTANCE_API, params, true, true);

	}

	public void checkBill(String billId, String message, String encryptedToken) {
		// TODO Auto-generated method stub
		_action = ServiceAction.ActionCheckbill;
		Map<String, String> params = new HashMap<String, String>();
		params.put("token", encryptedToken);
		params.put("bill_id", billId);
		params.put("message", message);
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		request("/m/member/order/show", params, true, false);
	}

}
