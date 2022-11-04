package com.malas.appsr.malasapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


@SuppressLint("SimpleDateFormat")
public class Utils {

    private static final int MY_PERMISSIONS_REQUEST = 123;

    public static void hideKeyBoard(Context c, View v) {
        InputMethodManager imm = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public static boolean isInternetConnected(Context mContext) {
        try {
            ConnectivityManager connect;
            connect = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connect != null) {
                NetworkInfo resultMobile = connect.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                NetworkInfo resultWifi = connect.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                return (resultMobile != null && resultMobile.isConnectedOrConnecting()) || (resultWifi != null && resultWifi.isConnectedOrConnecting());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static int getDeviceWidth(Context context) {
        try {
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            return metrics.widthPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 480;
    }

    public static int getDeviceHeight(Context context) {
        try {
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            return metrics.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 480;
    }

    private static AlertDialog getAlertDialogObject(Context context) {
        AlertDialog alertDialog;
//        if (alertDialog == null) {
        alertDialog = new AlertDialog.Builder(context).create();
//        }
        return alertDialog;


    }

    public static void showAlertDialog(Context context, String title, String message, Boolean status) {
        try {
//        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            AlertDialog alertDialog = getAlertDialogObject(context);
            // Setting Dialog Title
            alertDialog.setTitle(title);

            // Setting Dialog Message
            alertDialog.setMessage(message);
            alertDialog.setCancelable(false);
            alertDialog.setCanceledOnTouchOutside(false);
            if (status != null)
                // Setting alert dialog icon
                alertDialog.setIcon(R.mipmap.malas_logo);

            // Setting OK Button
            alertDialog.setButton("OK", (dialog, which) -> dialog.cancel());

            // Showing Alert Message
            if (!((Activity) context).isFinishing())
                alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    public static void showAlertDialog(Context context, String title, String message, Boolean status, final MyDialogCloseListener closeListener) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        if (status != null) {
            // Setting alert dialog icon
            alertDialog.setIcon(R.mipmap.ic_launcher);
        }  // No icon set to preference


        // Setting OK Button
        alertDialog.setButton("OK", (dialog, which) -> {
            dialog.cancel();
            if (closeListener != null) {
                closeListener.handleDialogClose(null);
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    public static void showToast(Context mContext, String message) {
        if (mContext != null)
            Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
    }

    public static void showSnackBar(Context mContext, View view, String message) {

        if (mContext != null) {
            Vibrator v = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
            if (Build.VERSION.SDK_INT >= 26) {
                v.vibrate(
                        VibrationEffect.createOneShot(
                                150,
                                VibrationEffect.EFFECT_HEAVY_CLICK
                        )
                );
            } else {
                v.vibrate(150);
            }

            Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();

        }
    }

    public static boolean checkPermissions(Activity activity, String[] permissions) {

        if (!hasPermissions(activity, permissions)) {
            ActivityCompat.requestPermissions(activity, permissions, MY_PERMISSIONS_REQUEST);
        } else {
            return true;
        }

        return false;
    }

    private static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public static int differenceInTwoDates(String fDate, String eDate) {
        Date c = new Date();

        int days = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date1 = null;
        Date date2 = null;


        try {
            date1 = sdf.parse(fDate);
            date2 = sdf.parse(eDate);
            assert date1 != null;
            assert date2 != null;
            long different = date2.getTime() - date1.getTime();
            int days1 = (int) TimeUnit.DAYS.convert(different, TimeUnit.MILLISECONDS);

            days = days1 + 1;
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }


        return days;
    }

    public static boolean thirtyDaysDifference(String date) {
        Date c = new Date();


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date1 = null;

        try {
            date1 = sdf.parse(date);
            assert date1 != null;
            if (c.getTime() > date1.getTime()) {
                long different = c.getTime() - date1.getTime();
                if (TimeUnit.DAYS.convert(different, TimeUnit.MILLISECONDS) <= 30) {
                    return true;
                }
            } else {
                return true;

            }

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }


        return false;
    }


    public static boolean compareDates(String dateone, String dateTwo) {
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date1;
        Date date2;
        try {
            date1 = inputFormat.parse(dateone);
            date2 = inputFormat.parse(dateTwo);
            assert date1 != null;
            assert date2 != null;
            if (date2.getTime() > date1.getTime() || date1.getTime() == date2.getTime()) {
                return true;
            } else if (date2.getTime() < date1.getTime()) {
                return false;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }


    public static String splitDay(String date) {
        String[] splitData = date.split("-");
        String sDate = splitData[0];
        String sMonth = splitData[1];
        String syear = splitData[2];
        return sDate;
    }

    public static String splitMonth(String date) {
        String[] splitData = date.split("-");
        String sDate = splitData[0];
        String sMonth = splitData[1];
        String syear = splitData[2];
        return sMonth;
    }

    public static String splitYear(String date) {
        String[] splitData = date.split("-");
        String sDate = splitData[0];
        String sMonth = splitData[1];
        String syear = splitData[2];
        return syear;
    }


    public static boolean compareDatesForCompOff(String dateone, String dateTwo) {
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date1;
        Date date2;
        try {
            date1 = inputFormat.parse(dateone);
            date2 = inputFormat.parse(dateTwo);
            assert date1 != null;
            assert date2 != null;
            if (date2.getTime() > date1.getTime()) {
                return true;
            } else if (date2.getTime() < date1.getTime() || date1.getTime() == date2.getTime()) {
                return false;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static String formatDate(String inputdate) {
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date date;
        String outputDateStr = "";
        try {
            date = inputFormat.parse(inputdate);
            assert date != null;
            outputDateStr = outputFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return outputDateStr;
    }

    public static String formatDatetoSubmit(String inputdate) {
        DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date date;
        String outputDateStr = "";
        try {
            date = inputFormat.parse(inputdate);
            assert date != null;
            outputDateStr = outputFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return outputDateStr;
    }

    public static String formatDateFromtime(String inputdate) {
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
        DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date date;
        String outputDateStr = "";
        try {
            date = inputFormat.parse(inputdate);
            assert date != null;
            outputDateStr = outputFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return outputDateStr;
    }

    // Function to insert string
    public static String insertString(
            String originalString,
            String stringToBeInserted,
            int index) {

        // Create a new string
        StringBuilder newString = new StringBuilder();

        for (int i = 0; i < originalString.length(); i++) {

            // Insert the original string character
            // into the new string
            newString.append(originalString.charAt(i));

            if (i == index) {

                // Insert the string to be inserted
                // into the new string
                newString.append(stringToBeInserted);
            }
        }

        // return the modified String

        return newString.toString();
    }

    public static String currrentMonth() {
        DateFormat dateFormat = new SimpleDateFormat("MM");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String selectedMonth(String date1) {
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        DateFormat output = new SimpleDateFormat("MM", Locale.getDefault());

        Date date = null;
        String ot = "";
        try {
            date = inputFormat.parse(date1);

            assert date != null;
            ot = output.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ot;
    }
}
