package me.nuwan.seofficial.Fireabse;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class FirebaseDB {
    private static FirebaseDatabase mDatabase;
    private static DatabaseReference loginDatabaseReference;
    private static DatabaseReference userDatabaseReference;
    private static DatabaseReference feedDatabaseReference;


    public static FirebaseDatabase getDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }
        return mDatabase;
    }

    public static DatabaseReference getLoginReference(){
        if(loginDatabaseReference == null){
            loginDatabaseReference = getDatabase().getReference("login");
        }
        return loginDatabaseReference;
    }

    public static DatabaseReference getUserReference(){
        if(userDatabaseReference == null){
            userDatabaseReference = getDatabase().getReference("users");
        }
        return userDatabaseReference;
    }

    public static DatabaseReference getFeedReference() {
        if(feedDatabaseReference == null){
            feedDatabaseReference = getDatabase().getReference("feed");
        }
        return feedDatabaseReference;
    }
}
