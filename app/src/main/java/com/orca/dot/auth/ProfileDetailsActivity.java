package com.orca.dot.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orca.dot.R;
import com.orca.dot.model.UserDetails;
import com.orca.dot.services.HomeActivity;

public class ProfileDetailsActivity extends AppCompatActivity {

    private static final String TAG = "ProfileDetailsActivity";

    private EditText nameEditText;
    private EditText emailEditText;
    private RadioGroup genderRadioGroup;
    private Button profileSaveButton;
    private String currentUserId;
    private String mGender;
    private int mGenderCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);

        bindUI();

    }

    private void bindUI() {
        nameEditText = (EditText) findViewById(R.id.name_edit_text);
        emailEditText = (EditText) findViewById(R.id.email_edit_text);
        genderRadioGroup = (RadioGroup) findViewById(R.id.radio_group_gender);
        genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_btn_male:
                        Log.i(TAG, "onCheckedChanged: male");
                        break;
                    case R.id.radio_btn_female:
                        Log.i(TAG, "onCheckedChanged: female");
                        break;
                    default:
                }
            }
        });
        profileSaveButton = (Button) findViewById(R.id.profile_save_button);
        profileSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfileToFirebase();
            }
        });
    }

    private void saveProfileToFirebase() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = database.getReference();
            UserDetails userDetails = new UserDetails(nameEditText.getText().toString().trim(), emailEditText.getText().toString().trim(), mGender, mGenderCode);
            databaseReference.child("Users").child(currentUser.getUid()).setValue(userDetails, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    startActivity(new Intent(ProfileDetailsActivity.this, HomeActivity.class));
                    finish();
                }
            });
        }
    }
}
