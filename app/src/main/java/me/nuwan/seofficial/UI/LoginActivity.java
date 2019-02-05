package me.nuwan.seofficial.UI;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import me.nuwan.seofficial.Fireabse.FirebaseDB;
import me.nuwan.seofficial.Model.Common;
import me.nuwan.seofficial.Model.Login;
import me.nuwan.seofficial.Model.User;
import me.nuwan.seofficial.R;
import me.nuwan.seofficial.UI.Main.MainActivity;

public class LoginActivity extends Activity {

    private EditText txtUN, txtPW;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final DatabaseReference loginRef = FirebaseDB.getLoginReference();

        txtPW = findViewById(R.id.logPassword);
        txtUN = findViewById(R.id.logUsername);
        final TextInputLayout textPWDInputLayout = findViewById(R.id.logPwdLayout);
        final TextInputLayout textUNInputLayout = findViewById(R.id.logUnameLayout);
        textPWDInputLayout.setPasswordVisibilityToggleEnabled(true);

        final Button btnLogin = findViewById(R.id.logButoon);

        txtPW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textUNInputLayout.setError(null);
                textPWDInputLayout.setError(null);
            }
        });

        txtUN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textUNInputLayout.setError(null);
                textPWDInputLayout.setError(null);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String inputUN = txtUN.getText().toString();
                final String inputPW = txtPW.getText().toString();

                if (!(inputPW.isEmpty() || inputUN.isEmpty())) {
                    loginRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(inputUN).exists()) {
                                Login.currentToken = dataSnapshot.child(inputUN).getValue(Login.class);

                                if (inputPW.equals(Login.currentToken.getPwd())) {
                                    DatabaseReference userRef = FirebaseDB.getUserReference();
                                    userRef.addValueEventListener(new ValueEventListener() {

                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            User.currentUser = dataSnapshot.child(inputUN).getValue(User.class);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            Common.showToast(LoginActivity.this,"Please make sure your internet connection is working.");
                                        }
                                    });
                                    saveLogin(inputUN, inputPW);
                                    loginIn();
                                } else {
                                    textPWDInputLayout.setError("Incorrect Password");
                                }

                            } else {
                                textUNInputLayout.setError("Account not found! Please contact Admin.");
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Common.showToast(LoginActivity.this,"Please make sure your internet connection is working.");
                        }
                    });
                } else {
                    textUNInputLayout.setError("* Required.");
                    textPWDInputLayout.setError("* Required.");
//                    Common.showToast(LoginActivity.this,"Please fill the required fields.");
                }
            }
        });
    }

    private void saveLogin(String inputUN, String inputPW) {
        SharedPreferences.Editor editor =
                getSharedPreferences("LoginPreferences", MODE_PRIVATE).edit();
        editor.putString("un", inputUN);
        editor.putString("pw", inputPW);
        editor.apply();
    }

    private void loginIn() {
//        Common.showToast(this,"Login Successfully!");
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }
}
