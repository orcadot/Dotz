package com.orca.dot.services.widgets;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.orca.dot.R;


/**
 * Created by master on 22/6/16.
 */
public class FilterDialog extends DialogFragment {

    public static final String FACECUT_NONE = "NONE";
    public static final String FACECUT_OBLONG = "O";
    public static final String FACECUT_DIAMOND = "D";
    public static final String FACECUT_HEART = "R";

    public static final String HAIRQUALITY_SOFT = "T";
    public static final String HAIRQUALITY_HARD = "H";

    public static final String HAIRLENGTH_SMALL = "S";
    public static final String HAIRLENGTH_MEDIUM = "M";
    public static final String HAIRLENGTH_LARGE = "L";

    public static String hairLength = "NA";
    public static String hairQuality = "NA";
    public static String faceCut = "NA";

    private FilterRadioButton small, medium, large;
    private RadioButton soft, hard;
    private RadioButton oblong, diamond, heart;
    private String temphairLength = "NA";
    private String temphairQuality = "NA";
    private String tempfaceCut = "NA";

    public FilterDialog() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.filter_style_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        small = (FilterRadioButton) view.findViewById(R.id.small);
        medium = (FilterRadioButton) view.findViewById(R.id.medium);
        large = (FilterRadioButton) view.findViewById(R.id.large);
        soft = (RadioButton) view.findViewById(R.id.soft);
        hard = (RadioButton) view.findViewById(R.id.hard);
        oblong = (RadioButton) view.findViewById(R.id.oblong);
        diamond = (RadioButton) view.findViewById(R.id.heart);
        heart = (RadioButton) view.findViewById(R.id.diamond);


        TextView apply = (TextView) view.findViewById(R.id.apply);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                OnDialogClickedListener listener = (OnDialogClickedListener) getTargetFragment();
                hairLength = temphairLength;
                hairQuality = temphairQuality;
                faceCut = tempfaceCut;
                listener.onDialogClicked(hairLength, hairQuality, faceCut);

            }
        });


        TextView cancel = (TextView) view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });


        RadioGroup rg_hairLength = (RadioGroup) view.findViewById(R.id.rg_hairlength);

        rg_hairLength.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.small:
                        temphairLength = HAIRLENGTH_SMALL;
                        break;
                    case R.id.medium:
                        temphairLength = HAIRLENGTH_MEDIUM;
                        break;
                    case R.id.large:
                        temphairLength = HAIRLENGTH_LARGE;
                        break;
                    default:
                        temphairLength = "NA";
                        break;
                }
            }
        });

        RadioGroup rg_hairQuality = (RadioGroup) view.findViewById(R.id.rg_hairQuality);
        rg_hairQuality.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.soft:
                        temphairQuality = HAIRQUALITY_SOFT;
                        break;
                    case R.id.hard:
                        temphairQuality = HAIRQUALITY_HARD;
                        break;
                    default:
                        temphairQuality = "NA";
                        break;
                }
            }
        });


        RadioGroup rg_faceCut = (RadioGroup) view.findViewById(R.id.rg_faceCut);
        rg_faceCut.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.oblong:
                        tempfaceCut = FACECUT_OBLONG;
                        break;
                    case R.id.diamond:
                        tempfaceCut = FACECUT_DIAMOND;
                        break;
                    case R.id.heart:
                        tempfaceCut = FACECUT_HEART;
                        break;
                    default:
                        tempfaceCut = "NA";
                        break;
                }
            }
        });

        if (!hairLength.equals("NA"))
            checkHairlengthoption(hairLength, rg_hairLength);
        if (!hairQuality.equals("NA"))
            checkHairQualityoption(hairQuality, rg_hairQuality);
        if (!faceCut.equals("NA"))
            checkFaceCutoption(faceCut, rg_faceCut);


    }

    private void checkFaceCutoption(String faceCut, RadioGroup rg_faceCut) {

        if (faceCut.equals(FACECUT_DIAMOND))
            rg_faceCut.check(R.id.diamond);
        else if (faceCut.equals(FACECUT_HEART))
            rg_faceCut.check(R.id.heart);
        else if (faceCut.equals(FACECUT_OBLONG))
            rg_faceCut.check(R.id.oblong);
    }

    private void checkHairQualityoption(String hairQuality, RadioGroup rg_hairQuality) {

        if (hairQuality.equals(HAIRQUALITY_HARD))
            rg_hairQuality.check(R.id.hard);
        else if (hairQuality.equals(HAIRQUALITY_SOFT))
            rg_hairQuality.check(R.id.soft);
    }

    private void checkHairlengthoption(String hairLength, RadioGroup rg_hairLength) {
        if (hairLength.equals(HAIRLENGTH_SMALL))
            rg_hairLength.check(R.id.small);
        else if (hairLength.equals(HAIRLENGTH_MEDIUM))
            rg_hairLength.check(R.id.medium);
        else if (hairLength.equals(HAIRLENGTH_LARGE))
            rg_hairLength.check(R.id.large);

    }


    public interface OnDialogClickedListener {
        public abstract void onDialogClicked(String hairLength, String hairQuality, String faceCut);
    }
}
