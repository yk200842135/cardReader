package cn.com.reformer.poi.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import cn.com.reformer.poi.R;

/**
 * Created by Administrator on 2016-11-09.
 */
public class LoadingDialog extends Dialog {
    private static Context mContext;

    public LoadingDialog(Context context) {
        super(context);
    }

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context; //上下文对象

        public Builder(Context context) {
            this.context = context;
        }

        public LoadingDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final LoadingDialog dialog = new LoadingDialog(context, R.style.black70style);
            View layout = inflater.inflate(R.layout.dialog_loading, null);
            dialog.setCancelable(false);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            dialog.setContentView(layout);
            return dialog;
        }
    }
}
