package com.orca.dot.services.favorites;

import android.content.Context;
import android.support.annotation.NonNull;
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
import com.orca.dot.model.BaseModel;
import com.orca.dot.model.Style;
import com.orca.dot.model.Header;

import java.util.ArrayList;
import java.util.List;

import static com.orca.dot.utils.Providers.getUid;


/**
 * Created by amit on 31/10/16.
 */

public class FavoritesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_STYLE = 1;

    private final Context context;
    private final LayoutInflater layoutInflater;
    private List<BaseModel> items;

    private static final String TAG = "CartAdapter";

    public FavoritesAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        items = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                return createHeaderHolder(parent);
            case TYPE_STYLE:
                return createStyleHolder(parent);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                bindHeader((Header) getItem(position), (HeaderHolder) holder);
                break;
            case TYPE_STYLE:
                bindStyle((Style) getItem(position), (StyleViewHolder) holder);
                break;
        }
    }

    @NonNull
    private StyleViewHolder createStyleHolder(ViewGroup parent) {
        final StyleViewHolder holder = new StyleViewHolder(layoutInflater.inflate(R.layout.item_style_grid_item, parent, false));
        return holder;
    }

    private void bindStyle(Style style, StyleViewHolder holder) {
        if (!style.style_image.isEmpty())
            Glide.with(context).load(style.style_image).into(holder.mImageView);

        if (!style.style_name.isEmpty())
            holder.mTextView.setText(style.style_name);


        holder.numLikes.setText(String.valueOf(" Likes"));
    }


    @NonNull
    private HeaderHolder createHeaderHolder(ViewGroup parent) {
        final HeaderHolder holder = new HeaderHolder(layoutInflater.inflate(R.layout.recycler_view_header, parent, false));
        return holder;
    }


    private void bindHeader(Header header, HeaderHolder holder) {
        holder.mHeader.setText(header.headerText);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    @Override
    public int getItemViewType(int position) {
        BaseModel model = getItem(position);
        if (model instanceof Style)
            return TYPE_STYLE;
        else if (model instanceof Header)
            return TYPE_HEADER;
        else
            return TYPE_HEADER;
    }

    private BaseModel getItem(int position) {
        return items.get(position);
    }

    public int getItemColumnSpan(int position) {
        return getItem(position).colSpan;
    }

    public void add(List<? extends BaseModel> favItems) {
        for (BaseModel favItem : favItems) {
            items.add(favItem);
        }
        expandHeaders();
        notifyDataSetChanged();
    }

    private void expandHeaders() {
        Log.d(TAG, "expandHeaders() called");
        for (BaseModel item : items) {
            if (item instanceof Header) {
                item.colSpan = 2;
            } else
                item.colSpan = 1;
        }
    }

    static class HeaderHolder extends RecyclerView.ViewHolder {
        TextView mHeader;

        HeaderHolder(View view) {
            super(view);
            mHeader = (TextView) view.findViewById(R.id.header_title);
        }
    }

    static class StyleViewHolder extends RecyclerView.ViewHolder {
        private final ImageView fav;
        private final Button cart;
        private final TextView numLikes;
        public TextView mTextView;
        public ImageView mImageView;
        private View ItemView;

        public StyleViewHolder(View itemView) {
            super(itemView);

            ItemView = itemView;
            mImageView = (ImageView) itemView.findViewById(R.id.img);
            fav = (ImageView) itemView.findViewById(R.id.fav);
            cart = (Button) itemView.findViewById(R.id.cart);
            numLikes = (TextView) itemView.findViewById(R.id.numLikes);
            mTextView = (TextView) itemView.findViewById(R.id.text);

            cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });

        }
    }

}



