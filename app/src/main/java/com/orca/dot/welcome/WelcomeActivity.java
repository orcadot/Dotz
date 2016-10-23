package com.orca.dot.welcome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.orca.dot.R;
import com.orca.dot.auth.LoginSignUpActivity;
import com.orca.dot.auth.ProfileDetailsActivity;
import com.orca.dot.services.HomeActivity;

/**
 * A Welcome (launcher) activity containing a splash screen.
 * <p>This class also check whether any user is currently logged in or not.</p>
 */
public class WelcomeActivity extends AppCompatActivity {

    private static final String TAG = "WelcomeActivity";
    private boolean userLoggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            // Activity was brought to front and not created,
            // Thus finishing this will get us to the last viewed activity
            finish();
            return;
        }
        splashDone();
    }

    private void splashDone() {
        if (isUserLoggedIn() && isProfileFilled()) {
            Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else if (isUserLoggedIn() && !isProfileFilled()) {
            Intent intent = new Intent(WelcomeActivity.this, ProfileDetailsActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(WelcomeActivity.this, LoginSignUpActivity.class);
            startActivity(intent);
            finish();
        }

    }

    private boolean isUserLoggedIn() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return true;
        } else
            return false;
    }

    private boolean isProfileFilled() {
        SharedPreferences profilePrefs = this.getSharedPreferences(getString(R.string.profile_prefs_file_key), MODE_PRIVATE);
        return profilePrefs.getBoolean(getString(R.string.profile_filled), false);
    }
}
