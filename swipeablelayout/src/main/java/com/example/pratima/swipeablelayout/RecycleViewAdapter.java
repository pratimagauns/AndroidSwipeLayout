package com.example.pratima.swipeablelayout;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by pratima on 15/03/16.
 */
public class RecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<String> posts = new ArrayList<>();
    private Context mContext;

    public RecycleViewAdapter(List<String> posts) {
        this.posts = posts;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
        return new WallHeaderHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String string = posts.get(position);
        ((WallHeaderHolder) holder).textDescription.setText(string);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    private class WallHeaderHolder extends RecyclerView.ViewHolder {
        SwipeLayout swipeLayout;
        TextView textDescription;

        public WallHeaderHolder(View view) {
            super(view);

            swipeLayout = (SwipeLayout) view.findViewById(R.id.recycler_view_swipe_layout);
            textDescription = (TextView) swipeLayout.findViewById(R.id.content_view_text_description);
        }
    }
}
