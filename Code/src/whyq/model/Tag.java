package whyq.model;

public class Tag {
	private String id;
	private String nameTag;
	private int countUsed;
	private String createData;
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the countUsed
	 */
	public int getCountUsed() {
		return countUsed;
	}
	/**
	 * @param countUsed the countUsed to set
	 */
	public void setCountUsed(int countUsed) {
		this.countUsed = countUsed;
	}
	/**
	 * @return the nameTag
	 */
	public String getNameTag() {
		return nameTag;
	}
	/**
	 * @param nameTag the nameTag to set
	 */
	public void setNameTag(String nameTag) {
		this.nameTag = nameTag;
	}
	/**
	 * @return the createData
	 */
	public String getCreateData() {
		return createData;
	}
	/**
	 * @param createData the createData to set
	 */
	public void setCreateData(String createData) {
		this.createData = createData;
	}
}
