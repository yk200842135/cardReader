package cn.com.reformer.poi.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.com.reformer.poi.R;

/**
 * Created by Administrator on 2016-11-03.
 */
public class WaitFragment extends Fragment {
    ImageView iv_bg;
    TextView tv_tint;
    int state;
    boolean CanUpdate = false;

    public void setData(int state){
        this.state = state;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_wait, container, false);
        initView(view);
        setData(0);
        return view;
    }

    private void initView(View view){
        iv_bg = (ImageView) view.findViewById(R.id.iv_bg);
        tv_tint = (TextView) view.findViewById(R.id.tv_tint);
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

    private void update(){
        switch (state){
            case 0:
                iv_bg.setBackgroundResource(R.mipmap.bg_wait);
                tv_tint.setText(R.string.wait_put_card);
                break;
            case 1:
                iv_bg.setBackgroundResource(R.mipmap.bg_net_error);
                tv_tint.setText(R.string.error_network);
                break;
            case 2:
                iv_bg.setBackgroundResource(R.mipmap.bg_card_invalid);
                tv_tint.setText(R.string.error_card_invalid);
                break;
        }
    }

}
