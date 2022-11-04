package com.Amitlibs.utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.NameValuePair;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class AppUtils {

	// public static String getUrlEncodedString(List<NameValuePair> nvp) {
	// String encodedString = "";
	// for (NameValuePair n : nvp) {
	// try {
	// String name = URLEncoder.encode(n.getName(), "ISO-8859-1");
	// String value = URLEncoder.encode(n.getValue(), "ISO-8859-1");
	// encodedString += name + "=" + value + "&";
	// } catch (UnsupportedEncodingException e) {
	// System.out.println("Encoding Error from AppUtils");
	// e.printStackTrace();
	// }
	// }
	// return encodedString;
	// }

	public static void makeEdittextDull(final EditText et) {
		et.setSingleLine();
		et.setClickable(false);
		et.setEnabled(false);
		et.setLongClickable(false);
		et.setFocusable(false);
	}

	public static void overrideStyles(final Context context, final View v) {
		try {
			if (v instanceof ViewGroup) {
				ViewGroup vg = (ViewGroup) v;
				for (int i = 0; i < vg.getChildCount(); i++) {
					View child = vg.getChildAt(i);
					overrideStyles(context, child);
				}
			} else if (v instanceof TextView) {
				((TextView) v).setTypeface(Typeface.DEFAULT, Typeface.BOLD);
				((TextView) v).setTextColor(context.getResources().getColor(android.R.color.white));
			}
		} catch (Exception e) {
		}
	}

	public static String getUrlEncodedString(List<NameValuePair> nvp) {
		String encodedString = "";
		if (nvp == null) {
			return encodedString;
		}
		for (NameValuePair n : nvp) {
			try {
				String name = URLEncoder.encode(n.getName(), "ISO-8859-1");
				String value = URLEncoder.encode(n.getValue(), "ISO-8859-1");
				encodedString += name + "=" + value + "&";
			} catch (UnsupportedEncodingException e) {
				System.out.println("Encoding Error from AppUtils");
				e.printStackTrace();
			}
		}
		return encodedString;
	}

	public static File createImageFile() {
		String imageFileName = "IMG_" + System.currentTimeMillis();
		File dir = new File(Environment.getExternalStorageDirectory() + File.separator + "POINTEPAY");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File image = null;
		try {
			image = File.createTempFile(imageFileName, ".jpg", dir);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return image;
	}

	public static String getRealPathFromURI(Context context, Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	@Deprecated
	public static String getRealPathFromURIa(Context context, Uri contentUri) {
		Cursor cursor = context.getContentResolver().query(contentUri, null, null, null, null);
		if (cursor == null) { // Source is Dropbox or other similar local file
								// path
			return contentUri.getPath();
		} else {
			cursor.moveToFirst();
			int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
			return cursor.getString(idx);
		}
	}

	public final static Bitmap getBitmapFromFile(String filePath, int width, int height) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		options.inSampleSize = calculateInSampleSize(options, width, height);

		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

		return bitmap;
	}

	public final static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		if (reqWidth == 0 && reqHeight == 0)
			return 1;

		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}
}
