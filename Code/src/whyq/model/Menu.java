package whyq.model;

import java.util.ArrayList;

public class Menu {
	String id;
	String storeId;
	String nameProduct;
	String value;
	String valuePromotion;
	String imageProduct;
	String imageThumb;
	String status;
	String sort;
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
	public String getNameProduct() {
		return nameProduct;
	}
	public void setNameProduct(String nameProduct) {
		this.nameProduct = nameProduct;
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
	public void setProductTypeInfo(ProductTypeInfo productTypeInfo) {
		this.productTypeInfo = productTypeInfo;
	}
	public StoreInfo getStoreInfo() {
		return storeInfo;
	}
	public void setStoreInfo(StoreInfo storeInfo) {
		this.storeInfo = storeInfo;
	}
	String typeProductId;
	String createDate;
	String updateData;
	ProductTypeInfo productTypeInfo;
	StoreInfo storeInfo;
}
