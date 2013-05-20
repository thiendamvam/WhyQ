package whyq.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import whyq.PermpingApplication;
import whyq.PermpingMain;
import whyq.activity.FollowerActivity;
import whyq.activity.FollowerActivityGroup;
import whyq.activity.GoogleMapActivity;
import whyq.activity.JoinPermActivity;
import whyq.activity.NewPermActivity;
import whyq.activity.PrepareRequestTokenActivity;
import whyq.controller.AuthorizeController;
import whyq.controller.PermListController;
import whyq.model.Comment;
import whyq.model.Perm;
import whyq.model.User;
import whyq.utils.API;
import whyq.utils.Constants;
import whyq.utils.HttpPermUtils;
import whyq.utils.PermUtils;
import whyq.utils.UrlImageViewHelper;
import whyq.utils.facebook.FacebookConnector;
import whyq.utils.facebook.sdk.DialogError;
import whyq.utils.facebook.sdk.Facebook;
import whyq.utils.facebook.sdk.Facebook.DialogListener;
import whyq.utils.facebook.sdk.FacebookError;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.whyq.R;

public class PermAdapter extends ArrayAdapter<Perm> implements OnClickListener {

	private ArrayList<Perm> items;
	public static final String TAG = "PermAdapter";
	public Button join;
	public Button login;
	public Button like;
	public Button reperm;
	public Button comment;

	private Activity activity;
	private Boolean header;
	private User user;
	public static String currentPermId;
	private FacebookConnector facebookConnector;

	private SharedPreferences prefs;

	private int screenWidth;
	private int screenHeight;
	private Context context;
	private HashMap<String, View> viewList = new HashMap<String, View>();
	private HashMap<String, Perm> newPermList = new HashMap<String, Perm>();
	private HashMap<String, TextView> permStateList = new HashMap<String, TextView>();
	public int count = 11;
	
	private FragmentManager fragmentManager;
	private int nextItems = -1;
	
//	new
	
	final String likeString = this.getContext().getResources().getString(R.string.bt_like);
	final String unlikeString = this.getContext().getResources().getString(R.string.bt_unlike);
	final String repermString = this.getContext().getResources().getString(R.string.bt_reperm);
	final String commentString = this.getContext().getResources().getString(R.string.bt_comment);
	final String textCurrentLike = this.getContext().getResources().getString(R.string.delete);
/*	
	PermAdapter(ArrayList<Perm> perms) {
		super(PermAdapter.getContext(), new SpecialAdapter(perms), R.layout.pending);
	}
	*/
	public PermAdapter(Context context, int textViewResourceId,
			ArrayList<Perm> items, Activity activity, int screenWidth,
			int screenHeight, Boolean header) {
		super(context, textViewResourceId, items);
		this.context = context;
		this.activity = activity;
		this.header = header;
		this.items = items;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		facebookConnector = new FacebookConnector(Constants.FACEBOOK_APP_ID,
				this.activity, context, new String[] { Constants.EMAIL,
						Constants.PUBLISH_STREAM });
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
	}

	public PermAdapter(Context context,FragmentManager fragmentManager, int textViewResourceId,
			ArrayList<Perm> items, Activity activity, int screenWidth,
			int screenHeight, Boolean header, User user) {
		super(context, textViewResourceId, items);
		this.context = context;
		this.fragmentManager = fragmentManager;
		this.activity = activity;
		this.header = header;
		this.items = items;
		this.user = user;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		facebookConnector = new FacebookConnector(Constants.FACEBOOK_APP_ID,
				this.activity, context, new String[] { Constants.EMAIL,
						Constants.PUBLISH_STREAM });
		prefs = PreferenceManager.getDefaultSharedPreferences(context);

	}

