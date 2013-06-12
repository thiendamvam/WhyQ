package whyq.model;

public class FacebookFriend {

	private final User user;
	private final boolean isFriend;
	private final boolean isWhyQMember;

	public FacebookFriend(User user, boolean isFriend, boolean isWhyQMember) {
		this.user = user;
		this.isFriend = isFriend;
		this.isWhyQMember = isWhyQMember;
	}

	public User getUser() {
		return this.user;
	}

	public boolean isFriend() {
		return isFriend;
	}

	public boolean isWhyQMember() {
		return isWhyQMember;
	}
}
