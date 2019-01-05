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

    RecyclerView rv;
    public List<Person> data;

    public PeopleFragment() {
        // Required empty public constructor
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
        DatabaseReference feedRef = FirebaseDB.getUserReference();

        feedRef.addValueEventListener(new ValueEventListener() {
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
                String mobile = singleSnapshot.child("mob").getValue().toString();
                String image = singleSnapshot.child("img").getValue().toString();
                String dob = singleSnapshot.child("dob").getValue().toString();

                Person person = new Person(sno,name,mobile,dob,image);

                data.add(person);
            }

        }

        Collections.sort(data);

        PeopleAdapter adapter = new PeopleAdapter(getActivity(), data);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

}
