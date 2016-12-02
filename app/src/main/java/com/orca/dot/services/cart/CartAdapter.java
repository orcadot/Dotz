package com.orca.dot.services.cart;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.orca.dot.R;
import com.orca.dot.model.BaseModel;
import com.orca.dot.model.Footer;
import com.orca.dot.model.SubHeader;
import com.orca.dot.model.Style;
import java.util.ArrayList;
import java.util.List;

import static com.orca.dot.utils.Constants.LIKE_UPDATE;

class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_STYLE = 1;
    private static final int TYPE_FOOTER = 2;

    private final static int LIKE_UPDATE = 10;
    private final static int ADD_UPDATE = 20;

    private final Context context;
    private final LayoutInflater layoutInflater;
    private List<BaseModel> items;

    CartAdapter(Context context) {
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
            case TYPE_FOOTER:
                return createFooterHolder(parent);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                bindHeader((SubHeader) getItem(position), (HeaderHolder) holder);
                break;
            case TYPE_STYLE:
                bindStyle((Style) getItem(position), (StyleViewHolder) holder);
                break;
            case TYPE_FOOTER:
                bindFooter((Footer) getItem(position), (FooterHolder) holder);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        if(payloads.isEmpty())
            onBindViewHolder(holder, position);
        else if(payloads.contains(LIKE_UPDATE)){
            Style style = (Style) getItem(position);
            ((StyleViewHolder) holder).fav.setImageResource((style.isLiked) ?
                    R.drawable.ic_favorite_red_18dp : R.drawable.ic_favorite_border_black_18dp);
        }
    }

    @NonNull
    private StyleViewHolder createStyleHolder(ViewGroup parent) {
        final StyleViewHolder styleViewHolder = new StyleViewHolder(layoutInflater.inflate(R.layout.item_style_grid_item, parent, false));
        styleViewHolder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position = styleViewHolder.getAdapterPosition();
                Style style = (Style) getItem(position);
                style.isLiked = !style.isLiked;
                notifyItemChanged(position, LIKE_UPDATE);
                ((CartActivity)context).onFavOrAddClicked(style);
            }
        });
        return styleViewHolder;
    }

    private void bindStyle(Style item, StyleViewHolder holder) {
        if (!item.style_image.isEmpty())
            Glide.with(context).load(item.style_image).into(holder.mImageView);

        if (!item.style_name.isEmpty())
            holder.mTextView.setText(item.style_name);
        holder.fav.setImageResource((item.isLiked) ? R.drawable.ic_favorite_red_18dp : R.drawable.ic_favorite_border_black_18dp);
        holder.cart.setText(item.isAdded ? "-ADD" : "+ADD");
    }


    @NonNull
    private HeaderHolder createHeaderHolder(ViewGroup parent) {
        return new HeaderHolder(layoutInflater.inflate(R.layout.recycler_view_header, parent, false));
    }


    private void bindHeader(SubHeader subHeader, HeaderHolder holder) {
        holder.mHeader.setText(subHeader.headerText);
    }

    private FooterHolder createFooterHolder(ViewGroup parent){
        return new FooterHolder(layoutInflater.inflate(R.layout.cart_recycler_view_footer, parent, false));
    }

    private void bindFooter(Footer item, FooterHolder holder) {
        holder.mFooter.setText(String.format("Rs. %d /-", item.footerString));
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
        else if (model instanceof SubHeader)
            return TYPE_HEADER;
        else
            return TYPE_FOOTER;
    }

    private BaseModel getItem(int position) {
        return items.get(position);
    }

    int getItemColumnSpan(int position) {
        return getItem(position).getColSpan();
    }

    public void add(List<? extends BaseModel> cartItems) {
        for (BaseModel cartItem : cartItems) {
            items.add(cartItem);
        }
        expandHeaders();
        notifyDataSetChanged();
    }

    private void expandHeaders() {
        for (BaseModel item : items) {
            if (item instanceof SubHeader || item instanceof Footer) {
                item.setColSpan(2);
            } else
                item.setColSpan(1);
        }
    }

    private static class HeaderHolder extends RecyclerView.ViewHolder {
        TextView mHeader;

        HeaderHolder(View view) {
            super(view);
            mHeader = (TextView) view.findViewById(R.id.header_title);
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

    private static class FooterHolder extends RecyclerView.ViewHolder {

        TextView mFooter;

        FooterHolder(View itemView) {
            super(itemView);
            mFooter = (TextView) itemView.findViewById(R.id.total_price_textview);
        }
    }

}



