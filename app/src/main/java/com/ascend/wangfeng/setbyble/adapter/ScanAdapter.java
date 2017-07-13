package com.ascend.wangfeng.setbyble.adapter;

import android.bluetooth.BluetoothDevice;
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

public class ScanAdapter extends RecyclerView.Adapter<ScanAdapter.ViewHolder> {
   private ArrayList<BluetoothDevice> mDevices;
    OnItemListener mOnItemListener;

    public ScanAdapter(ArrayList<BluetoothDevice> devices) {
        mDevices = devices;
    }
    public void setOnItemListener(OnItemListener listener){
        this.mOnItemListener =listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scan,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BluetoothDevice device =mDevices.get(position);
        holder.name.setText(device.getName()+"");
        holder.mac.setText(device.getAddress()+"");
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mDevices.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name ;
        TextView mac;
        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            mac = (TextView) itemView.findViewById(R.id.mac);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mOnItemListener!=null)mOnItemListener.onClickListener(view, (Integer) view.getTag());
        }
    }
    public interface OnItemListener{
        void onClickListener(View view,int position);
    }
}
