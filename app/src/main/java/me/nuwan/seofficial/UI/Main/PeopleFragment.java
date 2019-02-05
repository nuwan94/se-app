package me.nuwan.seofficial.UI.Main;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.nuwan.seofficial.Fireabse.FirebaseDB;
import me.nuwan.seofficial.Adapters.PeopleAdapter;
import me.nuwan.seofficial.Model.Common;
import me.nuwan.seofficial.Model.Person;
import me.nuwan.seofficial.Model.User;
import me.nuwan.seofficial.R;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class PeopleFragment extends Fragment {

    private RecyclerView rv;
    private SearchView searchView;
    private Spinner spinner;
    private List<User> data;

    public PeopleFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_people, container, false);
        searchView = v.findViewById(R.id.peopleSearch);
//        spinner = v.findViewById(R.id.peopleSortBy);
        data = new ArrayList<>();
        rv = v.findViewById(R.id.peopleList);
        final DatabaseReference userRef = FirebaseDB.getUserReference();
        userRef.keepSynced(true);
        LoadPeoples(userRef);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {


//        final String[] types = {"Index", "Name","Days to Bday"};
//        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
//                android.R.layout.simple_spinner_dropdown_item, types);
//        spinner.setAdapter(adapter);
//        spinner.setSelection(adapter.getPosition(Common.sortPeopleType));

//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                searchView.setQuery("", false);
//                Common.sortPeopleType = types[i];
//                saveSortPreferences(types[i]);
//                LoadPeoples(userRef);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

        super.onStart();
    }

//    private void saveSortPreferences(String type){
//        SharedPreferences.Editor editor =  this.getActivity().getSharedPreferences("sortTypes", MODE_PRIVATE).edit();
//        editor.putString("peopleSort", type);
//        editor.apply();
//    }

    private void LoadPeoples(DatabaseReference userRef) {
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

//        Collections.sort(data);

        final PeopleAdapter adapter = new PeopleAdapter(getActivity(), data);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return true;
            }
        });


    }

//    int firstVisiblePosition = adapter.findFirstVisibleItemPosition();
//    View v = layoutManager.getChildAt(0);
//        if (firstVisiblePosition > 0 && v != null) {
//        int offsetTop = v.getTop();
//        chatAdapter.notifyDataSetChanged();
//
//        if (firstVisiblePosition - 1 >= 0 && chatAdapter.getItemCount() > 0) {
//            layoutManager.scrollToPositionWithOffset(firstVisiblePosition - 1, offsetTop);
//        }
//    }
}
