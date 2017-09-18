package com.ascend.wangfeng.setbyble.adapter;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ascend.wangfeng.setbyble.MainActivity;
import com.ascend.wangfeng.setbyble.R;
import com.ascend.wangfeng.setbyble.SetBean;

import java.util.ArrayList;

/**
 * Created by fengye on 2017/6/22.
 * email 1040441325@qq.com
 */

public class SetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<SetBean> mSetBeen;
    private OnItemListener mListener;
    private MainActivity mActivity;

    public SetAdapter(MainActivity mainActivity, ArrayList<SetBean> setBeen) {
        mSetBeen = setBeen;
        this.mActivity = mainActivity;
    }

    public void setListener(OnItemListener listener) {
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == SetBean.TYPE_ONE_TO_ONE) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_set, parent, false);
            return new ViewHolder(itemView);
        } else {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_set_one_to_more, parent, false);
            return new MoreViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        holder.itemView.setTag(position);
        SetBean setBean = mSetBeen.get(position);
        if (holder instanceof ViewHolder) {
            ((ViewHolder) holder).hd.setText(setBean.getHd() + "");
            ((ViewHolder) holder).bd.setText(setBean.getBd() + "");
            ((ViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final EditText edit = new EditText(mActivity);
                    new AlertDialog.Builder(mActivity)
                            .setTitle("请输入")
                            .setView(edit)
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface anInterface, int i) {
                                    String value = "SET " + mSetBeen.get(position).getHd() + ":"
                                            + edit.getText().toString();
                                    mActivity.sendData(value);
                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();
                }
            });

        } else if (holder instanceof MoreViewHolder) {
            ((MoreViewHolder) holder).hd.setText(setBean.getHd());
            ((MoreViewHolder) holder).bd.setAdapter(new SetItemAdapter(setBean.getList(), new SetItemAdapter.OnItemListener() {
                @Override
                public void onItemClickListener(View view, final String message) {
                    final EditText edit = new EditText(mActivity);
                    new AlertDialog.Builder(mActivity)
                            .setTitle("请输入")
                            .setView(edit)
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface anInterface, int i) {
                                    String editText = edit.getText().toString();
                                    if (checkEdit(editText, position)) {
                                        if (editText != null && editText.length() > 0) {
                                            String senMessage = null;
                                            if (message != null && message.length() > 0) {
                                                senMessage = editText + "," + message;
                                            } else {
                                                senMessage = editText;
                                            }

                                            String value = "SET " + mSetBeen.get(position).getHd() + ":"
                                                    + senMessage;
                                            Log.i("TEST", "onClick: " + value);
                                            mActivity.sendData(value);
                                        }

                                    }else {
                                        Toast.makeText(mActivity, "输入不合法", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            })
                            .setNegativeButton("取消", null)
                            .show();
                }

                @Override
                public void onItemLongClickListener(View view, final String message) {
                    new AlertDialog.Builder(mActivity)
                            .setTitle("是否删除")
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface anInterface, int i) {
                                    String value = "SET " + mSetBeen.get(position).getHd() + ":"
                                            + message;
                                    mActivity.sendData(value);
                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface anInterface, int i) {

                        }
                    }).show();

                }
            }));
        }

    }

    /**
     * 检查输入合法性
     *
     * @param text
     */
    private boolean checkEdit(String text, int position) {
        if (mSetBeen.get(position).getHd().equals("AMAC")) {
            if (text.length() != 12) {
                return false;
            } else {
                return true;
            }
        }
        if (mSetBeen.get(position).getHd().equals("APHONE")) {
            if (text.length() > 13) {
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    @Override
    public int getItemCount() {
        return mSetBeen.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mSetBeen.get(position).getType();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
            if (mListener != null) mListener.onItemClickListener(view, (Integer) view.getTag());
        }
    }

    //一对多的ViewHolder
    class MoreViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView hd;
        RecyclerView bd;

        public MoreViewHolder(View itemView) {
            super(itemView);
            hd = (TextView) itemView.findViewById(R.id.hd);
            bd = (RecyclerView) itemView.findViewById(R.id.list);
            LinearLayoutManager manager = new LinearLayoutManager(itemView.getContext());
            manager.setAutoMeasureEnabled(true);
            bd.setLayoutManager(manager);
            bd.addItemDecoration(new MyItemDecoration(itemView.getContext(), LinearLayoutManager.HORIZONTAL));
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mListener != null) mListener.onItemClickListener(view, (Integer) view.getTag());
        }
    }

    public interface OnItemListener {
        void onItemClickListener(View view, int position);
    }

}
