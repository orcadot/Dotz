package com.orca.dot.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orca.dot.auth.LoginSignUpActivity;
import com.orca.dot.auth.ProfileDetailsActivity;
import com.orca.dot.model.UserDetails;
import com.orca.dot.services.StylesActivity;
import com.orca.dot.utils.Constants;

/**
 * A Welcome (launcher) activity containing a splash screen.
 * <p>This class also check whether any user is currently logged in or not.</p>
 */
public class WelcomeActivity extends AppCompatActivity {

    private static final String TAG = "WelcomeActivity";

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ValueEventListener mListener;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        // [START auth_state_listener]
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    onAuthSuccess(user);
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                } else {
                    Intent intent = new Intent(WelcomeActivity.this, LoginSignUpActivity.class);
                    startActivity(intent);
                    finish();
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
        // [END auth_state_listener]

        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void onAuthSuccess(FirebaseUser currentUser) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_LOCATION_USER_LISTS).child(currentUser.getUid());
        mDatabase.keepSynced(true);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    Intent intent = new Intent(WelcomeActivity.this, ProfileDetailsActivity.class);
                    intent.putExtra("FRAGMENT_ID", Constants.FRAGMENT_PROFILE_DETAILS);
                    startActivity(intent);
                    finish();
                } else {
                    UserDetails userDetails = dataSnapshot.getValue(UserDetails.class);
                    if (userDetails.isFCTFilled) {
                        Log.i(TAG, "onDataChange: " + userDetails.username);
                        Intent intent = new Intent(WelcomeActivity.this, StylesActivity.class);
                        intent.putExtra(Constants.USER_NAME_KEY, userDetails.username);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(WelcomeActivity.this, ProfileDetailsActivity.class);
                        intent.putExtra("FRAGMENT_ID", Constants.FRAGMENT_FACECUT);
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                Toast.makeText(WelcomeActivity.this, "Failed to load user details.",
                        Toast.LENGTH_SHORT).show();
            }
        };

        mDatabase.addValueEventListener(valueEventListener);

        mListener = valueEventListener;
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mListener != null) {
            mDatabase.removeEventListener(mListener);
        }

        mAuth.removeAuthStateListener(mAuthListener);
    }
}
