package com.ascend.wangfeng.setbyble.adapter;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ascend.wangfeng.setbyble.MainActivity;
import com.ascend.wangfeng.setbyble.R;
import com.ascend.wangfeng.setbyble.util.RegularExprssion;
import com.ascend.wangfeng.setbyble.util.StringUtil;

/**
 * Created by fengye on 2017/9/15.
 * email 1040441325@qq.com
 */

public class SetItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_BUTTON = 2;
    private String[] list;
    private MainActivity mActivity;
    private String mHd;

    public SetItemAdapter(String[] list, MainActivity mainActivity, String hd) {
        this.list = list;
        this.mActivity = mainActivity;
        mHd = hd;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_NORMAL:
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_set_item, parent, false));
            case TYPE_BUTTON:
                return new ButtonViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_button, parent, false));
            default:
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_set_item, parent, false));
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ButtonViewHolder) {
            ((ButtonViewHolder) holder).mBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final EditText editText = new EditText(mActivity);
                    if (mHd.equals("APHONE")) {
                        editText.setInputType(InputType.TYPE_CLASS_PHONE);
                    }
                    final AlertDialog.Builder editDialog =
                            new AlertDialog.Builder(mActivity);
                    editDialog.setTitle(StringUtil.toChinese(mHd,mActivity) + "(添加)").setView(editText);
                    editDialog.setNegativeButton("取消", null);
                    editDialog.setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String editAfter = editText.getText().toString();
                                    if (isMatch(editAfter,mHd)){
                                        mActivity.sendData(mHd, getString() + "," + editAfter);
                                    }else {
                                        Toast.makeText(mActivity, R.string.input_error, Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }).show();
                }
            });
        } else {
            ((ViewHolder) holder).mTextView.setText(list[position]);
            holder.itemView.setTag(position);
            ((ViewHolder) holder).mDeleteImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder deleteBuilder =new AlertDialog.Builder(mActivity);
                    deleteBuilder.setTitle("确认删除吗")
                            .setMessage(StringUtil.toChinese(mHd,mActivity)+":　"+list[position])
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface anInterface, int i) {
                                    list[position] = "";
                                    mActivity.sendData(mHd, getString());
                                }
                            }).setNegativeButton("取消",null)
                            .show();

                }
            });
            /**
             * 编辑页面
             */
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final EditText editText = new EditText(mActivity);
                    editText.setText(list[position]);
                    if (mHd.equals("APHONE")) {
                        editText.setInputType(InputType.TYPE_CLASS_PHONE);
                    }
                    final AlertDialog.Builder editDialog =
                            new AlertDialog.Builder(mActivity);
                    editDialog.setTitle(StringUtil.toChinese(mHd,mActivity)+ "(修改)").setView(editText);
                    editDialog.setNegativeButton("取消", null);
                    editDialog.setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String editAfter = editText.getText().toString();
                                    if (isMatch(editAfter,mHd)){
                                    list[position] = editAfter;
                                    mActivity.sendData(mHd, getString());}else {
                                        Toast.makeText(mActivity, R.string.input_error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).show();

                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if (list.length == 5) {
            return list.length;
        } else {
            return list.length + 1;
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (list.length == position) {
            return TYPE_BUTTON;
        }
        return TYPE_NORMAL;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        ImageView mDeleteImg;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.text);
            mDeleteImg = (ImageView) itemView.findViewById(R.id.delete);
        }
    }

    class ButtonViewHolder extends RecyclerView.ViewHolder {
        Button mBtn;

        public ButtonViewHolder(View itemView) {
            super(itemView);
            mBtn = (Button) itemView.findViewById(R.id.btn);
        }
    }

    private String getString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < list.length; i++) {
            if (!list[i].equals("")) {
                if (builder.length() > 0) {
                    builder.append(",");
                }
                builder.append(list[i]);
            }
        }
        return builder.toString();
    }

public boolean isMatch(String value,String hd){
    switch (hd){
        case "AMAC":
            return RegularExprssion.isMac(value);
        case "APHONE":
            return RegularExprssion.isPhone(value);
        default:
            return true;

    }
}


}
