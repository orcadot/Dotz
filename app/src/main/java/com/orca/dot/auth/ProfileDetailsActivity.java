package com.orca.dot.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orca.dot.R;
import com.orca.dot.auth.fragments.FacecutDetectFragment;
import com.orca.dot.auth.fragments.ProfileDetailsFragment;
import com.orca.dot.model.User;
import com.orca.dot.services.styles.StylesActivity;
import com.orca.dot.utils.AccountUtils;
import com.orca.dot.utils.Constants;

public class ProfileDetailsActivity extends AppCompatActivity implements ProfileDetailsFragment.OnProfileSaveClickListener, FacecutDetectFragment.OnFacecutDetectClickListener {

    private static final String TAG = "ProfileDetailsActivity";

    private Fragment fragment;
    private ProgressDialog progressDialog;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);

        fragment = ProfileDetailsFragment.newInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_fragment, fragment);
        fragmentTransaction.commit();
    }


    @Override
    public void onProfileSave(User user) {
        hideSoftKeyBoard();
        this.user = user;
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
        if (AccountUtils.hasActiveAccount(getApplicationContext())) {

            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Saving your facecut");
            progressDialog.setCancelable(false);
            progressDialog.show();

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                    .child(Constants.FIREBASE_LOCATION_USER_LISTS + "/" + AccountUtils.getActiveAccountName(getApplicationContext()));

            user.phone = AccountUtils.getPhone(getApplicationContext());
            user.facecut = fct;
            user.facecut_type = facecut;

            databaseReference.setValue(user, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError == null) {
                        AccountUtils.setName(getApplicationContext(), user.username);
                        AccountUtils.setProfileFilled(getApplicationContext(), true);

                        Intent intent = new Intent(ProfileDetailsActivity.this, StylesActivity.class);
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
