package whyq.mockup;

import java.util.ArrayList;
import java.util.List;

import whyq.model.ActivityItem;
import whyq.model.BillItem;
import whyq.model.Comment;
import whyq.model.FacebookFriend;
import whyq.model.User;
import whyq.model.WhyqBoard;
import whyq.model.WhyqImage;
import whyq.model.ActivityItem.ActivityType;

public class MockupDataLoader {

	static final String mockAvatarUrl = "http://205.196.121.136/2769ds3m3s9g/5grk18uexk6e3zr/avatar.jpg";
	static final String mockThumUrl = "http://205.196.123.198/8zcwfkywoftg/0x81ecbp1ruf4r9/thumb.png";

	public static List<FacebookFriend> loadFacebookFriends() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		List<FacebookFriend> mItems = new ArrayList<FacebookFriend>();
		for (int i = 0; i < 10; i++) {
			mItems.add(newFriend(i));
		}
		return mItems;
	}

	private static FacebookFriend newFriend(int i) {
		if (i == 0) {
			return new FacebookFriend(newUser(i), false, true);
		} else {
			return new FacebookFriend(newUser(i), false, false);
		}
	}

	private static User newUser(int i) {
		return new User("id" + i, "Sample name " + i, new WhyqImage(
				mockAvatarUrl), i, i, i, i, i, newWhyqBoard(i));
	}

	private static List<WhyqBoard> newWhyqBoard(int i) {
		List<WhyqBoard> result = new ArrayList<WhyqBoard>();
		result.add(new WhyqBoard("id" + i));
		return result;
	}

	public static List<Comment> loadComments() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		List<Comment> mItems = new ArrayList<Comment>();
		for (int i = 0; i < 10; i++) {
			mItems.add(newComment(i));
		}
		return mItems;
	}

	private static Comment newComment(int i) {
		Comment comment = new Comment("id " + i,
				"This is just a sample for testing comment list. This comment is number "
						+ i);
		comment.setAuthor(newUser(i));
		comment.setThumbUrl(mockThumUrl);
		return comment;
	}

	public static List<BillItem> loadBills() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		List<BillItem> mItems = new ArrayList<BillItem>();
		for (int i = 0; i < 10; i++) {
			mItems.add(newBill(i));
		}
		return mItems;
	}

	private static BillItem newBill(int i) {
		return new BillItem("Bill name " + i, mockAvatarUrl, i, i);
	}
	
	public static List<ActivityItem> loadActivities() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		List<ActivityItem> mItems = new ArrayList<ActivityItem>();
		for (int i = 0; i < 10; i++) {
			if (i % 2 == 0) {
				mItems.add(new ActivityItem(ActivityType.comment, "Van Pham Comment on Sushi Bar"));
			} else {
				mItems.add(new ActivityItem(ActivityType.like, "Van Pham liked a comment of on Sushi Bar"));
			}
		}
		return mItems;
	}
}
