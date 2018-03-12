package cn.com.reformer.poi.fragment;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;
import java.util.HashMap;

import cn.com.reformer.poi.R;
import cn.com.reformer.poi.bean.response.ResponseEmplyMsg;
import cn.com.reformer.poi.event.MessageEvent;

/**
 * Created by Administrator on 2016-11-03.
 */
public class PersonFragment extends Fragment{

    LinearLayout ll_dept;
    ImageView iv_person;
    TextView tv_name;
    TextView tv_department;
    TextView tv_idnum;
    TextView tv_cardtype;
    TextView tv_cardnum;
    Button btn_confirm;
    ResponseEmplyMsg responseEmplyMsg;
    boolean CanUpdate = false;

    public void setData(ResponseEmplyMsg responseEmplyMsg){
        this.responseEmplyMsg = responseEmplyMsg;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_personnel, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        update();
        CanUpdate = true;
    }

    public void notifyView(){
        if (!CanUpdate)
            return;
        update();
    }

    private void initView(View view){
        ll_dept = (LinearLayout) view.findViewById(R.id.ll_dept);
        iv_person = (ImageView) view.findViewById(R.id.iv_person);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_department = (TextView) view.findViewById(R.id.tv_department);
        tv_idnum = (TextView) view.findViewById(R.id.tv_idnum);
        tv_cardtype = (TextView) view.findViewById(R.id.tv_cardtype);
        tv_cardnum = (TextView) view.findViewById(R.id.tv_cardnum);
        btn_confirm = (Button) view.findViewById(R.id.btn_confirm);
        setListener();
    }

    private void setListener() {
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_WAIT));
            }
        });
    }

    public void update(){
        if (responseEmplyMsg.getPhotoPath() != null) {
            Glide.with(iv_person.getContext())
                    .load(responseEmplyMsg.getPhotoPath())
                    .into(iv_person);
        }
        if (responseEmplyMsg.getEmplyName() != null) {
            tv_name.setText(responseEmplyMsg.getEmplyName());
        }
        if (responseEmplyMsg.getDepartName() != null) {
            ll_dept.setVisibility(View.VISIBLE);
            tv_department.setText(responseEmplyMsg.getDepartName());
        }else {
            ll_dept.setVisibility(View.GONE);
        }
        if (responseEmplyMsg.getEmplyId() != null) {
            tv_idnum.setText(responseEmplyMsg.getEmplyId());
        }
        if (responseEmplyMsg.getEmplyType() != null) {
            tv_cardtype.setText(responseEmplyMsg.getEmplyType());
        }
        if (responseEmplyMsg.getCardNum() != null) {
            tv_cardnum.setText(responseEmplyMsg.getCardNum());
        }
        //1~9 1~3 4~6 7~9
    }
}
