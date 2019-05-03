package com.donyd.jsunscripted.www.shopkeep;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

// code adapted from https://codeburst.io/android-swipe-menu-with-recyclerview-8f28a235ff28
public class ItemDataAdapter extends RecyclerView.Adapter<ItemDataAdapter.ItemViewHolder> {
    private List<Item> items;

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView name, price;

        public ItemViewHolder(View view){
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            price = (TextView) view.findViewById(R.id.price);

        }
    }

    public ItemDataAdapter(List<Item> items){
        this.items = items;
    }

    @NonNull
    @Override
    public ItemDataAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_row, viewGroup, false);

        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemDataAdapter.ItemViewHolder itemViewHolder, int i) {
        Item item = items.get(i);
        itemViewHolder.name.setText(item.getName());
        itemViewHolder.price.setText(item.getPrice());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
