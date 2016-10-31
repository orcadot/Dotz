package com.orca.dot.services.fragments;


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
import com.orca.dot.R;
import com.orca.dot.model.HairStyle;
import com.orca.dot.services.Cart;
import com.orca.dot.services.StylesDataContracts;
import com.orca.dot.services.adapters.StylesAdapter;
import com.orca.dot.services.interfaces.ChangeViewListener;
import com.orca.dot.services.widgets.FilterDialog;
import com.orca.dot.utils.Constants;

import java.util.List;


/**
 * Created by master on 17/6/16.
 */
public class StylesFragment extends Fragment implements FilterDialog.OnDialogClickedListener, StylesDataContracts.View {

    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final String TAG = "StylesFragment";
    private final int SPAN_COUNT = 2;
    public String previousCode = "NANANA";
    ChangeViewListener changeViewListener;
    private Context mContext = getActivity();
    private int mCategory = -1;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private ValueEventListener valueEventListener;
    private LinearLayoutManager mLayoutManager;
    private LayoutManagerType mCurrentLayoutManagerType;
    private Button switch2;
    private StylesAdapter adapterStyle;
    private TextView filter;
    private TextView cart;
    private TextView filterBar;
    private TextView filterBarClear;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference userRef = database.getReference(Constants.FIREBASE_LOCATION_USER_LISTS).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    private DatabaseReference databaseReference;
    private ChildEventListener favDataListener;
    private ChildEventListener styleDataListener;
    private ChildEventListener childlistenerfordata;
    private ValueEventListener valueListener;
    private ChildEventListener cartDataListener;

    //My Variables
    private StylesDataContracts.Presenter mPresenter;
    private String mDataChildKey;


    public StylesFragment() {

    }

    public static StylesFragment newInstance(String refKey) {
        Bundle args = new Bundle();
        args.putString("data_reference_key", refKey);
        StylesFragment fragment = new StylesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataChildKey = getArguments().getString("data_reference_key");
        Log.d(TAG, "onCreate() called with: savedInstanceState = [" + mDataChildKey + "]");
        databaseReference = database.getReference(Constants.FIREBASE_STYLE_DATA_NODE).child(mDataChildKey);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = inflater.getContext();
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
                startActivity(new Intent(mContext, Cart.class));
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
        if (getActivity() instanceof Listener) {
            ((Listener) getActivity()).onFragmentAttached(this);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        if (getActivity() instanceof Listener) {
            ((Listener) getActivity()).onFragmentViewCreated(this, databaseReference);
            Log.i(TAG, "onViewCreated: " + databaseReference);
        }
        Log.d(TAG, "onStart() called");
        setmyLayoutManager(mCurrentLayoutManagerType);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPresenter != null)
            mPresenter.start();
    }

    private void initQueryforFav() {
        Query favquery = userRef.child(Constants.FIREBASE_LOCATION_USER_FAVORITE);
        //favquery.addChildEventListener(favDataListener);
        favquery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.i(TAG, "onDataChange: " + snapshot.getValue());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void initQueryforCart() {
        Query favquery = userRef.child(Constants.FIREBASE_LOCATION_USER_CART);
        favquery.addChildEventListener(cartDataListener);
        favquery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("cartvalue", "we are done with loading cart");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
        StringBuilder code = new StringBuilder();

        if (!hairLength.equals("NA")) {
            code.append(hairLength);
        }
        if (!hairQuality.equals("NA")) {
            code.append(hairQuality);
        }
        if (!faceCut.equals("NA"))
            code.append(faceCut);
        if (hairLength.equals("NA") && hairQuality.equals("NA") && faceCut.equals("NA"))
            code.append("NANANA");

        Log.d("filterCode", code.toString());
        Log.d("previousCode", previousCode.toString());
        if (!code.toString().isEmpty())
            if (!previousCode.equals(code.toString())) {
                previousCode = code.toString();
                adapterStyle.clear();
                filterBar.setVisibility(View.VISIBLE);
                filterBarClear.setVisibility(View.VISIBLE);
                filterBar.setText(setFilterBarText(hairLength, hairQuality, faceCut).toString());


                Query query;
                if (!code.toString().equals("NANANA"))
                    query = databaseReference.orderByChild(code.toString()).equalTo("true");
                else {
                    query = databaseReference;
                    filterBar.setVisibility(View.GONE);
                    filterBarClear.setVisibility(View.GONE);
                }
                childlistenerfordata = new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if (!dataSnapshot.getValue().toString().isEmpty()) {

                            HairStyle hairStyle = dataSnapshot.getValue(HairStyle.class);
                            // hairStyle.setLiked(liked);
                            adapterStyle.add(hairStyle);

                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                query.addChildEventListener(childlistenerfordata);
                initQueryforFav();
            }
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
        if (getActivity() instanceof Listener) {
            ((Listener) getActivity()).onFragmentDetached(this);
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (favDataListener != null)
            databaseReference.removeEventListener(favDataListener);
        if (styleDataListener != null)
            databaseReference.removeEventListener(styleDataListener);
        if (childlistenerfordata != null)
            databaseReference.removeEventListener(childlistenerfordata);
        if (valueEventListener != null) {
            databaseReference.removeEventListener(valueEventListener);
            Log.i(TAG, "onStop: referece removed");
        }

        mPresenter.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void showStylesData(List<HairStyle> dataList) {
        adapterStyle = new StylesAdapter(mContext, mCurrentLayoutManagerType, dataList, getUid(), this);
        recyclerView.setAdapter(adapterStyle);
    }

    @Override
    public void updateData(int adapterPosition, HairStyle hairStyle) {
        adapterStyle.updateDataSet(adapterPosition, hairStyle);
    }

    @Override
    public void setPresenter(@NonNull StylesDataContracts.Presenter presenter) {
        mPresenter = presenter;
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public void onFavClicked(String styleKey, int adapterPosition) {
        Log.d(TAG, "onFavClicked() called with: styleKey = [" + styleKey + "], adapterPosition = [" + adapterPosition + "]");
        mPresenter.favClicked(styleKey, adapterPosition);
    }

    public enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }


    public interface Listener {
        void onFragmentViewCreated(StylesFragment stylesFragment, DatabaseReference dataRef);

        void onFragmentAttached(StylesFragment stylesFragment);

        void onFragmentDetached(StylesFragment stylesFragment);
    }

}
