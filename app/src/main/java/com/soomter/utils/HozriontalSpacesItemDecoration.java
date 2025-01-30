package com.soomter.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class HozriontalSpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public HozriontalSpacesItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {

        if (parent.getChildLayoutPosition(view) == 0)
        {
            outRect.left = space;
            outRect.right = 0;
        }else{
            outRect.left = space;
            outRect.right = space;
        }
    }
}