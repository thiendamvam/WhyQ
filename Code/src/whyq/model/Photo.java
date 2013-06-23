package whyq.model;

public class Photo {

	private static final String TAG_ID = "id";
	private static final String TAG_STORE_ID = "store_id";
	private static final String TAG_USER_ID = "user_id";
	private static final String TAG_COMMENT_ID = "comment_id";
	private static final String TAG_IMAGE = "image";
	private static final String TAG_THUMB = "thumb";
	private static final String TAG_STATUS = "status";
	private static final String TAG_TYPE_PHOTO = "type_photo";
	private static final String TAG_CREATEDATE = "createdate";
	private static final String TAG_UPDATEDATE = "updatedate";

	
	private String id;
	private String store_id;
	private String user_id;
	private String comment_id;
	private String image;
	private String thumb;
	private String status;
	private String type_photo;
	private String createdate;
	private String updatedate;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStore_id() {
		return store_id;
	}
	public void setStore_id(String store_id) {
		this.store_id = store_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getComment_id() {
		return comment_id;
	}
	public void setComment_id(String comment_id) {
		this.comment_id = comment_id;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getThumb() {
		return thumb;
	}
	public void setThumb(String thumb) {
		this.thumb = thumb;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getType_photo() {
		return type_photo;
	}
	public void setType_photo(String type_photo) {
		this.type_photo = type_photo;
	}
	public String getCreatedate() {
		return createdate;
	}
	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}
	public String getUpdatedate() {
		return updatedate;
	}
	public void setUpdatedate(String updatedate) {
		this.updatedate = updatedate;
	}

	
}
