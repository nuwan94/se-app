package me.nuwan.seofficial.Widgets;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import me.nuwan.seofficial.Fireabse.FirebaseDB;
import me.nuwan.seofficial.Model.Feed;
import me.nuwan.seofficial.Model.User;
import me.nuwan.seofficial.R;

public class PostDialog extends Dialog {

    public static int EDIT_POST = 1;
    public static int NEW_POST = 0;

    private Context context;
    private String title;
    private String desc;
    private String type;

    public PostDialog(Context context, int action) {
        super(context);
        this.title = "";
        this.desc = "";
        this.type = "";
        this.context = context;
        Build(action, "");
    }

    public PostDialog(Context context, int action, String pid, String title, String desc, String type) {
        super(context);
        this.title = title;
        this.desc = desc;
        this.type = type;
        this.context = context;
        Build(action, pid);
    }

    private void Build(final int action, final String pid) {

        this.setContentView(R.layout.dialog_post);

        TextView dialogTitle = findViewById(R.id.dialogTextViewTitle);
        final EditText titleBox = findViewById(R.id.dialogEditTitle);
        final Spinner sp = findViewById(R.id.dialogSpinnerType);
        final EditText descriptionBox = findViewById(R.id.dialogEditDescription);

        Button okButton = findViewById(R.id.dialogPositiveButton);
        Button cancelButton = findViewById(R.id.dialogNegativeButton);

        ImageView boldButton = findViewById(R.id.dialogBtnBold);
        ImageView underlineButton = findViewById(R.id.dialogBtnUnderline);
        ImageView linkButton = findViewById(R.id.dialogBtnLink);


        String[] types = {"Alert", "Note", "SESA", "Other"};
        final ArrayAdapter<String> adp = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_dropdown_item, types);
        sp.setAdapter(adp);

        dialogTitle.setText((action == 0) ? "Create Post" : "Edit Post");
        titleBox.setText(this.title);
        descriptionBox.setText(this.desc);
        sp.setSelection(adp.getPosition(this.type),true);
        okButton.setText((action == 0) ? "Post" : "Update");

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleBox.getText().toString();

                String desc = descriptionBox.getText().toString();
                String htmlDesc = desc.replaceAll("\\*(.+?)\\*", "<b>$1</b>");
                htmlDesc = htmlDesc.replaceAll("\\_(.+?)\\_", "<u>$1</u>");
                htmlDesc = htmlDesc.replaceAll("\\[(.+?)\\]\\((.+?)\\)", "<a href='$2'>$1</a>");

                if (!(title.isEmpty() || desc.isEmpty())) {
                    DatabaseReference databaseReference = (action == 0) ? FirebaseDB.getFeedReference().push() : FirebaseDB.getFeedReference().child(pid);
                    databaseReference.setValue(
                            new Feed(title,
                                    htmlDesc,
                                    sp.getSelectedItem().toString().toLowerCase(),
                                    User.currentUser.getName(),
                                    User.currentUser.getSno(),
                                    String.valueOf(System.currentTimeMillis())
                            )
                    );
                    dismiss();
                } else {
                    Toast.makeText(getContext(), "Please fill required fields", Toast.LENGTH_SHORT).show();
                }

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
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

//        linkButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                descriptionBox.append(" [text](link) ");
//                descriptionBox.setSelection(descriptionBox.getText().length() - 1);
//                descriptionBox.requestFocus();
//            }
//        });


        this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        this.getWindow().setGravity(Gravity.CENTER);
        this.show();
    }


}
