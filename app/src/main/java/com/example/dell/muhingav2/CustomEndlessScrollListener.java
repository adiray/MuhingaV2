package com.example.dell.muhingav2;

import android.support.v7.widget.RecyclerView;

import com.mikepenz.fastadapter.adapters.FooterAdapter;
import com.mikepenz.fastadapter_extensions.scroll.EndlessRecyclerOnScrollListener;

public abstract class CustomEndlessScrollListener extends EndlessRecyclerOnScrollListener {

    Boolean hasRefreshed = false;

    public CustomEndlessScrollListener(FooterAdapter adapter) {
        super(adapter);
    }

    void refreshCheck() {

        if (hasRefreshed){

            onLoadMore(1);
        }

    }

    void setHasRefreshed(Boolean hasRefreshed) {

        this.hasRefreshed = hasRefreshed;

    }


}
