package com.stranger.chat.fuctionality;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HideOnScroll<T extends View> {
    public HideOnScroll(@NonNull RecyclerView recyclerView, T view) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 && view.isShown()) {
                    view.setVisibility(View.GONE);
                } else if (dy < 0) {
                    view.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }
}