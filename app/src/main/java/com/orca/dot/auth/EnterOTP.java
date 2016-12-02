package com.orca.dot.auth;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orca.dot.R;
import com.orca.dot.auth.callbacks.OTPVerificationCallback;
import com.orca.dot.auth.internal.OTPVerification;
import com.orca.dot.model.User;
import com.orca.dot.services.styles.StylesActivity;
import com.orca.dot.utils.AccountUtils;
import com.orca.dot.utils.Constants;
import com.orca.dot.utils.validate.OTPValidator;
import com.orca.dot.welcome.WelcomeActivity;

public class EnterOTP extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "EnterOTP";
    IntentFilter intentFilter;

    private EditText otpField;
    private Button verifyBtn;
    private TextView countDownTimer;
    private ProgressBar waitingSpinner;
    private TextWatcher mNumberTextWatcher;
    private ProgressDialog progressDialog;

    private String sessionID;
    private String phone;

    private boolean isVerifying = false;

    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;

    private BroadcastReceiver intentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!isVerifying) {
                isVerifying = true;
                waitingSpinner.setVisibility(View.GONE);
                countDownTimer.setVisibility(View.GONE);
                String receivedOTP = intent.getExtras().getString("otp");
                otpField.setText(receivedOTP);
                if (new OTPValidator().validate(receivedOTP)) {
                    verifyOTP(receivedOTP);
                } else {
                    isVerifying = false;
                    Toast.makeText(context, "Check your OTP", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
    private DatabaseReference mDatabase;
    private ValueEventListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent() != null) {
            sessionID = getIntent().getStringExtra("SESSION_ID");
            phone = getIntent().getStringExtra("PHONE_KEY");
        } else
            finish();
        setContentView(R.layout.activity_enter_otp);

        intentFilter = new IntentFilter();
        intentFilter.addAction("DOT_OTP_RECEIVED");

        otpField = (EditText) findViewById(R.id.otp_edit_text);
        resetNumberTextWatcher();

        waitingSpinner = (ProgressBar) findViewById(R.id.waiting_spinner);
        if (waitingSpinner != null) {
            waitingSpinner.setIndeterminate(true);
            waitingSpinner.setVisibility(View.VISIBLE);
        }

        verifyBtn = (Button) findViewById(R.id.verify_button);
        assert verifyBtn != null;
        verifyBtn.setOnClickListener(this);
        countDownTimer = (TextView) findViewById(R.id.countdown_timer);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    AccountUtils.setActiveAccount(getApplicationContext(), user.getUid());
                    updateUI(user);
                }
            }
        };
    }

    private void updateUI(FirebaseUser user) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_LOCATION_USER_LISTS).child(user.getUid());
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (progressDialog != null)
                    progressDialog.dismiss();

                if (dataSnapshot.getValue() == null) {
                    Intent intent = new Intent(EnterOTP.this, ProfileDetailsActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    User user = dataSnapshot.getValue(User.class);

                    AccountUtils.setName(getApplicationContext(), user.username);
                    AccountUtils.setProfileFilled(getApplicationContext(), true);

                    Intent intent = new Intent(EnterOTP.this, StylesActivity.class);
                    intent.putExtra(Constants.USER_NAME_KEY, user.username);
                    startActivity(intent);
                    finish();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(EnterOTP.this, "Failed to load user details.",
                        Toast.LENGTH_SHORT).show();

                if (progressDialog != null)
                    progressDialog.dismiss();

                Intent intent = new Intent(EnterOTP.this, WelcomeActivity.class);
                startActivity(intent);
            }
        };

        mDatabase.addValueEventListener(valueEventListener);
        mListener = valueEventListener;

    }

    public void verifyOTP(String receivedOTP) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Verifying...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        OTPVerification.verify(this, receivedOTP, phone, sessionID, new OTPVerificationCallback() {
            @Override
            public void otpVerificationFailure(String error) {
                isVerifying = false;
                Snackbar.make(verifyBtn, error, Snackbar.LENGTH_LONG).show();
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void otpVerificationSuccess(String token) {
                AccountUtils.setPhone(getApplicationContext(), phone);
                createFirebaseUser(token);

                progressDialog.setMessage("Setting your account");
                isVerifying = false;
            }
        });
    }

    private void createFirebaseUser(String token) {
        Log.d(TAG, "createFirebaseUser() called with: token = [" + token + "]");
        mAuth.signInWithCustomToken(token).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Log.d(TAG, "onComplete() called with: task = [" + task + "]");
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            Intent intent = new Intent(EnterOTP.this, LoginSignUpActivity.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(EnterOTP.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.verify_button:
                if (!isVerifying) {
                    isVerifying = true;
                    waitingSpinner.setVisibility(View.GONE);
                    countDownTimer.setVisibility(View.GONE);
                    verifyOTP(otpField.getText().toString().trim());
                }
                break;

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onResume() {
        registerReceiver(intentReceiver, intentFilter);
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(intentReceiver);
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        if (mListener != null) {
            mDatabase.removeEventListener(mListener);
        }
    }


    private void resetNumberTextWatcher() {

        if (mNumberTextWatcher != null) {
            otpField.removeTextChangedListener(mNumberTextWatcher);
        }

        mNumberTextWatcher = new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (new OTPValidator().validate(otpField.getText().toString().trim())) {
                    setButtonsEnabled(true);
                } else
                    setButtonsEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }


        };

        otpField.addTextChangedListener(mNumberTextWatcher);
    }


    private void setButtonsEnabled(boolean enabled) {
        if (enabled) {
            verifyBtn.setTextColor(ContextCompat.getColor(this, R.color.text_primary_light));
        } else {
            verifyBtn.setTextColor(ContextCompat.getColor(this, R.color.text_secondary_light));
        }
        verifyBtn.setEnabled(enabled);
        verifyBtn.setClickable(enabled);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(EnterOTP.this, LoginSignUpActivity.class));
        finish();
        super.onBackPressed();
    }
}
