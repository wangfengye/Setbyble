package com.ascend.wangfeng.setbyble.adapter;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;

import com.ascend.wangfeng.setbyble.MainActivity;
import com.ascend.wangfeng.setbyble.R;
import com.ascend.wangfeng.setbyble.SetBean;
import com.ascend.wangfeng.setbyble.util.StringUtil;

import java.util.ArrayList;

/**
 * Created by fengye on 2017/6/22.
 * email 1040441325@qq.com
 */

public class SetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<SetBean> mSetBeen;
    private MainActivity mActivity;

    public SetAdapter(MainActivity mainActivity, ArrayList<SetBean> setBeen) {
        mSetBeen = setBeen;
        this.mActivity = mainActivity;
    }

    /**
     * 更新数据
     */
    public void update(ArrayList<SetBean> setBeen) {
        setDataType();
        notifyDataSetChanged();
    }


    private void setDataType() {
        for (SetBean setBean : mSetBeen) {
            switch (setBean.getHd()) {
                case "MAC":
                    setBean.setType(SetConstant.READ);
                    break;
                case "DEV":
                    setBean.setType(SetConstant.READ);
                    break;
                case "UPLD":
                    setBean.setType(SetConstant.SET_BOOlEAN);
                    break;
                case "TVAL":
                    setBean.setType(SetConstant.SET_INT);
                    break;
                case "TCP":
                    setBean.setType(SetConstant.SET_TCP);
                    break;
                case "AMAC":
                    setBean.setType(SetConstant.SET_MANY);
                    setBean.setList(stringTolist(setBean.getBd()));
                    break;
                case "APHONE":
                    setBean.setType(SetConstant.SET_MANY);
                    setBean.setList(stringTolist(setBean.getBd()));
                    break;
            }
        }
    }

    private String[] stringTolist(String s) {
        String[] strings=s.split(",");
        if (strings.length==1&&strings[0]==""){
            strings=new String[0];
        }
        return strings;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case SetConstant.READ:
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_set, parent, false));
            case SetConstant.SET_BOOlEAN:
                return new BooleanViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_set_boolean, parent, false));
            case SetConstant.SET_INT:
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_set, parent, false));
            case SetConstant.SET_TCP:
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_set, parent, false));
            case SetConstant.SET_MANY:
                return new MoreViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_set_one_to_more, parent, false));
            default:
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_set, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        holder.itemView.setTag(position);
        final SetBean setBean = mSetBeen.get(position);
        if (holder instanceof ViewHolder) {
            ((ViewHolder) holder).hd.setText(StringUtil.toChinese(setBean.getHd(), mActivity));
            ((ViewHolder) holder).bd.setText(setBean.getBd() + "");
            if (setBean.getHd().equals("TVAL")){
                ((ViewHolder) holder).bd.setText(setBean.getBd() + " 分钟");
            }
            addDialog(setBean, holder.itemView);

        } else if (holder instanceof MoreViewHolder) {
            ((MoreViewHolder) holder).hd.setText(StringUtil.toChinese(setBean.getHd(), mActivity));
            ((MoreViewHolder) holder).bd.setAdapter(new SetItemAdapter(setBean.getList(),mActivity,setBean.getHd()));
        } else if (holder instanceof BooleanViewHolder) {
            ((BooleanViewHolder) holder).hd.setText(StringUtil.toChinese(setBean.getHd(), mActivity));
            ((BooleanViewHolder) holder).bdSwitch.setChecked(setBean.getBd().equals(SetBean.YES) ? true : false);
            ((BooleanViewHolder) holder).bdSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton button, boolean b) {
                    if (b) {
                        mActivity.sendData(setBean.getHd(), SetBean.YES);
                    } else {
                        mActivity.sendData(setBean.getHd(), SetBean.NO);
                    }
                }
            });
        } else if (holder instanceof IntViewHolder) {
            ((IntViewHolder) holder).hd.setText(StringUtil.toChinese(setBean.getHd(), mActivity));
            int value = 0;
            try {
                value = Integer.valueOf(setBean.getBd());
            } catch (NumberFormatException e) {

            }
            ((IntViewHolder) holder).bdNp.setValue(value);
            //设置选择器范围
            ((IntViewHolder) holder).bdNp.setMaxValue(5);
            ((IntViewHolder) holder).bdNp.setMinValue(5 * 60);

        }

    }

    /**
     *
     */
    private void addDialog(SetBean setBean, View view) {
        switch (setBean.getType()) {
            case SetConstant.SET_INT:
                setNumPickerDialog(setBean, view);
                break;
            case SetConstant.SET_TCP:
                setTcpDialog(setBean, view);
                break;
        }

    }

    /**
     * tcp配置点击出现dialog
     *
     * @param bean
     * @param view
     */
    private void setTcpDialog(final SetBean bean, View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] params = bean.getBd().split(",");
                final AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setTitle(StringUtil.toChinese(bean.getHd(), mActivity));
                final View layout = mActivity.getLayoutInflater().inflate(R.layout.dialog_tcp
                        , null);
                final EditText ipEdit = (EditText) layout.findViewById(R.id.ip);
                final EditText portEdit = (EditText) layout.findViewById(R.id.port);
                ipEdit.setText(params[0]);
                portEdit.setText(params[1]);
                builder.setView(layout);
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface anInterface, int i) {
                        String ipAfter = ipEdit.getText().toString();
                        String portAfter = portEdit.getText().toString();
                        mActivity.sendData(bean.getHd(), ipAfter + "," + portAfter);
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create();
                builder.show();
            }
        });
    }

    public void setNumPickerDialog(final SetBean setBean, View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int value = 5;
                try {
                    value = Integer.valueOf(setBean.getBd());
                } catch (NumberFormatException e) {
                }
                final AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setTitle(StringUtil.toChinese(setBean.getHd(), mActivity));
                final View layout = mActivity.getLayoutInflater().inflate(R.layout.dialog_number_picker
                        , null);
                final NumberPicker np = (NumberPicker) layout.findViewById(R.id.np);
                //设置选择器范围
                np.setMaxValue(60);
                np.setMinValue(1);
                builder.setView(layout);
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface anInterface, int i) {
                        String value = String.valueOf(np.getValue());
                        mActivity.sendData(setBean.getHd(), value);
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create();
                np.setValue(value);
                builder.show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return mSetBeen.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mSetBeen.get(position).getType();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView hd;
        TextView bd;

        public ViewHolder(View itemView) {
            super(itemView);
            hd = (TextView) itemView.findViewById(R.id.hd);
            bd = (TextView) itemView.findViewById(R.id.bd);
        }
    }

    /**
     * boolean
     */
    class BooleanViewHolder extends RecyclerView.ViewHolder {
        TextView hd;
        Switch bdSwitch;

        public BooleanViewHolder(View itemView) {
            super(itemView);
            hd = (TextView) itemView.findViewById(R.id.hd);
            bdSwitch = (Switch) itemView.findViewById(R.id.switch_bd);
        }
    }

    /**
     * int
     */
    class IntViewHolder extends RecyclerView.ViewHolder {
        TextView hd;
        NumberPicker bdNp;

        public IntViewHolder(View itemView) {
            super(itemView);
            hd = (TextView) itemView.findViewById(R.id.hd);
            bdNp = (NumberPicker) itemView.findViewById(R.id.np_bd);
        }
    }

    //一对多的ViewHolder
    class MoreViewHolder extends RecyclerView.ViewHolder {
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
        }
    }


}
