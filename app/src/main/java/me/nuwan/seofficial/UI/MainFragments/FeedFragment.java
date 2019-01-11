package me.nuwan.seofficial.UI.MainFragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.nuwan.seofficial.Adapters.FeedAdapter;
import me.nuwan.seofficial.Model.Feed;
import me.nuwan.seofficial.Fireabse.FirebaseDB;
import me.nuwan.seofficial.R;
import me.nuwan.seofficial.Widgets.MovableFAB;
import me.nuwan.seofficial.Widgets.PostDialog;

public class FeedFragment extends Fragment {

    private RecyclerView rv;
    private List<Feed> data;
    DatabaseReference feedRef;

    public FeedFragment() {

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_feed, container, false);
        rv = v.findViewById(R.id.feedList);

        data = new ArrayList<>();
        feedRef = FirebaseDB.getFeedReference();
        feedRef.keepSynced(true);

        MovableFAB addPostBtn = v.findViewById(R.id.feedAddPostBtn);

        addPostBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                PostDialog dialog = new PostDialog(view.getContext(),PostDialog.NEW_POST);
            }
        });

        addPostBtn.setMargin(20);

        return v;
    }

    @Override
    public void onStart() {

        feedRef.orderByChild("time").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                data.clear();
                try {
                    fetchFeed(dataSnapshot);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        super.onStart();
    }

    private void fetchFeed(DataSnapshot dataSnapshot) {

        for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {

            if (singleSnapshot.hasChildren()) {
                String title = singleSnapshot.child("title").getValue().toString();
                String desc = singleSnapshot.child("desc").getValue().toString();
                String type = singleSnapshot.child("type").getValue().toString();
                String by = singleSnapshot.child("by").getValue().toString();
                String uid = singleSnapshot.child("uid").getValue().toString() + "," + singleSnapshot.getKey();
                String time = singleSnapshot.child("time").getValue().toString();
                Feed feed = new Feed(title, desc, type, by, uid, time);
                data.add(feed);
            }
        }

        Collections.reverse(data);

        FeedAdapter adapter = new FeedAdapter(getActivity(), data);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

    }
}
