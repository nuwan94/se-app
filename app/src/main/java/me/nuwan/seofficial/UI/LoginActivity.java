package me.nuwan.seofficial.UI;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import me.nuwan.seofficial.Fireabse.FirebaseDB;
import me.nuwan.seofficial.Model.Login;
import me.nuwan.seofficial.Model.User;
import me.nuwan.seofficial.R;

public class LoginActivity extends Activity {

    private EditText txtUN,txtPW;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final DatabaseReference loginRef = FirebaseDB.getLoginReference();

        setContentView(R.layout.activity_login);
        txtPW = findViewById(R.id.splash_txtPassword);
        txtUN = findViewById(R.id.splash_txtUserName);
        final Button btnLogin = findViewById(R.id.splash_btnlogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 final String inputUN = txtUN.getText().toString();
                 final String inputPW = txtPW.getText().toString();

                if(!(inputPW.isEmpty() || inputUN.isEmpty())){
                    loginRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.child(inputUN).exists()) {
                                Login.currentToken = dataSnapshot.child(inputUN).getValue(Login.class);
                                if (inputPW.equals(Login.currentToken.getPwd())) {

                                    DatabaseReference userRef = FirebaseDB.getUserReference();
                                    userRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            User.currentUser = dataSnapshot.child(inputUN).getValue(User.class);
                                            showToast("user set : " + User.currentUser.getImg());
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                    saveLogin(inputUN, inputPW);
                                    loginIn();
                                } else {
                                    showToast("Incorrect Password");
                                }
                            }else{
                                showToast("Account not found! Please contact Admin.");
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            showToast("Something went wrong in Server.");
                        }
                    });
                }else{
                    showToast("Please fill the required fields.");
                }
            }});
    }

    private void saveLogin(String inputUN, String inputPW) {
        SharedPreferences.Editor editor =
                getSharedPreferences("LoginPreferences", MODE_PRIVATE).edit();
        editor.putString("un", inputUN);
        editor.putString("pw", inputPW);
        editor.apply();
    }

    private void loginIn(){
        showToast("Hello " + Login.currentToken.getName());
        Intent i = new Intent(LoginActivity.this,MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }

    void showToast(String msg){
        Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
