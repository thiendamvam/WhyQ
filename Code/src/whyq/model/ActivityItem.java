package whyq.model;

public class ActivityItem {

	private final String subject;
	private final String action;

	public ActivityItem(String action, String subject) {
		this.action = action;
		this.subject = subject;
	}

	public String getAction() {
		return action;
	}

	public String getSubject() {
		return subject;
	}
}