	private String getActivityGroupName() {
		// com.permping.activity.FollowerActivity

		String activityName = activity.getParent().getClass().getName()
				.replace("com.permping.activity.", "");
		return activityName;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		try {

			//ViewHolder holder;
			/*if (position == 0 && this.header == true) {
				LayoutInflater inflater = (LayoutInflater) this.getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				final View view = inflater.inflate(R.layout.perm_item_2, null);

				// Process buttons
				join = (Button) view.findViewById(R.id.bt_join);
				login = (Button) view.findViewById(R.id.bt_login);
				login.setOnClickListener(PermAdapter.this);
				join.setOnClickListener(PermAdapter.this);
				return view;
			} else*/
			if(items != null && !items.isEmpty() && position < items.size()){
				/*if(position == 0) {
					if( PermpingMain.getCurrentTab() == 0 || PermpingMain.getCurrentTab()==1){
						LayoutInflater inflater = (LayoutInflater) this.getContext()
								.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						final View view = inflater.inflate(R.layout.perm_item_2, null);
							
						join = (Button) view.findViewById(R.id.bt_join);
						login = (Button) view.findViewById(R.id.bt_login);
						// Process buttons					
						login.setOnClickListener(PermAdapter.this);
						join.setOnClickListener(PermAdapter.this);

						if(this.header == false) {
							//remove button
							TableRow loginRow = (TableRow) view.findViewById(R.id.loginBar);
							loginRow.setVisibility(View.GONE);
						}
						return view;
					}

				}*/
				
				if(position == items.size() - 1 && PermListController.isFooterAdded == true) {
					if(!PermListController.isLoading){
						//PermListController.selectedPos = items.size() -1;
						PermListController.isLoading = true;
						loadMoreItems();
					}
					/*if(getNextItems() != -1) {
						loadMoreItems();
					}*/
					//View footerView = createFooterView();
					View footerView = createNullView();
					/*if(getNextItems() == -1) {						
						ImageButton nextButton = (ImageButton) footerView.findViewById(R.id.next);
						nextButton.setVisibility(View.INVISIBLE);
					}
					if(getNextItems() == 2) {
						//currently, the page is 1, so no page is previous page
						ImageButton previousButton = (ImageButton) footerView.findViewById(R.id.previous);
						previousButton.setVisibility(View.INVISIBLE);
					}
					*/
					
					//disable the separator view
					/*int previousPosition = position - 1;
					if(previousPosition < items.size() && previousPosition >= 0) {
						Perm perm = items.get(previousPosition);
						String viewId = perm.getId();
						View previousView = viewList.get(viewId);
						if(previousView != null) {
							View seperatorLine = (View) previousView.findViewById(R.id.item_separator);
							if(seperatorLine != null) {
								seperatorLine.setVisibility(View.GONE);
							}
						}
					}*/
					
					return footerView;
				}
				final Perm perm = items.get(position);
				final String viewId = perm.getId();
				if(viewId == null || viewId.length() == 0) {
					return createNullView();
				}
				currentPermId = viewId;
				convertView = viewList.get(viewId);
				newPermList.put(viewId, perm);
				if (convertView != null){
//					Log.i(TAG, "getView() convertView != null");
					addComments(convertView, perm);
					return convertView;
				}else{
//					Log.i(TAG, "getView() convertView == null");
					LayoutInflater inflater = (LayoutInflater) this.getContext()
							.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					final View view = inflater.inflate(R.layout.perm_item_1, null);
		
					like = (Button) view.findViewById(R.id.btnLike);
					// Validate Like or Unlike
					if (perm != null && user!=null){
						if(perm.getAuthor().getId().equals(user.getId())){
							like.setText(textCurrentLike);
						}else{
							if (perm.getPermUserLikeCount() != null
									&& "0".equals(perm.getPermUserLikeCount())) {
								like.setText(R.string.bt_like);
							} else {
								like.setText(R.string.bt_unlike);
							}
							
						}
					}

					like.setTag(viewId);
					like.setNextFocusDownId(position);
					
					if(perm != null && user != null) {
						if( perm.getAuthor() != null)
							if(perm.getAuthor().getId().equalsIgnoreCase(user.getId()))
								like.setText(R.string.delete);
//								like.setVisibility(View.GONE);
					}
					like.setOnClickListener(PermAdapter.this);
					reperm = (Button) view.findViewById(R.id.btnRepem);
					reperm.setTag(viewId);
					reperm.setOnClickListener(PermAdapter.this);
		
					comment = (Button) view.findViewById(R.id.btnComment);
					comment.setTag(viewId);
					comment.setOnClickListener(PermAdapter.this);
					ImageView gotoMap = (ImageView)view.findViewById(R.id.btnLocation);
					gotoMap.setTag(viewId);
					gotoMap.setOnClickListener(PermAdapter.this);
					TextView permViewInfo = (TextView)view.findViewById(R.id.permVoiceInfo);
//					if(perm.getLon() ==0 && perm.getLat() == 0){
//						gotoMap.setVisibility(View.INVISIBLE);
////						permViewInfo.setVisibility(View.GONE);
//						
//					}else{
////						gotoMap.setVisibility(View.VISIBLE);
////						permViewInfo.setVisibility(View.VISIBLE);
//					}
						gotoMap.setVisibility(View.INVISIBLE);
					
					if(position == items.size() - 1) {
						View seperatorLine = (View) view.findViewById(R.id.item_separator);
						if(seperatorLine != null) {
							seperatorLine.setVisibility(View.GONE);
						}
					}
					
					if (perm != null) {
						
						// Set the nextItems no.
						if (perm.getNextItem() != null)
							nextItems = Integer.parseInt(perm.getNextItem());
						else
							nextItems = -1;
						ImageView btnPlayAudio = (ImageView)view.findViewById(R.id.btnVoice);
						btnPlayAudio.setTag(viewId);
						btnPlayAudio.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								PermpingMain.gotoTab(6, perm.getPermAudio());
							}
						});
						
						if(perm.getPermAudio() !=null && !perm.getPermAudio().equals("")){
							btnPlayAudio.setVisibility(View.VISIBLE);
							
						}else{
							btnPlayAudio.setVisibility(View.GONE);
							
						}
						ImageView av = (ImageView) view.findViewById(R.id.authorAvatar);
						if( perm.getAuthor() != null)
							if(perm.getAuthor().getAvatar()!=null)
								if(perm.getAuthor().getAvatar().getUrl() != null)
									UrlImageViewHelper.setUrlDrawable(av, perm.getAuthor()
								.getAvatar().getUrl());
		
						TextView an = (TextView) view.findViewById(R.id.authorName);
						//holder.authorName.setText(perm.getAuthor().getName());
						if( perm != null)
							if(perm.getAuthor() != null)
								if(perm.getAuthor().getName() != null)
									an.setText(perm.getAuthor().getName());
						av.setOnClickListener(new View.OnClickListener() {
							
							public void onClick(View v) {
								// TODO Auto-generated method stub
								Comment comment = new Comment(perm.getAuthor().getId());
								comment.setAuthor(perm.getAuthor());
								PermpingMain.gotoTab(4, comment);
							}
						});
						an.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								Comment comment = new Comment(perm.getAuthor().getId());
								comment.setAuthor(perm.getAuthor());
								PermpingMain.gotoTab(4, comment);
							}
						});
						// Board name
						TextView bn = (TextView) view.findViewById(R.id.boardName);
						//holder.boardName.setText(perm.getBoard().getName());
						if(perm.getBoard() != null)
							if( perm.getBoard().getName() != null)
								bn.setText(perm.getBoard().getName());
		
						ImageView imageView = (ImageView) view.findViewById(R.id.permImage);
						/**
						 * MSA
						 */
						imageView.setOnClickListener(new View.OnClickListener() {
							
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								String permUrl = perm.getPermUrl();
								if(permUrl != null && permUrl != "")
									PermpingMain.gotoTab(5, permUrl);
								//Log.d("=====>", "=================>go to Perm Browser >");
							}
						});
					
						UrlImageViewHelper.setUrlDrawable(imageView, perm.getImage().getUrl() , true ); 
						TextView pd = (TextView) view.findViewById(R.id.permDesc);
						//holder.permDesc.setText(perm.getDescription());
						if(perm != null)
							if(perm.getDescription() != null)
								pd.setText(perm.getDescription());
						String permInfo = perm.getPermDatemessage();
						TextView pi = (TextView) view.findViewById(R.id.permInfo);
						//holder.permInfo.setText(permInfo);
						if( permInfo != null)
							pi.setText(permInfo);
						
						String permStat = likeString + ": " + perm.getPermLikeCount()
								+ " - " + repermString + ": " + perm.getPermRepinCount()
								+ " - " + commentString + ": " + perm.getPermCommentCount();
						
						TextView ps = (TextView) view.findViewById(R.id.permStat);
						permStateList.put(viewId, ps);
						if(permStat !=null)
							ps.setText(permStat);
		
						LinearLayout comments = (LinearLayout) view
								.findViewById(R.id.comments);
						if(perm.getComments() != null){
							for (int i = 0; i < perm.getComments().size(); i++) {
								View cm = inflater.inflate(R.layout.comment_item, null);
								final Comment pcm = perm.getComments().get(i);
								if (pcm != null && pcm.getAuthor() != null) {
			
									ImageView cma = (ImageView) cm
											.findViewById(R.id.commentAvatar);
									cma.setOnClickListener(new View.OnClickListener() {
										
										public void onClick(View v) {
											// TODO Auto-generated method stub
											PermpingMain.gotoTab(4, pcm);
										}
									});
									if( pcm.getAuthor() != null)
										if(pcm.getAuthor().getAvatar()!=null)
											if(pcm.getAuthor().getAvatar().getUrl() != null)
												UrlImageViewHelper.setUrlDrawable(cma, pcm.getAuthor()
											.getAvatar().getUrl());
			
									TextView authorName = (TextView) cm
											.findViewById(R.id.commentAuthor);
									if(pcm !=null){
										if(pcm.getAuthor() != null)
											if(pcm.getAuthor().getName() != null)
												authorName.setText(pcm.getAuthor().getName());
			
										TextView cmt = (TextView) cm
											.findViewById(R.id.commentContent);
										if(pcm.getContent() != null)
											cmt.setText(pcm.getContent());
									}
									authorName.setOnClickListener(new View.OnClickListener() {
										
										@Override
										public void onClick(View v) {
											// TODO Auto-generated method stub
											PermpingMain.gotoTab(4, pcm);	
										}
									});
									if (i == (perm.getComments().size() - 1)) {
										View sp = (View) cm.findViewById(R.id.separator);
										sp.setVisibility(View.INVISIBLE);
									}

									comments.addView(cm);
								}
							}
							
						}
					}
					//return convertView;
					viewList.put(perm.getId(), view);
					//Log.d("aaa", "================"+view);
					return view;
				}
			}
			else{
				//position > items.size
				//try to create a null view				
				return createNullView();
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			//Log.d("aaa", "=========asdfsfsd======="+e.toString());
			return createNullView();
		}
	}
	
	public void addComments(View view, Perm perm) {
		LinearLayout comments = (LinearLayout) view
				.findViewById(R.id.comments);
		if(perm != null && comments != null){
			if(perm.getComments() != null){
				if(perm.getComments().size() == comments.getChildCount()) {
					return;
				}				
			}
		}

		comments.removeAllViews();
		LayoutInflater inflater = (LayoutInflater) this.getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(perm.getComments() != null){
			for (int i = 0; i < perm.getComments().size(); i++) {
				View cm = inflater.inflate(R.layout.comment_item, null);
				final Comment pcm = perm.getComments().get(i);
				if (pcm != null && pcm.getAuthor() != null) {

					ImageView cma = (ImageView) cm
							.findViewById(R.id.commentAvatar);
					cma.setOnClickListener(new View.OnClickListener() {
						
						public void onClick(View v) {
							// TODO Auto-generated method stub
							PermpingMain.gotoTab(4, pcm);
						}
					});
					UrlImageViewHelper.setUrlDrawable(cma, pcm.getAuthor()
							.getAvatar().getUrl());

					TextView authorName = (TextView) cm
							.findViewById(R.id.commentAuthor);
					authorName.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							PermpingMain.gotoTab(4, pcm);
						}
					});
					if(pcm !=null){
						if(pcm.getAuthor() != null)
							if(pcm.getAuthor().getName() != null)
								authorName.setText(pcm.getAuthor().getName());

						TextView cmt = (TextView) cm
							.findViewById(R.id.commentContent);
						if(pcm.getContent() != null)
							cmt.setText(pcm.getContent());
					}
					/*
					 * boolean isWrapped = PermUtils.isTextWrapped(activity,
					 * cmt.getText().toString(), cmt.getContext()); if
					 * (isWrapped) { cmt.setMaxLines(5);
					 * cmt.setSingleLine(false);
					 * cmt.setEllipsize(TruncateAt.MARQUEE); }
					 */
					if (i == (perm.getComments().size() - 1)) {
						View sp = (View) cm.findViewById(R.id.separator);
						sp.setVisibility(View.INVISIBLE);
					}
					/*
					 * EllipsizingTextView cmt = (EllipsizingTextView) cm
					 * .findViewById(R.id.commentContent);
					 * cmt.setText(pcm.getContent());
					 * 
					 * if (i == (perm.getComments().size() - 1)) { View sp =
					 * (View) cm.findViewById(R.id.separator);
					 * sp.setVisibility(View.INVISIBLE); }
					 */
					comments.addView(cm);
				}
			}
			
		}
	}
	
	/**
	 * MSA
	 * @return
	 */
	public void loadMoreItems() {
		FollowerActivity follow = ((FollowerActivity)activity);
		follow.loadNextItems();
	}
	
	public View createFooterView() {
		final Context context = this.getContext();
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View footerView = inflater.inflate(R.layout.prev_next_layout, null);
		ImageButton previousButton = (ImageButton) footerView.findViewById(R.id.previous);
		previousButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(activity instanceof FollowerActivity) {
					FollowerActivity follow = ((FollowerActivity)activity);
					follow.loadPreviousItems();
				}

			}
		});

	
		ImageButton nextButton = (ImageButton) footerView.findViewById(R.id.next);
		nextButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(activity instanceof FollowerActivity) {
						FollowerActivity follow = ((FollowerActivity)activity);
						follow.loadNextItems();
					}
					}
			});
		return footerView;
    
	}

	public View createNullView() {
		LayoutInflater inflater = (LayoutInflater) this.getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.perm_item_3, null);
		view.setVisibility(view.GONE);
		return view;
	}
	
	private void loadImage(ImageView imageView, String url) {
		// TODO Auto-generated method stub
		
	}

	public class getData extends AsyncTask< ImageView  , String, Bitmap>{

		String imageUrl;
		ImageView imageView;
		public getData(String imageLink) {
			// TODO Auto-generated constructor stub
			super();
			imageUrl = imageLink;
		}

		@Override
		protected Bitmap doInBackground(ImageView... params) {
			// TODO Auto-generated method stub
			Bitmap bm = null;
			imageView = params[0];
			//Log.d("a","==========>"+imageUrl);
			bm = PermUtils.scaleBitmap(imageUrl);
			return bm;
		}
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            // TODO Auto-generated method stub
            super.onPostExecute(bitmap);   
//            Drawable bm =  imageView.getDrawable();
//            if( bm != null)
//            	((BitmapDrawable)imageView.getDrawable()).getBitmap().recycle();
//            imageView.setScaleType(ScaleType.FIT_XY);
            imageView.setImageBitmap(bitmap);
        }

	}

	class CommentDialog extends Dialog implements android.view.View.OnClickListener{
		
		Perm perm  = null;
		User user = null;
		
		Button btnOK = null;
		EditText txtComment = null;
		
		private ProgressDialog dialog;
		
		public CommentDialog(Context context, Perm perm, User user) {
			super(context);
			setContentView( R.layout.comment_dialog );
			this.setTitle( R.string.comment_title );
			
			this.perm = perm;
			this.user = user;
			
			btnOK = (Button) findViewById( R.id.btnOK );
			btnOK.setOnClickListener(this);
			
			txtComment = ( EditText ) findViewById( R.id.commentEditText );
			
			
		}

		public void onClick(View v) {
			if( v == btnOK )
			{				
				final String cmText = txtComment.getText().toString();
				
				AsyncTask<Void, Void, String> comment = new AsyncTask<Void, Void, String >(){

					@Override
					protected String doInBackground(Void... arg0) {
						
						HttpPermUtils util = new HttpPermUtils();
						List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
						nameValuePairs.add(new BasicNameValuePair("cmnt", cmText ) );
						nameValuePairs.add(new BasicNameValuePair("pid", perm.getId() ) );
						nameValuePairs.add(new BasicNameValuePair("uid", user.getId() ) );
						String response  = util.sendRequest(API.commentUrl, nameValuePairs, false);
						
						return response;
					}
					
					
					@Override
			  		protected void onProgressUpdate(Void... unsued) {

			  		}

			  		@Override
			  		protected void onPostExecute(String sResponse) {
			  			if (dialog.isShowing()){
			  				dialog.dismiss();
			  				Toast.makeText(context,"Added comment!",Toast.LENGTH_LONG).show();
			  				int position = items.indexOf(perm);			  				
			  				Comment comment = new Comment(perm.getId(), cmText);
			  				comment.setAuthor(user);
			  				//perm.addCommnent(comment);
			  				items.get(position).addCommnent(comment);
			  				
			  				if(activity != null) {
			  					ListView list = (ListView) activity.findViewById(R.id.permList);
			  					if(list != null) {
			  						list.invalidateViews();
			  					}
			  				}			  				
			  			}
			  		}
					
				};
				
				if(cmText.length() > 0 ){ //!cmText.isEmpty()
					this.dismiss();
					dialog = ProgressDialog.show(context, "Uploading","Please wait...", true);
					comment.execute();
				}
				else{
					Toast.makeText(txtComment.getContext().getApplicationContext(),"Don't leave text blank!",Toast.LENGTH_LONG).show();
				}
				
			}
		}
		
	}

	/**
	 * This listener is to handle the click action of Join button This should
	 * show the dialog and user can choose Facebook or Twitter login or login to
	 * Permping directly.
	 */
	class OptionsDialog extends Dialog implements
			android.view.View.OnClickListener {

		// The "Login with Facebook" button
		private Button facebookLogin;

		// The "Login with Twitter" button
		private Button twitterLogin;

		// The "Join Permping" button
		private Button joinPermping;

		// PermpingApplication state;
		
		Context context;
		
		
		
		

		public OptionsDialog(Context context) {
			super(context);
			setContentView(R.layout.join_options);
			this.context = context;
			facebookLogin = (Button) findViewById(R.id.bt_login_with_facebook);
			facebookLogin.setOnClickListener(this);
			twitterLogin = (Button) findViewById(R.id.bt_login_with_twitter);
			twitterLogin.setOnClickListener(this);
			joinPermping = (Button) findViewById(R.id.bt_join_permping);
			joinPermping.setOnClickListener(this);
		}

		public void onClick(View v) {
			if (v == facebookLogin) {
				    Facebook mFacebook;
					mFacebook = new Facebook(Constants.FACEBOOK_APP_ID);
					//final Activity activity = getParent();
					mFacebook.authorize( FollowerActivityGroup.context, new String[] { "email", "status_update",
							"user_birthday" }, new DialogListener() {
						@Override
						public void onComplete(Bundle values) {
							//Log.d("", "=====>"+values.toString());
							PermUtils permutils = new PermUtils();
							String accessToken = values.getString("access_token");
							permutils.saveFacebookToken("oauth_token", accessToken, activity);
//								// Check on server
							List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
							nameValuePairs.add(new BasicNameValuePair("type", Constants.FACEBOOK_LOGIN));
							nameValuePairs.add(new BasicNameValuePair("oauth_token", accessToken));
							nameValuePairs.add(new BasicNameValuePair("email", ""));
							nameValuePairs.add(new BasicNameValuePair("password", ""));
							if(activity instanceof FollowerActivity) {
								AuthorizeController authorizeController = new AuthorizeController((FollowerActivity)activity);
								authorizeController.authorize(getContext(), nameValuePairs);	
							}
														
						}

							@Override
							public void onFacebookError(FacebookError error) {

							}

							@Override
							public void onError(DialogError e) {

							}

							@Override
							public void onCancel() {
								// cancel press or back press
							}
						});
					//}				
					
					this.dismiss();
				
			} else if (v == twitterLogin) {
				Intent i = new Intent(context,
						PrepareRequestTokenActivity.class);
				context.startActivity(i);
				this.dismiss();

			} else { // Show Join Permping screen
				prefs.edit().putString(Constants.LOGIN_TYPE,
						Constants.PERMPING_LOGIN);
				Intent i = new Intent(context, JoinPermActivity.class);
				context.startActivity(i);
				this.dismiss();
			}

			// this.dismiss();

		}
	}

	/**
	 * @return the count
	 */
	public int getCount() {
		if(items == null) {
		return count;
	}
		return items.size();
	}
	

	/**
	 * @return the nextItems
	 */
	public int getNextItems() {
		return nextItems;
	}

	/**
	 * @param nextItems the nextItems to set
	 */
	public void setNextItems(int nextItems) {
		this.nextItems = nextItems;
	}

	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getItem(int)
	 */
	@Override
	public Perm getItem(int position) {
		// TODO Auto-generated method stub
		return super.getItem(position);
	}

	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return super.getItemId(position);
	}
	
	public View createHeaderView() {
		LayoutInflater inflater = (LayoutInflater) this.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.perm_item_2, null);
		// Process buttons
		join = (Button) view.findViewById(R.id.bt_join);
		login = (Button) view.findViewById(R.id.bt_login);
		login.setOnClickListener(PermAdapter.this);
		join.setOnClickListener(PermAdapter.this);
		
		updateHeaderView(view);
		
		return view;
	}
	
	public void updateHeaderView(View view) {
		TableRow loginRow = (TableRow) view.findViewById(R.id.loginBar);
		if(loginRow == null) {
			return;
		}
		user = PermUtils.isAuthenticated(view.getContext());
		if (user != null) {
			loginRow.setVisibility(View.GONE);
		} else {
			loginRow.setVisibility(View.VISIBLE);
		}
	}
	
	public boolean isPermDuplicate(Perm perm) {
		if(perm == null || perm.getId() == null) {
			return false;
		}
		for(int i = 0; i < items.size(); i++) {
			Perm permItem = items.get(i);
			if(permItem != null && permItem.getId() != null) {
				if(permItem.getId().equals(perm.getId())) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	/*
	static class ViewHolder {
		ImageView avatar;
		TextView authorName;
		TextView boardName;
		ImageView permImage;
		TextView permDesc;
		TextView permInfo;
		TextView permStat;
		LinearLayout comments;
	}*/

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id= v.getId();
		switch (id) {
		case R.id.bt_login:
			exeLogin();
			break;
		case R.id.bt_join:
			exeJoin();
			break;
		case R.id.btnLike:
			exeLike(v);
			break;
		case R.id.btnRepem:
			exeReperm(v);
			break;
		case R.id.btnComment:
			exeComment(v);
			break;
		case R.id.btnLocation:
			exeGotoMap(v);
			break;
		default:
			break;
		}
		
	}

	private void exeGotoMap(View view) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		String permId = (String)view.getTag();
		if(permId != null){
			Perm perm = newPermList.get(permId);
			Intent googleMap = new Intent(context,
					GoogleMapActivity.class);
			Bundle bundle = new Bundle();
			bundle.putFloat("lat", perm.getLat());
			bundle.putFloat("lon", perm.getLon());
			bundle.putString("thumbnail", perm.getImage().getUrl());
			googleMap.putExtra("locationData", bundle);
			//Log.d("AA+++++============","========="+perm.getImage().getUrl());
			View view2 = FollowerActivityGroup.group.getLocalActivityManager().startActivity( "GoogleMapActivity"+perm.getId(), googleMap).getDecorView();
			FollowerActivityGroup.group.replaceView(view2);

		}	
	}

	private void exeComment(View view) {
		// TODO Auto-generated method stub
		String permId = (String)view.getTag();
		if(permId != null){
			Perm perm = newPermList.get(permId);
			user = PermUtils.isAuthenticated(view.getContext());
			if (user != null) {
				
				PermpingApplication state = (PermpingApplication)view.getContext().getApplicationContext();
				
				CommentDialog commentDialog = new CommentDialog( view.getContext(), perm , state.getUser() );
				commentDialog.show();
			} else {
				Toast.makeText(view.getContext(), Constants.NOT_LOGIN, Toast.LENGTH_LONG).show();
			}

		}
	
	}

	private void exeReperm(View view) {
		// TODO Auto-generated method stub
		String permId = (String)view.getTag();
		if(permId != null){
			Perm perm = newPermList.get(permId);
			user = PermUtils.isAuthenticated(view.getContext());
			if (user != null) {
				Intent myIntent = new Intent(view.getContext(),
						NewPermActivity.class);
				myIntent.putExtra("reperm", true);
				NewPermActivity.boardList = user.getBoards();
				myIntent.putExtra("boardId", perm.getBoard().getId());
				myIntent.putExtra("boardDesc", (String) perm.getBoard().getDescription());
				myIntent.putExtra("permId", (String) perm.getId());
				myIntent.putExtra("userId", user.getId());
				context.startActivity(myIntent);
			} else {
				Toast.makeText(view.getContext(), Constants.NOT_LOGIN,
						Toast.LENGTH_LONG).show();
			}
			
		}
	
	}

	private void exeLike(final View v) {
		// TODO Auto-generated method stub
		String permId = (String)v.getTag();
		if(permId != null){
			final Perm perm = newPermList.get(permId);
			user = PermUtils.isAuthenticated(v.getContext());
			if (user != null) {
				// final ProgressDialog dialog =
				// ProgressDialog.show(v.getContext(),
				// "Loading","Please wait...", true);

				final HttpPermUtils util = new HttpPermUtils();
				//Log.d("aasdfsdss", like.getText().toString()+"======="+R.string.delete);
				if(like.getText().toString().equals(likeString) || like.getText().toString().equals(unlikeString)){
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
					nameValuePairs.add(new BasicNameValuePair("pid", String
							.valueOf(perm.getId())));
					nameValuePairs.add(new BasicNameValuePair("uid", String
							.valueOf(user.getId())));
					util.sendRequest(API.likeURL, nameValuePairs, false);

					if (v instanceof Button) {
						String label = ((Button) v).getText().toString();
						int likeCount = Integer.parseInt(perm
								.getPermLikeCount());
						if (label != null && label.equals(likeString)) { // Like
							// Update the count
							likeCount++;
							perm.setPermLikeCount(String.valueOf(likeCount));
							// Change the text to "Unlike"
							((Button)v).setText(R.string.bt_unlike);
							v.invalidate();
						} else { // Unlike
							likeCount = likeCount - 1;
							if (likeCount < 0)
								likeCount = 0;
							perm.setPermLikeCount(String.valueOf(likeCount));
							((Button)v).setText(R.string.bt_like);
							v.invalidate();
						}
					}
					
					String permStatus = likeString + ": " + perm.getPermLikeCount()
							+ " - " + repermString + ": " + perm.getPermRepinCount()
							+ " - " + commentString + ": " + perm.getPermCommentCount();
					TextView txtStatus = permStateList.get(permId);
					if(permStatus != null)
						txtStatus.setText(permStatus);
				}else if(like.getText().toString().equals(textCurrentLike)){
					AlertDialog alertDialog = new AlertDialog.Builder(context).create();		
//					alertDialog.setTitle(AlertDialog);
					alertDialog.setMessage(v.getContext().getString(R.string.confirm_delete));
					alertDialog.setButton(v.getContext().getString(R.string.yes_delete), new DialogInterface.OnClickListener() {
					   public void onClick(DialogInterface dialog, int which) {
					      // here you can add functions
							viewList.remove(perm.getId());
							int position = v.getNextFocusDownId();
							if(position >= 0 && items.size() > position)
								items.remove(position);
							notifyDataSetChanged();
							List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
							nameValuePairs.add(new BasicNameValuePair("delid", String
									.valueOf(perm.getId())));
							nameValuePairs.add(new BasicNameValuePair("uid", String
									.valueOf(user.getId())));
							util.sendRequest(API.deleteUrl, nameValuePairs, false);
					   }
					});
					alertDialog.setButton2(v.getContext().getString(R.string.no), new DialogInterface.OnClickListener() {
					   public void onClick(DialogInterface dialog, int which) {
							      
					   }
					});
					alertDialog.setIcon(R.drawable.icon);
					alertDialog.show();

					
				}
			} else {

			}

		}	
	}

	private void exeJoin() {
		// TODO Auto-generated method stub
		final OptionsDialog dialog = new OptionsDialog(context);
		dialog.show();
	}

	private void exeLogin() {
		// TODO Auto-generated method stub
		SharedPreferences.Editor editor = prefs.edit();
		// Set default login type.
		editor.putString(Constants.LOGIN_TYPE,
				Constants.PERMPING_LOGIN);
		editor.commit();
		PermpingMain.showLogin();
	}

}

