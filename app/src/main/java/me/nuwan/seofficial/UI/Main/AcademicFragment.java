package me.nuwan.seofficial.UI.Main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.nuwan.seofficial.Model.Common;
import me.nuwan.seofficial.R;
import me.nuwan.seofficial.UI.Academic.ResultActivity;
import me.nuwan.seofficial.UI.Academic.TimetableAcivity;

public class AcademicFragment extends Fragment implements View.OnClickListener {

    CardView timetable, result, note, skill;

    public AcademicFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_academic, container, false);

        timetable = v.findViewById(R.id.academicTimetable);
        result = v.findViewById(R.id.academicResults);
        note = v.findViewById(R.id.academicNotes);
        skill = v.findViewById(R.id.academicCareer);


        timetable.setOnClickListener(this);
        result.setOnClickListener(this);
        note.setOnClickListener(this);
        skill.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.academicTimetable:
                Intent timetable = new Intent(getActivity(), TimetableAcivity.class);
                startActivity(timetable);
                break;

            case R.id.academicResults:
                Intent results = new Intent(getActivity(), ResultActivity.class);
                startActivity(results);
                break;

            case R.id.academicNotes:
            case R.id.academicCareer:
                Common.showToast(view.getContext(),"Available Soon");
                break;
        }
    }
}
