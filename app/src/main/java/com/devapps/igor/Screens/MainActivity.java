package com.devapps.igor.Screens;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;

import com.devapps.igor.DataObject.Profile;
import com.devapps.igor.R;
import com.devapps.igor.RequestManager.Database;
import com.devapps.igor.Screens.AdventureProgress.AdventureProgressFragment;
import com.devapps.igor.Screens.Books.BooksFragment;
import com.devapps.igor.Screens.DiceRoller.NewRollDialogFragment;
import com.devapps.igor.Screens.CreateAdventure.CreateAdventureFragment;
import com.devapps.igor.Screens.HomeJogosFrontend.FragmentAdventure;
import com.devapps.igor.Screens.DiceRoller.DiceRollerFragment;
import com.devapps.igor.Screens.Login.LoginActivity;
import com.devapps.igor.Screens.NavigationDrawer.CustomDrawerAdapter;
import com.devapps.igor.Screens.NavigationDrawer.DrawerItem;
import com.devapps.igor.Screens.Notifications.NotificationsFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements DiceRollerFragment.OnListFragmentInteractionListener,
        NewRollDialogFragment.NewRollDialogListener {

    private static final String TAG = "MainActivity";
    private Profile mUserProfile;

    private FragmentManager fragmentManager;

    private ImageView _navAppBarButton;

    private DrawerLayout mDrawerLayout;
    private CustomDrawerAdapter drawerAdapter;
    private ListView mDrawerList;
    private LinearLayout mLinearLayout;
    List<DrawerItem> dataList;

    private enum menuItens {ADVENTURES, BOOKS, PROFILE, DICE, NOTIFICATION, SETTINGS, LOGOUT}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        mUserProfile = (Profile) intent.getSerializableExtra("userProfile");

        DatabaseReference ref = Database.getUsersReference();
        ref.child(mUserProfile.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUserProfile = dataSnapshot.getValue(Profile.class);
                updateNotifications();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        //myToolbar.setNavigationIcon(R.drawable.buttom_drawer_menu);
        //myToolbar.setLogo(R.drawable.appbar_logo);
        setSupportActionBar(myToolbar);

        fragmentManager = getSupportFragmentManager();

        dataList = new ArrayList<DrawerItem>();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        String[] mMenuItens = getResources().getStringArray(R.array.menu_array);
        dataList.add(new DrawerItem(mMenuItens[0], R.drawable.adventure_icon));
        dataList.add(new DrawerItem(mMenuItens[1], R.drawable.books_icon));
        dataList.add(new DrawerItem(mMenuItens[2], R.drawable.profile_icon));
        dataList.add(new DrawerItem(mMenuItens[3], R.drawable.profile_icon));
        dataList.add(new DrawerItem(mMenuItens[4], R.drawable.notification_icon));
        dataList.add(new DrawerItem(mMenuItens[5], R.drawable.settings_icon));
        dataList.add(new DrawerItem(mMenuItens[6], R.drawable.logout_icon));

        drawerAdapter = new CustomDrawerAdapter(this, R.layout.custom_drawer_item, dataList);
        mDrawerList.setAdapter(drawerAdapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        if (savedInstanceState == null) {
            selectItem(0);
        }

        _navAppBarButton = (ImageView) findViewById(R.id.nav_image_button);

        _navAppBarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        mLinearLayout = (LinearLayout) findViewById(R.id.notification_box);
        mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = NotificationsFragment.newInstance(mUserProfile.getId());
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment).commit();

            }
        });

    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        // update the main content by replacing fragments
        // TODO link with the others Fragments
        Fragment newFragment;
        switch (position) {
            case 0:
                newFragment = new FragmentAdventure();
                break;
            case 1:
                newFragment = new BooksFragment();
                break;
            case 2:
                newFragment = AdventureProgressFragment.newInstance("-KvV1jHurj0sGVPXP9GO");
                break;
            case 3:
                newFragment = DiceRollerFragment.newInstance();
                break;
            case 6:
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                return;
            default:
                newFragment = new CreateAdventureFragment();
                break;
        }

        fragmentManager.beginTransaction().replace(R.id.fragment_container, newFragment, newFragment.getClass().getName()).commit();

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void openMenu(View view) {
        Context wrapper = new ContextThemeWrapper(this, R.style.MyPopupMenu);
        PopupMenu popupMenu = new PopupMenu(wrapper, view);
        // This activity implements OnMenuItemClickListener
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_edit:
                        Log.d(TAG, "Botão edit.");
                        //TODO action
                        return true;
                    case R.id.action_order:
                        Log.d(TAG, "Botão order.");
                        //TODO action
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.inflate(R.menu.actionbar_menu);
        popupMenu.show();
    }

    @Override
    public Profile getUserProfile() {
        return mUserProfile;
    }

    @Override
    public void onDialogPositiveClick(int diceType, int diceNumber, int diceModifier) {
        DiceRollerFragment diceRollerFragment = (DiceRollerFragment) getSupportFragmentManager().findFragmentByTag(DiceRollerFragment.class.getName());
        diceRollerFragment.createDiceRoll(diceType, diceNumber, diceModifier);
    }

    public void updateNotifications() {
        if (mUserProfile.getNotifications() == null) {
            mLinearLayout.setVisibility(View.GONE);
        } else {
            mLinearLayout.setVisibility(View.VISIBLE);
        }
    }
}
