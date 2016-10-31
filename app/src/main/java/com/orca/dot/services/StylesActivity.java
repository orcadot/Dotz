package com.orca.dot.services;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orca.dot.R;
import com.orca.dot.model.KeyValue;
import com.orca.dot.services.fragments.StylesFragment;
import com.orca.dot.services.interfaces.ChangeViewListener;
import com.orca.dot.services.presenter.StylesDataPresenter;
import com.orca.dot.services.presenter.TabDataPresenter;
import com.orca.dot.utils.Constants;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * A StylesActivity contains home screen for showing services and products.
 *
 * @author Mayank will work on it.
 */
public class StylesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, StylesFragment.Listener, TabDataContracts.View {

    public static RecyclerView.RecycledViewPool recycledViewPool;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private String userName;
    private ChangeViewListener changeViewListener;

    private static final String TAG = "StylesActivity";


    private ViewPagerAdapter adapter;
    private DrawerLayout drawer;

    private StylesDataPresenter mStylesDataPresenter;
    private TabDataPresenter mTabPresenter;
    private TabDataContracts.Presenter mTabDataPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent() == null) {
            finish();
        } else
            userName = getIntent().getStringExtra(Constants.USER_NAME_KEY);

        setContentView(R.layout.activity_home);

        initializeUI();

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(2);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabPresenter = new TabDataPresenter(FirebaseDatabase.getInstance().getReference().child("services_keys"), this);
        mTabDataPresenter.start();

    }

    private void initializeUI() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_left);
        navigationView.setNavigationItemSelectedListener(this);

        TextView userNameTextView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.username);
        userNameTextView.setText("Hello! " + userName);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

    }

    private void initRecycledPool() {
    }

    private void setupViewPager(ViewPager viewPager, List<KeyValue> tabData) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), tabData);
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else
            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_layout:
                Fragment fragment = adapter.getFragment(tabLayout.getSelectedTabPosition());
                if (fragment != null)
                    ((StylesFragment) fragment).setLayoutManagerNow();
                break;
            case R.id.action_cart:
                startActivity(new Intent(StylesActivity.this, Cart.class));
                break;
            case R.id.action_fav:
                startActivity(new Intent(StylesActivity.this, Favourite.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_camera) {
            Toast.makeText(this, "Camera clicked", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_gallery) {
            Toast.makeText(this, "Camera clicked", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    @Override
    public void onFragmentViewCreated(StylesFragment stylesFragment, DatabaseReference dataRef) {
        Log.d(TAG, "onFragmentViewCreated() called with: stylesFragment = [" + stylesFragment + "], dataRef = [" + dataRef + "]");
        mStylesDataPresenter = new StylesDataPresenter(dataRef, stylesFragment);
    }

    @Override
    public void onFragmentAttached(StylesFragment stylesFragment) {

    }

    @Override
    public void onFragmentDetached(StylesFragment stylesFragment) {

    }

    @Override
    public void setPresenter(TabDataContracts.Presenter presenter) {
        mTabDataPresenter = presenter;
    }

    @Override
    public void populateTabData(List<KeyValue> tabData) {
        setupViewPager(viewPager, tabData);
        tabLayout.setupWithViewPager(viewPager);
    }


    static class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final SparseArray<WeakReference<Fragment>> instantiatedFragments = new SparseArray<>();
        private List<KeyValue> tabsData = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm, List<KeyValue> tabData) {
            super(fm);
            this.tabsData = tabData;
        }

        @Override
        public Fragment getItem(int position) {
            return StylesFragment.newInstance(tabsData.get(position).key);
        }

        @Override
        public int getCount() {
            return tabsData.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabsData.get(position).value;
        }

        @Override
        public Object instantiateItem(final ViewGroup container, final int position) {
            final Fragment fragment = (Fragment) super.instantiateItem(container, position);
            instantiatedFragments.put(position, new WeakReference<>(fragment));
            return fragment;
        }

        @Override
        public void destroyItem(final ViewGroup container, final int position, final Object object) {
            instantiatedFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        @Nullable
        public Fragment getFragment(final int position) {
            final WeakReference<Fragment> wr = instantiatedFragments.get(position);
            if (wr != null) {
                return wr.get();
            } else {
                return null;
            }
        }
    }
}

