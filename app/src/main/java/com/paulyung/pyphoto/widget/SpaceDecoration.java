package com.paulyung.pyphoto.widget;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.View;

public class SpaceDecoration extends RecyclerView.ItemDecoration {

    private int halfSpace;
    private boolean mPaddingEdgeSide = true;
    private boolean mPaddingStart = true;
    private boolean mPaddingHeaderFooter = false;


    public SpaceDecoration(int space) {
        this.halfSpace = space / 2;
    }

    public void setPaddingEdgeSide(boolean mPaddingEdgeSide) {
        this.mPaddingEdgeSide = mPaddingEdgeSide;
    }

    public void setPaddingStart(boolean mPaddingStart) {
        this.mPaddingStart = mPaddingStart;
    }

    public void setPaddingHeaderFooter(boolean mPaddingHeaderFooter) {
        this.mPaddingHeaderFooter = mPaddingHeaderFooter;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int spanCount = 0;
        int orientation = 0;
        int spanIndex = 0;

        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
         if (layoutManager instanceof StaggeredGridLayoutManager){
             orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
             spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
             spanIndex = ((StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams()).getSpanIndex();
        }else if (layoutManager instanceof GridLayoutManager){
             orientation = ((GridLayoutManager) layoutManager).getOrientation();
             spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
             spanIndex = ((GridLayoutManager.LayoutParams) view.getLayoutParams()).getSpanIndex();
        }else if (layoutManager instanceof LinearLayoutManager){
             orientation = ((LinearLayoutManager) layoutManager).getOrientation();
             spanCount = 1;
             spanIndex = 0;
        }

        /**
         * 普通Item的尺寸
         */
            int gravity;
            if (spanIndex == 0&&spanCount>1)gravity = Gravity.LEFT;
            else if (spanIndex == spanCount-1&&spanCount>1)gravity = Gravity.RIGHT;
            else if (spanCount == 1)gravity = Gravity.FILL_HORIZONTAL;
            else {
                gravity = Gravity.CENTER;
            }
            if (orientation == OrientationHelper.VERTICAL){
                switch (gravity){
                    case Gravity.LEFT:
                        if (mPaddingEdgeSide)
                            outRect.left = halfSpace*2;
                        outRect.right = halfSpace;
                        break;
                    case Gravity.RIGHT:
                        outRect.left = halfSpace;
                        if (mPaddingEdgeSide)
                            outRect.right = halfSpace*2;
                        break;
                    case Gravity.FILL_HORIZONTAL:
                        if (mPaddingEdgeSide){
                            outRect.left = halfSpace*2;
                            outRect.right = halfSpace*2;
                        }
                        break;
                    case Gravity.CENTER:
                        outRect.left = halfSpace;
                        outRect.right = halfSpace;
                        break;
                }
            }else {
                switch (gravity){
                    case Gravity.LEFT:
                        if (mPaddingEdgeSide)
                            outRect.bottom = halfSpace*2;
                        outRect.top = halfSpace;
                        break;
                    case Gravity.RIGHT:
                        outRect.bottom = halfSpace;
                        if (mPaddingEdgeSide)
                            outRect.top = halfSpace*2;
                        break;
                    case Gravity.FILL_HORIZONTAL:
                        if (mPaddingEdgeSide){
                            outRect.left = halfSpace*2;
                            outRect.right = halfSpace*2;
                        }
                        break;
                    case Gravity.CENTER:
                        outRect.bottom = halfSpace;
                        outRect.top = halfSpace;
                        break;
                }
            }
    }
}