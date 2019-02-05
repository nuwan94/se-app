package me.nuwan.seofficial.UI.Settings;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
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
import java.util.Date;
import java.util.TimeZone;

import me.nuwan.seofficial.Fireabse.FirebaseDB;
import me.nuwan.seofficial.Model.Common;
import me.nuwan.seofficial.Model.Login;
import me.nuwan.seofficial.Model.User;
import me.nuwan.seofficial.R;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private StorageReference storageReference;
    private ProgressDialog progressDialog;
    private CircleImageView avatar, selectImageButton;
    private User user;

    private EditText settUN, settPhone;
    private TextView settDoB, settLocation;
    private Button saveProfile;
    private Switch showMyLocationSwitch;


    EditText oldPass, newPass, confirmPass;
    String erro = "";

    private static final int GALLERY_INTENT = 1, MAP_INTENT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        storageReference = FirebaseStorage.getInstance().getReference().child("Avatars");
        user = User.currentUser;

        setViews();
        buildViews();
        setActions();

    }

    private void setViews() {
        selectImageButton = findViewById(R.id.settings_selectImage);
        avatar = findViewById(R.id.settings_avatar);

        settUN = findViewById(R.id.settEditUserName);
        settPhone = findViewById(R.id.settEditPhone);
        settDoB = findViewById(R.id.settEditDOB);
        saveProfile = findViewById(R.id.settUpdateProfile);
        settLocation = findViewById(R.id.settEditLocation);
        showMyLocationSwitch = findViewById(R.id.settEditLocationVisibility);

        progressDialog = new ProgressDialog(this);
    }

    private void buildViews() {
        Toolbar toolbar = findViewById(R.id.settToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        settUN.setText(user.getName());
        settPhone.setText(user.getMob());
        if ("".equals(User.currentUser.getDob()) || User.currentUser.getDob() == null) {
            settDoB.setText("");
        } else {
            settDoB.setText(Common.dateFormat.format(Long.parseLong(User.currentUser.getDob())));
        }

        Picasso.get().load(user.getImg()).placeholder(R.color.silver).error(R.color.silver).into(avatar);

        if (user.getLat().startsWith("#") && user.getLang().startsWith("#")) {
            showMyLocationSwitch.setChecked(false);
        } else {
            showMyLocationSwitch.setChecked(true);
        }

    }

    private void setActions() {

        settUN.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                User.currentUser.setName(editable.toString());
                sync();
            }
        });

        settPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                User.currentUser.setMob(editable.toString());
                sync();
            }
        });

        settDoB.setOnClickListener(this);
        selectImageButton.setOnClickListener(this);
        settLocation.setOnClickListener(this);

        showMyLocationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    User.currentUser.setLat("#".concat(User.currentUser.getLat()));
                    User.currentUser.setLang("#".concat(User.currentUser.getLang()));
                } else {
                    User.currentUser.setLat(User.currentUser.getLat().replaceAll("#", ""));
                    User.currentUser.setLang(User.currentUser.getLang().replaceAll("#", ""));
                }
                sync();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            return true;
        }
        if (item.getItemId() == R.id.settChangePassword) {
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(SettingsActivity.this);
            alertDialog.setTitle("Change Password");

            oldPass = new EditText(SettingsActivity.this);
            newPass = new EditText(SettingsActivity.this);
            confirmPass = new EditText(SettingsActivity.this);

            oldPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
            newPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
            confirmPass.setTransformationMethod(PasswordTransformationMethod.getInstance());

            oldPass.setHint("Old Password");
            newPass.setHint("New Password (MinLen : 5)");
            confirmPass.setHint("Confirm New Password");
            LinearLayout ll = new LinearLayout(SettingsActivity.this);
            ll.setOrientation(LinearLayout.VERTICAL);
            ll.setPaddingRelative(50, 50, 50, 50);

            ll.addView(oldPass);
            ll.addView(newPass);
            ll.addView(confirmPass);

            alertDialog.setView(ll);

            alertDialog.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Login.currentToken.setPwd(newPass.getText().toString());
                            FirebaseDB.getLoginReference()
                                    .child(User.currentUser.getSno())
                                    .setValue(Login.currentToken);
                            Common.showToast(getApplicationContext(),"Password Updated");

                        }
                    });

            alertDialog.setNegativeButton("No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            final AlertDialog alert11 = alertDialog.create();
            alert11.show();
            alert11.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);

            TextWatcher passwordWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String old = oldPass.getText().toString();
                    String newP = newPass.getText().toString();
                    String newPC = confirmPass.getText().toString();

                    if (!Login.currentToken.getPwd().equals(old)) {
                        erro = "Invalid old password";
                    } else if (newP.equals(old)) {
                        erro = "Same password";
                    } else if (!newP.equals(newPC)) {
                        erro = "Mismatch new password";
                    } else if (newP.length() < 5 || newPC.length() < 5) {
                        erro = "Weak new password";
                    } else {
                        erro = "";
                    }

                    if (!"".equals(erro)) {
                        alert11.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
                    } else {
                        alert11.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
                    }
                }
            };
            oldPass.addTextChangedListener(passwordWatcher);
            newPass.addTextChangedListener(passwordWatcher);
            confirmPass.addTextChangedListener(passwordWatcher);

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
                            sync();
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

        if (requestCode == MAP_INTENT && resultCode == RESULT_OK && data != null) {
            String lat = data.getStringExtra("lat");
            String lang = data.getStringExtra("lang");
//            Common.showToast(this, lat + " " + lang);
            if (!(lat.isEmpty() || lang.isEmpty())) {
                User.currentUser.setLat(lat);
                User.currentUser.setLang(lang);
                sync();
            }
        }

    }

    private void sync() {
        FirebaseDB.getUserReference()
                .child(User.currentUser.getSno())
                .setValue(User.currentUser);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.settings_selectImage:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);
                break;

            case R.id.settEditLocation:
                if (!User.currentUser.getLat().startsWith("#")) {
                    Intent i = new Intent(this, SelectLocationActivity.class);
                    startActivityForResult(i, MAP_INTENT);
                } else {
                    Common.showToast(this, "Please enable location sharing.");
                }

                break;

            case R.id.settEditDOB:
                Calendar calDate = Calendar.getInstance();
                if (user.getDob().isEmpty()) {
                    calDate.set(1995, 0, 1);
                } else {
                    Date dob = new Date(Long.parseLong(user.getDob()));
                    settDoB.setText(Common.dateFormat.format(dob));
                    calDate.setTime(dob);
                }
                final DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar selected = Calendar.getInstance(TimeZone.getDefault());
                        selected.set(year, monthOfYear, dayOfMonth);
                        settDoB.setText(Common.dateFormat.format(selected.getTime()));
                        User.currentUser.setDob(String.valueOf(selected.getTimeInMillis()));
                        sync();
                    }
                }, calDate.get(Calendar.YEAR), calDate.get(Calendar.MONTH), calDate.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
                break;

            default:
                break;

        }
    }
}
