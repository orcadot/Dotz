package com.orca.dot.auth.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.orca.dot.R;
import com.orca.dot.model.User;
import com.orca.dot.utils.validate.EmailValidator;

public class ProfileDetailsFragment extends Fragment {

    private EditText nameEditText;
    private EditText emailEditText;
    private RadioGroup genderRadioGroup;
    private Button profileSaveButton;

    private String mGender;
    private int mGenderCode;

    private static final String TAG = "ProfileDetailsFragment";

    private OnProfileSaveClickListener mListener;

    public ProfileDetailsFragment() {
        // Required empty public constructor
    }

    public static ProfileDetailsFragment newInstance() {
        return new ProfileDetailsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView() called with: inflater = [" + inflater + "], container = [" + container + "], savedInstanceState = [" + savedInstanceState + "]");
        return inflater.inflate(R.layout.fragment_profile_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        bindUI(view);
    }

    public void saveProfile(User user) {
        if (mListener != null) {
            mListener.onProfileSave(user);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnProfileSaveClickListener) {
            mListener = (OnProfileSaveClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnProfileSaveClickListener {
        void onProfileSave(User user);
    }

    private void bindUI(View view) {
        nameEditText = (EditText) view.findViewById(R.id.name_edit_text);
        emailEditText = (EditText) view.findViewById(R.id.email_edit_text);
        genderRadioGroup = (RadioGroup) view.findViewById(R.id.radio_group_gender);
        genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_btn_male:
                        mGender = "Male";
                        mGenderCode = 0;
                        Log.i(TAG, "onCheckedChanged: male");
                        break;
                    case R.id.radio_btn_female:
                        mGender = "Female";
                        mGenderCode = 1;
                        Log.i(TAG, "onCheckedChanged: female");
                        break;
                    default:
                }
            }
        });

        profileSaveButton = (Button) view.findViewById(R.id.profile_save_button);
        profileSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmailValidator emailValidator = new EmailValidator();
                String email = emailEditText.getText().toString().trim();
                String name = nameEditText.getText().toString().trim();
                if (name.isEmpty()) {
                    Snackbar.make(v, "Name is empty.", Snackbar.LENGTH_LONG).show();
                } else if (!emailValidator.validate(email))
                    Snackbar.make(v, "Email is not valid", Snackbar.LENGTH_LONG).show();
                else
                    saveProfile(new User(name, email, mGender, mGenderCode));

            }
        });
    }
}
