package whyq.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Menu implements Serializable{
	String id;
	String storeId;
	private String groupName;
	private String groupId;
	private String nameProduct;
	private ArrayList<ProductTypeInfo> productTypeInfoList;
	private List<SizeItem> sizeItemList;
	private List<ExtraItem> extraItemList;
	private List<OptionItem> optionItemList;
	String value;
	String valuePromotion;
	String imageProduct;
	String imageThumb;
	String status;
	String sort;
	private int unitForBill =0;
	private String note;
	private int countFavorite;
	private boolean isFavorite;
	
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getValuePromotion() {
		return valuePromotion;
	}
	public void setValuePromotion(String valuePromotion) {
		this.valuePromotion = valuePromotion;
	}
	public String getImageProduct() {
		return imageProduct;
	}
	public void setImageProduct(String imageProduct) {
		this.imageProduct = imageProduct;
	}
	public String getImageThumb() {
		return imageThumb;
	}
	public void setImageThumb(String imageThumb) {
		this.imageThumb = imageThumb;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTypeProductId() {
		return typeProductId;
	}
	public void setTypeProductId(String typeProductId) {
		this.typeProductId = typeProductId;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getUpdateData() {
		return updateData;
	}
	public void setUpdateData(String updateData) {
		this.updateData = updateData;
	}
	public ProductTypeInfo getProductTypeInfo() {
		return productTypeInfo;
	}

	public StoreInfo getStoreInfo() {
		return storeInfo;
	}
	public void setStoreInfo(StoreInfo storeInfo) {
		this.storeInfo = storeInfo;
	}

	/**
	 * @return the productTypeInfoList
	 */
	public ArrayList<ProductTypeInfo> getProductTypeInfoList() {
		return productTypeInfoList;
	}
	/**
	 * @param productTypeInfoList the productTypeInfoList to set
	 */
	public void setProductTypeInfoList(ArrayList<ProductTypeInfo> productTypeInfoList) {
		this.productTypeInfoList = productTypeInfoList;
	}
	/**
	 * @return the nameProduct
	 */
	public String getNameProduct() {
		return nameProduct;
	}
	/**
	 * @param nameProduct the nameProduct to set
	 */
	public void setNameProduct(String nameProduct) {
		this.nameProduct = nameProduct;
	}
	/**
	 * @return the extraItemList
	 */
	public List<ExtraItem> getExtraItemList() {
		return extraItemList;
	}
	/**
	 * @param extraItemList the extraItemList to set
	 */
	public void setExtraItemList(List<ExtraItem> extraItemList) {
		this.extraItemList = extraItemList;
	}
	/**
	 * @return the sizeItemList
	 */
	public List<SizeItem> getSizeItemList() {
		return sizeItemList;
	}
	/**
	 * @param sizeItemList the sizeItemList to set
	 */
	public void setSizeItemList(List<SizeItem> sizeItemList) {
		this.sizeItemList = sizeItemList;
	}
	/**
	 * @return the optionItemList
	 */
	public List<OptionItem> getOptionItemList() {
		return optionItemList;
	}
	/**
	 * @param optionItemList the optionItemList to set
	 */
	public void setOptionItemList(List<OptionItem> optionItemList) {
		this.optionItemList = optionItemList;
	}
	/**
	 * @return the unitForBill
	 */
	public int getUnitForBill() {
		return unitForBill;
	}
	/**
	 * @param unitForBill the unitForBill to set
	 */
	public void setUnitForBill(int unitForBill) {
		this.unitForBill = unitForBill;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public boolean isFavorite() {
		return isFavorite;
	}
	public void setFavorite(boolean isFavorite) {
		this.isFavorite = isFavorite;
	}
	public int getCountFavorite() {
		return countFavorite;
	}
	public void setCountFavorite(int countFavorite) {
		this.countFavorite = countFavorite;
	}
	String typeProductId;
	String createDate;
	String updateData;
	ProductTypeInfo productTypeInfo;
	StoreInfo storeInfo;
}
