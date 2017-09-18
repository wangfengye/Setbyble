package com.ascend.wangfeng.setbyble.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ascend.wangfeng.setbyble.R;

/**
 * Created by fengye on 2017/9/15.
 * email 1040441325@qq.com
 */

public class SetItemAdapter extends RecyclerView.Adapter<SetItemAdapter.ViewHolder> {
    private String[] list;
    private OnItemListener mListener;

    public SetItemAdapter(String[] list,OnItemListener listener) {
        this.list = list;
        mListener =listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_set_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(list[position]);
        holder.itemView.setTag(position);

    }

    @Override
    public int getItemCount() {
        return list.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {
        TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.text);
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            int position = (int) view.getTag();
            list[position] = "";//删除该条记录
            if (mListener!=null){
                mListener.onItemLongClickListener(view,getString());
            }
            return true;
        }

        @Override
        public void onClick(View view) {
            if (list.length>=5){
                Toast.makeText(itemView.getContext(), "最多设置五个", Toast.LENGTH_SHORT).show();
                return;
            }
            int position = (int) view.getTag();
            if (mListener!=null){
                mListener.onItemClickListener(view,getString());
            }
        }
    }

    private String getString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < list.length; i++) {
            if (!list[i].equals("")) {
                if (builder.length()>0){
                builder.append(",");}
                builder.append(list[i]);
            }
        }
        return builder.toString();
    }
    public interface OnItemListener {
        void onItemClickListener(View view,String message);
        void onItemLongClickListener(View view,String message);
    }
}
