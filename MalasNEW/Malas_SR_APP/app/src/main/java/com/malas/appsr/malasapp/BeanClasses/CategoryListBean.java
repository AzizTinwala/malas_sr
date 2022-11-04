package com.malas.appsr.malasapp.BeanClasses;

/**
 * Created by xyz on 9/27/2016.
 */
public class CategoryListBean {

    String category_name, id, status;
    String selectedOption = "";

    public CategoryListBean(String category_name, String id, String status, String selectedOption) {
        this.category_name = category_name;
        this.id = id;
        this.status = status;
        this.selectedOption = selectedOption;
    }

    public CategoryListBean( String id,String category_name) {
        this.category_name = category_name;
        this.id = id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(String selectedOption) {
        this.selectedOption = selectedOption;
    }

    @Override
    public String toString() {
        return "CategoryListBean{" +
                "category_name='" + category_name + '\'' +
                ", id='" + id + '\'' +
                ", status='" + status + '\'' +
                ", selectedOption='" + selectedOption + '\'' +
                '}';
    }
}
