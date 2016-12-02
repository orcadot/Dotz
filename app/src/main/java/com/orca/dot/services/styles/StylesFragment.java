package com.orca.dot.services.styles;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.orca.dot.BasePresenter;
import com.orca.dot.R;
import com.orca.dot.model.Style;
import com.orca.dot.ui.widgets.FilterDialog;
import com.orca.dot.utils.AccountUtils;
import com.orca.dot.utils.Constants;

import java.util.List;


/**
 * Created by master on 17/6/16.
 */
public class StylesFragment extends Fragment implements FilterDialog.OnDialogClickedListener, StylesDataContract.View {

    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final String TAG = "StylesFragment";
    private final int SPAN_COUNT = 2;;
    private Context mContext;
    private int mCategory = -1;
    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private LayoutManagerType mCurrentLayoutManagerType;
    private Button switch2;
    private StylesAdapter adapterStyle;
    private TextView filter;
    private TextView cart;
    private TextView filterBar;
    private TextView filterBarClear;
    private StylesDataContract.Presenter mPresenter;
    private String categoryId;


    public StylesFragment() {}

    public static StylesFragment newInstance(String category_id) {
        StylesFragment fragment = new StylesFragment();
        Bundle args = new Bundle();
        args.putString("CATEGORY_ID", category_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryId = getArguments().getString("CATEGORY_ID");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        View rootview = inflater.inflate(R.layout.item_fragment, container, false);
        rootview.setTag(TAG);
        initializeScreen(rootview);
        mCurrentLayoutManagerType = (savedInstanceState != null) ? ((LayoutManagerType) savedInstanceState.getSerializable(KEY_LAYOUT_MANAGER)) : LayoutManagerType.GRID_LAYOUT_MANAGER;
        setRecyclerViewAttr();
        return rootview;
    }

    private void initializeScreen(View rootview) {
        filterBar = (TextView) rootview.findViewById(R.id.showfiltersbar);
        filterBarClear = (TextView) rootview.findViewById(R.id.clearall);
        filter = (TextView) rootview.findViewById(R.id.filter);
        cart = (TextView) rootview.findViewById(R.id.cart);
        recyclerView = (RecyclerView) rootview.findViewById(R.id.recyclerView);

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCategory == 0)
                    showFilterDialog();
            }
        });
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        filterBarClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDialogClicked("NA", "NA", "NA");
                FilterDialog.hairLength = "NA";
                FilterDialog.hairQuality = "NA";
                FilterDialog.faceCut = "NA";
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        new StylesDataPresenter(AccountUtils.getActiveAccountName(getActivity().getApplicationContext()), categoryId, this);
        setmyLayoutManager(mCurrentLayoutManagerType);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPresenter != null)
            mPresenter.start();
    }


    public void setLayoutManagerNow() {
        if (mCurrentLayoutManagerType == LayoutManagerType.GRID_LAYOUT_MANAGER) {
            setRecyclerViewLayoutManager(LayoutManagerType.LINEAR_LAYOUT_MANAGER);
            mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        } else {
            setRecyclerViewLayoutManager(LayoutManagerType.GRID_LAYOUT_MANAGER);
            mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
        }
    }

    public void setmyLayoutManager(LayoutManagerType mCurrentLayoutManagerType) {
        switch (mCurrentLayoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(getActivity());
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getActivity());
                break;

        }
        mLayoutManager.setRecycleChildrenOnDetach(true);
        recyclerView.setLayoutManager(mLayoutManager);
    }

    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        if (recyclerView.getLayoutManager() != null) {
            if (mCurrentLayoutManagerType == LayoutManagerType.LINEAR_LAYOUT_MANAGER)
                scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
                        .findFirstVisibleItemPosition();
            else
                scrollPosition = ((GridLayoutManager) recyclerView.getLayoutManager())
                        .findFirstVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
                mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
                adapterStyle.toggleItemViewType();

                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                adapterStyle.toggleItemViewType();
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        mLayoutManager.setRecycleChildrenOnDetach(true);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.scrollToPosition(scrollPosition);

        Log.d("scroll", String.valueOf(scrollPosition));
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onDialogClicked(final String hairLength, String hairQuality, String faceCut) {
    }

    private StringBuilder setFilterBarText(String hairLength, String hairQuality, String faceCut) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Filters- ");
        if (!hairLength.equals("NANANA")) {
            if (hairLength.equals(FilterDialog.HAIRLENGTH_SMALL))
                stringBuilder.append("Length: small ");
            else if (hairLength.equals(FilterDialog.HAIRLENGTH_MEDIUM))
                stringBuilder.append("Length: medium ");
            else if (hairLength.equals(FilterDialog.HAIRLENGTH_LARGE))
                stringBuilder.append("Length: large ");

        }
        if (!hairQuality.equals("NANANA")) {
            if (hairQuality.equals(FilterDialog.HAIRQUALITY_SOFT))
                stringBuilder.append("Quality: soft ");
            else if (hairQuality.equals(FilterDialog.HAIRQUALITY_HARD))
                stringBuilder.append("Quality: hard ");

        }
        if (!faceCut.equals("NANANA")) {
            if (faceCut.equals(FilterDialog.FACECUT_OBLONG))
                stringBuilder.append("FaceCut: oblong");
            else if (faceCut.equals(FilterDialog.FACECUT_DIAMOND))
                stringBuilder.append("FaceCut: diamond");
            else if (faceCut.equals(FilterDialog.FACECUT_HEART))
                stringBuilder.append("FaceCut: heart");

        }
        return stringBuilder;


    }

    private void showFilterDialog() {
        FragmentManager fm = getFragmentManager();
        FilterDialog filterDialog = new FilterDialog();
        filterDialog.setTargetFragment(StylesFragment.this, 300);
        filterDialog.show(fm, "fragment_filter");
    }

    private void setRecyclerViewAttr() {
        recyclerView.hasFixedSize();
        recyclerView.setItemViewCacheSize(6);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        Log.d("rec Pool", String.valueOf(recyclerView.getRecycledViewPool()));
        recyclerView.setRecyclerListener(new RecyclerView.RecyclerListener() {
            @Override
            public void onViewRecycled(RecyclerView.ViewHolder holder) {
                Log.d("viewRecycled", Integer.toString(holder.getLayoutPosition()));
                ImageView img = (ImageView) holder.itemView.findViewById(R.id.img);
                if (img.getDrawable() != null)
                    img.setImageDrawable(null);
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void showStylesData(List<Style> dataList) {
        adapterStyle = new StylesAdapter(mContext, mCurrentLayoutManagerType, dataList, this);
        recyclerView.setAdapter(adapterStyle);
    }

    @Override
    public void setPresenter(@NonNull StylesDataContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public String getUid() {
        return AccountUtils.getActiveAccountName(getActivity().getApplicationContext());
    }

    public void onFavOrAddClicked(final Style style) {
        mPresenter.favOrAddClicked(style);
    }

    public enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

}
