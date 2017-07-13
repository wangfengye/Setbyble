package com.ascend.wangfeng.setbyble.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ascend.wangfeng.setbyble.R;
import com.ascend.wangfeng.setbyble.SetBean;

import java.util.ArrayList;

/**
 * Created by fengye on 2017/6/22.
 * email 1040441325@qq.com
 */

public class SetAdapter extends RecyclerView.Adapter<SetAdapter.ViewHolder> {
    private ArrayList<SetBean> mSetBeen;
    private OnItemListener mListener;

    public SetAdapter(ArrayList<SetBean> setBeen) {
        mSetBeen = setBeen;
    }
    public void setListener(OnItemListener listener){
        mListener =listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_set,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        SetBean setBean = mSetBeen.get(position);
        holder.hd.setText(setBean.getHd()+"");
        holder.bd.setText(setBean.getBd()+"");

    }

    @Override
    public int getItemCount() {
        return mSetBeen.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView hd;
        TextView bd;
        public ViewHolder(View itemView) {
            super(itemView);
            hd = (TextView) itemView.findViewById(R.id.hd);
            bd = (TextView) itemView.findViewById(R.id.bd);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mListener!=null)mListener.onItemClickListener(view, (Integer) view.getTag());
        }
    }
   public interface  OnItemListener{void onItemClickListener(View view,int position);}

}
