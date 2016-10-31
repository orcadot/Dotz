package com.orca.dot.services.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.orca.dot.R;
import com.orca.dot.model.HairStyle;
import com.orca.dot.services.fragments.StylesFragment;
import com.orca.dot.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import static com.orca.dot.utils.Constants.GRID_ITEM;
import static com.orca.dot.utils.Constants.LIST_ITEM;


/**
 * Created by master on 15/6/16.
 */
public class StylesAdapter extends RecyclerView.Adapter<StylesAdapter.ViewHolder> {

    private static final String TAG = "StylesAdapter";
    private final int SET_CART = 1;
    private Context context;
    private List<HairStyle> list = new ArrayList<>();
    private int mCurrentViewType = GRID_ITEM;
    private Fragment fragment;
    private String currentUserId;

    public StylesAdapter(Context context, StylesFragment.LayoutManagerType mCurrentLayoutManagerType, List<HairStyle> list, String uid, Fragment fragment) {
        this.context = context;
        if (mCurrentLayoutManagerType == StylesFragment.LayoutManagerType.GRID_LAYOUT_MANAGER)
            mCurrentViewType = GRID_ITEM;
        else mCurrentViewType = LIST_ITEM;

        this.list = list;
        this.currentUserId = uid;
        this.fragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == GRID_ITEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_style_grid_item, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_style_list_item, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (!list.get(position).Image.isEmpty())
            Glide.with(context).load(list.get(position).Image).into(holder.mImageView);

        if (!list.get(position).Name.isEmpty())
            holder.mTextView.setText(list.get(position).Name);

        if (list.get(position).likes.containsKey(currentUserId)) {
            if (mCurrentViewType == GRID_ITEM)
                holder.fav.setImageResource(R.drawable.ic_favorite_red_18dp);
            else holder.fav.setImageResource(R.drawable.ic_favorite_red_24dp);

        } else {
            if (mCurrentViewType == GRID_ITEM)
                holder.fav.setImageResource(R.drawable.ic_favorite_border_black_18dp);
            else holder.fav.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }


        holder.numLikes.setText(String.valueOf(list.get(position).likesCount) + " Likes");
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {

        if (!payloads.isEmpty()) {
            if (payloads.contains(Constants.LIKE_UPDATE)) {
                if (list.get(position).likes.containsKey(currentUserId)) {
                    if (mCurrentViewType == GRID_ITEM)
                        holder.fav.setImageResource(R.drawable.ic_favorite_red_18dp);
                    else holder.fav.setImageResource(R.drawable.ic_favorite_red_24dp);

                } else {
                    if (mCurrentViewType == GRID_ITEM)
                        holder.fav.setImageResource(R.drawable.ic_favorite_border_black_18dp);
                    else holder.fav.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                }

                holder.numLikes.setText(String.valueOf(list.get(position).likesCount) + " Likes");
            }
        } else
            super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mCurrentViewType;
    }

    public void toggleItemViewType() {
        if (mCurrentViewType == GRID_ITEM) {
            mCurrentViewType = LIST_ITEM;
            notifyDataSetChanged();
        } else {
            mCurrentViewType = GRID_ITEM;
            notifyDataSetChanged();
        }
    }

    public void add(HairStyle value) {
        list.add(value);
        notifyItemInserted(list.size() - 1);
    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    public void updateDataSet(int adapterPosition, HairStyle hairStyle) {
        if (hairStyle.likesCount != list.get(adapterPosition).likesCount) {
            list.get(adapterPosition).likesCount = hairStyle.likesCount;
            list.get(adapterPosition).likes = hairStyle.likes;
            notifyItemChanged(adapterPosition, Constants.LIKE_UPDATE);
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView fav;
        private final Button cart;
        private final TextView numLikes;
        public TextView mTextView;
        public ImageView mImageView;
        private View ItemView;

        public ViewHolder(View itemView) {
            super(itemView);

            ItemView = itemView;
            mImageView = (ImageView) itemView.findViewById(R.id.img);
            fav = (ImageView) itemView.findViewById(R.id.fav);
            cart = (Button) itemView.findViewById(R.id.cart);
            numLikes = (TextView) itemView.findViewById(R.id.numLikes);
            mTextView = (TextView) itemView.findViewById(R.id.text);


            fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position = getAdapterPosition();
                    HairStyle hairStyle = list.get(position);
                    Log.i(TAG, "onClick: " + hairStyle.Name);
                    if (hairStyle.likes.containsKey(currentUserId)) {
                        hairStyle.likesCount = hairStyle.likesCount - 1;
                        hairStyle.likes.remove(currentUserId);
                        if (mCurrentViewType == GRID_ITEM)
                            fav.setImageResource(R.drawable.ic_favorite_red_18dp);
                        else fav.setImageResource(R.drawable.ic_favorite_red_24dp);
                    } else {
                        hairStyle.likesCount = hairStyle.likesCount + 1;
                        hairStyle.likes.put(currentUserId, true);
                        if (mCurrentViewType == GRID_ITEM)
                            fav.setImageResource(R.drawable.ic_favorite_border_black_18dp);
                        else fav.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    }
                    notifyItemChanged(position, Constants.LIKE_UPDATE);
                    ((StylesFragment) fragment).onFavClicked(list.get(getAdapterPosition()).uniqueKey, getAdapterPosition());

                }
            });
            cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });

        }
    }


}
