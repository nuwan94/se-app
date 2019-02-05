package me.nuwan.seofficial.UI.Academic;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import me.nuwan.seofficial.Fireabse.FirebaseDB;
import me.nuwan.seofficial.Model.Common;
import me.nuwan.seofficial.Model.User;
import me.nuwan.seofficial.R;

public class ResultActivity extends AppCompatActivity {

    private TextView gpaText, webLink;
    private EditText manualGPA;
    private Button updateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        setViews();
    }

    private void setViews() {
        Toolbar toolbar = findViewById(R.id.resultToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        gpaText = findViewById(R.id.resultGPA);
        gpaText.setText(User.currentUser.getGpa());

        manualGPA = findViewById(R.id.resultManulaGPA);
        manualGPA.setText(User.currentUser.getGpa());

        webLink = findViewById(R.id.resultGPAsitenotice);
        webLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://nuwan94.github.io/SE-GPA-Calculator");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        updateBtn = findViewById(R.id.resultGPAupdatBtn);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String readGPA = String.valueOf(manualGPA.getText());
                float GPA_Validation = Float.valueOf(readGPA);
                if (readGPA != null && GPA_Validation >= 0.0F && GPA_Validation <= 4.0F) {
                    User.currentUser.setGpa(readGPA);
                    sync();
                    Common.showToast(view.getContext(),"GPA Updated");
                }else{
                    Common.showToast(view.getContext(),"Please enter valid GPA value.");
                }
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

    private void sync() {
        FirebaseDB.getUserReference()
                .child(User.currentUser.getSno())
                .setValue(User.currentUser);
    }
}
