package com.orca.dot.services.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.orca.dot.R;
import com.orca.dot.model.HairStyle;

import java.util.ArrayList;

/**
 * Created by master on 28/6/16.
 */
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private ArrayList<HairStyle> list;
    private Context context;

    public CartAdapter() {

    }

    public CartAdapter(Context context, ArrayList<HairStyle> favArrayList) {
        this.list = favArrayList;
        this.context = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart_grid_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (!list.get(position).Name.isEmpty())
            holder.name.setText(list.get(position).Name);
        if (!list.get(position).Image.isEmpty())
            Glide.with(context).load(list.get(position).Image).into(holder.image);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void add(HairStyle value) {
        list.add(value);
        notifyItemInserted(list.size() - 1);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            name = (TextView) itemView.findViewById(R.id.name);
        }
    }
}
