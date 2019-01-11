package me.nuwan.seofficial.UI.MainFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.nuwan.seofficial.Fireabse.FirebaseDB;
import me.nuwan.seofficial.Adapters.PeopleAdapter;
import me.nuwan.seofficial.Model.Person;
import me.nuwan.seofficial.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class PeopleFragment extends Fragment {

    private RecyclerView rv;
    private List<Person> data;

    public PeopleFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_people, container, false);

        rv = v.findViewById(R.id.peopleList);
        data = new ArrayList<>();
        return v;
    }

    @Override
    public void onStart() {
        DatabaseReference userRef = FirebaseDB.getUserReference();
        userRef.keepSynced(true);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                data.clear();
                fetchPeople(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        super.onStart();
    }

    private void fetchPeople(DataSnapshot dataSnapshot) {
        for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){

            if(singleSnapshot.hasChildren()){
                String name = singleSnapshot.child("name").getValue().toString();
                String sno = singleSnapshot.child("sno").getValue().toString();
                String mob = singleSnapshot.child("mob").getValue().toString();
                String img = singleSnapshot.child("img").getValue().toString();
                String dob = singleSnapshot.child("dob").getValue().toString();
                String lat = singleSnapshot.child("lat").getValue().toString();
                String lang = singleSnapshot.child("lang").getValue().toString();
                String gpa = singleSnapshot.child("gpa").getValue().toString();

                Person person = new Person(sno,name,mob,dob,img,lat,lang,gpa);

                data.add(person);
            }

        }

        Collections.sort(data);

        PeopleAdapter adapter = new PeopleAdapter(getActivity(), data);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

}
