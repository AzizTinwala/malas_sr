package com.malas.appsr.malasapp.BeanClasses;

/**
 * Created by Arwa on 25-Sep-18.
 */

public class ProductInActivityBean {
    String productId;
    String ProductName;
    String categoryId;
    String catergoryName;
    String sku;

    public ProductInActivityBean( String productName, String sku) {

        ProductName = productName;

        this.sku = sku;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCatergoryName() {
        return catergoryName;
    }

    public void setCatergoryName(String catergoryName) {
        this.catergoryName = catergoryName;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }
}
