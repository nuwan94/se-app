package me.nuwan.seofficial.UI;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

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

public class LoaderActivity extends Activity {
    private String unSaved, pwSaved;
    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader);

        final DatabaseReference loginRef = FirebaseDB.getDatabase()
                .getReference("login");

        checkSavedData();

        logo = findViewById(R.id.loader_logo);
        Animation fadeandZoom = AnimationUtils.loadAnimation(this, R.anim.zoom_in_out);
        logo.setAnimation(fadeandZoom);

        fadeandZoom.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (pwSaved != null && unSaved != null) {
                    loginRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Login.currentToken = dataSnapshot.child(unSaved).getValue(Login.class);
                            if (pwSaved.equals(Login.currentToken.getPwd())) {
                                DatabaseReference userRef = FirebaseDB.getUserReference();
                                userRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        User.currentUser = dataSnapshot.child(unSaved).getValue(User.class);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Common.showToast(LoaderActivity.this, "Please make sure your internet connection is working.");
                                    }
                                });
                                loginIn(MainActivity.class);
                            } else {
                                loginIn(LoginActivity.class);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            loginIn(LoginActivity.class);
                        }
                    });
                } else {
                    loginIn(LoginActivity.class);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }

    private void loginIn(Class activity) {
        Intent i = new Intent(LoaderActivity.this, activity);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }


    private void checkSavedData() {
        SharedPreferences prefs = getSharedPreferences("LoginPreferences", MODE_PRIVATE);
        String restoreCheck = prefs.getString("un", null);
        if (restoreCheck != null) {
            unSaved = prefs.getString("un", "undefined");
            pwSaved = prefs.getString("pw", "undefined");
        }

        SharedPreferences sort = getSharedPreferences("sortTypes", MODE_PRIVATE);
        Common.sortPeopleType = sort.getString("peopleSort", "Name");
    }

}
