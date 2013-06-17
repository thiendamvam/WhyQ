package whyq.model;

public class FriendFacebook {

	private String facebookId;
	private String ituneUrl;
	private String firstName;
	private String id;
	private int isFriend;
	private String avatar;

	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}

	public void setItuneUrl(String ituneUrl) {
		this.ituneUrl = ituneUrl;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setIsFriend(int isFriend) {
		this.isFriend = isFriend;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getFacebookId() {
		return facebookId;
	}

	public String getItuneUrl() {
		return ituneUrl;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getId() {
		return id;
	}

	public int getIsFriend() {
		return isFriend;
	}

	public String getAvatar() {
		return avatar;
	}

}
