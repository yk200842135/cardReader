package cn.com.reformer.poi;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.com.reformer.poi.bean.request.RequestSearchDevice;
import cn.com.reformer.poi.event.base.EventCode;
import cn.com.reformer.poi.event.SearchDeviceEvent;
import cn.com.reformer.poi.global.Constant;
import cn.com.reformer.poi.global.SysApplication;
import cn.com.reformer.poi.http.KHttp;
import cn.com.reformer.poi.util.NetworkUtils;
import cn.com.reformer.poi.util.SPUtils;
import cn.com.reformer.poi.view.JustifyTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016-11-02.
 */
public class ConfigActivity extends Activity {
    private static final String TAG = "ConfigInfo";

    ImageButton ibtn_back;
    TextView tv_title;
    EditText edit_connect_ip;
    EditText edit_connect_port;
    Button btn_test;
    Button btn_confirm;
    Button btn_cancel;
    LinearLayout ll_body;
    JustifyTextView jtv_deviceId;
    long lastSearchDeviceTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        initView();
        initData();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSearchDeviceEvent(SearchDeviceEvent event) {
        if (event == null)
            return;
        if (event.code == EventCode.SUCCESS && event.responseBase != null) {
            Toast.makeText(ConfigActivity.this, event.responseBase.getResultInfo(), Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(ConfigActivity.this, event.message, Toast.LENGTH_SHORT).show();
        }
    }

    private void initView(){
        ibtn_back = (ImageButton) findViewById(R.id.ibtn_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        ll_body = (LinearLayout) findViewById(R.id.ll_body);
        btn_test = (Button) findViewById(R.id.btn_test);
        edit_connect_ip = (EditText) findViewById(R.id.edit_connect_ip);
        edit_connect_port = (EditText) findViewById(R.id.edit_connect_port);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        btn_cancel =(Button) findViewById(R.id.btn_cancel);
        jtv_deviceId = (JustifyTextView) findViewById(R.id.jtv_deviceId);
        setListener();
    }

    private void setListener() {
        ll_body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(ConfigActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
        ibtn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(ConfigActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                String ip = edit_connect_ip.getText().toString().trim();
                boolean matches = Pattern.matches("((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))", ip);
                if (matches) {
                    KHttp.IP = ip;
                    SPUtils.setConnectIp(ip);
                    String port = edit_connect_port.getText().toString().trim();
                    if (port.length() != 0
                            && Integer.parseInt(port) >= 0
                            && Integer.parseInt(port) <= 65535){
                        KHttp.PORT = port;
                        SPUtils.setConnectPort(port);
                        finish();
                    } else{
                        Toast.makeText(ConfigActivity.this, "invalid port", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(ConfigActivity.this, "invalid IP", Toast.LENGTH_SHORT).show();
                }

            }
        });
        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip = edit_connect_ip.getText().toString().trim();
                boolean matches = Pattern.matches("((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))", ip);
                if (matches) {
                    KHttp.IP = ip;
                    SPUtils.setConnectIp(ip);
                    String port = edit_connect_port.getText().toString().trim();
                    if (port.length() != 0
                            && Integer.parseInt(port) >= 0
                            && Integer.parseInt(port) <= 65535){
                        KHttp.PORT = port;
                        SPUtils.setConnectPort(port);
                        if (NetworkUtils.checkNetState(ConfigActivity.this)) {
                            searchDevice();
                        } else {
                            Toast.makeText(ConfigActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                        }
                    } else{
                        Toast.makeText(ConfigActivity.this, "invalid port", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(ConfigActivity.this, "invalid IP", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        tv_title.setText(TAG);
        StringBuffer stringBuffer = new StringBuffer()
                .append("Device ID:")
                .append(Constant.DeviceId);
        jtv_deviceId.setText(stringBuffer.toString());
        edit_connect_ip.setText(SPUtils.getConnectIp());
        edit_connect_port.setText(SPUtils.getConnectPort());
        Editable etext = edit_connect_ip.getText();
        Selection.setSelection(etext, etext.length());
    }

    private void searchDevice(){
        long curTime = System.currentTimeMillis();
        if ((curTime - lastSearchDeviceTime) > 2000 ) {
            lastSearchDeviceTime = curTime;
            RequestSearchDevice requestSearchDevice = new RequestSearchDevice();
            requestSearchDevice.setSerial(Constant.DeviceId);
            KHttp.searchDevice(ConfigActivity.this, requestSearchDevice);
        }
    }
}
