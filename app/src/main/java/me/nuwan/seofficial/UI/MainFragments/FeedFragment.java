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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import me.nuwan.seofficial.Adapters.FeedAdapter;
import me.nuwan.seofficial.Model.Feed;
import me.nuwan.seofficial.Fireabse.FirebaseDB;
import me.nuwan.seofficial.R;

public class FeedFragment extends Fragment {

    RecyclerView rv;
    public List<Feed> data;

    public FeedFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_feed, container, false);
        rv = v.findViewById(R.id.feedList);
        data = new ArrayList<>();

        return v;
    }

    @Override
    public void onStart() {
        DatabaseReference feedRef = FirebaseDB.getFeedReference();

        feedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                data.clear();
                fetchFeed(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        super.onStart();
    }

    private void fetchFeed(DataSnapshot dataSnapshot) {

        for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){

            if(singleSnapshot.hasChildren()){
                String event = singleSnapshot.child("title").getValue().toString();
                String desc = singleSnapshot.child("desc").getValue().toString();
                String by = singleSnapshot.child("by").getValue().toString();
                String type = singleSnapshot.child("type").getValue().toString();

                Feed feed = new Feed();
                feed.setFeedTitle(event);
                feed.setFeedDesc(desc);
                feed.setFeedAuthor(by);

                switch (type){
                    case "alert":
                        feed.setFeedImg(R.mipmap.alarm);
                        break;
                    case "note":
                        feed.setFeedImg(R.mipmap.open_book);
                        break;

                        default:
                            feed.setFeedImg(R.mipmap.se_logo);
                }
                data.add(feed);
            }

        }

        FeedAdapter adapter = new FeedAdapter(getActivity(), data);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

    }
}
