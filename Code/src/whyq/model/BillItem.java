package whyq.model;

public class BillItem {

	private final String name;
	private final String photoUrl;
	private final int count;
	private final float price;
	
	public BillItem(String name, String photoUrl, int count, float price) {
		this.name = name;
		this.photoUrl = photoUrl;
		this.count = count;
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public int getCount() {
		return count;
	}

	public float getPrice() {
		return price;
	}
}
