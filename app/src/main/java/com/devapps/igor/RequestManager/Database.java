package com.devapps.igor.RequestManager;

import com.devapps.igor.Util.FirebaseReference;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by eduar on 16/05/2017.
 */

public class Database {

    public static DatabaseReference getUsersReference(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child(FirebaseReference.DATABASE_USER_REFERENCE).child(FirebaseReference.DATABASE_USERS_REFERENCE);
        databaseReference.keepSynced(true);
        return databaseReference;
    }

    public static DatabaseReference getAdventuresReference(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child(FirebaseReference.DATABASE_ADVENTURES_REFERENCE);
        databaseReference.keepSynced(true);
        return databaseReference;
    }
}
