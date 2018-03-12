package cn.com.reformer.poi;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.os.Handler;
import android.os.Parcelable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import cn.com.reformer.poi.bean.request.RequestBase;
import cn.com.reformer.poi.bean.request.RequestQueryEmplyMsg;
import cn.com.reformer.poi.bean.response.ResponseEmplyMsg;
import cn.com.reformer.poi.dialog.LoadingDialog;
import cn.com.reformer.poi.event.MessageEvent;
import cn.com.reformer.poi.event.QueryEmplyMsgEvent;
import cn.com.reformer.poi.event.base.EventCode;
import cn.com.reformer.poi.fragment.PersonFragment;
import cn.com.reformer.poi.fragment.WaitFragment;
import cn.com.reformer.poi.global.Constant;
import cn.com.reformer.poi.http.KHttp;
import cn.com.reformer.poi.thread.OnlineThread;
import cn.com.reformer.poi.util.ByteUtils;
import cn.com.reformer.poi.util.NetworkUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.Arrays;

public class MainActivity extends Activity implements NavigationView.OnNavigationItemSelectedListener {
    static final String TECH_ISODEP = "android.nfc.tech.IsoDep";
    static final int DELAY_RESET_MESSAGE = 3000;
    private static final String TAG = "LoyaltyCardReader";
    // AID for our loyalty card service.
    private static final String SAMPLE_LOYALTY_CARD_AID = "52464D4A01";
    // ISO-DEP command HEADER for selecting an AID.
    // Format: [Class | Instruction | Parameter 1 | Parameter 2]
    private static final String SELECT_APDU_HEADER = "00A40400";
    // "OK" status word sent in response to SELECT AID command (0x9000)
    private static final byte[] SELECT_OK_SW = {(byte) 0x90, (byte) 0x00};

    DrawerLayout drawer;
    NavigationView navigationView;
    TextView tv_title;
//    SwipeRefreshLayout swipeLayout;
    WaitFragment waitFragment;
    PersonFragment personFragment;
    NfcAdapter mNfcAdapter;
    PendingIntent mPendingIntent;
    int action; //0: 等待查询 1: 显示中
    LoadingDialog loadingDialog;
    Handler handler;

    ArrayAdapter<String> adapter;
    OnlineThread onlineThread;

