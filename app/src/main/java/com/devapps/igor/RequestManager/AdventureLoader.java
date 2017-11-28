package com.devapps.igor.RequestManager;

import android.os.AsyncTask;
import android.util.Log;

import com.devapps.igor.DataObject.Adventure;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by hercules on 26/11/17.
 */

public class AdventureLoader implements ValueEventListener {
    private boolean finished = false;
    private Adventure mAdventure;
    private ArrayList<Adventure> mAdventures;
    private ArrayList<AdventureLoaderListener> mAdventureLoaderListeners;
    private ArrayList<AllAdventuresLoaderListener> mAllAdventuresLoaderListeners;

    public AdventureLoader() {
        mAdventureLoaderListeners = new ArrayList<>();
        mAllAdventuresLoaderListeners = new ArrayList<>();
    }

    public void load(String adventureId) {
        DatabaseReference ref = Database.getAdventuresReference();
        ref.child(adventureId).addValueEventListener(this);

    }

    public void load() {
        Database.getAdventuresReference().addValueEventListener(this);
    }

    public interface AdventureLoaderListener {

        void onAdventureLoaded(Adventure a);
    }

    public interface AllAdventuresLoaderListener {

        void onAllAdventuresLoaded(ArrayList<Adventure> adventures);
    }

    public void setAdventureLoaderListener(AdventureLoaderListener a) {
        if (isFinished()) {
            a.onAdventureLoaded(mAdventure);
        }
        mAdventureLoaderListeners.add(a);
    }

    public void removeAdventureLoaderListener(AdventureLoaderListener a) {
        mAdventureLoaderListeners.remove(a);
    }

    public void setAllAdventuresLoaderListeners(AllAdventuresLoaderListener a) {
        if (isFinished()) {
            a.onAllAdventuresLoaded(mAdventures);
        }
        mAllAdventuresLoaderListeners.add(a);
    }

    public void removeAllAdventuresLoaderListener(AllAdventuresLoaderListener a) {
        mAllAdventuresLoaderListeners.remove(a);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        new LoadAdventureTask(dataSnapshot).execute();

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    public boolean isFinished() {
        return finished;
    }


    private void onAdventureLoaderFinished() {
        for (AdventureLoaderListener a : mAdventureLoaderListeners) {
            a.onAdventureLoaded(mAdventure);
        }
    }

    private void onAllAdventuresLoaderFinished() {
        for (AllAdventuresLoaderListener a : mAllAdventuresLoaderListeners) {
            a.onAllAdventuresLoaded(mAdventures);
        }
    }

    private class LoadAdventureTask extends AsyncTask<Void, Void, Void> {
        DataSnapshot mAdventureSnapshot;
        boolean mIsSingleAdventure = true;

        public LoadAdventureTask(DataSnapshot adventureSnapshot) {
            mAdventureSnapshot = adventureSnapshot;
            if (mAdventureSnapshot.getKey().equals(Database.getAdventuresReference())) {
                mIsSingleAdventure = false;
            }
            finished = false;
        }


        @Override
        protected Void doInBackground(Void... voids) {
            if (mIsSingleAdventure) {
                mAdventure = mAdventureSnapshot.getValue(Adventure.class);
                return null;
            } else {
                for (DataSnapshot child : mAdventureSnapshot.getChildren()) {
                    mAdventures.add(child.getValue(Adventure.class));

                }
                return null;
            }
        }

        @Override
        protected void onPostExecute(Void result) {
            if (mIsSingleAdventure) {
                onAdventureLoaderFinished();
            } else {
                onAllAdventuresLoaderFinished();
            }
            finished = true;
        }

    }

}
