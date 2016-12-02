package com.orca.dot.services.styles;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.orca.dot.R;
import com.orca.dot.model.Style;

import java.util.ArrayList;
import java.util.List;

import static com.orca.dot.utils.Constants.GRID_ITEM;
import static com.orca.dot.utils.Constants.LIST_ITEM;

class StylesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final LayoutInflater layoutInflater;
    private Context context;
    private List<Style> styleList = new ArrayList<>();
    private final StylesFragment stylesFragment;
    private int mCurrentViewType = GRID_ITEM;
    private final static int LIKE_UPDATE = 10;
    private final static int ADD_UPDATE = 20;

    StylesAdapter(Context context, StylesFragment.LayoutManagerType mCurrentLayoutManagerType, List<Style> styleList, StylesFragment stylesFragment) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        mCurrentViewType = (mCurrentLayoutManagerType == StylesFragment.LayoutManagerType.GRID_LAYOUT_MANAGER)
                ? GRID_ITEM : LIST_ITEM;

        this.styleList = styleList;
        this.stylesFragment = stylesFragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return createStyleHolder(parent, viewType);
    }

    private StyleViewHolder createStyleHolder(ViewGroup parent, int viewType) {
        final StyleViewHolder styleViewHolder;
        switch (viewType) {
            case GRID_ITEM:
                styleViewHolder = new StyleViewHolder(layoutInflater.inflate(R.layout.item_style_grid_item, parent, false));
                break;
            case LIST_ITEM:
                styleViewHolder = new StyleViewHolder(layoutInflater.inflate(R.layout.item_style_list_item, parent, false));
                break;
            default:
                styleViewHolder = new StyleViewHolder(layoutInflater.inflate(R.layout.item_style_grid_item, parent, false));
                break;
        }
        styleViewHolder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position = styleViewHolder.getAdapterPosition();
                Style style = getItem(position);
                style.isLiked = !style.isLiked;
                notifyItemChanged(position, LIKE_UPDATE);
                stylesFragment.onFavOrAddClicked(style);

            }
        });

        styleViewHolder.cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position = styleViewHolder.getAdapterPosition();
                Style style = getItem(position);
                style.isAdded = !style.isAdded;
                notifyItemChanged(position, ADD_UPDATE);
                stylesFragment.onFavOrAddClicked(style);
            }
        });

        return styleViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case GRID_ITEM:
                bindStyleGrid(getItem(position), (StyleViewHolder) holder);
                break;
            case LIST_ITEM:
                bindStyleList(getItem(position), (StyleViewHolder) holder);
                break;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else if (payloads.contains(LIKE_UPDATE)) {
            Style style = getItem(position);
            if (getItemViewType(position) == GRID_ITEM)
                ((StyleViewHolder) holder).fav.setImageResource((style.isLiked) ? R.drawable.ic_favorite_red_18dp : R.drawable.ic_favorite_border_black_18dp);
            else
                ((StyleViewHolder) holder).fav.setImageResource((style.isLiked) ? R.drawable.ic_favorite_red_24dp : R.drawable.ic_favorite_border_dribble_dark_24dp);
        } else if (payloads.contains(ADD_UPDATE)) {
            Style style = getItem(position);
            ((StyleViewHolder) holder).cart.setText((style.isAdded) ? "-ADD" : "+ADD");

        }
    }

    private void bindStyleList(Style item, StyleViewHolder holder) {
        if (!item.style_image.isEmpty())
            Glide.with(context).load(item.style_image).into(holder.mImageView);

        if (!item.style_name.isEmpty())
            holder.mTextView.setText(item.style_name);
        holder.fav.setImageResource(R.drawable.ic_favorite_border_dribble_dark_24dp);
    }

    private void bindStyleGrid(Style item, StyleViewHolder holder) {
        if (!item.style_image.isEmpty())
            Glide.with(context).load(item.style_image).into(holder.mImageView);

        if (!item.style_name.isEmpty())
            holder.mTextView.setText(item.style_name);
        holder.fav.setImageResource((item.isLiked) ? R.drawable.ic_favorite_red_18dp : R.drawable.ic_favorite_border_black_18dp);
        holder.cart.setText(item.isAdded ? "-ADD" : "+ADD");
    }



   /* @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {

        if (!payloads.isEmpty()) {
            if (payloads.contains(Constants.LIKE_UPDATE)) {
                if (list.get(position).user_likes.containsKey(currentUserId)) {
                    if (mCurrentViewType == GRID_ITEM)
                        holder.fav.setImageResource(R.drawable.ic_favorite_red_18dp);
                    else holder.fav.setImageResource(R.drawable.ic_favorite_red_24dp);

                } else {
                    if (mCurrentViewType == GRID_ITEM)
                        holder.fav.setImageResource(R.drawable.ic_favorite_border_black_18dp);
                    else holder.fav.setImageResource(R.drawable.ic_favorite_border_dribble_dark_24dp);
                }

                holder.numLikes.setText(String.valueOf(list.get(position).likesCount) + " Likes");
            }
        } else
            super.onBindViewHolder(holder, position, payloads);
    }*/

    private Style getItem(int position) {
        return styleList.get(position);
    }

    @Override
    public int getItemCount() {
        return styleList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mCurrentViewType;
    }

    void toggleItemViewType() {
        if (mCurrentViewType == GRID_ITEM) {
            mCurrentViewType = LIST_ITEM;
            notifyDataSetChanged();
        } else {
            mCurrentViewType = GRID_ITEM;
            notifyDataSetChanged();
        }
    }

    private static class StyleViewHolder extends RecyclerView.ViewHolder {
        private final ImageView fav;
        private final Button cart;
        private TextView mTextView;
        private ImageView mImageView;


        StyleViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.img);
            fav = (ImageView) itemView.findViewById(R.id.fav);
            cart = (Button) itemView.findViewById(R.id.cart);
            mTextView = (TextView) itemView.findViewById(R.id.text);


        }
    }


}
