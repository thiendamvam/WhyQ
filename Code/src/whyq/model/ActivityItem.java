package whyq.model;

public class ActivityItem {

	private String id;
	private String user_id;
	private String active_id;
	private String optional_id;
	private String activity_type;
	private String message;
	private String is_read;
	private String createdate;
	private String updatedate;
	private String user_name_active;
	private Comment comment;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getActive_id() {
		return active_id;
	}
	public void setActive_id(String active_id) {
		this.active_id = active_id;
	}
	public String getOptional_id() {
		return optional_id;
	}
	public void setOptional_id(String optional_id) {
		this.optional_id = optional_id;
	}
	public String getActivity_type() {
		return activity_type;
	}
	public void setActivity_type(String activity_type) {
		this.activity_type = activity_type;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getIs_read() {
		return is_read;
	}
	public void setIs_read(String is_read) {
		this.is_read = is_read;
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
	public String getUser_name_active() {
		return user_name_active;
	}
	public void setUser_name_active(String user_name_active) {
		this.user_name_active = user_name_active;
	}
	public Comment getComment() {
		return comment;
	}
	public void setComment(Comment comment) {
		this.comment = comment;
	}
	public BusinessInfo getBusiness_info() {
		return business_info;
	}
	public void setBusiness_info(BusinessInfo business_info) {
		this.business_info = business_info;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	private BusinessInfo business_info;
	private String user_name;
}
