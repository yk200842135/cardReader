package cn.com.reformer.poi.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import cn.com.reformer.poi.R;
import cn.com.reformer.poi.util.AnimationUtil;

/**
 * Created by Administrator on 2016-11-04.
 */
public class LoginningDialog extends Dialog {
    private static Context mContext;

    public LoginningDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context; //上下文对象
        private String message; //对话框内容
        private View contentView; //对话框中间加载的其他布局界面

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

        /**
         * 设置对话框界面
         * @param v View
         * @return
         */
        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }
        public LoginningDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final LoginningDialog dialog = new LoginningDialog(context, R.style.black70style);
            View layout = inflater.inflate(R.layout.loginning_dialog, null);
            View iv_loading = layout.findViewById(R.id.iv_loading);
            AnimationUtil.playRotateAnimation(iv_loading, 1200, Animation.INFINITE,
                    Animation.RESTART, 1);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            dialog.setCancelable(false);
            dialog.setContentView(layout);
            return dialog;
        }

        public LoginningDialog show() {
            LoginningDialog dialog = create();
            dialog.show();
            return dialog;
        }
    }
}
