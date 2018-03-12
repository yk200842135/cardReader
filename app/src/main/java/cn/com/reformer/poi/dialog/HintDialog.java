package cn.com.reformer.poi.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.com.reformer.poi.R;

/**
 * Created by Administrator on 2016-10-28.
 */
public class HintDialog extends Dialog {

    public HintDialog(Context context) {
        super(context);
    }

    public HintDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private final Context context; //上下文对象
        private String message; //对话框内容

        public Builder(Context context) {
            this.context = context;
        }

        /*设置对话框信息*/
        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        /**
         * Set the Dialog message from resource
         *
         * @param message
         * @return
         */
        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        public HintDialog create(){
            HintDialog hintDialog = new HintDialog(context, R.style.black70style);
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.dialog_alert_black, null);
            hintDialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            TextView textView = (TextView)layout.findViewById(R.id.tv_message);
            if (message != null) {
                textView.setText(message);
            }
            hintDialog.setContentView(layout);
            return hintDialog;
        }
    }

}
