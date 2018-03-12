package cn.com.reformer.poi;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import cn.com.reformer.poi.bean.request.RequestLogin;
import cn.com.reformer.poi.dbservice.LoginRecordDbs;
import cn.com.reformer.poi.dialog.LoginningDialog;
import cn.com.reformer.poi.event.GetNewApkEvent;
import cn.com.reformer.poi.event.LoginEvent;
import cn.com.reformer.poi.event.base.EventCode;
import cn.com.reformer.poi.global.Constant;
import cn.com.reformer.poi.http.KHttp;
import cn.com.reformer.poi.service.DownloadApkService;
import cn.com.reformer.poi.util.DateUtils;
import cn.com.reformer.poi.util.GsonUtils;
import cn.com.reformer.poi.util.NetworkUtils;
import cn.com.reformer.poi.util.SPUtils;
import greendao.LoginRecord;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by Administrator on 2016-11-02.
 */
public class LoginActivity extends Activity {
    private static final String TAG = "Login";
    private static final int MIN_PASSWORD_LENGTH = 6;

    LinearLayout ll_body;
    ImageButton ibtn_setting;
    EditText edit_user;
    EditText edit_password;
    Button btn_login;
    LoginningDialog loginningDialog;
    long lastLoginTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        if (NetworkUtils.checkNetState(LoginActivity.this)) {
            KHttp.getNewApk(getApplicationContext());
        }
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

    @Override
    protected void onDestroy() {
        if (loginningDialog != null && loginningDialog.isShowing()) {
            loginningDialog.dismiss();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private void initView(){
        ll_body = (LinearLayout) findViewById(R.id.ll_body);
        ibtn_setting = (ImageButton) findViewById(R.id.ibtn_setting);
        edit_user = (EditText) findViewById(R.id.edit_user);
        edit_password = (EditText) findViewById(R.id.edit_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        setListener();
    }

    private void setListener() {
        ll_body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(LoginActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
        edit_user.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String editable = edit_user.getText().toString();
                String str = stringFilter(editable.toString());
                if(!editable.equals(str)){
                    edit_user.setText(str);
                    //设置新的光标所在位置
                    edit_user.setSelection(str.length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        edit_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String editable = edit_password.getText().toString();
                String str = stringFilter(editable.toString());
                if(!editable.equals(str)){
                    edit_password.setText(str);
                    //设置新的光标所在位置
                    edit_password.setSelection(str.length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        ibtn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,ConfigActivity.class);
                startActivity(intent);
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String user = edit_user.getText().toString();
//                String password = edit_password.getText().toString();
//                if (user.length() == 0){
//                    Toast.makeText(LoginActivity.this, "Please input username", Toast.LENGTH_SHORT).show();
//                }else if (password.length() == 0){
//                    Toast.makeText(LoginActivity.this, "Please input password", Toast.LENGTH_SHORT).show();
//                }else if (password.length() < MIN_PASSWORD_LENGTH) {
//                    Toast.makeText(LoginActivity.this, "Password is too short", Toast.LENGTH_SHORT).show();
//                }else if (SPUtils.getConnectIp().equals("")){
//                    Toast.makeText(LoginActivity.this, "Please config connect IP", Toast.LENGTH_SHORT).show();
//                }else if (SPUtils.getConnectPort().equals("")){
//                    Toast.makeText(LoginActivity.this, "Please config connect port", Toast.LENGTH_SHORT).show();
//                }else if (!NetworkUtils.checkNetState(LoginActivity.this)) {
//                    Toast.makeText(LoginActivity.this, "Please check network", Toast.LENGTH_SHORT).show();
//                }else{
//                    login(user,password);
//                    loginningDialog = new LoginningDialog.Builder(LoginActivity.this).show();
//                }
                Constant.UserName = edit_user.getText().toString();
                addLoginRecord();
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetNewApkEvent(final GetNewApkEvent event){
        if (event == null)
            return;
        if (event.responseGetNewApk != null && event.responseGetNewApk.getResult() == 200) {
            new AlertDialog.Builder(this)
                    .setMessage("Find new version, need to upgrade?")
                    .setPositiveButton("upgrade",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getApplicationContext(), DownloadApkService.class);
                            intent.putExtra("url", event.responseGetNewApk.getResultInfo());
                            getApplicationContext().startService(intent);
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginEvent(LoginEvent event) {
        if (loginningDialog != null) {
            loginningDialog.dismiss();
        }
        if (event == null)
            return;
        if (event.code == EventCode.SUCCESS && event.responseBase != null) {
            if (event.responseBase.getResult() == 200){
                Constant.UserName = edit_user.getText().toString();
                addLoginRecord();
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(LoginActivity.this, event.responseBase.getResultInfo(), Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(LoginActivity.this, event.message, Toast.LENGTH_SHORT).show();
        }
    }

    private void addLoginRecord() {
        LoginRecord loginRecord;
        List<LoginRecord> lst = new ArrayList<>();
        if (!SPUtils.getLastOnlineInfo().equals("")) {
            try {
                loginRecord = GsonUtils.getInstance().fromJson(SPUtils.getLastOnlineInfo(),LoginRecord.class);
                lst.add(loginRecord);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        loginRecord = new LoginRecord();
        loginRecord.setState(0);
        loginRecord.setUser(Constant.UserName);
        loginRecord.setTime(DateUtils.getCurrentDate(DateUtils.dateFormatYMDHM));
        lst.add(loginRecord);
        LoginRecordDbs.getInstance(LoginActivity.this).saveNoteLists(lst);
    }

    private void login(String user,String password){
        long curTime = System.currentTimeMillis();
        if ((curTime - lastLoginTime) > 2000 ) {
            RequestLogin requestLogin = new RequestLogin();
            requestLogin.setSerial(Constant.DeviceId);
            requestLogin.setUsername(user);
            requestLogin.setPassword(password);
            KHttp.login(LoginActivity.this, requestLogin);
        }
    }

    private static String stringFilter(String str)throws PatternSyntaxException {
        // 只允许字母和数字
        String   regEx  =  "[^a-zA-Z0-9]";
        Pattern p   =   Pattern.compile(regEx);
        Matcher m   =   p.matcher(str);
        return   m.replaceAll("").trim();
    }
}
