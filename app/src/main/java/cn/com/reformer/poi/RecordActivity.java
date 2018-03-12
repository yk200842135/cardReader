package cn.com.reformer.poi;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import cn.com.reformer.poi.bean.base.LoginRecordBean;
import cn.com.reformer.poi.dbservice.LoginRecordDbs;
import cn.com.reformer.poi.decoration.DividerItemDecoration;
import cn.com.reformer.poi.util.DateUtils;
import greendao.LoginRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-11-02.
 */
public class RecordActivity extends Activity {
    private static final String TAG = "Records";

    ImageButton ibtn_back;
    TextView tv_title;
    RecyclerView mRecyclerView;
    LoginRecordAdapter mAdapter;
    List<LoginRecord> mDatas;
    long curTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_record);
        initData();
        initView();
    }

    private void initView(){
        ibtn_back = (ImageButton) findViewById(R.id.ibtn_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(TAG);
        mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter = new LoginRecordAdapter());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));
        setListener();
    }

    private void setListener() {
        ibtn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    protected void initData() {
        curTime = DateUtils.getDateByFormat(DateUtils.getCurrentDate(DateUtils.dateFormatYMDHM),DateUtils.dateFormatYMDHM).getTime();
        mDatas = LoginRecordDbs.getInstance(RecordActivity.this).loadAllNoteByDsc();
    }

    class LoginRecordAdapter extends RecyclerView.Adapter<LoginRecordAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    RecordActivity.this).inflate(R.layout.item_login_record, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            LoginRecord loginRecord = mDatas.get(position);
            if (loginRecord != null) {
                if (loginRecord.getState() == 0) {
                    holder.iv_logo.setBackgroundResource(R.mipmap.record_login);
                } else {
                    holder.iv_logo.setBackgroundResource(R.mipmap.record_logout);
                }
                holder.tv_num.setText(loginRecord.getUser());
                long recordTime = DateUtils.getDateByFormat(loginRecord.getTime(),DateUtils.dateFormatYMDHM).getTime();
                if (DateUtils.getOffectDay(curTime,recordTime) == 0 && loginRecord.getTime().length() > 11) {
                    holder.tv_date.setText(loginRecord.getTime().substring(11));
                }else if (loginRecord.getTime().length() > 5){
                    holder.tv_date.setText(loginRecord.getTime().substring(5));
                }//
            }
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView iv_logo;
            TextView tv_num;
            TextView tv_date;//民间新闻

            public MyViewHolder(View view)
            {
                super(view);
                iv_logo = (ImageView) view.findViewById(R.id.iv_logo);
                tv_num = (TextView) view.findViewById(R.id.tv_num);
                tv_date = (TextView) view.findViewById(R.id.tv_date);
            }
        }
    }
}
