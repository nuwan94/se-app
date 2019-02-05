package me.nuwan.seofficial.UI.Academic;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.security.auth.Subject;

import me.nuwan.seofficial.Adapters.TimeTableAdapter;
import me.nuwan.seofficial.Fireabse.FirebaseDB;
import me.nuwan.seofficial.Model.Common;
import me.nuwan.seofficial.Model.Feed;
import me.nuwan.seofficial.Model.TimeTableSubject;
import me.nuwan.seofficial.Model.User;
import me.nuwan.seofficial.R;
import me.nuwan.seofficial.UI.Settings.SelectLocationActivity;

public class TimetableAcivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private RecyclerView rv;
    private Button btnMon, btnTue, btnWed, btnThu, btnFri, btnSat, btnSun, left, right;
    Button[] weekdayButtons = new Button[7];
    List<String> weekDays;
    int white, highlighted;
    List<TimeTableSubject> data;
    TimeTableAdapter dayAdapter;
    int selectedTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);


        white = getResources().getColor(R.color.white);
        highlighted = getResources().getColor(R.color.silver);
        data = new ArrayList<>();
        setView();

        weekDays = new ArrayList<>(Arrays.asList("mon", "tue", "wed", "thu", "fri", "sat", "sun" ));
//        focusToday();
        dayAdapter = new TimeTableAdapter(this, data);

        rv = findViewById(R.id.ttRecyclerView);
        rv.setAdapter(dayAdapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = findViewById(R.id.ttFab);

        if("admin".equals(User.currentUser.getType())){
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(this);
        }else{
            fab.setVisibility(View.GONE);
        }
    }

    private void focusToday() {
        Calendar today = new GregorianCalendar();
        selectedTab = weekDays.indexOf(today.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.US).toLowerCase());
        weekdayButtons[selectedTab].performClick();
    }

    @Override
    public void onResume(){
        super.onResume();
        focusToday();
    }


    private void setView() {
        toolbar = findViewById(R.id.timeTableToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        left = findViewById(R.id.ttOverlayLeft);
        right = findViewById(R.id.ttOverlayRight);
        left.setOnClickListener(this);
        right.setOnClickListener(this);

        weekdayButtons[0] = btnMon = findViewById(R.id.ttBtnMon);
        weekdayButtons[1] = btnTue = findViewById(R.id.ttBtnTue);
        weekdayButtons[2] = btnWed = findViewById(R.id.ttBtnWed);
        weekdayButtons[3] = btnThu = findViewById(R.id.ttBtnThu);
        weekdayButtons[4] = btnFri = findViewById(R.id.ttBtnFri);
        weekdayButtons[5] = btnSat = findViewById(R.id.ttBtnSat);
        weekdayButtons[6] = btnSun = findViewById(R.id.ttBtnSun);

        for (Button i : weekdayButtons) {
            i.setOnClickListener(this);
        }
    }

    private void setupListView(String day) {
//        Common.showToast(this, day);
        data.clear();
        Query timeTableReference = FirebaseDB.getTimeTableReference().child(day).orderByChild("start");
        timeTableReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    if (singleSnapshot.hasChildren()) {
                        String code = singleSnapshot.child("code").getValue().toString() + ","
                                + String.valueOf(weekDays.get(selectedTab)) + ","
                                + singleSnapshot.getKey();
                        String name = singleSnapshot.child("course").getValue().toString();
                        String start = singleSnapshot.child("start").getValue().toString();
                        String end = singleSnapshot.child("end").getValue().toString();
                        String hall = singleSnapshot.child("hall").getValue().toString();
                        String lec = singleSnapshot.child("lec").getValue().toString();
                        TimeTableSubject subject = new TimeTableSubject(code, name, start, end, hall, lec);
                        data.add(subject);
                    }
                }
                dayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
    public void onClick(View view) {
        for (Button i : weekdayButtons) {
            i.setBackgroundColor(white);
        }
        view.setBackgroundColor(highlighted);
        switch (view.getId()) {
            case R.id.ttBtnSun:
                selectedTab = 6;
                setupListView("sun");
                break;
            case R.id.ttBtnMon:
                selectedTab = 0;
                setupListView("mon");
                break;
            case R.id.ttBtnTue:
                selectedTab = 1;
                setupListView("tue");
                break;
            case R.id.ttBtnWed:
                selectedTab = 2;
                setupListView("wed");
                break;
            case R.id.ttBtnThu:
                selectedTab = 3;
                setupListView("thu");
                break;
            case R.id.ttBtnFri:
                selectedTab = 4;
                setupListView("fri");
                break;
            case R.id.ttBtnSat:
                selectedTab = 5;
                setupListView("sat");
                break;
            case R.id.ttFab:
                Intent i = new Intent(this, AddTimeTableActivity.class);
                startActivity(i);
                break;

//            case R.id.ttOverlayLeft:
//                if (selectedTab - 1 < 0) {
//                    weekdayButtons[0].performClick();
//                    selectedTab = 0;
//                } else {
//                    weekdayButtons[--selectedTab].performClick();
//                }
//                break;
//            case R.id.ttOverlayRight:
//                if (selectedTab + 1 > 6) {
//                    weekdayButtons[6].performClick();
//                    selectedTab = 6;
//                } else {
//                    weekdayButtons[++selectedTab].performClick();
//                }
//                break;
            default:
                setupListView("mon");
                break;
        }
    }
}
