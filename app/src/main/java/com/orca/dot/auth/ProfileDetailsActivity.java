package com.orca.dot.auth;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orca.dot.R;
import com.orca.dot.auth.fragments.FacecutDetectFragment;
import com.orca.dot.auth.fragments.ProfileDetailsFragment;
import com.orca.dot.model.UserDetails;
import com.orca.dot.services.styles.StylesActivity;
import com.orca.dot.utils.Constants;

import java.util.HashMap;
import java.util.Map;

public class ProfileDetailsActivity extends AppCompatActivity implements ProfileDetailsFragment.OnProfileSaveClickListener, FacecutDetectFragment.OnFacecutDetectClickListener {

    private static final String TAG = "ProfileDetailsActivity";

    private Fragment fragment;
    private FirebaseUser currentUser;
    private ProgressDialog progressDialog;
    private UserDetails userDetails;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        fragment = ProfileDetailsFragment.newInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_fragment, fragment);
        fragmentTransaction.commit();
    }


    @Override
    public void onProfileSave(UserDetails userDetails) {
        hideSoftKeyBoard();
        this.userDetails = userDetails;
        fragment = FacecutDetectFragment.newInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.content_fragment, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onFacecutSelect(int facecut, String fct) {
        // hideSoftKeyBoard();
        if (currentUser != null) {

            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Saving your facecut");
            progressDialog.setCancelable(false);
            progressDialog.show();

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = database.getReference();

            sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.profile_prefs_file_key), Context.MODE_PRIVATE);
            userDetails.phone = sharedPref.getString(getString(R.string.phone_number), "");
            userDetails.facecut = fct;
            userDetails.facecut_type = facecut;

            databaseReference.child(Constants.FIREBASE_LOCATION_USER_LISTS + "/" + currentUser.getUid()).setValue(userDetails, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError == null) {
                        sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.profile_prefs_file_key), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putBoolean(getString(R.string.profile_filled), true);
                        editor.putString(getString(R.string.profile_user_name), userDetails.username);
                        editor.apply();

                        Intent intent = new Intent(ProfileDetailsActivity.this, StylesActivity.class);
                        intent.putExtra(Constants.USER_NAME_KEY, userDetails.username);
                        startActivity(intent);
                        progressDialog.dismiss();
                        finish();

                    } else
                        Snackbar.make(fragment.getView(), databaseError.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            });
        }

    }

    private void hideSoftKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm.isAcceptingText()) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

}
