package com.orca.dot.auth.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.orca.dot.R;

public class FacecutDetectFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private OnFacecutDetectClickListener mListener;
    private Spinner fctSpinnner;
    private Button knowfctBtn;
    private Button doneFCT;

    private int fctType = 0;
    private String fct;

    public FacecutDetectFragment() {
        // Required empty public constructor
    }

    public static FacecutDetectFragment newInstance() {
        FacecutDetectFragment fragment = new FacecutDetectFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_facecut_detect, container, false);
        fctSpinnner = (Spinner) view.findViewById(R.id.facecuts_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.fct_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fctSpinnner.setAdapter(adapter);
        fctSpinnner.setOnItemSelectedListener(this);

        knowfctBtn = (Button) view.findViewById(R.id.know_fct_button);
        knowfctBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Will add this feature soon!!!", Toast.LENGTH_SHORT).show();
            }
        });

        doneFCT = (Button) view.findViewById(R.id.done_fct_button);
        doneFCT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFCT(fctType, fct);
            }
        });
        return view;
    }

    public void saveFCT(int fctType, String fct) {
        if (mListener != null && fctType != -1) {
            mListener.onFacecutSelect(fctType, fct);
        } else if (mListener == null) {
            Toast.makeText(getActivity(), "click listener not found", Toast.LENGTH_SHORT).show();
        } else
            Snackbar.make(doneFCT, "Select a facecut type to proceed", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFacecutDetectClickListener) {
            mListener = (OnFacecutDetectClickListener) context;
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        fct = (String) parent.getItemAtPosition(position);
        fctType = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        fctType = -1;
    }

    public interface OnFacecutDetectClickListener {
        void onFacecutSelect(int facecut, String fct);
    }
}
