package com.devapps.igor.Screens;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.devapps.igor.R;
import com.devapps.igor.Screens.AdventureProgress.AdventureProgressFragment;
import com.devapps.igor.Screens.CreateNewAdventure.CreateNewAdventureFragment;
import com.devapps.igor.Screens.Login.LoginActivity;
import com.devapps.igor.Screens.NavigationDrawer.CustomDrawerAdapter;
import com.devapps.igor.Screens.NavigationDrawer.DrawerItem;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;


public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;

    private DrawerLayout mDrawerLayout;
    CustomDrawerAdapter drawerAdapter;
    private ListView mDrawerList;

    List<DrawerItem> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        fragmentManager = getSupportFragmentManager();

        dataList = new ArrayList<DrawerItem>();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        //mDrawerList.addHeaderView(getLayoutInflater().inflate(R.layout.drawer_header, null));

        String[] mMenuItens = getResources().getStringArray(R.array.menu_array);
        //dataList.add(new DrawerItem("", 0));
        dataList.add(new DrawerItem(mMenuItens[0], R.drawable.adventure_icon));
        dataList.add(new DrawerItem(mMenuItens[1], R.drawable.books_icon));
        dataList.add(new DrawerItem(mMenuItens[2], R.drawable.profile_icon));
        dataList.add(new DrawerItem(mMenuItens[3], R.drawable.notification_icon));
        dataList.add(new DrawerItem(mMenuItens[4], R.drawable.settings_icon));
        dataList.add(new DrawerItem(mMenuItens[5], R.drawable.logout_icon));

        drawerAdapter = new CustomDrawerAdapter(this, R.layout.custom_drawer_item, dataList);
        mDrawerList.setAdapter(drawerAdapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        if (savedInstanceState == null) {
            selectItem(0);
        }
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
                newFragment = AdventureProgressFragment.newInstance("-KvV1jHurj0sGVPXP9GO");
                break;
            case 1:
                newFragment = new CreateNewAdventureFragment();
                break;
            case 5:
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                return;
            default:
                newFragment = new CreateNewAdventureFragment();
                break;
        }

        fragmentManager.beginTransaction().replace(R.id.fragment_container, newFragment).commit();

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

}
