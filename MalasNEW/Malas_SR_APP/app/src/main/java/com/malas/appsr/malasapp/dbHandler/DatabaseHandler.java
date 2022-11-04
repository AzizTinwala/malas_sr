package com.malas.appsr.malasapp.dbHandler;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.malas.appsr.malasapp.AttendanceReasonBean;
import com.malas.appsr.malasapp.BeanClasses.AttendanceBean;
import com.malas.appsr.malasapp.BeanClasses.DistributerBean;
import com.malas.appsr.malasapp.BeanClasses.FocusedProductBean;
import com.malas.appsr.malasapp.BeanClasses.JointWorkBean;
import com.malas.appsr.malasapp.BeanClasses.OutletOrderDateBean;
import com.malas.appsr.malasapp.BeanClasses.OutletOrdersBean;
import com.malas.appsr.malasapp.BeanClasses.ReasonBean;
import com.malas.appsr.malasapp.BeanClasses.ReasonSubmitBean;
import com.malas.appsr.malasapp.BeanClasses.RouteBean;
import com.malas.appsr.malasapp.BeanClasses.SaveData;
import com.malas.appsr.malasapp.BeanClasses.ShowOutLetBeen;
import com.malas.appsr.malasapp.BeanClasses.StockPlaced;
import com.malas.appsr.malasapp.BeanClasses.TakeOutletOrderItemBean;
import com.malas.appsr.malasapp.BeanClasses.TakeOutletOrderListBean;
import com.malas.appsr.malasapp.BeanClasses.TargetAchievementProgress;
import com.malas.appsr.malasapp.BeanClasses.TotalOutletCountHome;

import java.util.ArrayList;


