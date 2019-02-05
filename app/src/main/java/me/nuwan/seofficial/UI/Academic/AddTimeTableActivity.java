package me.nuwan.seofficial.UI.Academic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import me.nuwan.seofficial.R;

public class AddTimeTableActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText start, end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_time_table);

        Toolbar toolbar = findViewById(R.id.addTimeToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        start = findViewById(R.id.addTimeStart);
        end = findViewById(R.id.addTimeEnd);
        start.setOnClickListener(this);
        end.setOnClickListener(this);
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

    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.addTimeStart:
//                TimePickerDialog mTimePicker = new TimePickerDialog(view.getContext(),
//                        android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
//                        new TimePickerDialog.OnTimeSetListener() {
//                            @Override
//                            public void onTimeSet(TimePicker timePicker, int hour, int minutes) {
//                                start.setText(timePicker);
//                            }
//                        }, 0, 0, false);
//
//                break;
//            case R.id.addTimeEnd:
//                break;
        }

    }
}
