package com.paulyung.pyphoto.fragment;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by yang on 2016/11/15.
 * paulyung@outlook.com
 */

public class BaseFragment extends Fragment {
    protected View mRootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(getLayoutId(), container, false);
        if (mRootView != null) {
            initView();
            return mRootView;
        }
        else
            return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected View findViewById(@IdRes int id) {
        return mRootView.findViewById(id);
    }

    protected int getLayoutId() {
        return 0;
    }

    protected void initView() {
    }
}
