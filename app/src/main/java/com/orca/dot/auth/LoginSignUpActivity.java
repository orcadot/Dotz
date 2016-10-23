package com.orca.dot.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.orca.dot.R;
import com.orca.dot.auth.callbacks.ServerCallback;
import com.orca.dot.auth.internal.DotAuth;

/**
 * LoginSignUpActivity for implementing login and signup functionality
 * by using firebase authentication
 */
public class LoginSignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText phoneNumberEditText;
    private TextWatcher mNumberTextWatcher;
    private Button mNextButton;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_sign_up);
        mNextButton = (Button) findViewById(R.id.next_button);
        assert mNextButton != null;
        mNextButton.setOnClickListener(this);

        phoneNumberEditText = (EditText) findViewById(R.id.phone_number_edit_text);
        setButtonsEnabled(false);
        resetNumberTextWatcher();

        InitializeAPI.init(getApplicationContext());

    }

    private void resetNumberTextWatcher() {

        if (mNumberTextWatcher != null) {
            phoneNumberEditText.removeTextChangedListener(mNumberTextWatcher);
        }

        mNumberTextWatcher = new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isValidNumber(phoneNumberEditText.getText().toString().trim())) {
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

        phoneNumberEditText.addTextChangedListener(mNumberTextWatcher);
    }

    private boolean isValidNumber(String phoneNum) {
        String regEx = "^[0-9]{10}$";
        return phoneNum.matches(regEx);
    }

    private void setButtonsEnabled(boolean enabled) {
        if (enabled) {
            mNextButton.setTextColor(ContextCompat.getColor(this, R.color.text_primary_light));
        } else {
            mNextButton.setTextColor(ContextCompat.getColor(this, R.color.text_secondary_light));
        }
        mNextButton.setEnabled(enabled);
        mNextButton.setClickable(enabled);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_button:
                authenticateNumber();
                break;
            default:
                break;
        }
    }

    private void authenticateNumber() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Connecting...");
        progressDialog.show();

        DotAuth.authenticate(this, phoneNumberEditText.getText().toString().trim(), new ServerCallback() {
            @Override
            public void otpSendSuccessfully(String phoneNumber, String details) {
                Intent intent = new Intent(LoginSignUpActivity.this, EnterOTP.class);
                intent.putExtra("SESSION_ID", details);
                intent.putExtra("PHONE_KEY", phoneNumber);
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                startActivity(intent);
                finish();
            }

            @Override
            public void otpSentError(String errorMsg) {
                showSnackbar(errorMsg);
            }

        });
    }

    private void showSnackbar(String msg) {
        Snackbar.make(mNextButton, msg, Snackbar.LENGTH_LONG)
                .show();
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
