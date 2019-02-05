package me.nuwan.seofficial.Widgets;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import me.nuwan.seofficial.Fireabse.FirebaseDB;
import me.nuwan.seofficial.Model.Common;
import me.nuwan.seofficial.Model.Feed;
import me.nuwan.seofficial.Model.User;
import me.nuwan.seofficial.R;

public class PostDialog extends AppCompatActivity {

    public static int EDIT_POST = 1;
    public static int NEW_POST = 0;

    String title, desc, type, pid, uid, author;

    private TextView dialogTitle;
    private EditText titleBox, descriptionBox;
    private Spinner spinnrType;
    private Button okButton;
    private ImageView boldButton, underlineButton, linkButton;
    private LinearLayout editor;

    ArrayAdapter<String> adapter;

    public PostDialog() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_post);
        setViews();
        loadExtras();
        BuildView((this.title.equals("")) ? PostDialog.NEW_POST : PostDialog.EDIT_POST, pid, uid, author);
    }

    private void loadExtras() {
        Intent intent = getIntent();
        this.title = intent.getStringExtra("title");
        this.desc = intent.getStringExtra("desc");
        this.type = intent.getStringExtra("type");
        this.pid = intent.getStringExtra("pid");
        this.uid = intent.getStringExtra("uid");
        this.author = intent.getStringExtra("author");
    }

    private void setViews() {
        Toolbar toolbar = findViewById(R.id.dialogPostToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        titleBox = findViewById(R.id.dialogEditTitle);
        spinnrType = findViewById(R.id.dialogSpinnerType);
        descriptionBox = findViewById(R.id.dialogEditDescription);

        okButton = findViewById(R.id.dialogPositiveButton);

        boldButton = findViewById(R.id.dialogBtnBold);
        underlineButton = findViewById(R.id.dialogBtnUnderline);
        linkButton = findViewById(R.id.dialogBtnLink);

        editor = findViewById(R.id.dialogEditorPanel);

        String[] types = {"Alert", "Academic", "Meetup", "SE"};
        adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, types);
        spinnrType.setAdapter(adapter);

    }

    private void BuildView(final int action, final String pid, final String uid, final String author) {
        getSupportActionBar().setTitle((action == 0) ? "Create Post" : "Edit Post");

        titleBox.setHint((action == 0) ? "Create Post" : "Edit Post");
        titleBox.setText(title);

        String updatedDesc = desc.replaceAll("(?i)<br */?>", "\n");
        updatedDesc = updatedDesc.replaceAll("<b>(.+?)</b>", "*$1*");
        updatedDesc = updatedDesc.replaceAll("<u>(.+?)</u>", "_$1_");
        descriptionBox.setText(updatedDesc);

        spinnrType.setSelection(adapter.getPosition(type));
        okButton.setText((action == 0) ? "Post" : "Update");

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = titleBox.getText().toString();
                String desc = descriptionBox.getText().toString();
                String htmlDesc = desc.replaceAll("\\*(.+?)\\*", "<b>$1</b>");
                htmlDesc = htmlDesc.replaceAll("\\_(.+?)\\_", "<u>$1</u>");
                htmlDesc = htmlDesc.replaceAll("\\[(.+?)\\]\\((.+?)\\)", "<a href='$2'>$1</a>");
                htmlDesc = htmlDesc.replaceAll("(\n)", "<br>");

                if (!(title.isEmpty() || desc.isEmpty())) {
                    DatabaseReference databaseReference = (action == 0) ? FirebaseDB.getFeedReference().push() : FirebaseDB.getFeedReference().child(pid);
                    databaseReference.setValue(
                            new Feed(
                                    (action == 0) ? title : "U-" + title,
                                    htmlDesc,
                                    spinnrType.getSelectedItem().toString(),
                                    uid,
                                    author,
                                    String.valueOf(System.currentTimeMillis())
                            )
                    );
                    finish();
                } else {
                    Common.showToast(view.getContext(), "Please enter post details");
                }

            }
        });


        boldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int start = descriptionBox.getSelectionStart();
                int end = descriptionBox.getSelectionEnd();
                String modified_selection = "*" + descriptionBox.getText().subSequence(start, end) + "*";
                descriptionBox.getText().replace(start, end, modified_selection);
            }
        });

        underlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int start = descriptionBox.getSelectionStart();
                int end = descriptionBox.getSelectionEnd();
                String modified_selection = "_" + descriptionBox.getText().subSequence(start, end) + "_";
                descriptionBox.getText().replace(start, end, modified_selection);
            }
        });

        linkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                descriptionBox.append(" [text](link) ");
//                descriptionBox.setSelection(descriptionBox.getText().length() - 1);
//                descriptionBox.requestFocus();
            }
        });

        descriptionBox.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    editor.setVisibility(View.VISIBLE);
                } else {
                    editor.setVisibility(View.GONE);
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

}
