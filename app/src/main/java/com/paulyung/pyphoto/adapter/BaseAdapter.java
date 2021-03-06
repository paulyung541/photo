package com.paulyung.pyphoto.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paulyung.pyphoto.adapter.viewholder.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yang on 2016/11/16.
 * paulyung@outlook.com
 */

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    private List<T> mData;
    private Context mContext;

    private View mHeaderView;
    private View mFooterView;

    private OnItemClickListener mItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public BaseAdapter(Context context, List<T> datas) {
        mContext = context;
        mData = datas;
    }

    public BaseAdapter(Context context) {
        this(context, null);
    }

    //GridLayoutManager构造函数传入 数据每行的item个数
    public class GridSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {
        protected int mNormalSize;

        public GridSpanSizeLookup(int normalSize) {
            mNormalSize = normalSize;
        }

        @Override
        public int getSpanSize(int position) {
            if (mHeaderView != null && position == 0)
                return mNormalSize;
            if (mFooterView != null && position - mData.size() - 1 >= 0)
                return mNormalSize;
            return 1;
        }
    }

    public GridSpanSizeLookup getGridLookUp(int normalSize) {
        return new GridSpanSizeLookup(normalSize);
    }

    protected abstract BaseViewHolder createHolder(ViewGroup parent, int viewType);

    private View createSpViewByType(int viewType) {
        if (mHeaderView != null && mHeaderView.hashCode() == viewType) {
            StaggeredGridLayoutManager.LayoutParams layoutParams;
            if (mHeaderView.getLayoutParams() != null)
                layoutParams = new StaggeredGridLayoutManager.LayoutParams(mHeaderView.getLayoutParams());
            else
                layoutParams = new StaggeredGridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setFullSpan(true);
            mHeaderView.setLayoutParams(layoutParams);
            return mHeaderView;
        }
        if (mFooterView != null && mFooterView.hashCode() == viewType) {
            StaggeredGridLayoutManager.LayoutParams layoutParams;
            if (mFooterView.getLayoutParams() != null)
                layoutParams = new StaggeredGridLayoutManager.LayoutParams(mFooterView.getLayoutParams());
            else
                layoutParams = new StaggeredGridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setFullSpan(true);
            mFooterView.setLayoutParams(layoutParams);
            return mFooterView;
        }
        return null;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = createSpViewByType(viewType);
        if (view != null)
            return new SpViewHolder(view);

        final BaseViewHolder viewHolder = createHolder(parent, viewType);
        if (mItemClickListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int p = viewHolder.getAdapterPosition();
                    if (mHeaderView != null)
                        p -= 1;
                    int componentP = getComponentP(p);
                    mItemClickListener.onItemClick(v, componentP);
                }
            });
        }
        if (onItemLongClickListener != null) {
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int p = viewHolder.getAdapterPosition();
                    if (mHeaderView != null)
                        p -= 1;
                    int componentP = getComponentP(p);
                    return onItemLongClickListener.onItemLongClick(v, componentP);
                }
            });
        }
        return viewHolder;
    }

    //如果mData中夹杂其它数据，则需要处理，让点击事件响应该相应的数据
    protected int getComponentP(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if (mHeaderView != null)
            if (position == 0)
                return;
        if (mFooterView != null)
            if (position - mData.size() - 1 >= 0)
                return;
        if (mHeaderView != null)
            holder.setData(getItem(position - 1));
        else
            holder.setData(getItem(position));
        onBindView(holder, position);
    }

    protected void onBindView(BaseViewHolder holder, int position) {
    }

    public T getItem(int position) {
        return mData.get(position);
    }

    public List<T> getAllData() {
        return mData;
    }

    @Override
    public int getItemCount() {
        if (mData == null)
            return 0;
        int res = mData.size();
        if (mHeaderView != null)
            res += 1;
        if (mFooterView != null)
            res += 1;
        return res;
    }

    public int getDataSize() {
        return mData.size();
    }

    /**
     * 刷新某个item的界面
     *
     * @param position position
     */
    public synchronized void update(int position) {
        if (getItem(position) != null)
            notifyItemChanged(position);
    }

    public synchronized void add(T data) {
        if (mData == null)
            mData = new ArrayList<>();
        if (data != null) {
            mData.add(data);
            int position = getDataSize();
            if (mHeaderView == null)
                position -= 1;
            notifyItemInserted(position);
        }
    }

    public synchronized void addAll(List<T> dataList) {
        if (mData == null)
            mData = new ArrayList<>();
        if (dataList != null && !dataList.isEmpty()) {
            int position = getDataSize();
            if (mHeaderView != null)
                position += 1;
            mData.addAll(dataList);
            notifyItemRangeInserted(position, dataList.size());
        }
    }

    public synchronized void clear() {
        if (mData != null && mData.size() != 0) {
            int size = mData.size();
            mData.clear();
            int p = mHeaderView == null ? 0 : 1;
            notifyItemRangeRemoved(p, size);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeaderView != null)
            if (position == 0)
                return mHeaderView.hashCode();
        if (mFooterView != null)
            if (position - mData.size() - 1 >= 0)
                return mFooterView.hashCode();
        return super.getItemViewType(position);
    }

    /**
     * 添加头部
     *
     * @param id
     */
    public void addHeader(@LayoutRes int id) {
        mHeaderView = LayoutInflater.from(mContext).inflate(id, null, false);
    }

    /**
     * 添加底部
     *
     * @param id
     */
    public void addFooter(@LayoutRes int id) {
        mFooterView = LayoutInflater.from(mContext).inflate(id, null, false);
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        mItemClickListener = l;
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(View v, int position);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener l) {
        onItemLongClickListener = l;
    }

    private static class SpViewHolder extends BaseViewHolder {

        public SpViewHolder(View itemView) {
            super(itemView);
        }
    }
}
