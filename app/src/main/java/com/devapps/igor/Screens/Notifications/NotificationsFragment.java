package com.devapps.igor.Screens.Notifications;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devapps.igor.DataObject.Profile;
import com.devapps.igor.R;
import com.devapps.igor.Screens.MainActivity;


public class NotificationsFragment extends Fragment {
    private static final String PLAYER_ID = "PLAYER_ID";

    private String mPlayerId;
    private RecyclerView mNotificationsRecyclerView;
    private FragmentActivity mActivity;


    public NotificationsFragment() {
        // Required empty public constructor
    }

    public static NotificationsFragment newInstance(String playerId) {
        NotificationsFragment fragment = new NotificationsFragment();
        Bundle args = new Bundle();
        args.putString(PLAYER_ID, playerId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPlayerId = getArguments().getString(PLAYER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mNotificationsRecyclerView = (RecyclerView) view.findViewById(R.id.notification_recycler_view);
        mNotificationsRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));

        MainActivity activity = (MainActivity) mActivity;
        Profile user = activity.getUserProfile();
        mNotificationsRecyclerView.setAdapter(
                new NotificationsAdapter(user.getNotifications(), mActivity, mPlayerId));

    }


}
