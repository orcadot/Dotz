package com.orca.dot.services.styles;

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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orca.dot.BasePresenter;
import com.orca.dot.R;
import com.orca.dot.model.Style;
import com.orca.dot.model.StyleCategory;
import com.orca.dot.services.cart.CartActivity;
import com.orca.dot.services.favorites.FavoritesActivity;
import com.orca.dot.ui.BaseActivity;
import com.orca.dot.utils.Constants;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A StylesActivity contains home screen for showing services and products.
 */
public class StylesActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, TabDataContract.View {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private String userName;
    private ViewPagerAdapter adapter;
    private DrawerLayout drawer;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    private TabDataContract.Presenter mTabDataPresenter;
    private static final String TAG = "StylesActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent() == null) {
            finish();
        } else
            userName = getIntent().getStringExtra(Constants.USER_NAME_KEY);

        setContentView(R.layout.activity_home);
        initializeUI();

    }

    private void initializeUI() {

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        assert viewPager != null;
        viewPager.setOffscreenPageLimit(2);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        new TabDataPresenter(this);
        mTabDataPresenter.start();
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_left);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);

        TextView userNameTextView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.username);
        userNameTextView.setText("Hello! " + userName);

        Toolbar toolbar = getToolbar();
        assert toolbar != null;
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

    }


    private void setupViewPager(ViewPager viewPager, List<StyleCategory> tabData) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), tabData);
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
                startActivity(new Intent(StylesActivity.this, CartActivity.class));
                break;
            case R.id.action_fav:
                startActivity(new Intent(StylesActivity.this, FavoritesActivity.class));
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
        }
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }


    @Override
    public void setPresenter(TabDataContract.Presenter presenter) {
            mTabDataPresenter = presenter;
    }

    @Override
    public void populateTabData(List<StyleCategory> tabData) {
        setupViewPager(viewPager, tabData);
        tabLayout.setupWithViewPager(viewPager);
    }


    static class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final SparseArray<WeakReference<Fragment>> instantiatedFragments = new SparseArray<>();
        private List<StyleCategory> tabsData = new ArrayList<>();

        ViewPagerAdapter(FragmentManager fm, List<StyleCategory> tabData) {
            super(fm);
            this.tabsData = tabData;
        }

        @Override
        public Fragment getItem(int position) {
            return StylesFragment.newInstance(tabsData.get(position).category_id);
        }

        @Override
        public int getCount() {
            return tabsData.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabsData.get(position).category_name;
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
        Fragment getFragment(final int position) {
            final WeakReference<Fragment> wr = instantiatedFragments.get(position);
            if (wr != null) {
                return wr.get();
            } else {
                return null;
            }
        }
    }
}

