package me.nuwan.seofficial.UI;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


import java.io.File;
import java.util.Calendar;
import java.util.TimeZone;

import me.nuwan.seofficial.Fireabse.FirebaseDB;
import me.nuwan.seofficial.Model.Common;
import me.nuwan.seofficial.Model.User;
import me.nuwan.seofficial.R;

public class SettingsActivity extends AppCompatActivity {

    private StorageReference storageReference;
    private ProgressDialog progressDialog;
    private CircleImageView avatar;
    private User user;

    private static final int GALLERY_INTENT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        progressDialog = new ProgressDialog(this);
        storageReference = FirebaseStorage.getInstance().getReference().child("Avatars");

        user = User.currentUser;

        CircleImageView selectImageButton = findViewById(R.id.settings_selectImage);
        avatar = findViewById(R.id.settings_avatar);

        Picasso.get().load(user.getImg()).placeholder(R.color.silver).error(R.color.silver).into(avatar);

        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);
            }
        });

        final TextView settingsEditUN = findViewById(R.id.settEditUserName);
        final TextView settPhone = findViewById(R.id.settEditPhone);
        final TextView settDoB = findViewById(R.id.settDOB);

        final Button saveLocationButton = findViewById(R.id.settLocationUpdateButton);
        TextView settLocation = findViewById(R.id.settLocation);
        Switch showMyLocationSwitch = findViewById(R.id.settShowLocation);

        settingsEditUN.setText(user.getName());
        settPhone.setText(user.getMob());
        settDoB.setText(user.getDob());

        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar selected = Calendar.getInstance(TimeZone.getDefault());
                selected.set(year, monthOfYear, dayOfMonth);
                settDoB.setText(Common.dateFormat.format(selected.getTime()));
                User.currentUser.setDob(Common.dateFormat.format(selected.getTime()));
            }

        }, 1995, 0, 1);

        settDoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        if(user.getLat().equals("0.0") && user.getLang().equals("0.0")){
            showMyLocationSwitch.setChecked(false);
        }else{
            showMyLocationSwitch.setChecked(true);
        }

        settLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent selectLocationIntetnt = new Intent(SettingsActivity.this,SelectLocation.class);
//                startActivity(selectLocationIntetnt);
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            return true;

        }
        return false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri destination = null;

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
            Crop.of(data.getData(), destination)
                    .asSquare()
                    .start(this);
        }

        if (requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK) {

            progressDialog.setMessage("Uploading...");
            progressDialog.show();

            Picasso.get().load(Crop.getOutput(data)).into(avatar);

            final StorageReference uploaderRef = storageReference.child(user.getSno());

            uploaderRef.putFile(Crop.getOutput(data)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    uploaderRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            User.currentUser.setImg(uri.toString());
                            updateCurrentUser();
                            progressDialog.dismiss();
                            Common.showToast(SettingsActivity.this, "Profile picture updated.");
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Common.showToast(SettingsActivity.this, "Error : " + e.getMessage());
                }
            });
        }

    }

    private void updateCurrentUser() {
        FirebaseDB.getUserReference()
                .child(User.currentUser.getSno())
                .setValue(User.currentUser);
    }
}
