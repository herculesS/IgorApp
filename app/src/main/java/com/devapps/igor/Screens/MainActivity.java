package com.devapps.igor.Screens;

import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.devapps.igor.R;
import com.devapps.igor.Screens.CriarNovaAventura.CriarNovaAventuraFragment;
import com.devapps.igor.Screens.NavigationDrawer.CustomDrawerAdapter;
import com.devapps.igor.Screens.NavigationDrawer.DrawerItem;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    CustomDrawerAdapter drawerAdapter;

    private String[] mMenuItens;

    List<DrawerItem> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();

        dataList = new ArrayList<DrawerItem>();
        mTitle = mDrawerTitle = getTitle();
        mMenuItens = getResources().getStringArray(R.array.menu_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        dataList.add(new DrawerItem(mMenuItens[0], R.drawable.profile_icon));
        dataList.add(new DrawerItem(mMenuItens[1], R.drawable.profile_icon));
        dataList.add(new DrawerItem(mMenuItens[2], R.drawable.profile_icon));
        dataList.add(new DrawerItem(mMenuItens[3], R.drawable.profile_icon));
        dataList.add(new DrawerItem(mMenuItens[4], R.drawable.profile_icon));

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
        Fragment newFragment;
        switch (position) {
            case 0:
                newFragment = new CriarNovaAventuraFragment();
                break;
            case 1:
                newFragment = new CriarNovaAventuraFragment();
                break;
            default:
                newFragment = new CriarNovaAventuraFragment();
                break;
        }

        fragmentManager.beginTransaction().replace(R.id.fragment_container, newFragment).commit();

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

}
