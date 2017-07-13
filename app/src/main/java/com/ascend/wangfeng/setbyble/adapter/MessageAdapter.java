package com.ascend.wangfeng.setbyble.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ascend.wangfeng.setbyble.R;

import java.util.ArrayList;

/**
 * Created by fengye on 2017/6/22.
 * email 1040441325@qq.com
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{
    private ArrayList<String> mMessages;

    public MessageAdapter(ArrayList<String> messages) {
        mMessages = messages;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.message.setText(mMessages.get(position)+"");
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView message;
        public ViewHolder(View itemView) {
            super(itemView);
            message = (TextView) itemView.findViewById(R.id.message);
        }
    }

}
