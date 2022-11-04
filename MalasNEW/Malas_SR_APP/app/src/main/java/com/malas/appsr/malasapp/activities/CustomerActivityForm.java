package com.malas.appsr.malasapp.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.Amitlibs.net.HttpUrlConnectionJSONParser;
import com.Amitlibs.utils.TextUtils;

import com.malas.appsr.malasapp.BeanClasses.ProductInActivityBean;
import com.malas.appsr.malasapp.BeanClasses.ProductRatingBean;
import com.malas.appsr.malasapp.BeanClasses.ProductSelectedSpinnerAdapter;
import com.malas.appsr.malasapp.Constant;
import com.malas.appsr.malasapp.R;
import com.malas.appsr.malasapp.adapter.ProductSelectedRatingAdapter;
import com.malas.appsr.malasapp.adapter.ProductSoldSpinnerAdapter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Arwa on 22-Sep-18.
 */


public class CustomerActivityForm extends Activity implements View.OnClickListener {
    EditText edtCustomerName;
    EditText edtCustomerContact;
    EditText edtCustomerEmail;
    EditText edtProductSold;
    EditText edtSelectedCheckProduct;
    ListView lvProductSol;
    Button btnSubmit;
    ArrayList<String> productSoldList;
    ProductSoldSpinnerAdapter productSoldAdapter;
    ProductSelectedSpinnerAdapter productSelectedAdapter;
    ProductSelectedRatingAdapter productSelectedRatingAdapter;
    LinearLayout llProductSelected, llListviewProduct, llSubmitButton;
    ArrayList<ProductRatingBean> poductSelectedRatingList;
    private String productSoldValueSelected;
    private ArrayList<ProductInActivityBean> productToBeInActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_form);
        initView();
        btnSubmit.setOnClickListener(this);
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        edtCustomerName = findViewById(R.id.edt_cust_name);
        edtCustomerContact = findViewById(R.id.edt_cust_contact);
        edtCustomerEmail = findViewById(R.id.edt_cust_email);
        edtProductSold = findViewById(R.id.spnr_product_sold);
        edtSelectedCheckProduct = findViewById(R.id.spnr_select_product);
        lvProductSol = findViewById(R.id.lv_rating_act);

        btnSubmit = findViewById(R.id.btn_submit);
        llListviewProduct = findViewById(R.id.product_selected_list);
        llProductSelected = findViewById(R.id.product_select);
        llSubmitButton = findViewById(R.id.ll_submit);

        productSoldList = new ArrayList<>();
        productSoldList.add("Yes");
        productSoldList.add("No");


        edtProductSold.setOnClickListener(v -> {
            if (productSoldList != null) {
                final Dialog dialog = new Dialog(CustomerActivityForm.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.statelist_dialogbox);
                final EditText etserach = dialog.findViewById(R.id.edittext_dialog);
                final ListView productListView = dialog.findViewById(R.id.dialogbox_listview);
                dialog.show();
                productSoldAdapter = new ProductSoldSpinnerAdapter(CustomerActivityForm.this, productSoldList);
                productListView.setAdapter(productSoldAdapter);

                etserach.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String st = etserach.getText().toString();
                        if (!s.equals("") && s.length() > 0) {
                            productSoldAdapter.filter(st);
                        } else {
                            productSoldAdapter.filter(st);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                productListView.setOnItemClickListener((parent, view, position, id) -> {
                    String productSoldSelectedValues = ProductSoldSpinnerAdapter.resultArrayshort.get(position);
                    edtProductSold.setText(productSoldSelectedValues);
                    dialog.dismiss();
                    try {
                        productSoldValueSelected = productSoldList.get(position);
                        if (productSoldValueSelected.equals("Yes")) {
                            llProductSelected.setVisibility(View.VISIBLE);
                            llListviewProduct.setVisibility(View.GONE);
                            llSubmitButton.setVisibility(View.GONE);
                            //Call service of product list

                            new productList().execute("1");


                        } else if (productSoldValueSelected.equals("No")) {
                            llProductSelected.setVisibility(View.GONE);
                            llListviewProduct.setVisibility(View.GONE);
                            llSubmitButton.setVisibility(View.VISIBLE);


                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }

        });


        edtSelectedCheckProduct.setOnClickListener(v -> {
            if (productSoldList != null) {
                final Dialog dialog = new Dialog(CustomerActivityForm.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_multiple_option);
                final EditText etserach = dialog.findViewById(R.id.edittext_dialog);
                final ListView productListView = dialog.findViewById(R.id.dialogbox_listview);
                final Button productButton = dialog.findViewById(R.id.btn_done);
                dialog.show();
                productSelectedAdapter = new ProductSelectedSpinnerAdapter(CustomerActivityForm.this, productToBeInActivity);
                productListView.setAdapter(productSelectedAdapter);
                setListViewHeightBasedOnChildrenForproductSold(productListView);

                poductSelectedRatingList = new ArrayList<>();
                etserach.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String st = etserach.getText().toString();
                        if (!s.equals("") && s.length() > 0) {
                            productSelectedAdapter.filter(st);
                        } else {
                            productSelectedAdapter.filter(st);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                productButton.setOnClickListener(v1 -> {
                    edtSelectedCheckProduct.setText(productSelectedAdapter.mCheckStates.size() + " ITEM SELECTED");

                    for (int i = 0; i < productSelectedAdapter.mCheckStates.size(); i++) {

                        Toast.makeText(CustomerActivityForm.this, productToBeInActivity.get(productSelectedAdapter.mCheckStates.keyAt(i)).getProductName(), Toast.LENGTH_SHORT).show();
                        llListviewProduct.setVisibility(View.VISIBLE);

                        llSubmitButton.setVisibility(View.VISIBLE);

                        ProductRatingBean productRatingBean = new ProductRatingBean(productToBeInActivity.get(productSelectedAdapter.mCheckStates.keyAt(i)).getProductName());

                        ArrayList<String> ratingList;
                        ratingList = new ArrayList<>();
                        ratingList.add("1");
                        ratingList.add("2");
                        ratingList.add("3");
                        ratingList.add("4");
                        ratingList.add("5");
                        ratingList.add("6");
                        ratingList.add("7");
                        ratingList.add("8");
                        ratingList.add("9");
                        ratingList.add("10");
                        productRatingBean.setQty(ratingList);
                        productRatingBean.setTaste(ratingList);
                        productRatingBean.setPackaging(ratingList);
                        productRatingBean.setSku(productToBeInActivity.get(productSelectedAdapter.mCheckStates.keyAt(i)).getSku());

                        poductSelectedRatingList.add(productRatingBean);


                    }
                    productSelectedRatingAdapter = new ProductSelectedRatingAdapter(CustomerActivityForm.this, poductSelectedRatingList);
                    lvProductSol.setAdapter(productSelectedRatingAdapter);
                    setListViewHeightBasedOnChildren(lvProductSol);

                    dialog.dismiss();
                });


                productListView.setOnItemClickListener((parent, view, position, id) -> {
                    // String productSoldSelectedValues = ProductSelectedSpinnerAdapter.resultArrayshort.get(position);
                    //edtProductSold.setText(productSoldSelectedValues);

                    try {
                        productSoldValueSelected = productSoldList.get(position);
                        if (productSoldValueSelected.equals("Yes")) {
                            llProductSelected.setVisibility(View.VISIBLE);

                        } else if (productSoldValueSelected.equals("No")) {
                            llProductSelected.setVisibility(View.GONE);
                            llListviewProduct.setVisibility(View.GONE);
                            llSubmitButton.setVisibility(View.VISIBLE);


                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }

        });


    }

    private void setListViewHeightBasedOnChildren(ListView lvProductSol) {
        ProductSelectedRatingAdapter listAdapter = (ProductSelectedRatingAdapter) lvProductSol.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, lvProductSol);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = lvProductSol.getLayoutParams();
        params.height = totalHeight + (lvProductSol.getDividerHeight() * (listAdapter.getCount() - 1));
        lvProductSol.setLayoutParams(params);
        lvProductSol.requestLayout();
    }


    private void setListViewHeightBasedOnChildrenForproductSold(ListView lvProductSol) {
        ProductSelectedSpinnerAdapter listAdapter = (ProductSelectedSpinnerAdapter) lvProductSol.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, lvProductSol);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = lvProductSol.getLayoutParams();
        params.height = totalHeight + (lvProductSol.getDividerHeight() * (listAdapter.getCount() - 1));
        lvProductSol.setLayoutParams(params);
        lvProductSol.requestLayout();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_submit) {
            if (edtCustomerName.getText().toString().isEmpty()) {
                edtCustomerName.setError("Enter user name");
            } else if (!TextUtils.isValidMobileNo(edtCustomerContact.getText().toString().trim())) {
                edtCustomerContact.setError("Enter valid mobile no");
            } else if (!TextUtils.isValidEmail(edtCustomerEmail.getText().toString().trim())) {
                edtCustomerEmail.setError("Enter valid email");
            } else {
                String name = edtCustomerName.getText().toString();
                String email = edtCustomerEmail.getText().toString();
                String mobile = edtCustomerContact.getText().toString();
                String productSoldOption = edtProductSold.getText().toString();
                if (productSoldOption.equalsIgnoreCase("Yes")) {
                    JSONObject mJsonObject = new JSONObject();

                    JSONArray mJsonArray = new JSONArray();
                    boolean callApi = false;
                    if (productSelectedRatingAdapter != null) {
                        if (productSelectedRatingAdapter.getProductRatingList != null) {
                            if (productSelectedRatingAdapter.getProductRatingList.size() > 0) {
                                for (int i = 0; i < productSelectedRatingAdapter.getProductRatingList.size(); i++) {
                                    JSONObject mTempObj = new JSONObject();
                                    try {
                                        mTempObj.put("sku", productSelectedRatingAdapter.getProductRatingList.get(i).getSku());
                                        mTempObj.put("product_name", productSelectedRatingAdapter.getProductRatingList.get(i).getName());
                                        mTempObj.put("qty", productSelectedRatingAdapter.getProductRatingList.get(i).getQty());
                                        mTempObj.put("taste", productSelectedRatingAdapter.getProductRatingList.get(i).getTaste());
                                        mTempObj.put("packaging", productSelectedRatingAdapter.getProductRatingList.get(i).getPackaging());

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    mJsonArray.put(mTempObj);
                                    if (productSelectedRatingAdapter.getProductRatingList.get(i).getQty().equals("") || productSelectedRatingAdapter.getProductRatingList.get(i).getTaste().equals("") || productSelectedRatingAdapter.getProductRatingList.get(i).getPackaging().equals("")) {
                                        callApi = false;
                                        Toast.makeText(CustomerActivityForm.this, "Please Select Appropriate value", Toast.LENGTH_SHORT).show();
                                        break;
                                    } else {
                                        callApi = true;
                                        Toast.makeText(CustomerActivityForm.this, productSelectedRatingAdapter.getProductRatingList.get(i).getName() + " " + productSelectedRatingAdapter.getProductRatingList.get(i).getQty(), Toast.LENGTH_SHORT).show();
//
                                    }

                                }
                                try {
                                    mJsonObject.put("product_feedback", mJsonArray);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if (callApi) {
//call yes service

                                    new addCustomerDetail().execute("1", "12756", "1", "test", "test@g.com", "11111111", "Y", mJsonObject.toString());
                                }

                            }
                        }
                    }

                } else {
//call no service

                    new addCustomerDetail().execute("1", "12756", "1", "test", "test@g.com", "11111111", "N");
                }


            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class productList extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {

            mDialog = new Dialog(CustomerActivityForm.this);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Loading Activity List please wait");
            mDialog.setCancelable(false);
            mDialog.show();

        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "getProductListInActivity"));
            nameValuePair.add(new BasicNameValuePair("act_id", params[0]));
            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @Override
        protected void onPostExecute(JSONObject object) {
            if (object != null) {
                try {
                    if (object.getString("success").equalsIgnoreCase("true")) {
                        JSONArray mJsonArray = object.getJSONArray("productArray");
                        productToBeInActivity = new ArrayList<>();
                        for (int i = 0; i < mJsonArray.length(); i++) {

                            String sku = mJsonArray.getJSONObject(i).getString("SKU");
                            String product_name = mJsonArray.getJSONObject(i).getString("Product_Name");
                            ProductInActivityBean activitySRBean = new ProductInActivityBean(product_name, sku);
                            productToBeInActivity.add(activitySRBean);


                        }

                    } else {

                        //Utils.showAlertDialog(BroadcastActivity.this, "Broadcast Message", object.getString("message"), true);
                        Toast.makeText(CustomerActivityForm.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(CustomerActivityForm.this, "OOPS! Something went wrong", Toast.LENGTH_SHORT).show();

                    e.printStackTrace();
                }

            } else {

                // Utils.showAlertDialog(BroadcastActivity.this, "Broadcast Message", "Improper response from server", true);
                Toast.makeText(CustomerActivityForm.this, "Improper response from server", Toast.LENGTH_SHORT).show();
            }
            mDialog.dismiss();
        }

    }

    @SuppressLint("StaticFieldLeak")
    public class addCustomerDetail extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {

            mDialog = new Dialog(CustomerActivityForm.this);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Loading Activity List please wait");
            mDialog.setCancelable(false);
            mDialog.show();

        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "insertActivityCustomerDetail"));
            nameValuePair.add(new BasicNameValuePair("act_id", params[0]));
            nameValuePair.add(new BasicNameValuePair("outlet_id", params[1]));
            nameValuePair.add(new BasicNameValuePair("asm_id", params[2]));
            nameValuePair.add(new BasicNameValuePair("cus_name", params[3]));
            nameValuePair.add(new BasicNameValuePair("cus_email", params[4]));
            nameValuePair.add(new BasicNameValuePair("cus_contact", params[5]));
            nameValuePair.add(new BasicNameValuePair("sold", params[6]));
            if (params[6].equals("Y")) {
                nameValuePair.add(new BasicNameValuePair("dataList", params[7]));

            }
            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @Override
        protected void onPostExecute(JSONObject object) {
            if (object != null) {
                try {
                    if (object.getString("success").equalsIgnoreCase("true")) {
                        Toast.makeText(CustomerActivityForm.this, object.getString("message"), Toast.LENGTH_SHORT).show();


                    } else {

                        //Utils.showAlertDialog(BroadcastActivity.this, "Broadcast Message", object.getString("message"), true);
                        Toast.makeText(CustomerActivityForm.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(CustomerActivityForm.this, "OOPS! Something went wrong", Toast.LENGTH_SHORT).show();

                    e.printStackTrace();
                }

            } else {

                // Utils.showAlertDialog(BroadcastActivity.this, "Broadcast Message", "Improper response from server", true);
                Toast.makeText(CustomerActivityForm.this, "Improper response from server", Toast.LENGTH_SHORT).show();
            }
            mDialog.dismiss();
        }

    }

}
