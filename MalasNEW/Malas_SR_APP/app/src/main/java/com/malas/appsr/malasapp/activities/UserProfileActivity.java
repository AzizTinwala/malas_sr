package com.malas.appsr.malasapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.Amitlibs.utils.ComplexPreferences;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.malas.appsr.malasapp.BeanClasses.UserLoginInfoBean;
import com.malas.appsr.malasapp.Constant;
import com.malas.appsr.malasapp.R;
import com.malas.appsr.malasapp.serverconnection.BackgroundWork;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;


public class UserProfileActivity extends AppCompatActivity {

    TextView tvName, tvEmail, tvDepartment, tvDesignation, tvState, tvCity, tvAddress, tvPinCode, tvMobile, tvDateOfBirth, tvDateOfJoining, tvCountry, tvAsmName, tvSsoName, tvChangePassword;
    SimpleDateFormat new_sdf, old_sdf;
    ImageView userImg;

    ComplexPreferences complexPreferences ;
    UserLoginInfoBean mUserLoginInfoBean ;

    final String APP_TAG = "Malas_Sr_App";
    final String photoFileName = "photo.jpg";

    File photoFile;
    Bitmap bitmap;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Profile");

        new_sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        old_sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        userImg = findViewById(R.id.user_img);
        tvName = findViewById(R.id.name);
        tvMobile = findViewById(R.id.tv_mobile);
        tvAddress = findViewById(R.id.tv_adress);
        tvEmail = findViewById(R.id.tv_email);
        tvDateOfBirth = findViewById(R.id.tv_date_of_birth);
        tvCountry = findViewById(R.id.tv_country);
        tvCity = findViewById(R.id.tv_city);
        tvDateOfJoining = findViewById(R.id.tv_date_of_Joining);
        tvAsmName = findViewById(R.id.tv_Asm);
        tvSsoName = findViewById(R.id.tv_sso_name);
        tvChangePassword = findViewById(R.id.tvChangePassword);
        tvDepartment = findViewById(R.id.tv_department);
        tvDesignation = findViewById(R.id.tv_desig);
        tvState = findViewById(R.id.tv_state);
        tvPinCode = findViewById(R.id.tv_pincode);


       complexPreferences = ComplexPreferences.getComplexPreferences(UserProfileActivity.this, Constant.UserRegInfoPref, MODE_PRIVATE);
       mUserLoginInfoBean = complexPreferences.getObject(Constant.UserRegInfoObj, UserLoginInfoBean.class);

         url = Constant.BASE_URL2 + "user_image/" + mUserLoginInfoBean.getUserId() + ".jpg?v="+  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Calendar.getInstance().getTime());

        Glide.with(this)
                .load(url)
                .placeholder(R.drawable.ic_person_add_black_24dp)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .circleCrop()
                .into(userImg);

        tvName.setText(mUserLoginInfoBean.getUserName());
        tvMobile.setText(mUserLoginInfoBean.getUserMobile());
        tvAddress.setText(mUserLoginInfoBean.getUserAddress());
        tvEmail.setText(mUserLoginInfoBean.getUserEmail());
        try{
        tvDateOfBirth.setText(new_sdf.format(old_sdf.parse(mUserLoginInfoBean.getUserDob())));
        }catch (Exception e){
            Log.e("DOB",e.getMessage());
        }
        tvCountry.setText(mUserLoginInfoBean.getCountryName());
        tvCity.setText(mUserLoginInfoBean.getUserCity());
        if (mUserLoginInfoBean.getSsoName() == null)
            tvSsoName.setText("");
        else
            tvSsoName.setText(mUserLoginInfoBean.getSsoName());
        if (mUserLoginInfoBean.getAsmName() == null)
            tvAsmName.setText("");
        else
            tvAsmName.setText(mUserLoginInfoBean.getAsmName());
        try{
            if (mUserLoginInfoBean.getDateOfJoining() == null)
                tvDateOfJoining.setText("");
            else {
                tvDateOfJoining.setText(new_sdf.format(Objects.requireNonNull(old_sdf.parse(mUserLoginInfoBean.getDateOfJoining()))));
            }
        }catch(Exception e){
            Log.e("DOB",e.getMessage());
        }
        tvDepartment.setText(mUserLoginInfoBean.getUserDepartment());
        tvDesignation.setText(mUserLoginInfoBean.getUserDesignation());
        tvState.setText(mUserLoginInfoBean.getUserState());
        tvPinCode.setText(mUserLoginInfoBean.getUserPinCode());
        tvChangePassword.setOnClickListener(v -> startActivity(new Intent(UserProfileActivity.this, ChangePasswordActivity.class)));

        userImg.setOnClickListener(v -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            // Create a File reference for future access
            photoFile = getPhotoFileUri(photoFileName);

            // wrap File object into a content provider
            // required for API >= 24
            // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher

            Uri fileProvider =
                    FileProvider.getUriForFile(this, "com.malas.appsr.fileprovider", photoFile);

            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.

            //     startActivityForResult(intent, 0)

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                cameraIntent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
            } else {
                cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
            }
            startActivityForResult(cameraIntent, 0);

        });
    }


    private File getPhotoFileUri(String photoFileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

        // Create the storage directory if it does not exist

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(APP_TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename

        // Return the file target for the photo based on filename

        return new File(mediaStorageDir.getPath() + File.separator + photoFileName);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            //    bitmap = data.extras?.get("data") as Bitmap
            Log.d("Path", photoFile.getAbsolutePath());
            bitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
            photoFile.delete();

        }
        String profile = imageUploadToServerFunction();
        BackgroundWork back = new BackgroundWork(this);
        back.execute(
                "Update Image Profile",
                profile
        );

       back.getDailog().setOnDismissListener(dialog -> {
           url = Constant.BASE_URL2 + "user_image/" + mUserLoginInfoBean.getUserId() + ".jpg?v="+  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Calendar.getInstance().getTime());

           Glide.with(this)
                    .load(url)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.drawable.ic_person_add_black_24dp)
                    .circleCrop()
                    .into(userImg);
        });
    }

    // Upload captured image online on server function.
    private String imageUploadToServerFunction() {

        bitmap = Bitmap.createScaledBitmap(bitmap, 786, 1024, true);

        ByteArrayOutputStream byteArrayOutputStreamObject = new ByteArrayOutputStream();
        // Converting bitmap image to jpeg format, so by default image will upload in jpeg format.
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject);
        int options = 90;
        while (byteArrayOutputStreamObject.toByteArray().length / 1024 > 300) {  //Loop if compressed picture is greater than 400kb, than to compression
            byteArrayOutputStreamObject.reset();//Reset baos is empty baos
            bitmap.compress(
                    Bitmap.CompressFormat.JPEG,
                    options,
                    byteArrayOutputStreamObject
            ); //The compression options%, storing the compressed data to the baos
            options -= 10;//Every time reduced by 10
        }

        byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();

        return Base64.encodeToString(byteArrayVar, Base64.DEFAULT);
    }

}