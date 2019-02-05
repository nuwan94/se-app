package me.nuwan.seofficial.UI.People;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Locale;

import me.nuwan.seofficial.Model.User;
import me.nuwan.seofficial.R;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView dob,name;
    private ImageView imgCall, imgSms, imgLocation, profilePic;
    private User profile;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setView();
        Intent intent = getIntent();
        profile = (User) intent.getSerializableExtra("user");
        setProfile();
    }

    private void setProfile() {
        getSupportActionBar().setTitle(profile.ShortName());
        name.setText(profile.getName());
        Picasso.get().load(profile.getImg()).placeholder(R.color.silver).error(R.color.silver).into(profilePic);
        if (profile.getDob().isEmpty()) {
            dob.setVisibility(View.INVISIBLE);
        } else {
            dob.setVisibility(View.VISIBLE);
            dob.setText(String.format("%s\n(%s days more)",
                    profile.FormattedDob(),

                    String.valueOf(profile.dayCount())));
        }

        imgSms.setOnClickListener(this);
        imgCall.setOnClickListener(this);

        if (profile.getLat().startsWith("#") && profile.getLang().startsWith("#")) {
            imgLocation.setVisibility(View.INVISIBLE);
        } else {
            imgLocation.setVisibility(View.VISIBLE);
            imgLocation.setOnClickListener(this);
        }
    }


    private void setView() {
        toolbar = findViewById(R.id.profileToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        name = findViewById(R.id.profileFullName);
        profilePic = findViewById(R.id.profilePhoto);
        imgCall = findViewById(R.id.profileBtnCall);
        imgSms = findViewById(R.id.profileBtnSMS);
        imgLocation = findViewById(R.id.profileBtnLocation);
        dob = findViewById(R.id.profileDob);
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


    private void sendSms(String peopleMobileNumber) {
        Intent dialIntent = new Intent(Intent.ACTION_SENDTO);
        dialIntent.setData(Uri.parse("sms:" + peopleMobileNumber));
        startActivity(dialIntent);
    }

    private void makeCall(String peopleMobileNumber) {
        Intent dialIntent = new Intent(Intent.ACTION_DIAL);
        dialIntent.setData(Uri.parse("tel:" + peopleMobileNumber));
        startActivity(dialIntent);
    }

    private void showDirection(String lat, String lang, String name) {
        float latF = Float.parseFloat(lat);
        float langF = Float.parseFloat(lang);
        String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", latF, langF, name.split(" ")[0] + "'s Location");
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.profileBtnSMS:
                sendSms(profile.getMob());
                break;
            case R.id.profileBtnCall:
                makeCall(profile.getMob());
                break;
            case R.id.profileBtnLocation:
                showDirection(profile.getLat(),profile.getLang(),profile.getName());
                break;
        }
    }
}
