package com.orca.dot.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.orca.dot.auth.LoginSignUpActivity;
import com.orca.dot.auth.ProfileDetailsActivity;
import com.orca.dot.services.styles.StylesActivity;
import com.orca.dot.utils.AccountUtils;
import com.orca.dot.utils.Constants;

/**
 * A Welcome (launcher) activity containing a splash screen.
 * <p>This class also check whether any user is currently logged in or not.</p>
 */
public class WelcomeActivity extends AppCompatActivity {

    private static final String TAG = "WelcomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        if (AccountUtils.hasActiveAccount(getApplicationContext()) && AccountUtils.getProfileFilled(getApplicationContext())) {
            onAuthSuccess(AccountUtils.getName(getApplicationContext()));
        }

        else if(AccountUtils.hasActiveAccount(getApplicationContext()) && !AccountUtils.getProfileFilled(getApplicationContext())){
            Intent intent = new Intent(WelcomeActivity.this, ProfileDetailsActivity.class);
            startActivity(intent);
            finish();
        }
        else if(!AccountUtils.hasActiveAccount(getApplicationContext())){
            Intent intent = new Intent(WelcomeActivity.this, LoginSignUpActivity.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
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
    }
}
