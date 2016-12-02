package com.orca.dot.services.cart;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.orca.dot.R;
import com.orca.dot.model.BaseModel;
import com.orca.dot.model.Style;
import com.orca.dot.ui.BaseActivity;
import com.orca.dot.utils.AccountUtils;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends BaseActivity implements CartContract.View {

    private CartContract.Presenter mPresenter;
    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private GridLayoutManager layoutManager;

    private static final String TAG = "CartActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        setActivityTitle("My Cart");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        adapter = new CartAdapter(CartActivity.this);

        layoutManager = new GridLayoutManager(this, 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapter.getItemColumnSpan(position);
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.cart_recyclerview);
        recyclerView.setLayoutManager(layoutManager);


        recyclerView.setAdapter(adapter);
        new CartPresenter(AccountUtils.getActiveAccountName(getApplicationContext()), this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.stop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showCartItems(List<? extends BaseModel> data) {
        adapter.add(data);
    }

    @Override
    public void setPresenter(CartContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public void onFavOrAddClicked(final Style style) {
        mPresenter.favOrAddClicked(style);
    }
}