    Runnable resetInputRunnable = new Runnable() {
        @Override
        public void run() {
            switchFragment(0,0);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
    public void onResume() {
        super.onResume();
        if (mNfcAdapter != null) {
            if (!mNfcAdapter.isEnabled()) {
                new AlertDialog.Builder(this).setMessage("NFC adapter is disabled.").setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent("android.settings.NFC_SETTINGS"));
                    }
                }).create().show();//The NFC is detected disabled. Please enable it in settings
            }
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
            if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(this.getIntent()
                    .getAction())) {
                processIntent(this.getIntent());
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mNfcAdapter != null) {
            stopNFC_Listener();
        }
    }

    @Override
    public void onStop() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (onlineThread != null){
            onlineThread.interrupt();
            onlineThread = null;
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public void onNewIntent(Intent intent) {
        processIntent(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onQueryEmplyMsgEvent(QueryEmplyMsgEvent event) {
        if (loadingDialog != null)
            loadingDialog.dismiss();
        if (event.code == EventCode.SUCCESS && event.responseEmplyMsg != null) {
            if (event.responseEmplyMsg.getResult() == 200){
                switchFragment(1,event.responseEmplyMsg);
            }else if (event.responseEmplyMsg.getResultInfo() != null){
                Log.e("EmplyMSG",event.responseEmplyMsg.getResultInfo());
                switchFragment(0,2);
            }else {
                Log.e("EmplyMSG","query error");
                switchFragment(0,2);
            }
        }else{
            Toast.makeText(getApplicationContext(), event.message, Toast.LENGTH_SHORT).show();
            switchFragment(0,2);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        switch (event.message) {
            case MessageEvent.MSG_WAIT:
                switchFragment(0,null,0);
                break;
        }
    }

    private void initView(){
        handler = new Handler();
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        tv_title = (TextView) findViewById(R.id.tv_title);
//        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_ly);
        loadingDialog = new LoadingDialog.Builder(MainActivity.this).create();
        TextView tv_user = (TextView)navigationView.getHeaderView(0).findViewById(R.id.tv_username);
        tv_user.setText(Constant.UserName);
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()), 0);
        setListener();
    }

    private void setListener() {
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initData() {
        action = 0;
        if (onlineThread == null){
            onlineThread = new OnlineThread();
            onlineThread.start();
        }
        tv_title.setText(R.string.main_title);
        switchFragment(0,0);
    }

    private void switchFragment(int index, int state){
        switchFragment(index,null,state);
    }

    private void switchFragment(int index, ResponseEmplyMsg responseEmplyMsg){
        switchFragment(index,responseEmplyMsg,0);
    }

    private void switchFragment(int index, ResponseEmplyMsg responseEmplyMsg, int state){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        handler.removeCallbacks(resetInputRunnable);
        switch (index) {
            case 0:
                if (waitFragment == null){
                    waitFragment = new WaitFragment();
                }
                waitFragment.setData(state);
                waitFragment.notifyView();
                if (state != 0){
                    handler.postDelayed(resetInputRunnable,DELAY_RESET_MESSAGE);
                }
                transaction.replace(R.id.content, waitFragment);
                action = 0;
                break;
            case 1:
                if (personFragment == null){
                    personFragment = new PersonFragment();
                }
                personFragment.setData(responseEmplyMsg);
                personFragment.notifyView();
                transaction.replace(R.id.content, personFragment);
                action = 1;
                break;
            default:
                break;
        }
        transaction.commitAllowingStateLoss();
    }

    private int getCardType(String[] techList){
        for (String str :techList){
            if (str.equals(TECH_ISODEP)){
                return 1;
            }
        }
        return 0;
    }

    private void processIntent(Intent intent) {
        final Tag tag = intent.getParcelableExtra(mNfcAdapter.EXTRA_TAG);
        Log.e(TAG, "New tag discovered");
        int type = getCardType(tag.getTechList());
        if (type == 0){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    byte[] ID = tag.getId();
                    String data = ByteUtils.byte2HexString(ID).toUpperCase();
                    StringBuffer stringBuffer = new StringBuffer();
                    for (String str : tag.getTechList()) {
                        stringBuffer.append(str).append("\r\n");
                    }
                    new AlertDialog.Builder(MainActivity.this)
                            .setMessage("techlist:\r\n" + stringBuffer.toString() + "Received:" + data)
                            .setNegativeButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create().show();
                }
            });
        }else if (type == 1) {
            IsoDep isoDep = IsoDep.get(tag);
            if (isoDep != null) {
                try {
                    // Connect to the remote NFC device
                    isoDep.connect();
                    // Build SELECT AID command for our loyalty card service.
                    // This command tells the remote device which service we wish to
                    // communicate with.
                    Log.e(TAG, "Requesting remote AID: " + SAMPLE_LOYALTY_CARD_AID);
                    byte[] command = BuildSelectApdu(SAMPLE_LOYALTY_CARD_AID);
                    // Send command to remote device
                    Log.e(TAG, "Sending: " + ByteArrayToHexString(command));
                    byte[] result = isoDep.transceive(command);
                    Log.e(TAG, "result: " + ByteArrayToHexString(result));
                    // If AID is successfully selected, 0x9000 is returned as the
                    // status word (last 2
                    // bytes of the result) by convention. Everything before the
                    // status word is
                    // optional payload, which is used here to hold the account
                    // number.
                    int resultLength = result.length;
                    byte[] statusWord = {result[resultLength - 2],
                            result[resultLength - 1]};
                    byte[] payload = Arrays.copyOf(result, resultLength - 2);
                    if (Arrays.equals(SELECT_OK_SW, statusWord)) {
                        // The remote NFC device will immediately respond with its
                        // stored account number
                        final String accountNumber = ByteArrayToHexString(payload);//new String(payload, "UTF-8");
                        Log.e(TAG, "Received: " + accountNumber);
                        isoDep.transceive(new byte[]{(byte)0x00,(byte)0xb0,(byte)0x00,(byte)0x00,(byte)0x04,(byte)0x01,(byte)0x56,(byte)0xA0,(byte)0x01});
                        // Inform CardReaderFragment of received account number
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                StringBuffer stringBuffer = new StringBuffer();
                                for (String str : tag.getTechList()) {
                                    stringBuffer.append(str).append("\r\n");
                                }
                                new AlertDialog.Builder(MainActivity.this)
                                        .setMessage("techlist:\r\n" + stringBuffer.toString() + "Received:" + accountNumber)
                                        .setNegativeButton("ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }).create().show();
                            }
                        });
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Error communicating with card: " + e.toString());
                }
            }
        }
    }


    /**
     * Build APDU for SELECT AID command. This command indicates which service a reader is
     * interested in communicating with. See ISO 7816-4.
     *
     * @param aid Application ID (AID) to select
     * @return APDU for SELECT AID command
     */
    public static byte[] BuildSelectApdu(String aid) {
        // Format: [CLASS | INSTRUCTION | PARAMETER 1 | PARAMETER 2 | LENGTH | DATA]
        return HexStringToByteArray(SELECT_APDU_HEADER + String.format("%02X", aid.length() / 2) + aid);
    }

    /**
     * Utility class to convert a byte array to a hexadecimal string.
     *
     * @param bytes Bytes to convert
     * @return String, containing hexadecimal representation.
     */
    public static String ByteArrayToHexString(byte[] bytes) {
        final char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for ( int j = 0; j < bytes.length; j++ ) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * Utility class to convert a hexadecimal string to a byte string.
     *
     * <p>Behavior with input strings containing non-hexadecimal characters is undefined.
     *
     * @param s String containing hexadecimal characters to convert
     * @return Byte array generated from input
     */
    public static byte[] HexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    private void stopNFC_Listener() {
        // 停止监听NFC设备是否连接
        mNfcAdapter.disableForegroundDispatch(this);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_gallery) {
            Intent intent = new Intent(this,RecordActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {
            new AlertDialog.Builder(this).setMessage("Exit current account?").setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (onlineThread != null){
                        onlineThread.interrupt();
                        onlineThread = null;
                    }
                    handler.removeCallbacks(resetInputRunnable);
                    logout();
                    Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void queryEmplyMsg(String cardNum) {
        RequestQueryEmplyMsg requestQueryEmplyMsg = new RequestQueryEmplyMsg();
        requestQueryEmplyMsg.setUsername(Constant.UserName);
        requestQueryEmplyMsg.setSerial(Constant.DeviceId);
        requestQueryEmplyMsg.setCardNum(cardNum);
        KHttp.queryEmplyMsg(MainActivity.this, requestQueryEmplyMsg);
    }

    private void logout() {
        RequestBase requestBase = new RequestBase();
        requestBase.setUsername(Constant.UserName);
        requestBase.setSerial(Constant.DeviceId);
        KHttp.logout(MainActivity.this,requestBase);
    }
}
