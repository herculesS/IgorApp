package com.devapps.igor.Screens.CreateNewSession;

import android.app.DatePickerDialog;

import java.util.Calendar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.devapps.igor.DataObject.Adventure;
import com.devapps.igor.DataObject.Session;
import com.devapps.igor.R;
import com.devapps.igor.RequestManager.Database;
import com.devapps.igor.Screens.AdventureProgress.AdventureProgressFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class CreateNewSessionFragment extends Fragment {
    private static final String ADVENTURE_ID = "ADVENTURE_ID";

    private String mAdventureId;
    private Adventure mAdventure;
    private Calendar mDate = Calendar.getInstance();
    private DatePickerDialog mDatePickerDialog;

    private ImageView mReadyButton;
    private ImageView mCloseButton;
    private EditText mTitleEditText;
    private ImageView mFinishButton;
    private Button mBtnDatePicker;


    public CreateNewSessionFragment() {
        // Required empty public constructor
    }

    public static CreateNewSessionFragment newInstance(String adventureId) {
        CreateNewSessionFragment fragment = new CreateNewSessionFragment();
        Bundle args = new Bundle();
        args.putString(ADVENTURE_ID, adventureId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAdventureId = getArguments().getString(ADVENTURE_ID);
            Log.d("AdventureId", mAdventureId);

        }

    }


    private void setListeners() {

        mReadyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = mTitleEditText.getText().toString().trim();
                String date = mDate.get(Calendar.DAY_OF_MONTH) + "/" +
                        mDate.get(Calendar.MONTH) + "/" + mDate.get(Calendar.YEAR);
                Session session;
                if (!title.equals("")) {
                    session = new Session(title, date);
                } else {
                    session = new Session();
                }

                mAdventure.addSession(session);


                DatabaseReference ref = Database.getAdventuresReference();
                ref.child(mAdventureId).setValue(mAdventure);

                Fragment fragment = AdventureProgressFragment.newInstance(mAdventureId);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment).commit();

            }
        });

        mFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = AdventureProgressFragment.newInstance(mAdventureId);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment).commit();

            }
        });
        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = AdventureProgressFragment.newInstance(mAdventureId);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment).commit();
            }
        });

        mBtnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate();
            }
        });

    }

    private void setDate() {
        Calendar newCalendar = mDate;
        if (mDatePickerDialog == null) {
            mDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    mDate.set(year, month, day);
                    mBtnDatePicker.setText(mDate.get(Calendar.DAY_OF_MONTH) + "/" + mDate.get(Calendar.MONTH));
                }
            }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        }
        mDatePickerDialog.show();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_new_session, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initializeMembers(view);
        DatabaseReference ref = Database.getAdventuresReference();
        ref.child(mAdventureId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mAdventure = dataSnapshot.getValue(Adventure.class);

                setListeners();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void initializeMembers(View view) {
        mReadyButton = (ImageView) view.findViewById(R.id.criar_session_botao_pronto);
        mCloseButton = (ImageView) view.findViewById(R.id.create_session_btn_close);
        mFinishButton = (ImageView) view.findViewById(R.id.create_session_btn_create);
        mTitleEditText = (EditText) view.findViewById(R.id.create_session_editText);
        mBtnDatePicker = (Button) view.findViewById(R.id.session_date_picker);
        mBtnDatePicker.setText(mDate.get(Calendar.DAY_OF_MONTH) + "/" + mDate.get(Calendar.MONTH));

    }
}
