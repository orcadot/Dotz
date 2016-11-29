package com.orca.dot.welcome;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.orca.dot.R;
import com.orca.dot.auth.LoginSignUpActivity;
import com.orca.dot.auth.ProfileDetailsActivity;
import com.orca.dot.model.UserDetails;
import com.orca.dot.services.styles.StylesActivity;
import com.orca.dot.utils.Constants;

/**
 * A Welcome (launcher) activity containing a splash screen.
 * <p>This class also check whether any user is currently logged in or not.</p>
 */
public class WelcomeActivity extends AppCompatActivity {

    private static final String TAG = "WelcomeActivity";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                SharedPreferences userSharedPref = getApplicationContext().getSharedPreferences(getString(R.string.profile_prefs_file_key), Context.MODE_PRIVATE);

                if (user != null && userSharedPref.getBoolean(getString(R.string.profile_filled), false)) {
                    onAuthSuccess(userSharedPref.getString(getString(R.string.profile_user_name), ""));
                }
                else if (user !=null && !userSharedPref.getBoolean(getString(R.string.profile_filled), false)) {
                    Intent intent = new Intent(WelcomeActivity.this, ProfileDetailsActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(WelcomeActivity.this, LoginSignUpActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void onAuthSuccess(String username) {
        Intent intent = new Intent(WelcomeActivity.this, StylesActivity.class);
        intent.putExtra(Constants.USER_NAME_KEY, username);
        startActivity(intent);
        finish();

    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }
}
