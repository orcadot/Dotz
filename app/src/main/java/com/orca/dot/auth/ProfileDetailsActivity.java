package com.orca.dot.auth;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.orca.dot.services.StylesActivity;
import com.orca.dot.utils.Constants;

import java.util.HashMap;
import java.util.Map;

public class ProfileDetailsActivity extends AppCompatActivity implements ProfileDetailsFragment.OnProfileSaveClickListener, FacecutDetectFragment.OnFacecutDetectClickListener {

    private static final String TAG = "ProfileDetailsActivity";

    private Fragment fragment;
    private FirebaseUser currentUser;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile_details);
        if (getIntent().getIntExtra("FRAGMENT_ID", 0) == Constants.FRAGMENT_PROFILE_DETAILS)
            fragment = ProfileDetailsFragment.newInstance();
        else
            fragment = FacecutDetectFragment.newInstance();


        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_fragment, fragment);
        fragmentTransaction.commit();
        Log.d(TAG, "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");

    }


    @Override
    public void onProfileSave(UserDetails userDetails) {
        hideSoftKeyBoard();
        if (currentUser != null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Saving your details");
            progressDialog.setCancelable(false);
            progressDialog.show();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = database.getReference();
            databaseReference.child("Users").child(currentUser.getUid()).setValue(userDetails, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError == null) {
                        fragment = FacecutDetectFragment.newInstance();
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.replace(R.id.content_fragment, fragment);
                        fragmentTransaction.commit();
                        progressDialog.dismiss();
                    } else
                        Snackbar.make(fragment.getView(), databaseError.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public void onFacecutSelect(int facecut, String fct) {
        hideSoftKeyBoard();
        if (currentUser != null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Saving your facecut");
            progressDialog.setCancelable(false);
            progressDialog.show();
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = database.getReference();

            Map<String, Object> updates = new HashMap<>();
            updates.put("facecut", fct);
            updates.put("facecut_type", facecut);
            updates.put("isFCTFilled", true);

            databaseReference.child(Constants.FIREBASE_LOCATION_USER_LISTS + "/" + currentUser.getUid()).updateChildren(updates, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError == null) {
                        startActivity(new Intent(ProfileDetailsActivity.this, StylesActivity.class));
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
