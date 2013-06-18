package whyq.mockup;

import java.util.ArrayList;
import java.util.List;

import whyq.model.BillItem;
import whyq.model.Comment;
import whyq.model.User;
import whyq.model.WhyqBoard;
import whyq.model.WhyqImage;

public class MockupDataLoader {

	static final String mockAvatarUrl = "https://lh6.googleusercontent.com/ZLodIZroN3vx7fhh_rCT4-elTezW0sj3aX-v-Ymrt3c=w37-h39-p-no";
	static final String mockThumUrl = "http://205.196.120.211/c5r6b9wwxbeg/0x81ecbp1ruf4r9/thumb.png";

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
			Thread.sleep(2000);
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
		return comment;
	}

	public static List<BillItem> loadBills() {
		try {
			Thread.sleep(2000);
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
	
//	public static List<ActivityItem> loadActivities() {
//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		List<ActivityItem> mItems = new ArrayList<ActivityItem>();
//		for (int i = 0; i < 10; i++) {
//			if (i % 2 == 0) {
//				mItems.add(new ActivityItem("đã kết bạn với", "Vit con"));
//			} else {
//				mItems.add(new ActivityItem("đã kết bạn với", "Andy Nguyen"));
//			}
//		}
//		return mItems;
//	}
}
