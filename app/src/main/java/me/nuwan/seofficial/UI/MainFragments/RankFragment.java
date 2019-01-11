package me.nuwan.seofficial.UI.MainFragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import me.nuwan.seofficial.Adapters.PeopleAdapter;
import me.nuwan.seofficial.Adapters.RankAdapter;
import me.nuwan.seofficial.Fireabse.FirebaseDB;
import me.nuwan.seofficial.Model.Person;
import me.nuwan.seofficial.Model.Rank;
import me.nuwan.seofficial.R;
import me.nuwan.seofficial.UI.MainActivity;

public class RankFragment extends Fragment {

    RecyclerView rv;
    public List<Rank> data;

    public RankFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_rank, container, false);

        rv = v.findViewById(R.id.gpaList);
        data = new ArrayList<>();

        return v;
    }

    @Override
    public void onStart() {

        DatabaseReference userReference = FirebaseDB.getUserReference();
        userReference.keepSynced(true);

        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                data.clear();
                fetchRanks(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        super.onStart();
    }

    private void fetchRanks(DataSnapshot dataSnapshot) {
        for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
            if (singleSnapshot.hasChildren()) {
                String name = singleSnapshot.child("name").getValue().toString();
                String gpa = singleSnapshot.child("gpa").getValue().toString();
                Rank rank = new Rank(name, gpa);
                data.add(rank);
            }
        }

        Collections.sort(data, new Comparator<Rank>() {
            @Override
            public int compare(Rank rank, Rank t1) {
                Float x = Float.parseFloat(rank.getScore());
                Float y = Float.parseFloat(t1.getScore());
                return (x > y) ? -1 : (x.equals(y) ? 0 : 1);
            }
        });

        RankAdapter adapter = new RankAdapter(getActivity(), (data.size()>=10)? data.subList(0,10) : data);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

}


