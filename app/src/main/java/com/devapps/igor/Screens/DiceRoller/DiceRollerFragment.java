package com.devapps.igor.Screens.DiceRoller;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.devapps.igor.DataObject.Profile;
import com.devapps.igor.R;
import com.devapps.igor.RequestManager.Database;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class DiceRollerFragment extends Fragment {

    private OnListFragmentInteractionListener mListener;

    private Profile mUserProfile;

    private Button _newRollButton;
    private RecyclerView diceRollsView;

    private List<DiceRoll> diceRolls;
    private DiceRollRecyclerViewAdapter mDiceRollAdapter;

    // Firebase instance variables
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDiceRollDatabaseReference;
    private ChildEventListener mChildEventListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DiceRollerFragment() {
    }

    @SuppressWarnings("unused")
    public static DiceRollerFragment newInstance() {
        DiceRollerFragment fragment = new DiceRollerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dice_roller_list, container, false);

        mUserProfile = mListener.getUserProfile();

        // Initialize Firebase components
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDiceRollDatabaseReference = Database.getDicesReference();

        // Initialize layout elements
        _newRollButton = (Button) view.findViewById(R.id.new_roll_button);

        // Initialize message ListView and its adapter
        diceRolls = new ArrayList<>();
        mDiceRollAdapter = new DiceRollRecyclerViewAdapter(diceRolls, mListener, mUserProfile.getName());

        // Set the adapter
        Context context = view.getContext();
        diceRollsView = (RecyclerView) view.findViewById(R.id.dice_roll_list);
        diceRollsView.setLayoutManager(new LinearLayoutManager(context));
        diceRollsView.setAdapter(mDiceRollAdapter);

        // New Roll button make a new roll of dices
        _newRollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment rollSetup = new NewRollDialogFragment();
                rollSetup.show(getFragmentManager(), "new_roll");
            }
        });

        attachDatabaseReadListener();

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        Profile getUserProfile();
    }

    @Override
    public void onPause() {
        super.onPause();
        //mDiceRollAdapter.clear;
        detachDatabaseReadListener();
    }

    private void attachDatabaseReadListener() {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    DiceRoll newRoll = dataSnapshot.getValue(DiceRoll.class);
                    diceRolls.add(newRoll);
                    int index = diceRolls.indexOf(newRoll);
                    //TODO testar com size()
                    mDiceRollAdapter.notifyItemInserted(index);
                    diceRollsView.scrollToPosition(diceRolls.size()-1);
                }

                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                public void onChildRemoved(DataSnapshot dataSnapshot) {}
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                public void onCancelled(DatabaseError databaseError) {}
            };
            mDiceRollDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }

    private void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            mDiceRollDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }

    public void createDiceRoll(int diceType, int diceNumber, int diceModifier) {
        DiceRoll newDiceRoll = new DiceRoll(mUserProfile.getName(), diceType, diceNumber, diceModifier);
        mDiceRollDatabaseReference.push().setValue(newDiceRoll);
    }
}
