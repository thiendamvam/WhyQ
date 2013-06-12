package whyq.model;

public class ActivityItem {

	public enum ActivityType {
		like, comment
	}

	private final String name;
	private final ActivityType type;

	public ActivityItem(ActivityType type, String name) {
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public ActivityType getType() {
		return type;
	}
}
