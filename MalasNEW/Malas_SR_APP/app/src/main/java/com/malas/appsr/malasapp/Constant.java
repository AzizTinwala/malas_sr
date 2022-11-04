package com.malas.appsr.malasapp;

import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

public class Constant {
    public static ArrayList<Integer> itempostion = new ArrayList<>();
    public static ArrayList<Integer> itempostionDistrict = new ArrayList<>();

//    public static final String BASE_URL = "http://168.235.88.106/Malas/Api/mainClass.php";
    public static final String BASE_URL = "https://erp.malasportal.in/mportal/Api/mainClass.php";
   // public static final String BASE_URL2 = "https://www.malasportal.in/mportal/Api/expense/";
    public static final String BASE_URL2 = "https://app.malasportal.in/expense/";
  public static final String BASE_URL3 = "https://app.malasportal.in/expense/mainClass.php";

    public static String DEVICETYPE = "android";

    public static String USER_GOOGLE_GCM_REG_INFO = "userGoogleGCMPref";
    public static String USER_Google_GCM_ID = "userGoogleGCM_Id";


    public static String CITY_DISTIC_COUNTRY_PREF = "city_distic_country_pref";
    public static String CITY_OBJ = "city_obj";
    public static String DISTIC_OBJ = "distic_obj";
    public static String distributor_id = "distic_id";
    public static String COUNTRY_OBJ = "country_obj";

    public static String UserRegInfoPref = "userreginfopref";
    public static String UserRegInfoObj = "userreginfoobj";

    public static String DISTRIBUTOR_LIST_PREF = "distributor_list_pref";
    public static String DISTRIBUTOR_LIST_OBJ = "distributor_list_obj";



    public static String ATTENDANCE_LIST_PREF = "attendance_list_pref";
    public static String ATTENDANCE_LIST_OBJ = "attendance_list_obj";

    public static String ROUTE_LIST_PREF = "route_list_pref";
    public static String ROUTE_LIST_OBJ = "route_list_obj";

    public static String Retailer_LIST_PREF = "Retailerr_list_pref";
    public static String Retailer_LIST_OBJ = "Retailer_list_obj";

    public static String SHOW_OUTLET_PREF = "show_outlet_pref";
    public static String SHOW_OUTLET_OBJ = "show_outlet_obj";

    public static String ProductListPref = "productlistpref";
    public static String ProductListObj = "productlistobj";
    public static String ProductListStockObj = "productliststockobj";
    public static String takestocklistPref = "takestocklistPref";
    public static String takestocklistPrefobj = "takestocklistPrefobj";
    public static String takeplaceorderPref = "takeplaceorderPref";
    public static String takeplaceorderPrefobj = "takeplaceorderPrefobj";
    public static String EditedProductListPref = "editedproductlistpref";
    public static String EditedProductListObj = "editedproductlistobj";

    public static String EditedOrderProductListPref = "editedorderproductlistpref";
    public static String EditedOrderProductListObj = "editedorderproductlistobj";

    public static String SHOW_OUTLET_ORDER_PREF = "show_outlet_order_pref";
    public static String SHOW_OUTLET_ORDER_OBJ = "show_outlet_order_obj";
    public static String SHOW_OUTLET_ORDER_DATE = "show_outlet_order_date_obj";
    public static String SHOW_OUTLET_ORDER_Outlet_ID = "show_outlet_order_outlet_id_obj";
    public static String SHOW_OUTLET_ORDER_Selected_distributer = "show_outlet_order_selected_distributor";
    public static String SHOW_OUTLET_ORDER_Selected_route = "show_outlet_order_outlet_selected_roue";

    public static String PlaceOrderProductListPref= "place_order_list_pref";
    public static String PlaceOrderProductListObj= "place_order_list_obj";
    public static String PlaceOrderProductList= "place_order_list_obj_temp";
    public static String PlaceOrderProductListTempPref= "place_order_list_obj_temp_pref";


    public static String REASON_LIST_PREF = "reason_list_pref";
    public static String REASON_LIST_OBJ = "reason_list_obj";

    public static double LATITUDE = 0;
    public static double LONGITUDE = 0;
    public static String ADDRESS = "";
    public static GoogleApiClient mGoogleApiClient;
    public static String PREVIOUS_LIST_PREF="previous_list_pref";
    public static String PREVIOUS_LIST_OBJ="previous_list_obj";
    public static String Update_DSR_PREF="update_dsr_status_PREF";
    public static String Update_DSR_OBJ="update_dsr_status";

    public static String ATTENDANCE_REASON_LIST_PREF = "attendance_reason_list_pref";
    public static String ATTENDANCE_REASON_LIST_OBJ = "attendance_reason_list_obj";
}