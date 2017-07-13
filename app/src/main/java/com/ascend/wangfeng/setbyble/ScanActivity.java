package com.ascend.wangfeng.setbyble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.ascend.wangfeng.setbyble.adapter.MyItemDecoration;
import com.ascend.wangfeng.setbyble.adapter.ScanAdapter;
import com.ascend.wangfeng.setbyble.ble.BluetoothLeClass;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScanActivity extends AppCompatActivity {

    public static final String TAG = ScanActivity.class.getName();
    public static final int SCAN_PERIOD = 10000;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.scans)
    RecyclerView mScans;
    @BindView(R.id.activity_scan)
    RelativeLayout mActivityScan;

    private ArrayList<BluetoothDevice> mDevices;
    private ScanAdapter adapter;
    private Boolean isScan;
    private BluetoothAdapter mBleAdapter;
    private BluetoothLeClass mBLE;
    private Handler mHandler;
    /*扫描 device的回调*/
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int i, byte[] bytes) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mDevices.contains(device)) return;
                    mDevices.add(device);
                    adapter.notifyDataSetChanged();
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        ButterKnife.bind(this);
        initData();
        initView();
        initBle();
    }

    private void initBle() {
        BluetoothManager manager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBleAdapter = manager.getAdapter();
        if (mBleAdapter == null) {
            Snackbar.make(mActivityScan, "不支持蓝牙", Snackbar.LENGTH_SHORT).show();
        }
        mBleAdapter.enable();
        mBLE = new BluetoothLeClass(this);
        if (!mBLE.initialize()) {
            Log.e(TAG, "Unable to initialize Bluetooth");
            finish();
        }
        scanDevice(true);
    }

    private void scanDevice(boolean enable) {
        if (enable) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isScan = false;
                    mBleAdapter.stopLeScan(mLeScanCallback);
                    invalidateOptionsMenu();
                }
            }, SCAN_PERIOD);
            isScan = true;
            mBleAdapter.startLeScan(mLeScanCallback);
        } else {
            isScan = false;
            mBleAdapter.stopLeScan(mLeScanCallback);
        }
        invalidateOptionsMenu();
    }

    private void initView() {
        mToolbar.setTitle("搜索设备");

        setSupportActionBar(mToolbar);
        mScans.setAdapter(adapter);
        mScans.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        mScans.addItemDecoration(new MyItemDecoration(this, LinearLayoutManager.HORIZONTAL));
    }

    private void initData() {
        mHandler = new Handler();
        mDevices = new ArrayList<>();
        adapter = new ScanAdapter(mDevices);
        adapter.setOnItemListener(new ScanAdapter.OnItemListener() {
            @Override
            public void onClickListener(View view, int position) {
                BluetoothDevice device = mDevices.get(position);
                Intent intent = new Intent(ScanActivity.this, MainActivity.class);
                intent.putExtra("address", device.getAddress());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.scan_set, menu);
        if (isScan) {
            menu.findItem(R.id.menu_search).setTitle("正在搜索");
        } else {
            menu.findItem(R.id.menu_search).setTitle("搜索");
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mDevices.clear();
        scanDevice(!isScan);
        invalidateOptionsMenu();
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