public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "malasData";

    //  table name
    private static final String TABLE_DISTRIBUTOR = "distributor";
    private static final String TABLE_ROUTE = "route";
    private static final String TABLE_OUTLET = "outlet";
    // private static final String TABLE_STOCK_PLACED = "stock_placed";
    private static final String TABLE_OUTLET_ORDERS_LAST_DATE = "order_date";
    private static final String TABLE_OUTLET_ORDERS = "orders";
    private static final String TABLE_OUTLET_ORDERS_CATEGORY = "category";
    private static final String TABLE_OUTLET_ORDERS_PRODUCT = "product";
    private static final String TABLE_OUTLET_ORDERS_SAVE_DATA = "save_table";
    private static final String TABLE_OUTLET_ORDERS_SAVE_PRODUCT = "product_save_table";
    private static final String TABLE_OUTLET_ORDERS_EDIT_SAVE_DATA = "edit_save_table";
    private static final String TABLE_OUTLET_ORDERS_EDIT_SAVE_PRODUCT = "edit_product_save_table";
    private static final String TABLE_REASON_LIST = "reason_list";
    private static final String TABLE_REASON_SUBMIT = "submit_reason";
    private static final String TABLE_OUTLET_ORDERS_NOT_FOUND = "orders_not_found";
    private static final String TABLE_ISORDER_ISSTOCK = "isorderplaced_stock";
    private static final String TABLE_ISORDER_OUTLETWISE_PLACED = "isorderoutletwiseplaced";
    private static final String TABLE_FOCUSED_PRODUCT = "focused_product_table";
    private static final String TABLE_PRODUCTIVITY_COUNT_HOME = "productivity_count_table";
    private static final String TABLE_TARGET_ACHIEVEMENT_HOME = "target_achievement_progress";
    private static final String TABLE_OUTLET_COUNT_HOME = "outlet_count_home";
    private static final String TABLE_DISTRIBUTOR_COUNT_HOME = "ds_count_home";
    private static final String TABLE_ATTENDANCE_STATUS = "attendance_status";
    private static final String TABLE_ATTENDANCE_REASON_LIST = "attendance_reason_list";
    private static final String TABLE_TODAY_ATTENDANCE_MARKED = "attendance_marked_today";
    private static final String TABLE_ATTENDANCE = "attendance_details";
    private static final String TABLE_JOINTWORK = "jointwork_list";
    private static final String TABLE_ATTENDANCE_UPDATE_ROWID = "attendance_update_rowId";
    private static final String TABLE_SR_STATUS_BY_OUTLET_ID = "sr_status_by_outlet_id";
    private static final String TABLE_SR_STATUS_COUNT_OUTLET_ID = "sr_status_by_count_outlet_id";

    //  Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_DISTRIBUTOR_ID = "distribution_id";
    private static final String KEY_FIRM_NAME = "firmname";
    private static final String KEY_ROUTE_ID = "route_id";
    private static final String KEY_ROUTE_NAME = "route_name";
    private static final String KEY_OUTLET_ID = "outlet_id";
    private static final String KEY_OUTLET_NAME = "outlet_name";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_DESIGNATION_ID = "designation_id";
    private static final String KEY_ORDER_DATE = "order_date";
    private static final String KEY_HIGHLIGHT = "highlight_or_not";
    private static final String KEY_ORDER_UNIQUE_ID = "order_unique_id";
    private static final String KEY_ORDER_TAKE_TIME = "order_take_time";
    private static final String KEY_ORDER_TIME_IN_LONG = "order_time_long";
    private static final String KEY_IS_ORDER_PLACED = "is_order_placed";
    private static final String KEY_WITH_SSO = "with_sso";
    private static final String KEY_WITH_ASM = "with_asm";
    private static final String KEY_CAT_ID = "cat_id";
    private static final String KEY_CAT_NAME = "cat_name";
    private static final String KEY_PRODUCT_ID = "product_id";
    private static final String KEY_PRODUCT_NAME = "product_name";
    private static final String KEY_PRODUCT_QUANTITY = "product_quantity";
    private static final String KEY_PRODUCT_MRP = "product_mrp";
    private static final String KEY_SKU_CODE = "sku_code";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_LAT = "latitude";
    private static final String KEY_LONG = "longitude";
    private static final String KEY_REASON_ID = "reason_id";
    private static final String KEY_REASON = "reason";
    private static final String KEY_DISTRIBUTOR_NAME = "distributor_name";
    private static final String KEY_IS_STOCK_TAKEN = "is_stock_taken";
    private static final String KEY_FOCUSED_PRODUCT_NAME = "focused_product_name";

    private static final String KEY_PRODUCTIVITY_PERCENT_HOME = "total_productivity_percent";
    private static final String KEY_DATE = "date";
    private static final String KEY_MONTH = "month";
    private static final String KEY_YEAR = "year";
    private static final String KEY_PROGRESS_VALUE = "progress_value_productivity";
    private static final String KEY_TARGET_PROGRESS_VALUE = "target_value";
    private static final String KEY_ACHIEVEMENT_PROGRESS_VALUE = "achievement_value";
    private static final String KEY_TARGET_ACHE_PROGRESS_VALUE = "progress_target_achievement";
    private static final String KEY_PERCENT_TARGET_ACHIEVEEMNT = "percent_target_achievement";
    private static final String KEY_TOTAL_OUTLET_COUNT = "total_outlet_count";
    private static final String KEY_TOTAL_BILLED_OUTLET_COUNT = "total_billed_outlet_count";
    private static final String KEY_TOTAL_UNBILLED_OUTLET_COUNT = "total_unbilled_outlet_count";
    private static final String KEY_TOTAL_DS_COUNT = "total_ds_count";

    private static final String KEY_TOTAL_BILLED_DS_COUNT = "total_billed_ds_count";
    private static final String KEY_TOTAL_UNBILLED_DS_COUNT = "total_unbilled_ds_count";
    private static final String KEY_ATTENDANCE_TYPE = "attendance_type";
    private static final String KEY_ATTENDANCE_REASON = "attendance_reason";
    private static final String KEY_ATTENDANCE_ID = "attendance_reason_id";
    private static final String KEY_ATTENDANCE_STATUS_MESSAGE = "attendance_status_message";
    private static final String KEY_IS_TODAY_ATTENDANCE_MARKED = "attendance_marked";

    private static final String KEY_ATTENDANCE_DSR_STATUS = "attendance_dsr_status";
    private static final String KEY_ATTENDANCE = "attendance";
    private static final String KEY_ATTENDANCE_DATE = "attendance_date";
    private static final String KEY_ATTENDANCE_TYPE_ID = "attendance_type_id";
    private static final String KEY_ATTENDANCE_ROW_ID = "attendance_row_id";
    private static final String KEY_COUNT = "count";
    private static final String KEY_WOCOUNT = "count";
    private static final String KEY_WORK_WITH_USER = "joint_user_id";
    private static final String KEY_WORK_WITH_DESIGNATION = "joint_user_desig";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("Create", "Created");
        String CREATE_DISTRIBUTOR_TABLE = "CREATE TABLE " + TABLE_DISTRIBUTOR + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_DISTRIBUTOR_ID + " TEXT," + KEY_FIRM_NAME + " TEXT" + ")";
        db.execSQL(CREATE_DISTRIBUTOR_TABLE);


        String CREATE_ROUTE_TABLE = "CREATE TABLE " + TABLE_ROUTE + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_DISTRIBUTOR_ID + " TEXT," + KEY_ROUTE_ID + " TEXT," + KEY_ROUTE_NAME + " TEXT" + ")";
        db.execSQL(CREATE_ROUTE_TABLE);

        String CREATE_OUTLET_TABLE = "CREATE TABLE " + TABLE_OUTLET + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_OUTLET_ID + " TEXT," + KEY_OUTLET_NAME + " TEXT," + KEY_DISTRIBUTOR_ID + " TEXT," + KEY_DISTRIBUTOR_NAME + " TEXT," + KEY_ROUTE_ID + " TEXT," + KEY_ROUTE_NAME + " TEXT" + ")";


        db.execSQL(CREATE_OUTLET_TABLE);

      /*  String CREATE_STOCK_PLACED_TABLE = "CREATE TABLE " + TABLE_STOCK_PLACED + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_DISTRIBUTOR_ID + " TEXT," + KEY_USER_ID + " TEXT," + KEY_STOCK_PLACED + " TEXT" + ")";
        db.execSQL(CREATE_STOCK_PLACED_TABLE);
*/

        String CREATE_OUTLET_ORDER_DATE_TABLE = "CREATE TABLE " + TABLE_OUTLET_ORDERS_LAST_DATE + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_DISTRIBUTOR_ID + " TEXT," + KEY_ROUTE_ID + " TEXT," + KEY_OUTLET_ID + " TEXT," + KEY_ORDER_DATE + " TEXT," + KEY_HIGHLIGHT + " TEXT" + ")";
        db.execSQL(CREATE_OUTLET_ORDER_DATE_TABLE);

        String CREATE_OUTLET_ORDER__TABLE = "CREATE TABLE " + TABLE_OUTLET_ORDERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_DISTRIBUTOR_ID + " TEXT," + KEY_OUTLET_ID + " TEXT," + KEY_ORDER_UNIQUE_ID + " TEXT," + KEY_ORDER_TAKE_TIME + " TEXT," + KEY_ORDER_TIME_IN_LONG + " TEXT," + KEY_IS_ORDER_PLACED + " TEXT," + KEY_WITH_SSO + " TEXT," + KEY_WITH_ASM + " TEXT" + ")";
        db.execSQL(CREATE_OUTLET_ORDER__TABLE);

        String CREATE_CATEGORY__TABLE = "CREATE TABLE " + TABLE_OUTLET_ORDERS_CATEGORY + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_ORDER_UNIQUE_ID + " TEXT ," + KEY_CAT_ID + " TEXT," + KEY_CAT_NAME + " TEXT, " + KEY_OUTLET_ID + " TEXT " + ")";
        db.execSQL(CREATE_CATEGORY__TABLE);

        String CREATE_PRODUCT__TABLE = "CREATE TABLE " + TABLE_OUTLET_ORDERS_PRODUCT + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_CAT_ID + " TEXT," + KEY_PRODUCT_ID + " TEXT ," + KEY_PRODUCT_NAME + " TEXT," + KEY_PRODUCT_QUANTITY + " TEXT, " + KEY_PRODUCT_MRP + " TEXT, " + KEY_SKU_CODE + " TEXT, " + KEY_OUTLET_ID + " TEXT, " + KEY_ORDER_UNIQUE_ID + " TEXT ," + KEY_CAT_NAME + " TEXT " + ")";
        db.execSQL(CREATE_PRODUCT__TABLE);

        String CREATE_SAVE_DATA__TABLE = "CREATE TABLE " + TABLE_OUTLET_ORDERS_SAVE_DATA + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_DISTRIBUTOR_ID + " TEXT," + KEY_USER_ID + " TEXT ," + KEY_OUTLET_ID + " TEXT," + KEY_LAT + " TEXT, " + KEY_LONG + " TEXT, " + KEY_ADDRESS + " TEXT, " + KEY_WITH_SSO + " TEXT, " + KEY_WITH_ASM + " TEXT," + KEY_ORDER_TAKE_TIME + "TEXT" + ")";
        db.execSQL(CREATE_SAVE_DATA__TABLE);

        String CREATE_PRODUCT_SAVE_TABLE = "CREATE TABLE " + TABLE_OUTLET_ORDERS_SAVE_PRODUCT + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_CAT_ID + " TEXT," + KEY_PRODUCT_ID + " TEXT ," + KEY_PRODUCT_NAME + " TEXT," + KEY_PRODUCT_QUANTITY + " TEXT, " + KEY_OUTLET_ID + " TEXT, " + KEY_DISTRIBUTOR_ID + " TEXT ," + KEY_CAT_NAME + " TEXT" + ")";
        db.execSQL(CREATE_PRODUCT_SAVE_TABLE);

        String CREATE_EDIT_SAVE_DATA__TABLE = "CREATE TABLE " + TABLE_OUTLET_ORDERS_EDIT_SAVE_DATA + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_DISTRIBUTOR_ID + " TEXT," + KEY_USER_ID + " TEXT ," + KEY_OUTLET_ID + " TEXT," + KEY_LAT + " TEXT, " + KEY_LONG + " TEXT, " + KEY_ADDRESS + " TEXT, " + KEY_WITH_SSO + " TEXT, " + KEY_WITH_ASM + " TEXT," + KEY_ORDER_UNIQUE_ID + " TEXT," + KEY_ORDER_TAKE_TIME + "TEXT" + ")";
        db.execSQL(CREATE_EDIT_SAVE_DATA__TABLE);

        String CREATE_EDIT_PRODUCT_SAVE_TABLE = "CREATE TABLE " + TABLE_OUTLET_ORDERS_EDIT_SAVE_PRODUCT + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_CAT_ID + " TEXT," + KEY_PRODUCT_ID + " TEXT ," + KEY_PRODUCT_NAME + " TEXT," + KEY_PRODUCT_QUANTITY + " TEXT, " + KEY_OUTLET_ID + " TEXT, " + KEY_DISTRIBUTOR_ID + " TEXT ," + KEY_CAT_NAME + " TEXT" + ")";
        db.execSQL(CREATE_EDIT_PRODUCT_SAVE_TABLE);


        String CREATE_REASON_LIST_TABLE = "CREATE TABLE " + TABLE_REASON_LIST + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_REASON_ID + " TEXT," + KEY_REASON + " TEXT" + ")";
        db.execSQL(CREATE_REASON_LIST_TABLE);


        String CREATE_REASON_SUBMIT_TABLE = "CREATE TABLE " + TABLE_REASON_SUBMIT + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_USER_ID + " TEXT," + KEY_OUTLET_ID + " TEXT ," + KEY_REASON_ID + " TEXT," + KEY_DISTRIBUTOR_ID + " TEXT, " + KEY_LAT + " TEXT, " + KEY_LONG + " TEXT ," + KEY_ADDRESS + " TEXT," + KEY_OUTLET_NAME + " TEXT ," + KEY_REASON + " TEXT," + KEY_ROUTE_ID + " TEXT" + ")";
        db.execSQL(CREATE_REASON_SUBMIT_TABLE);


        String CREATE_ORDER_OUTLET_NOT_FOUND_TABLE = "CREATE TABLE " + TABLE_OUTLET_ORDERS_NOT_FOUND + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_OUTLET_ID + " TEXT" + ")";
        db.execSQL(CREATE_ORDER_OUTLET_NOT_FOUND_TABLE);

        String CREATE_IS_ORDER_PLACED_STOCK_TABLE = "CREATE TABLE " + TABLE_ISORDER_ISSTOCK + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_DISTRIBUTOR_ID + " TEXT," + KEY_USER_ID + " TEXT," + KEY_IS_STOCK_TAKEN + " TEXT ," + KEY_IS_ORDER_PLACED + " TEXT" + ")";
        db.execSQL(CREATE_IS_ORDER_PLACED_STOCK_TABLE);

        String CREATE_IS_ORDER_PLACED_OUTLET_WISE = "CREATE TABLE " + TABLE_ISORDER_OUTLETWISE_PLACED + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_DISTRIBUTOR_ID + " TEXT," + KEY_USER_ID + " TEXT," + KEY_OUTLET_ID + " TEXT ," + KEY_IS_ORDER_PLACED + " TEXT" + ")";
        db.execSQL(CREATE_IS_ORDER_PLACED_OUTLET_WISE);


        //ATTENDANCE MODULE
        String CREATE_FOCUSED_PRODUCT_TABLE = "CREATE TABLE " + TABLE_FOCUSED_PRODUCT + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_FOCUSED_PRODUCT_NAME + " TEXT, " + KEY_USER_ID + " TEXT, " + KEY_MONTH + " TEXT," + KEY_YEAR + " TEXT" + ")";
        db.execSQL(CREATE_FOCUSED_PRODUCT_TABLE);


        String CREATE_PRODUCTIVITY_HOME_TABLE = "CREATE TABLE " + TABLE_PRODUCTIVITY_COUNT_HOME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_PRODUCTIVITY_PERCENT_HOME + " TEXT, " + KEY_PROGRESS_VALUE + " TEXT, " + KEY_USER_ID + " TEXT, " + KEY_MONTH + " TEXT," + KEY_YEAR + " TEXT" + ")";
        db.execSQL(CREATE_PRODUCTIVITY_HOME_TABLE);

        String CREATE_TARGET_ACHIEVEMENT_TABLE = "CREATE TABLE " + TABLE_TARGET_ACHIEVEMENT_HOME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TARGET_PROGRESS_VALUE + " TEXT, " + KEY_ACHIEVEMENT_PROGRESS_VALUE + " TEXT, " + KEY_PERCENT_TARGET_ACHIEVEEMNT + " TEXT, " + KEY_TARGET_ACHE_PROGRESS_VALUE + " TEXT, " + KEY_USER_ID + " TEXT, " + KEY_MONTH + " TEXT," + KEY_YEAR + " TEXT" + ")";
        db.execSQL(CREATE_TARGET_ACHIEVEMENT_TABLE);


        String CREATE_OUTLET_COUNT_HOME_TABLE = "CREATE TABLE " + TABLE_OUTLET_COUNT_HOME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TOTAL_OUTLET_COUNT + " TEXT, " + KEY_TOTAL_BILLED_OUTLET_COUNT + " TEXT, " + KEY_TOTAL_UNBILLED_OUTLET_COUNT + " TEXT, " + KEY_USER_ID + " TEXT, " + KEY_MONTH + " TEXT," + KEY_YEAR + " TEXT" + ")";
        db.execSQL(CREATE_OUTLET_COUNT_HOME_TABLE);


        String CREATE_DS_COUNT_HOME_TABLE = "CREATE TABLE " + TABLE_DISTRIBUTOR_COUNT_HOME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TOTAL_DS_COUNT + " TEXT, " + KEY_TOTAL_BILLED_DS_COUNT + " TEXT, " + KEY_TOTAL_UNBILLED_DS_COUNT + " TEXT, " + KEY_USER_ID + " TEXT, " + KEY_MONTH + " TEXT," + KEY_YEAR + " TEXT" + ")";
        db.execSQL(CREATE_DS_COUNT_HOME_TABLE);


        String CREATE_ATTENDANCE_STATUS_TABLE = "CREATE TABLE " + TABLE_ATTENDANCE_STATUS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_ATTENDANCE_REASON + " TEXT, " + KEY_ATTENDANCE_TYPE + " TEXT, " + KEY_ATTENDANCE_STATUS_MESSAGE + " TEXT, " + KEY_USER_ID + " TEXT, " + KEY_DATE + " TEXT" + ")";
        db.execSQL(CREATE_ATTENDANCE_STATUS_TABLE);

        String CREATE_ATTENDANCE_REASON_LIST = "CREATE TABLE " + TABLE_ATTENDANCE_REASON_LIST + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_ATTENDANCE_ID + " TEXT, " + KEY_ATTENDANCE_REASON + " TEXT, " + KEY_ATTENDANCE_TYPE + " TEXT" + ")";
        db.execSQL(CREATE_ATTENDANCE_REASON_LIST);

        String CREATE_ATTENDANCE_MARKED = "CREATE TABLE " + TABLE_TODAY_ATTENDANCE_MARKED + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_IS_TODAY_ATTENDANCE_MARKED + " TEXT, " + KEY_DATE + " TEXT," + KEY_USER_ID + " TEXT " + ")";
        db.execSQL(CREATE_ATTENDANCE_MARKED);


        String CREATE_ATTENDANCE = "CREATE TABLE " + TABLE_ATTENDANCE + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_USER_ID + " TEXT, " + KEY_ATTENDANCE_TYPE + " TEXT, " + KEY_ATTENDANCE_DSR_STATUS + " TEXT," + KEY_ATTENDANCE + " TEXT, " + KEY_DATE + " TEXT, " + KEY_ATTENDANCE_TYPE_ID + " TEXT," + KEY_WORK_WITH_USER + " TEXT," + KEY_WORK_WITH_DESIGNATION + " TEXT " + ")";
        db.execSQL(CREATE_ATTENDANCE);

        String CREATE_JOINTWORK = "CREATE TABLE " + TABLE_JOINTWORK + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_USER_ID + " INTEGER, " + KEY_USER_NAME + " TEXT, " + KEY_DESIGNATION_ID + " INTEGER " + ")";
        db.execSQL(CREATE_JOINTWORK);

        String CREATE_ATTENDANCE_UPDATE_ROWID = "CREATE TABLE " + TABLE_ATTENDANCE_UPDATE_ROWID + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_USER_ID + " TEXT, " + KEY_ATTENDANCE_ROW_ID + " TEXT" + ")";
        db.execSQL(CREATE_ATTENDANCE_UPDATE_ROWID);

        String CREATE_SR_STATUS = "CREATE TABLE " + TABLE_SR_STATUS_BY_OUTLET_ID + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_USER_ID + " TEXT, " + KEY_OUTLET_ID + " TEXT, " + KEY_DATE + " TEXT " + ")";
        db.execSQL(CREATE_SR_STATUS);

        String CREATE_SR_COUNT_STATUS = "CREATE TABLE " + TABLE_SR_STATUS_COUNT_OUTLET_ID + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_USER_ID + " TEXT, " + KEY_OUTLET_ID + " TEXT, " + KEY_DATE + " TEXT , " + KEY_COUNT + " TEXT " + ")";
        db.execSQL(CREATE_SR_COUNT_STATUS);


    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DISTRIBUTOR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROUTE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OUTLET);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OUTLET_ORDERS_LAST_DATE);
        // db.execSQL("DROP TABLE IF EXISTS " + TABLE_STOCK_PLACED);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OUTLET_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_JOINTWORK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OUTLET_ORDERS_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OUTLET_ORDERS_PRODUCT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OUTLET_ORDERS_SAVE_DATA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OUTLET_ORDERS_SAVE_PRODUCT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OUTLET_ORDERS_EDIT_SAVE_DATA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OUTLET_ORDERS_EDIT_SAVE_PRODUCT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REASON_LIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REASON_SUBMIT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OUTLET_ORDERS_NOT_FOUND);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ISORDER_ISSTOCK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ISORDER_OUTLETWISE_PLACED);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOCUSED_PRODUCT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTIVITY_COUNT_HOME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TARGET_ACHIEVEMENT_HOME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OUTLET_COUNT_HOME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DISTRIBUTOR_COUNT_HOME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENDANCE_STATUS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENDANCE_REASON_LIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODAY_ATTENDANCE_MARKED);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENDANCE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENDANCE_UPDATE_ROWID);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SR_STATUS_BY_OUTLET_ID);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SR_STATUS_COUNT_OUTLET_ID);
        // Create tables again
        onCreate(db);

    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */


    public void addDistributor(ArrayList<DistributerBean> distributerBean) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < distributerBean.size(); i++) {
            db.execSQL("INSERT INTO " +
                    TABLE_DISTRIBUTOR +
                    " Values(null,'" + distributerBean.get(i).getDistribution_id() + "','" + distributerBean.get(i).getFirm_name() + "');");
        }

        db.close();
    }


    public void addRoute(ArrayList<RouteBean> routeBean) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < routeBean.size(); i++) {
            db.execSQL("INSERT INTO " +
                    TABLE_ROUTE +
                    " Values(null,'" + routeBean.get(i).getDistributor_id() + "','" + routeBean.get(i).getRoutes_id() + "','" + routeBean.get(i).getRoute_name() + "');");
        }

        db.close();
    }

    public void addOutlet(ArrayList<ShowOutLetBeen> outletBean) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < outletBean.size(); i++) {
            db.execSQL("INSERT INTO " +
                    TABLE_OUTLET +
                    " Values(null,'" + outletBean.get(i).getOutlet_id() + "','" + outletBean.get(i).getOutlet_name() + "','" + outletBean.get(i).getDistribution_id() + "','" + outletBean.get(i).getDistribution_name() + "','" + outletBean.get(i).getRoute_id() + "','" + outletBean.get(i).getRoute_name() + "');");
        }

        db.close();
    }


    public void addOrderPlacedOutletWise(String distributorID, String userId, String outletId, String isorderPlaced) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("INSERT INTO " +
                TABLE_ISORDER_OUTLETWISE_PLACED +
                " Values(null,'" + distributorID + "','" + userId + "','" + outletId + "','" + isorderPlaced + "');");


        db.close();
    }

    public void addOutletOrderDate(OutletOrderDateBean outletOrderDateBean) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("INSERT INTO " +
                TABLE_OUTLET_ORDERS_LAST_DATE +
                " Values(null,'" + outletOrderDateBean.getDistributorId() + "','" + outletOrderDateBean.getRouteID() + "','" + outletOrderDateBean.getOutletId() + "','" + outletOrderDateBean.getOrderDate() + "','" + outletOrderDateBean.getHighlightOrNot() + "');");


        db.close();
    }

    public void addOrders(String distributorId, OutletOrdersBean outletOrdersBeans) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("INSERT INTO " +
                TABLE_OUTLET_ORDERS +
                " Values(null,'" + distributorId + "','" + outletOrdersBeans.getOutletId() + "','" + outletOrdersBeans.getOrder_unique_id() + "','" + outletOrdersBeans.getOrder_take_time() + "','" + outletOrdersBeans.getOrder_time_in_long() + "','" + outletOrdersBeans.getIs_order_placed() + "','" + outletOrdersBeans.getWithSso() + "','" + outletOrdersBeans.getWithAsm() + "');");


        db.close();
    }

    public void addCategory(TakeOutletOrderListBean takeOutletOrderListBeans) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("INSERT INTO " +
                TABLE_OUTLET_ORDERS_CATEGORY +
                " Values(null,'" + takeOutletOrderListBeans.getOrder_uni_id() + "','" + takeOutletOrderListBeans.getId() + "','" + takeOutletOrderListBeans.getItem() + "','" + takeOutletOrderListBeans.getOutlet_id() + "'); ");


        db.close();
    }

    public void addProduct(TakeOutletOrderItemBean outletOrdersBeans) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("INSERT INTO " +
                TABLE_OUTLET_ORDERS_PRODUCT +
                " Values(null,'" + outletOrdersBeans.getCat_id() + "','" + outletOrdersBeans.getProduct_id() + "','" + outletOrdersBeans.getProduct_name() + "','" + outletOrdersBeans.getProduct_qty() + "','" + outletOrdersBeans.getProduct_mrp() + "','" + outletOrdersBeans.getSku_code() + "','" + outletOrdersBeans.getOutlet_id() + "','" + outletOrdersBeans.getOrderUniqueID() + "','" + outletOrdersBeans.getCatName() + "');");
        db.close();
    }

    public void addProductSave(ArrayList<TakeOutletOrderItemBean> outletOrdersBeans, String distributorId, String outletId) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < outletOrdersBeans.size(); i++) {
            db.execSQL("INSERT INTO " +
                    TABLE_OUTLET_ORDERS_SAVE_PRODUCT +
                    " Values(null,'" + outletOrdersBeans.get(i).getCat_id() + "','" + outletOrdersBeans.get(i).getProduct_id() + "','" + outletOrdersBeans.get(i).getProduct_name() + "','" + outletOrdersBeans.get(i).getProduct_qty() + "','" + outletId + "','" + distributorId + "','" + outletOrdersBeans.get(i).getCatName() + "');");

        }
        db.close();
    }


    public void addisorderplaced_stock(StockPlaced stockPlaced) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("INSERT INTO " +
                TABLE_ISORDER_ISSTOCK +
                " Values(null,'" + stockPlaced.getDistributorId() + "','" + stockPlaced.getUserId() + "','" + stockPlaced.getStock_placed() + "','" + stockPlaced.getIs_order_placed() + "');");


        db.close();
    }

    public void addSaveData(SaveData saveData) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("INSERT INTO " +
                TABLE_OUTLET_ORDERS_SAVE_DATA +
                " Values(null,'" + saveData.getDistributorId() + "','" + saveData.getUserId() + "','" + saveData.getOutletId() + "','" + saveData.getLatitude() + "','" + saveData.getLongitude() + "','" + saveData.getAddress() + "','" + saveData.getWithSSo() + "','" + saveData.getWithAsm() + "','" + saveData.getTimeTakenOrderOffline() + "');");
        db.close();
    }


    public void addEdittedProductSave(ArrayList<TakeOutletOrderItemBean> outletOrdersBeans, String distributorId, String outletId) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < outletOrdersBeans.size(); i++) {
            db.execSQL("INSERT INTO " +
                    TABLE_OUTLET_ORDERS_EDIT_SAVE_PRODUCT +
                    " Values(null,'" + outletOrdersBeans.get(i).getCat_id() + "','" + outletOrdersBeans.get(i).getProduct_id() + "','" + outletOrdersBeans.get(i).getProduct_name() + "','" + outletOrdersBeans.get(i).getProduct_qty() + "','" + outletId + "','" + distributorId + "','" + outletOrdersBeans.get(i).getCatName() + "');");

        }
        db.close();
    }

    public void addEdittedSaveData(SaveData saveData) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("INSERT INTO " +
                TABLE_OUTLET_ORDERS_EDIT_SAVE_DATA +
                " Values(null,'" + saveData.getDistributorId() + "','" + saveData.getUserId() + "','" + saveData.getOutletId() + "','" + saveData.getLatitude() + "','" + saveData.getLongitude() + "','" + saveData.getAddress() + "','" + saveData.getWithSSo() + "','" + saveData.getWithAsm() + "','" + saveData.getOrder_unique_Id() + "','" + saveData.getTimeTakenOrderOffline() + "');");
        db.close();
    }


    public void addReasonList(ArrayList<ReasonBean> reasonBean) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < reasonBean.size(); i++) {
            db.execSQL("INSERT INTO " +
                    TABLE_REASON_LIST +
                    " Values(null,'" + reasonBean.get(i).getId() + "','" + reasonBean.get(i).getReason() + "');");
        }
        db.close();
    }


    public void addSubmittedReasonData(ReasonSubmitBean reasonBean) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("INSERT INTO " +
                TABLE_REASON_SUBMIT +
                " Values(null,'" + reasonBean.getUserId() + "','" + reasonBean.getOutletId() + "','" + reasonBean.getReasonId() + "','" + reasonBean.getDistributorId() + "','" + reasonBean.getLatitude() + "','" + reasonBean.getLongitude() + "','" + reasonBean.getAddress() + "','" + reasonBean.getOutletName() + "','" + reasonBean.getReason() + "','" + reasonBean.getRouteId() + "');");
        db.close();
    }


    public void addOutletNoOrder(String outletId) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("INSERT INTO " +
                TABLE_OUTLET_ORDERS_NOT_FOUND +
                " Values(null,'" + outletId + "');");
        db.close();
    }


    public ArrayList<DistributerBean> getAllDistributorRecord() {
        ArrayList<DistributerBean> distributerBeanArrayList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_DISTRIBUTOR;

        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);


        if (cursor.moveToFirst()) {
            do {
                DistributerBean distributerBean = new DistributerBean();
                distributerBean.setDistribution_id(cursor.getString(1));

                distributerBean.setFirm_name(cursor.getString(2));

                distributerBeanArrayList.add(distributerBean);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return distributerBeanArrayList;

    }


    public ArrayList<StockPlaced> getAllisOrderPlacedOutletWise() {
        ArrayList<StockPlaced> stockPlacedArrayList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_ISORDER_OUTLETWISE_PLACED;

        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);


        if (cursor.moveToFirst()) {
            do {
                StockPlaced stockPlaced = new StockPlaced();
                stockPlaced.setDistributorId(cursor.getString(1));
                stockPlaced.setUserId(cursor.getString(2));
                stockPlaced.setOutletId(cursor.getString(3));
                stockPlaced.setIs_order_placed(cursor.getString(4));


                stockPlacedArrayList.add(stockPlaced);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return stockPlacedArrayList;

    }

    public void deleteAllRecords() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_DISTRIBUTOR);
        db.close();
    }

    public void deleteisOrderOutletWiseRecords(String distributor) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_ISORDER_OUTLETWISE_PLACED, KEY_DISTRIBUTOR_ID + " = ?",
                new String[]{distributor});


        db.close();
    }


    public ArrayList<RouteBean> getAllRouteRecord() {
        ArrayList<RouteBean> routeBeanArrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_ROUTE;

        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                RouteBean routeBean = new RouteBean();
                routeBean.setDistributor_id(cursor.getString(1));

                routeBean.setRoutes_id(cursor.getString(2));
                routeBean.setRoute_name(cursor.getString(3));

                routeBeanArrayList.add(routeBean);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return routeBeanArrayList;
    }

    public void deleteAllRouteRecords() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_ROUTE);
        db.close();
    }

    public void deleteNoOrderFound(String outletId) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_OUTLET_ORDERS_NOT_FOUND, KEY_OUTLET_ID + " = ?",
                new String[]{outletId});


        db.close();
    }


    public ArrayList<ShowOutLetBeen> getAllOutletRecord() {
        ArrayList<ShowOutLetBeen> showOutLetBeenArrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_OUTLET;

        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ShowOutLetBeen showOutLetBeen = new ShowOutLetBeen();
                showOutLetBeen.setUniqueIdDB(cursor.getString(0));
                showOutLetBeen.setOutlet_id(cursor.getString(1));

                showOutLetBeen.setOutlet_name(cursor.getString(2));
                showOutLetBeen.setDistribution_id(cursor.getString(3));
                showOutLetBeen.setDistribution_name(cursor.getString(4));
                showOutLetBeen.setRoute_id(cursor.getString(5));
                showOutLetBeen.setRoute_name(cursor.getString(6));

                showOutLetBeenArrayList.add(showOutLetBeen);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return showOutLetBeenArrayList;
    }

    public ArrayList<ShowOutLetBeen> getOutletRecordWhileOffline(String distributorId, String RouteID) {
        ArrayList<ShowOutLetBeen> showOutLetBeenArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.query(TABLE_OUTLET, new String[]{KEY_OUTLET_NAME,
                        KEY_OUTLET_ID, KEY_ROUTE_ID, KEY_DISTRIBUTOR_ID, KEY_DISTRIBUTOR_NAME, KEY_ROUTE_NAME}, KEY_DISTRIBUTOR_ID + "=? AND " + KEY_ROUTE_ID + " = ?",
                new String[]{distributorId, RouteID}, null, null, null, null);
        if (cursor != null) {


            if (cursor.moveToFirst()) {
                do {
                    ShowOutLetBeen showOutLetBeen = new ShowOutLetBeen();
                    showOutLetBeen.setOutlet_name(cursor.getString(0));
                    showOutLetBeen.setOutlet_id(cursor.getString(1));
                    showOutLetBeen.setRoute_id(cursor.getString(2));
                    showOutLetBeen.setDistribution_id(cursor.getString(3));
                    showOutLetBeen.setDistribution_name(cursor.getString(4));
                    showOutLetBeen.setRoute_name(cursor.getString(5));
                    showOutLetBeenArrayList.add(showOutLetBeen);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();
        return showOutLetBeenArrayList;
    }


    public void deleteOutlet(String distributorId, String routeId) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_OUTLET, KEY_DISTRIBUTOR_ID + " = ? AND " + KEY_ROUTE_ID + " = ?",
                new String[]{distributorId, routeId});


        db.close();
    }


    public StockPlaced isOrderOtletWisePlaced(String distributorId, String userId, String outletId) {
        SQLiteDatabase db = this.getWritableDatabase();
        StockPlaced stockPlaced = new StockPlaced();
        @SuppressLint("Recycle") Cursor cursor = db.query(TABLE_ISORDER_OUTLETWISE_PLACED, new String[]{KEY_DISTRIBUTOR_ID,
                        KEY_IS_ORDER_PLACED, KEY_USER_ID, KEY_OUTLET_ID}, KEY_DISTRIBUTOR_ID + "=? AND " + KEY_USER_ID + " = ? AND " + KEY_OUTLET_ID + " = ? ",
                new String[]{distributorId, userId, outletId}, null, null, null, null);
        if (cursor != null) {


            if (cursor.moveToFirst()) {
                do {


                    stockPlaced.setDistributorId(cursor.getString(0));
                    stockPlaced.setIs_order_placed(cursor.getString(1));
                    stockPlaced.setUserId(cursor.getString(2));
                    stockPlaced.setOutletId(cursor.getString(3));

                } while (cursor.moveToNext());
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return stockPlaced;
    }

    public StockPlaced isOrderStockPlaced(String distributorId, String userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        StockPlaced stockPlaced = new StockPlaced();
        @SuppressLint("Recycle") Cursor cursor = db.query(TABLE_ISORDER_ISSTOCK, new String[]{KEY_DISTRIBUTOR_ID,
                        KEY_IS_ORDER_PLACED, KEY_USER_ID, KEY_IS_STOCK_TAKEN}, KEY_DISTRIBUTOR_ID + "=? AND " + KEY_USER_ID + " = ?",
                new String[]{distributorId, userId}, null, null, null, null);
        //String selectQuery = "SELECT  * FROM " + TABLE_ISORDER_ISSTOCK + " WHERE " + KEY_DISTRIBUTOR_ID + "='" + distributorId + "' AND " + KEY_USER_ID + "='" + userId + "'";

        //   @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list

        if (cursor != null) {


            if (cursor.moveToFirst()) {
                do {


                    stockPlaced.setDistributorId(cursor.getString(0));
                    stockPlaced.setIs_order_placed(cursor.getString(1));
                    stockPlaced.setUserId(cursor.getString(2));
                    stockPlaced.setStock_placed(cursor.getString(3));

                } while (cursor.moveToNext());
            }
        }

        cursor.close();
        db.close();
        return stockPlaced;
    }

    public int updateOrderOutlet(String distributor, String is_order_placed) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_IS_ORDER_PLACED, is_order_placed);


        // updating row
        int update = db.update(TABLE_OUTLET_ORDERS, values, KEY_DISTRIBUTOR_ID + " = ? ",
                new String[]{distributor});
        db.close();
        return update;
    }


    public int updateIsOrderStockPlaced(StockPlaced stockPlaced) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_IS_STOCK_TAKEN, stockPlaced.getStock_placed());
        values.put(KEY_IS_ORDER_PLACED, stockPlaced.getIs_order_placed());

        // updating row
        int update = db.update(TABLE_ISORDER_ISSTOCK, values, KEY_DISTRIBUTOR_ID + " = ? AND " + KEY_USER_ID + " = ? ",
                new String[]{stockPlaced.getDistributorId(), stockPlaced.getUserId()});

        db.close();
        return update;
    }

    public int updateIsOrderPlacedOutletWise(StockPlaced stockPlaced) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_IS_ORDER_PLACED, stockPlaced.getIs_order_placed());

        // updating row
        int update = db.update(TABLE_ISORDER_OUTLETWISE_PLACED, values, KEY_DISTRIBUTOR_ID + " = ? AND " + KEY_USER_ID + " = ? AND " + KEY_OUTLET_ID + " = ?  ",
                new String[]{stockPlaced.getDistributorId(), stockPlaced.getUserId(), stockPlaced.getOutletId()});
        db.close();
        return update;
    }

    public int updateIsOrderPlaced(StockPlaced stockPlaced) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        /*     values.put(KEY_STOCK_PLACED, stockPlaced.getStock_placed());*/
        values.put(KEY_IS_ORDER_PLACED, stockPlaced.getIs_order_placed());

        // updating row

        int row = db.update(TABLE_ISORDER_ISSTOCK, values, KEY_DISTRIBUTOR_ID + " = ? AND " + KEY_USER_ID + " = ? ",
                new String[]{stockPlaced.getDistributorId(), stockPlaced.getUserId()});
        db.close();
        return row;

    }

    public int updateIsStockPlaced(StockPlaced stockPlaced) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_IS_STOCK_TAKEN, stockPlaced.getStock_placed());
        /*    values.put(KEY_IS_ORDER_PLACED, stockPlaced.getIs_order_placed());*/

        // updating row
        int row = db.update(TABLE_ISORDER_ISSTOCK, values, KEY_DISTRIBUTOR_ID + " = ? AND " + KEY_USER_ID + " = ? ",
                new String[]{stockPlaced.getDistributorId(), stockPlaced.getUserId()});

        if (row == 1) {
            db.close();

            return 1;
        } else {

            db.execSQL("INSERT INTO " +
                    TABLE_ISORDER_ISSTOCK +
                    " Values(null,'" + stockPlaced.getDistributorId() + "','" + stockPlaced.getUserId() + "','" + stockPlaced.getStock_placed() + "','" + stockPlaced.getIs_order_placed() + "');");


            db.close();
            return 0;
        }

    }

    public ArrayList<StockPlaced> getAllisOrderStockPlaced(String userId) {
        ArrayList<StockPlaced> stockPlacedArrayList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_ISORDER_ISSTOCK + " WHERE " + KEY_USER_ID + " = " + userId;

        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                StockPlaced stockPlaced = new StockPlaced();
                stockPlaced.setDistributorId(cursor.getString(1));
                stockPlaced.setUserId(cursor.getString(2));
                stockPlaced.setStock_placed(cursor.getString(3));
                stockPlaced.setStock_placed(cursor.getString(4));

                stockPlacedArrayList.add(stockPlaced);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return stockPlacedArrayList;

    }


   /* public int updateStockPlaced(StockPlaced stockPlaced) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STOCK_PLACED, stockPlaced.getStock_placed());

        // updating row
        return db.update(TABLE_STOCK_PLACED, values, KEY_DISTRIBUTOR_ID + " = ? AND " + KEY_USER_ID + " = ? ",
                new String[]{stockPlaced.getDistributorId(), stockPlaced.getUserId()});
    }
*/

    public int updateOutletOrderDate(OutletOrderDateBean outletOrderDateBean) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ORDER_DATE, outletOrderDateBean.getOrderDate());
        values.put(KEY_HIGHLIGHT, outletOrderDateBean.getHighlightOrNot());


        int update = db.update(TABLE_OUTLET_ORDERS_LAST_DATE, values, KEY_DISTRIBUTOR_ID + " = ? AND " + KEY_ROUTE_ID + " = ? AND " + KEY_OUTLET_ID + " = ?",
                new String[]{outletOrderDateBean.getDistributorId(), outletOrderDateBean.getRouteID(), outletOrderDateBean.getOutletId()});
        db.close();
        return update;
    }

    public ArrayList<OutletOrderDateBean> getAllOutletOrderDate() {
        ArrayList<OutletOrderDateBean> outletOrderDateBeans = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_OUTLET_ORDERS_LAST_DATE;

        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                OutletOrderDateBean outletOrderDateBean = new OutletOrderDateBean();
                outletOrderDateBean.setDistributorId(cursor.getString(1));
                outletOrderDateBean.setRouteID(cursor.getString(2));
                outletOrderDateBean.setOutletId(cursor.getString(3));
                outletOrderDateBean.setOrderDate(cursor.getString(4));
                outletOrderDateBean.setHighlightOrNot(cursor.getString(5));

                outletOrderDateBeans.add(outletOrderDateBean);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return outletOrderDateBeans;

    }


    public ArrayList<OutletOrderDateBean> getOutletOrderDate(String distributorId, String RouteID) {
        ArrayList<OutletOrderDateBean> outletOrderDateBeans = new ArrayList<>();
        // Select All Query
        SQLiteDatabase db = this.getWritableDatabase();

        @SuppressLint("Recycle") Cursor cursor = db.query(TABLE_OUTLET_ORDERS_LAST_DATE, new String[]{KEY_DISTRIBUTOR_ID,
                        KEY_ROUTE_ID, KEY_OUTLET_ID, KEY_ORDER_DATE, KEY_HIGHLIGHT}, KEY_DISTRIBUTOR_ID + "=? AND " + KEY_ROUTE_ID + " = ?",
                new String[]{distributorId, RouteID}, null, null, null, null);


        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                OutletOrderDateBean outletOrderDateBean = new OutletOrderDateBean();
                outletOrderDateBean.setDistributorId(cursor.getString(0));
                outletOrderDateBean.setRouteID(cursor.getString(1));
                outletOrderDateBean.setOutletId(cursor.getString(2));
                outletOrderDateBean.setOrderDate(cursor.getString(3));
                outletOrderDateBean.setHighlightOrNot(cursor.getString(4));

                outletOrderDateBeans.add(outletOrderDateBean);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return outletOrderDateBeans;

    }

    public ArrayList<OutletOrdersBean> getAllOrderRecord() {
        ArrayList<OutletOrdersBean> outletOrdersBeans = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_OUTLET_ORDERS;

        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                OutletOrdersBean ordersBean = new OutletOrdersBean();
                ordersBean.setDistributorId(cursor.getString(1));
                ordersBean.setOutletId(cursor.getString(2));
                ordersBean.setOrder_unique_id(cursor.getString(3));
                ordersBean.setOrder_take_time(cursor.getString(4));
                ordersBean.setOrder_time_in_long(cursor.getString(5));
                ordersBean.setIs_order_placed(cursor.getString(6));
                ordersBean.setWithSso(cursor.getString(7));
                ordersBean.setWithAsm(cursor.getString(8));


                outletOrdersBeans.add(ordersBean);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return outletOrdersBeans;

    }

    public ArrayList<TakeOutletOrderItemBean> getProductRecord() {
        ArrayList<TakeOutletOrderItemBean> takeOutletOrderItemBeans = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_OUTLET_ORDERS_PRODUCT;

        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                TakeOutletOrderItemBean ordersBean = new TakeOutletOrderItemBean();
                ordersBean.setCat_id(cursor.getString(1));
                ordersBean.setProduct_id(cursor.getString(2));
                ordersBean.setProduct_name(cursor.getString(3));
                ordersBean.setProduct_qty(cursor.getString(4));
                ordersBean.setProduct_mrp(cursor.getString(5));
                ordersBean.setSku_code(cursor.getString(6));
                ordersBean.setOutlet_id(cursor.getString(7));
                ordersBean.setOrderUniqueID(cursor.getString(8));
                ordersBean.setCatName(cursor.getString(9));


                takeOutletOrderItemBeans.add(ordersBean);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return takeOutletOrderItemBeans;

    }

    public ArrayList<TakeOutletOrderListBean> getAllCategoryRecord() {
        ArrayList<TakeOutletOrderListBean> takeOutletOrderListBeans = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_OUTLET_ORDERS_CATEGORY;

        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                TakeOutletOrderListBean outletOrdersBean = new TakeOutletOrderListBean();
                outletOrdersBean.setOrder_uni_id(cursor.getString(1));
                outletOrdersBean.setId(cursor.getString(2));
                outletOrdersBean.setItem(cursor.getString(3));
                outletOrdersBean.setOutlet_id(cursor.getString(4));


                takeOutletOrderListBeans.add(outletOrdersBean);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return takeOutletOrderListBeans;

    }

    public void deleteAllOrderRecords(String outletId) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_OUTLET_ORDERS, KEY_OUTLET_ID + " = ?",
                new String[]{outletId});


        db.close();

    }

    public void deleteAll(String dis) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_ISORDER_ISSTOCK, KEY_DISTRIBUTOR_ID + " = ?",
                new String[]{dis});


        db.close();

    }

    public void deleteCategoryRecords(String outletId) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_OUTLET_ORDERS_CATEGORY, KEY_OUTLET_ID + " = ?",
                new String[]{outletId});


        db.close();

    }

    public void deleteProductRecords(String outletId) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_OUTLET_ORDERS_PRODUCT, KEY_OUTLET_ID + " = ?",
                new String[]{outletId});


        db.close();

    }


    public ArrayList<OutletOrdersBean> getOrdersData(String outletId) {
        ArrayList<OutletOrdersBean> outletOrdersBeans = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();

        @SuppressLint("Recycle") Cursor cursor = db.query(TABLE_OUTLET_ORDERS, new String[]{KEY_OUTLET_ID,
                        KEY_ORDER_UNIQUE_ID, KEY_ORDER_TAKE_TIME, KEY_ORDER_TIME_IN_LONG, KEY_IS_ORDER_PLACED, KEY_WITH_SSO, KEY_WITH_ASM, KEY_DISTRIBUTOR_ID}, KEY_OUTLET_ID + "=?",
                new String[]{outletId}, null, null, null, null);


        if (cursor.moveToFirst()) {
            do {
                OutletOrdersBean ordersBean = new OutletOrdersBean();
                ordersBean.setOutletId(cursor.getString(0));
                ordersBean.setOrder_unique_id(cursor.getString(1));
                ordersBean.setOrder_take_time(cursor.getString(2));
                ordersBean.setOrder_time_in_long(cursor.getString(3));
                ordersBean.setIs_order_placed(cursor.getString(4));
                ordersBean.setWithSso(cursor.getString(5));
                ordersBean.setWithAsm(cursor.getString(6));
                ordersBean.setDistributorId(cursor.getString(7));

                outletOrdersBeans.add(ordersBean);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return outletOrdersBeans;

    }


    public ArrayList<TakeOutletOrderListBean> getCategoryRecord(String outletId, String orderUniqueId) {
        ArrayList<TakeOutletOrderListBean> takeOutletOrderListBeans = new ArrayList<>();
        // Select All Query
        SQLiteDatabase db = this.getWritableDatabase();

        @SuppressLint("Recycle") Cursor cursor = db.query(TABLE_OUTLET_ORDERS_CATEGORY, new String[]{KEY_OUTLET_ID,
                        KEY_CAT_ID, KEY_CAT_NAME, KEY_ORDER_UNIQUE_ID}, KEY_OUTLET_ID + "=? AND " + KEY_ORDER_UNIQUE_ID + "=? ",
                new String[]{outletId, orderUniqueId}, null, null, null, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                TakeOutletOrderListBean outletOrdersBean = new TakeOutletOrderListBean();
                outletOrdersBean.setOrder_uni_id(cursor.getString(3));
                outletOrdersBean.setId(cursor.getString(1));
                outletOrdersBean.setItem(cursor.getString(2));
                outletOrdersBean.setOutlet_id(cursor.getString(0));


                takeOutletOrderListBeans.add(outletOrdersBean);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return takeOutletOrderListBeans;

    }

    public ArrayList<TakeOutletOrderItemBean> getProductRecord(String outletId, String orderUniqueId) {
        ArrayList<TakeOutletOrderItemBean> takeOutletOrderItemBeans = new ArrayList<>();
        // Select All Query

        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.query(TABLE_OUTLET_ORDERS_PRODUCT, new String[]{KEY_CAT_ID,
                        KEY_PRODUCT_ID, KEY_PRODUCT_NAME, KEY_PRODUCT_QUANTITY, KEY_PRODUCT_MRP, KEY_SKU_CODE, KEY_OUTLET_ID, KEY_ORDER_UNIQUE_ID, KEY_CAT_NAME}, KEY_OUTLET_ID + "=? AND " + KEY_ORDER_UNIQUE_ID + "=? ",
                new String[]{outletId, orderUniqueId}, null, null, null, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                TakeOutletOrderItemBean ordersBean = new TakeOutletOrderItemBean();
                ordersBean.setCat_id(cursor.getString(0));
                ordersBean.setProduct_id(cursor.getString(1));
                ordersBean.setProduct_name(cursor.getString(2));
                ordersBean.setProduct_qty(cursor.getString(3));
                ordersBean.setProduct_mrp(cursor.getString(4));
                ordersBean.setSku_code(cursor.getString(5));
                ordersBean.setOutlet_id(cursor.getString(6));
                ordersBean.setOrderUniqueID(cursor.getString(7));
                ordersBean.setCatName(cursor.getString(8));


                takeOutletOrderItemBeans.add(ordersBean);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return takeOutletOrderItemBeans;

    }

    public ArrayList<SaveData> getAllSaveDataRecord() {
        ArrayList<SaveData> saveData = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_OUTLET_ORDERS_SAVE_DATA;

        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SaveData data = new SaveData();
                data.setDistributorId(cursor.getString(1));
                data.setUserId(cursor.getString(2));
                data.setOutletId(cursor.getString(3));
                data.setLatitude(cursor.getString(4));
                data.setLongitude(cursor.getString(5));
                data.setAddress(cursor.getString(6));
                data.setWithSSo(cursor.getString(7));
                data.setWithAsm(cursor.getString(8));
                data.setTimeTakenOrderOffline(cursor.getString(9));

                saveData.add(data);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return saveData;
    }

    public ArrayList<TakeOutletOrderItemBean> getAllSaveProductRecord() {
        ArrayList<TakeOutletOrderItemBean> takeOutletOrderItemBeans = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_OUTLET_ORDERS_SAVE_PRODUCT;

        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                TakeOutletOrderItemBean takeOutletOrderItemBean = new TakeOutletOrderItemBean();
                takeOutletOrderItemBean.setCat_id(cursor.getString(1));
                takeOutletOrderItemBean.setProduct_id(cursor.getString(2));
                takeOutletOrderItemBean.setProduct_name(cursor.getString(3));
                takeOutletOrderItemBean.setProduct_qty(cursor.getString(4));
                takeOutletOrderItemBean.setOutlet_id(cursor.getString(5));
                takeOutletOrderItemBean.setDistributorId(cursor.getString(6));

                takeOutletOrderItemBean.setCatName(cursor.getString(7));

                takeOutletOrderItemBeans.add(takeOutletOrderItemBean);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return takeOutletOrderItemBeans;
    }


    public ArrayList<TakeOutletOrderItemBean> getSaveProductRecord(String distribution_id, String outlet_id) {


        ArrayList<TakeOutletOrderItemBean> takeOutletOrderItemBeans = new ArrayList<>();
        // Select All Query


        String selectQuery = "SELECT  * FROM " + TABLE_OUTLET_ORDERS_SAVE_PRODUCT + " WHERE " + KEY_DISTRIBUTOR_ID + "='" + distribution_id + "' AND " + KEY_OUTLET_ID + "='" + outlet_id + "'";
        // looping through all rows and adding to list
        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                TakeOutletOrderItemBean ordersBean = new TakeOutletOrderItemBean();
                ordersBean.setCat_id(cursor.getString(1));
                ordersBean.setProduct_id(cursor.getString(2));
                ordersBean.setProduct_name(cursor.getString(3));
                ordersBean.setProduct_qty(cursor.getString(4));
                ordersBean.setOutlet_id(cursor.getString(5));
                ordersBean.setDistributorId(cursor.getString(6));
                ordersBean.setCatName(cursor.getString(7));


                takeOutletOrderItemBeans.add(ordersBean);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return takeOutletOrderItemBeans;
    }


    public ArrayList<TakeOutletOrderItemBean> getSaveProductRecord(String distribution_id) {


        ArrayList<TakeOutletOrderItemBean> takeOutletOrderItemBeans = new ArrayList<>();
        // Select All Query


        String selectQuery = "SELECT  * FROM " + TABLE_OUTLET_ORDERS_SAVE_PRODUCT + " WHERE " + KEY_DISTRIBUTOR_ID + "='" + distribution_id + "'";
        // looping through all rows and adding to list
        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                TakeOutletOrderItemBean ordersBean = new TakeOutletOrderItemBean();


                ordersBean.setOutlet_id(cursor.getString(5));
                ordersBean.setDistributorId(cursor.getString(6));
                ordersBean.setCatName(cursor.getString(7));


                takeOutletOrderItemBeans.add(ordersBean);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return takeOutletOrderItemBeans;
    }

    public long getDataSaveCount(String distribution_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, TABLE_OUTLET_ORDERS_SAVE_DATA, KEY_DISTRIBUTOR_ID + "=? ",
                new String[]{distribution_id});
        db.close();
        return count;
    }

    public long getEditSaveCount(String distribution_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, TABLE_OUTLET_ORDERS_EDIT_SAVE_DATA, KEY_DISTRIBUTOR_ID + "=? ",
                new String[]{distribution_id});
        db.close();
        return count;
    }


    public ArrayList<SaveData> getAllSaveDataRecord(String distribution_id, String outlet_id) {
        ArrayList<SaveData> saveData = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_OUTLET_ORDERS_SAVE_DATA + " WHERE " + KEY_DISTRIBUTOR_ID + "='" + distribution_id + "' AND " + KEY_OUTLET_ID + "='" + outlet_id + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SaveData data = new SaveData();
                data.setDistributorId(cursor.getString(1));
                data.setUserId(cursor.getString(2));
                data.setOutletId(cursor.getString(3));
                data.setLatitude(cursor.getString(4));
                data.setLongitude(cursor.getString(5));
                data.setAddress(cursor.getString(6));
                data.setWithSSo(cursor.getString(7));
                data.setWithAsm(cursor.getString(8));
                data.setTimeTakenOrderOffline(cursor.getString(9));
                saveData.add(data);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return saveData;
    }


    public void deleteSaveRecordsAfterSubmit(String distributorId, String outletId) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_OUTLET_ORDERS_SAVE_DATA, KEY_DISTRIBUTOR_ID + " = ? AND " + KEY_OUTLET_ID + " = ?",
                new String[]{distributorId, outletId});


        db.close();

    }

    public void deleteSaveProductRecordsAfterSubmit(String distributorId, String outletId) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_OUTLET_ORDERS_SAVE_PRODUCT, KEY_DISTRIBUTOR_ID + " = ? AND " + KEY_OUTLET_ID + " = ?",
                new String[]{distributorId, outletId});


        db.close();

    }

    public ArrayList<TakeOutletOrderItemBean> getEditSaveProductRecord(String distribution_id, String outlet_id) {


        ArrayList<TakeOutletOrderItemBean> takeOutletOrderItemBeans = new ArrayList<>();
        // Select All Query


        String selectQuery = "SELECT  * FROM " + TABLE_OUTLET_ORDERS_EDIT_SAVE_PRODUCT + " WHERE " + KEY_DISTRIBUTOR_ID + "='" + distribution_id + "' AND " + KEY_OUTLET_ID + "='" + outlet_id + "'";
        // looping through all rows and adding to list
        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                TakeOutletOrderItemBean ordersBean = new TakeOutletOrderItemBean();
                ordersBean.setCat_id(cursor.getString(1));
                ordersBean.setProduct_id(cursor.getString(2));
                ordersBean.setProduct_name(cursor.getString(3));
                ordersBean.setProduct_qty(cursor.getString(4));
                ordersBean.setOutlet_id(cursor.getString(5));
                ordersBean.setDistributorId(cursor.getString(6));
                ordersBean.setCatName(cursor.getString(7));


                takeOutletOrderItemBeans.add(ordersBean);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return takeOutletOrderItemBeans;
    }


    public ArrayList<SaveData> getEditSaveDataRecord(String distribution_id, String outlet_id) {
        ArrayList<SaveData> saveData = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_OUTLET_ORDERS_EDIT_SAVE_DATA + " WHERE " + KEY_DISTRIBUTOR_ID + "='" + distribution_id + "' AND " + KEY_OUTLET_ID + "='" + outlet_id + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                SaveData data = new SaveData();
                data.setDistributorId(cursor.getString(1));
                data.setUserId(cursor.getString(2));
                data.setOutletId(cursor.getString(3));
                data.setLatitude(cursor.getString(4));
                data.setLongitude(cursor.getString(5));
                data.setAddress(cursor.getString(6));
                data.setWithSSo(cursor.getString(7));
                data.setWithAsm(cursor.getString(8));
                data.setOrder_unique_Id(cursor.getString(9));
                data.setTimeTakenOrderOffline(cursor.getString(10));
                saveData.add(data);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return saveData;
    }

    public void deleteEditSaveRecordsAfterSubmit(String distributorId, String outletId) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_OUTLET_ORDERS_EDIT_SAVE_DATA, KEY_DISTRIBUTOR_ID + " = ? AND " + KEY_OUTLET_ID + " = ?",
                new String[]{distributorId, outletId});


        db.close();

    }

    public void deleteEditSaveProductRecordsAfterSubmit(String distributorId, String outletId) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_OUTLET_ORDERS_EDIT_SAVE_PRODUCT, KEY_DISTRIBUTOR_ID + " = ? AND " + KEY_OUTLET_ID + " = ?",
                new String[]{distributorId, outletId});


        db.close();

    }

    public ArrayList<ReasonBean> getAllReasonListRecord() {
        ArrayList<ReasonBean> reasonBeans = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_REASON_LIST;

        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ReasonBean reasonBean = new ReasonBean();
                reasonBean.setId(cursor.getString(1));

                reasonBean.setReason(cursor.getString(2));


                reasonBeans.add(reasonBean);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return reasonBeans;
    }

    public ArrayList<ReasonSubmitBean> getAllReasonSubmitRecord() {
        ArrayList<ReasonSubmitBean> reasonBeans = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_REASON_SUBMIT;

        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ReasonSubmitBean reasonBean = new ReasonSubmitBean();
                reasonBean.setUserId(cursor.getString(1));

                reasonBean.setOutletId(cursor.getString(2));
                reasonBean.setReasonId(cursor.getString(3));
                reasonBean.setDistributorId(cursor.getString(4));
                reasonBean.setLatitude(cursor.getString(5));
                reasonBean.setLongitude(cursor.getString(6));
                reasonBean.setAddress(cursor.getString(7));
                reasonBean.setOutletName(cursor.getString(8));
                reasonBean.setReason(cursor.getString(9));
                reasonBean.setRouteId(cursor.getString(10));


                reasonBeans.add(reasonBean);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return reasonBeans;
    }

    public void deleteReasonList() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_REASON_LIST);
        db.close();
    }


    public void deleteReasonSubmitRecord(String distributorId) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_REASON_SUBMIT, KEY_DISTRIBUTOR_ID + " = ?",
                new String[]{distributorId});


        db.close();

    }


    public ArrayList<ReasonSubmitBean> getReasonSubmitRecord(String distributorId, String routeId) {
        ArrayList<ReasonSubmitBean> reasonBeans = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_REASON_SUBMIT + " WHERE " + KEY_DISTRIBUTOR_ID + "='" + distributorId + "' AND " + KEY_ROUTE_ID + "='" + routeId + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ReasonSubmitBean reasonBean = new ReasonSubmitBean();
                reasonBean.setUserId(cursor.getString(1));

                reasonBean.setOutletId(cursor.getString(2));
                reasonBean.setReasonId(cursor.getString(3));
                reasonBean.setDistributorId(cursor.getString(4));
                reasonBean.setLatitude(cursor.getString(5));
                reasonBean.setLongitude(cursor.getString(6));
                reasonBean.setAddress(cursor.getString(7));

                reasonBean.setOutletName(cursor.getString(8));
                reasonBean.setReason(cursor.getString(9));
                reasonBean.setRouteId(cursor.getString(10));

                reasonBeans.add(reasonBean);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return reasonBeans;
    }


    public String getOutletNotFound(String outletId) {
        String outletIds = "";
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_OUTLET_ORDERS_NOT_FOUND + " WHERE " + KEY_OUTLET_ID + "='" + outletId + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                outletIds = cursor.getString(1);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return outletIds;
    }


    //FOCUSED PRODUCT ASS
    public void addFocusedProduct(ArrayList<FocusedProductBean> focusedProductName, String userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < focusedProductName.size(); i++) {
            db.execSQL("INSERT INTO " +
                    TABLE_FOCUSED_PRODUCT +
                    " Values(null,'" + focusedProductName.get(i).getProductName() + "','" + userId + "','" + focusedProductName.get(i).getMonth() + "','" + focusedProductName.get(i).getYear() + "');");


        }


        db.close();
    }

    //GET FOCUSED PRODUCT
    public ArrayList<FocusedProductBean> getFocusedProduct(String month, String year, String userId) {

        String selectQuery = "SELECT  * FROM " + TABLE_FOCUSED_PRODUCT + " WHERE " + KEY_USER_ID + "='" + userId + "' AND " + KEY_MONTH + "='" + month + "' AND " + KEY_YEAR + "='" + year + "'";
        ArrayList<FocusedProductBean> focusedProductBeanArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                FocusedProductBean focusedProductBean = new FocusedProductBean();
                focusedProductBean.setProductName(cursor.getString(1));
                focusedProductBean.setMonth(cursor.getString(2));
                focusedProductBean.setYear(cursor.getString(3));
                focusedProductBeanArrayList.add(focusedProductBean);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return focusedProductBeanArrayList;
    }

    //Delete Focused Product
    public void deleteAllFocusedRecords(String userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FOCUSED_PRODUCT, KEY_USER_ID + " = ?",
                new String[]{userId});

        db.close();
    }

    //Add Productivity
    public void addProductivityHome(String productivity, String progressValue, String userId, String month, String year) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("INSERT INTO " +
                TABLE_PRODUCTIVITY_COUNT_HOME +
                " Values(null,'" + productivity + "','" + progressValue + "','" + userId + "','" + month + "','" + year + "');");


        db.close();
    }

    //Delete Productivtiy

    public void deleteAllProductivityRecords(String userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCTIVITY_COUNT_HOME, KEY_USER_ID + " = ?",
                new String[]{userId});

        db.close();
    }

    public TargetAchievementProgress getProductivity(String month, String year, String userId) {

        String selectQuery = "SELECT  * FROM " + TABLE_PRODUCTIVITY_COUNT_HOME + " WHERE " + KEY_USER_ID + "='" + userId + "' AND " + KEY_MONTH + "='" + month + "' AND " + KEY_YEAR + "='" + year + "'";
        TargetAchievementProgress targetAchievementProgress = new TargetAchievementProgress();
        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {

                targetAchievementProgress.setProductivity(cursor.getString(1));
                targetAchievementProgress.setProgressValueProductivity(cursor.getString(2));

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return targetAchievementProgress;
    }


    //Add TARGET ACHIEVEMENT
    public void addTargetAchievementHome(String target, String achievemnet, String percentage, String targetAchprogressValue, String userId, String month, String year) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("INSERT INTO " +
                TABLE_TARGET_ACHIEVEMENT_HOME +
                " Values(null,'" + target + "','" + achievemnet + "','" + percentage + "','" + targetAchprogressValue + "','" + userId + "','" + month + "','" + year + "');");


        db.close();
    }

    //Delete  TARGET ACHIEVEMENT
    public void deleteAllTargetAchievementRecords(String userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TARGET_ACHIEVEMENT_HOME, KEY_USER_ID + " = ?",
                new String[]{userId});

        db.close();
    }

    public TargetAchievementProgress getTargetAchievementHome(String month, String year, String userId) {

        String selectQuery = "SELECT  * FROM " + TABLE_TARGET_ACHIEVEMENT_HOME + " WHERE " + KEY_USER_ID + "='" + userId + "' AND " + KEY_MONTH + "='" + month + "' AND " + KEY_YEAR + "='" + year + "'";

        TargetAchievementProgress targetAchievementProgress = new TargetAchievementProgress();
        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {

                targetAchievementProgress.setTarget(cursor.getString(1));
                targetAchievementProgress.setAchievement(cursor.getString(2));
                targetAchievementProgress.setPercentage(cursor.getString(3));
                targetAchievementProgress.setProgressValueTargetAchievement(cursor.getString(4));

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return targetAchievementProgress;

    }


    //TOTAL OUTLET COUNTS<BILLED<UNBILLED


    //Add OUTLET
    public void addTotalOutletHome(String totalOutlet, String billedOutlet, String unbilledoutlet, String userId, String month, String year) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("INSERT INTO " +
                TABLE_OUTLET_COUNT_HOME +
                " Values(null,'" + totalOutlet + "','" + billedOutlet + "','" + unbilledoutlet + "','" + userId + "','" + month + "','" + year + "');");


        db.close();
    }

    //Delete  OUTLET
    public void deleteAllTotalOutletHomeRecords(String userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_OUTLET_COUNT_HOME, KEY_USER_ID + " = ?",
                new String[]{userId});

        db.close();
    }

    //get outlet
    public TotalOutletCountHome getTotalOutletHome(String month, String year, String userId) {

        String selectQuery = "SELECT  * FROM " + TABLE_OUTLET_COUNT_HOME + " WHERE " + KEY_USER_ID + "='" + userId + "' AND " + KEY_MONTH + "='" + month + "' AND " + KEY_YEAR + "='" + year + "'";

        TotalOutletCountHome totalOutletCountHome = new TotalOutletCountHome();
        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {

                totalOutletCountHome.setTotalOutlet(cursor.getString(1));
                totalOutletCountHome.setBilledOutlet(cursor.getString(2));
                totalOutletCountHome.setUnbilledOutlet(cursor.getString(3));

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return totalOutletCountHome;

    }

//TOTAL DS

    //Add DS
    public void addTotalDSHome(String totalDS, String billedDS, String unbilledDS, String userId, String month, String year) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("INSERT INTO " +
                TABLE_DISTRIBUTOR_COUNT_HOME +
                " Values(null,'" + totalDS + "','" + billedDS + "','" + unbilledDS + "','" + userId + "','" + month + "','" + year + "');");


        db.close();
    }

    //Delete  DS
    public void deleteAllDSHomeRecords(String userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DISTRIBUTOR_COUNT_HOME, KEY_USER_ID + " = ?",
                new String[]{userId});

        db.close();
    }

    //get DS
    public TotalOutletCountHome getDSHome(String month, String year, String userId) {

        String selectQuery = "SELECT  * FROM " + TABLE_DISTRIBUTOR_COUNT_HOME + " WHERE " + KEY_USER_ID + "='" + userId + "' AND " + KEY_MONTH + "='" + month + "' AND " + KEY_YEAR + "='" + year + "'";

        TotalOutletCountHome totalOutletCountHome = new TotalOutletCountHome();
        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {

                totalOutletCountHome.setTotalDS(cursor.getString(1));
                totalOutletCountHome.setBilledDS(cursor.getString(2));
                totalOutletCountHome.setUnbilledDS(cursor.getString(3));

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return totalOutletCountHome;

    }
//TABLE ATTENDANCE STATUS


    //Add attendace
    public void addAttendanceStatus(String attendanceReason, String attendanceType, String attendanceMessage, String userId, String date) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("INSERT INTO " +
                TABLE_ATTENDANCE_STATUS +
                " Values(null,'" + attendanceReason + "','" + attendanceType + "','" + attendanceMessage + "','" + userId + "','" + date + "');");


        db.close();
    }

    //Delete  DS
    public void deleteAllAttendanceStatusRecords(String userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ATTENDANCE_STATUS, KEY_USER_ID + " = ?",
                new String[]{userId});

        db.close();
    }

    //get DS
    public AttendanceReasonBean getAttendanceStatusHome(String date, String userId) {

        String selectQuery = "SELECT  * FROM " + TABLE_ATTENDANCE_STATUS + " WHERE " + KEY_USER_ID + "='" + userId + "' AND " + KEY_DATE + "='" + date + "'";

        AttendanceReasonBean attendanceReasonBean = new AttendanceReasonBean();
        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {

                attendanceReasonBean.setAttendanceReason(cursor.getString(1));
                attendanceReasonBean.setAttendanceType(cursor.getString(2));
                attendanceReasonBean.setAttendanceMessage(cursor.getString(3));

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return attendanceReasonBean;

    }


    //Add attendace
    public void addAttendanceReasonList(ArrayList<AttendanceReasonBean> attendanceReasonBeans) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < attendanceReasonBeans.size(); i++) {
            db.execSQL("INSERT INTO " +
                    TABLE_ATTENDANCE_REASON_LIST +
                    " Values(null,'" + attendanceReasonBeans.get(i).getAttendanceId() + "','" + attendanceReasonBeans.get(i).getAttendanceReason() + "','" + attendanceReasonBeans.get(i).getAttendanceType() + "');");


        }

        db.close();
    }

    //Delete  DS
    public void deleteAllAttendanceReasonListRecords() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_ATTENDANCE_REASON_LIST);

        db.close();
    }

    //get DS
    public ArrayList<AttendanceReasonBean> getAttendanceReasonList() {

        String selectQuery = "SELECT  * FROM " + TABLE_ATTENDANCE_REASON_LIST;
        ArrayList<AttendanceReasonBean> attendanceReasonBeanArray = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                AttendanceReasonBean attendanceReasonBean = new AttendanceReasonBean();
                attendanceReasonBean.setAttendanceId(cursor.getString(1));
                attendanceReasonBean.setAttendanceReason(cursor.getString(2));
                attendanceReasonBean.setAttendanceType(cursor.getString(3));
                attendanceReasonBeanArray.add(attendanceReasonBean);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return attendanceReasonBeanArray;

    }

    //Add attendace
    public void addIsTodayAttendanceMarkeds(String isTodayMarked, String date, String userId) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("INSERT INTO " +
                TABLE_TODAY_ATTENDANCE_MARKED +
                " Values(null,'" + isTodayMarked + "','" + date + "','" + userId + "');");


        db.close();
    }

    //Delete  DS
    public void deleteAllIsTodayAttendanceMarkeds(String userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        // db.execSQL("delete from " + TABLE_TODAY_ATTENDANCE_MARKED);
        db.delete(TABLE_TODAY_ATTENDANCE_MARKED, KEY_USER_ID + " = ?",
                new String[]{userId});


        //   db.close();
        db.close();
    }

    //get DS
    public String getIsTodayAttendanceMarked(String date, String userId) {

        String selectQuery = "SELECT  * FROM " + TABLE_TODAY_ATTENDANCE_MARKED + " Where " + KEY_DATE + "='" + date + "' AND " + KEY_USER_ID + "='" + userId + "'";

        String isTodayAttendance = "";
        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {


                isTodayAttendance = (cursor.getString(1));

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return isTodayAttendance;

    }

    //Add attendace
    public void addAttendance(String userId, String attendanceType, String dsr_status, String attendance, String date, String type_id, Integer work_with_userId, Integer work_with_dessignatonID) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("INSERT INTO " +
                TABLE_ATTENDANCE +
                " Values(null,'" + userId + "','" + attendanceType + "','" + dsr_status + "','" + attendance + "','" + date + "','" + type_id + "','" + work_with_userId + "','" + work_with_dessignatonID + "');");


        db.close();
    }

    //Delete  DS
    public void deleteAttendance(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ATTENDANCE, KEY_ID + " = ?",
                new String[]{id});


        db.close();
    }

    public void deleteAttendance() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_ATTENDANCE);
        db.close();
    }

    //get DS
    public ArrayList<AttendanceBean> getAttendance() {

        String selectQuery = "SELECT  * FROM " + TABLE_ATTENDANCE;
        ArrayList<AttendanceBean> attendanceBeanArrayList = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {

                AttendanceBean attendanceBean = new AttendanceBean();
                attendanceBean.setKeyId(cursor.getString(0));
                attendanceBean.setUserId(cursor.getString(1));
                attendanceBean.setAttendanceType(cursor.getString(2));
                attendanceBean.setDsrStatus(cursor.getString(3));
                attendanceBean.setPresAbsent(cursor.getString(4));
                attendanceBean.setAttendanceDate(cursor.getString(5));
                attendanceBean.setAttendanceTypeId(cursor.getString(6));
                attendanceBean.setJointUserID(cursor.getString(7));
                attendanceBean.setJointUserDesig(cursor.getString(8));

                attendanceBeanArrayList.add(attendanceBean);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return attendanceBeanArrayList;

    }

    //get DS
    public ArrayList<AttendanceBean> getAttendanceDate(String date) {

        String selectQuery = "SELECT  * FROM " + TABLE_ATTENDANCE + " Where " + KEY_DATE + "='" + date + "'";
        ArrayList<AttendanceBean> attendanceBeanArrayList = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {

                AttendanceBean attendanceBean = new AttendanceBean();
                attendanceBean.setKeyId(cursor.getString(0));
                attendanceBean.setUserId(cursor.getString(1));
                attendanceBean.setAttendanceType(cursor.getString(2));
                attendanceBean.setDsrStatus(cursor.getString(3));
                attendanceBean.setPresAbsent(cursor.getString(4));
                attendanceBean.setAttendanceDate(cursor.getString(5));
                attendanceBean.setAttendanceTypeId(cursor.getString(6));
                attendanceBean.setJointUserID(cursor.getString(7));
                attendanceBean.setJointUserDesig(cursor.getString(8));

                attendanceBeanArrayList.add(attendanceBean);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return attendanceBeanArrayList;

    }


    public void saveRowIdOnUpdate(String userId, String rowId) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("INSERT INTO " +
                TABLE_ATTENDANCE_UPDATE_ROWID +
                " Values(null,'" + userId + "','" + rowId + "');");


        db.close();
    }

    public void deleteAttendanceRowId() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_ATTENDANCE_UPDATE_ROWID);
        db.close();
    }

    public String getAttendancerowid() {

        String selectQuery = "SELECT  * FROM " + TABLE_ATTENDANCE_UPDATE_ROWID;
        String attendanceRowId = "";

        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {


                attendanceRowId = cursor.getString(2);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return attendanceRowId;


    }


    public void addSrReasonByOutletId(String userId, String outletId, String date) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("INSERT INTO " +
                TABLE_SR_STATUS_BY_OUTLET_ID +
                " Values(null,'" + userId + "','" + outletId + "','" + date + "');");


        db.close();
    }

    public void addSrReasonCountByID(String userId, String outletId, String date, String count) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("INSERT INTO " +
                TABLE_SR_STATUS_COUNT_OUTLET_ID +
                " Values(null,'" + userId + "','" + outletId + "','" + date + "','" + count + "');");


        db.close();
    }


    public int getSrStatusCountById(String userId, String outletId, String date) {

        String selectQuery = "SELECT " + KEY_COUNT + " FROM " + TABLE_SR_STATUS_COUNT_OUTLET_ID + " Where " + KEY_DATE + "='" + date + "' AND " + KEY_USER_ID + "=" + userId + " AND " + KEY_OUTLET_ID + "=" + outletId;


        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            do {


                count = cursor.getInt(0);


            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return count;

    }

    public int getSrStatusById(String userId, String outletId, String date) {

        String selectQuery = "SELECT  COUNT(*) FROM " + TABLE_SR_STATUS_BY_OUTLET_ID + " Where " + KEY_DATE + "='" + date + "' AND " + KEY_USER_ID + "=" + userId + " AND " + KEY_OUTLET_ID + "=" + outletId;


        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            do {


                count = cursor.getInt(0);


            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return count;

    }


    public void deleteSrStatusByID(String outletId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SR_STATUS_BY_OUTLET_ID, KEY_OUTLET_ID + " = ?",
                new String[]{outletId});


        db.close();
    }

    public void deleteSrStatusCountByID(String outletId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SR_STATUS_COUNT_OUTLET_ID, KEY_OUTLET_ID + " = ?",
                new String[]{outletId});
        db.close();
    }

    //Get Joint Work
    public ArrayList<JointWorkBean> getJointWorkList() {

        String selectQuery = "SELECT  * FROM " + TABLE_JOINTWORK;
        ArrayList<JointWorkBean> jointWorkBeanArrayList = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                JointWorkBean jointWorkBean = new JointWorkBean();
                jointWorkBean.setUserID(cursor.getInt(1));
                jointWorkBean.setUserName(cursor.getString(2));
                jointWorkBean.setDesig(cursor.getInt(3));
                jointWorkBeanArrayList.add(jointWorkBean);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return jointWorkBeanArrayList;

    }

    //Add Joint Work
    public void addJointWrorkList(ArrayList<JointWorkBean> jointWorkBeans) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < jointWorkBeans.size(); i++) {
            db.execSQL("INSERT INTO " +
                    TABLE_JOINTWORK +
                    " Values(null,'" + jointWorkBeans.get(i).getUserID() + "','"
                    + jointWorkBeans.get(i).getUserName() + "','"
                    + jointWorkBeans.get(i).getDesig() + "');");
        }

        db.close();
    }

    //Delete  DS
    public void deleteAllJointWorkListRecords() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_JOINTWORK);
        db.close();
    }


}