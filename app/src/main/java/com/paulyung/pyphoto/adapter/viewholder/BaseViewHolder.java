package com.paulyung.pyphoto.adapter.viewholder;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by yang on 2016/11/16.
 * paulyung@outlook.com
 */

abstract public class BaseViewHolder<T> extends RecyclerView.ViewHolder {
    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public BaseViewHolder(ViewGroup parent, @LayoutRes int res) {
        super(LayoutInflater.from(parent.getContext()).inflate(res, parent, false));
    }

    public void setData(T data) {
    }

    protected <M extends View> M $(@IdRes int id) {
        return (M) itemView.findViewById(id);
    }

    protected Context getContext(){
        return itemView.getContext();
    }

}
