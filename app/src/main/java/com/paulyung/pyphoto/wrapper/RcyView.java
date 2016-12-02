package com.paulyung.pyphoto.wrapper;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.paulyung.pyphoto.adapter.BaseAdapter;

/**
 * Created by yang on 2016/11/23.
 * paulyung@outlook.com
 */

public class RcyView {
    /**
     * 默认初始化，禁用默认item动画
     * @param rcView RecyclerView
     */
    public static void normalInit(RecyclerView rcView) {
        rcView.setHasFixedSize(true);
        RecyclerView.ItemAnimator animator = new DefaultItemAnimator();
        animator.setChangeDuration(0);//解决刷新使用默认item动画闪屏问题
        rcView.setItemAnimator(animator);
    }

    /**
     * 设置成网格布局，并且适应 HeaderView
     * @param rcView RecyclerView
     * @param adapter BaseAdapter
     * @param spanCount 行数
     */
    public static void setGridLayoutManager(RecyclerView rcView, BaseAdapter adapter, int spanCount) {
        GridLayoutManager manager = new GridLayoutManager(rcView.getContext(), spanCount);
        manager.setSpanSizeLookup(adapter.getGridLookUp(spanCount));
        rcView.setLayoutManager(manager);
    }
}
