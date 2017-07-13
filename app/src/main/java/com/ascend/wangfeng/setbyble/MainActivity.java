package com.ascend.wangfeng.setbyble;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.ascend.wangfeng.setbyble.adapter.MessageAdapter;
import com.ascend.wangfeng.setbyble.adapter.MyItemDecoration;
import com.ascend.wangfeng.setbyble.adapter.SetAdapter;
import com.ascend.wangfeng.setbyble.ble.BluetoothLeClass;
import com.ascend.wangfeng.setbyble.ble.Utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();
    private final static String UUID_KEY_DATA = "0000ffe1-0000-1000-8000-00805f9b34fb";
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.messages)
    RecyclerView mMessages;
    @BindView(R.id.sets)
    RecyclerView mSets;
    @BindView(R.id.activity_main)
    CoordinatorLayout mActivityMain;


    private ArrayList<String> mMessagesData;
    private ArrayList<SetBean> mSetBeen;
    private String address;
    private MessageAdapter mMessageAdapter;
    private SetAdapter mSetAdapter;
    private String value = "";//接收的字符串
    private String separator = "\r\n";//分隔符
    private boolean isConnected;
    private BluetoothLeClass mBLE;
    private BluetoothAdapter mBleAdapter;
    private BluetoothGattCharacteristic gattCharacteristic;


    private BluetoothLeClass.OnServiceDiscoverListener mOnServiceDiscover = new BluetoothLeClass.OnServiceDiscoverListener() {
        @Override
        public void onServiceDiscover(BluetoothGatt gatt) {
            displayGattSercices(gatt.getServices());
        }
    };
    private BluetoothLeClass.OnDataAvailableListener mOnDataAvailable = new BluetoothLeClass.OnDataAvailableListener() {
        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                // final String request =Utils.bytesToHexString(characteristic.getValue());
                String request = null;
                try {
                    request = new String(characteristic.getValue(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (request == null) return;
                final String finalRequest = request;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "run: " + finalRequest);
                        //处理数据
                        formatRequest(finalRequest);
                    }
                });
            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            Log.i(TAG, "onCharWrite " + gatt.getDevice().getName()
                    + " write "
                    + characteristic.getUuid().toString()
                    + " -> "
                    + new String(characteristic.getValue()));
        }
    };


    private BluetoothLeClass.OnConnectListener mOnConnectListener = new BluetoothLeClass.OnConnectListener() {
        @Override
        public void onConnect(BluetoothGatt gatt) {
            isConnected = true;
            invalidateOptionsMenu();
        }
    };
    private BluetoothLeClass.OnDisconnectListener mOnDisconnectListener = new BluetoothLeClass.OnDisconnectListener() {
        @Override
        public void onDisconnect(BluetoothGatt gatt) {
            isConnected = false;
            invalidateOptionsMenu();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();
        initView();
        initBle();
    }

    private void initView() {
        mToolbar.setTitle("配置");

        setSupportActionBar(mToolbar);
        mMessageAdapter = new MessageAdapter(mMessagesData);
        mMessages.setAdapter(mMessageAdapter);
        mMessages.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        mSetAdapter = new SetAdapter(mSetBeen);

        mSetAdapter.setListener(new SetAdapter.OnItemListener() {
            @Override
            public void onItemClickListener(View view, final int position) {
                Log.i(TAG, "onItemClickListener: ");
                final EditText edit = new EditText(MainActivity.this);
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("请输入")
                        .setView(edit)
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface anInterface, int i) {
                                String value = "SET " + mSetBeen.get(position).getHd() + ":"
                                        + edit.getText().toString();
                                sendData(value);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
        mSets.setAdapter(mSetAdapter);
        mSets.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        mSets.addItemDecoration(new MyItemDecoration(this, LinearLayoutManager.HORIZONTAL));
    }

    private void sendData(String value) {
        if (gattCharacteristic != null) {
            gattCharacteristic.setValue(value);
            mBLE.writeCharacteristic(gattCharacteristic);
        }
    }

    private void initBle() {
        BluetoothManager manager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBleAdapter = manager.getAdapter();
        if (mBleAdapter == null) {
            Snackbar.make(mActivityMain, "不支持蓝牙", Snackbar.LENGTH_SHORT).show();
        }
        mBleAdapter.enable();
        mBLE = new BluetoothLeClass(this);
        if (!mBLE.initialize()) {
            Log.e(TAG, "Unable to initialize Bluetooth");
            finish();
        }
        //BLE连接时回调
        mBLE.setOnConnectListener(mOnConnectListener);
        //BLE断开时回调
        mBLE.setOnDisconnectListener(mOnDisconnectListener);
        //发现BLE终端的Service时回调
        mBLE.setOnServiceDiscoverListener(mOnServiceDiscover);
        //收到BLE终端数据交互的事件
        mBLE.setOnDataAvailableListener(mOnDataAvailable);
        mBLE.connect(address);

    }


    private void initData() {
        mMessagesData = new ArrayList<>();
        mSetBeen = new ArrayList<>();
        address = getIntent().getStringExtra("address");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if (isConnected) {
            menu.findItem(R.id.menu_connected).setVisible(true);
            menu.findItem(R.id.menu_disconnected).setVisible(false);
        } else {
            menu.findItem(R.id.menu_connected).setVisible(false);
            menu.findItem(R.id.menu_disconnected).setVisible(true);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mBLE.connect(address);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBLE != null)
            mBLE.close();
    }

    private void displayGattSercices(List<BluetoothGattService> services) {
        if (services == null) return;
        for (BluetoothGattService gattService : services) {
            //-----Service的字段信息-----//
            int type = gattService.getType();
            Log.e(TAG, "-->service type:" + Utils.getServiceType(type));
            Log.e(TAG, "-->includedServices size:" + gattService.getIncludedServices().size());
            Log.e(TAG, "-->service uuid:" + gattService.getUuid());

            //-----Characteristics的字段信息-----//
            List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
            for (final BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                Log.e(TAG, "---->char uuid:" + gattCharacteristic.getUuid());

                int permission = gattCharacteristic.getPermissions();
                Log.e(TAG, "---->char permission:" + Utils.getCharPermission(permission));

                int property = gattCharacteristic.getProperties();
                Log.e(TAG, "---->char property:" + Utils.getCharPropertie(property));

                byte[] data = gattCharacteristic.getValue();
                if (data != null && data.length > 0) {
                    Log.e(TAG, "---->char value:" + new String(data));
                }
                if (gattCharacteristic.getUuid().toString().equals(UUID_KEY_DATA)) {
                    this.gattCharacteristic = gattCharacteristic;
                    mBLE.setCharacteristicNotification(gattCharacteristic, true);
                    sendData("GET ALL");
                }
                //-----Descriptors的字段信息-----//
                List<BluetoothGattDescriptor> gattDescriptors = gattCharacteristic.getDescriptors();
                for (BluetoothGattDescriptor gattDescriptor : gattDescriptors) {
                    Log.e(TAG, "-------->desc uuid:" + gattDescriptor.getUuid());
                    int descPermission = gattDescriptor.getPermissions();
                    Log.e(TAG, "-------->desc permission:" + Utils.getDescPermission(descPermission));

                    byte[] desData = gattDescriptor.getValue();
                    if (desData != null && desData.length > 0) {
                        Log.e(TAG, "-------->desc value:" + new String(desData));
                    }
                }
            }
        }
    }
    private int hasParam(String param) {
        for (int i = 0; i < mSetBeen.size(); i++) {
            SetBean bean = mSetBeen.get(i);
            if (param.equals(bean.getHd())) return i;
        }
        return -1;
    }
    private void formatRequest(String request) {
        value += request;
        String subValue = "";//截取第一段信息;
        int end = 0;
        if (end == -1) return;
        while (end == 0) {
            end = value.indexOf(separator);
            if (end == -1) return;
            subValue = value.substring(0, end);
            value = value.substring(end + separator.length(), value.length());
        }
        Log.i(TAG, "formatRequest: "+subValue);
        char[] chars = subValue.toCharArray();
        if (chars.length > 0) {
            int firstChar = chars[0];
            if (firstChar >= 65 && firstChar <= 90) {
                //配置信息
                String[] params = subValue.split(":");
                if (params.length<2)return;
                int position = hasParam(params[0]);
                if (position == -1) {
                    mSetBeen.add(new SetBean(params[0], params[1]));
                } else {
                    mSetBeen.get(position).setBd(params[1]);
                }
                mSetAdapter.notifyDataSetChanged();
            } else {
                //日志
                mMessagesData.add(subValue);
                if (mMessagesData.size() >= 100) mMessagesData.remove(0);
                mMessageAdapter.notifyDataSetChanged();
                mMessages.scrollToPosition(mMessagesData.size()-1);

            }
        }

    }

}
