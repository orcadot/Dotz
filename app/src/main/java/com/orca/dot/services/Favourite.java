package com.orca.dot.services;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.orca.dot.R;
import com.orca.dot.model.HairStyle;
import com.orca.dot.services.adapters.FavoritesAdapter;
import com.orca.dot.utils.Constants;

import java.util.ArrayList;

public class Favourite extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.fav_recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setItemViewCacheSize(4);

        final ArrayList<HairStyle> arrayList = new ArrayList<>();
        final FavoritesAdapter adapter = new FavoritesAdapter(this, arrayList);
        recyclerView.setAdapter(adapter);


        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference(Constants.FIREBASE_LOCATION_USER_LISTS)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(Constants.FIREBASE_LOCATION_USER_FAVORITE);
        Query query = databaseReference;
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // Log.d("favourite", dataSnapshot.getValue().toString());
                DatabaseReference databaseReference1 = firebaseDatabase.getReference(Constants.FIREBASE_STYLE_DATA_NODE);
                Query query1 = databaseReference1.orderByKey().equalTo(dataSnapshot.getValue().toString());
                query1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot1) {
                        if (dataSnapshot1.exists()) {
                            //Log.d("favData", dataSnapshot1.getChildren().iterator().next().getValue().toString());
                            HairStyle hairStyle = dataSnapshot1.getChildren().iterator().next().getValue(HairStyle.class);
                            adapter.add(hairStyle);

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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

}
