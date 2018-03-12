package cn.com.reformer.poi.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import cn.com.reformer.poi.R;

/**
 * Created by Administrator on 2016-10-26.
 */
public class JustifyTextView extends TextView {

    private int textColor;

    public JustifyTextView(Context context) {
        super(context, null);
    }

    public JustifyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);

    }

    public JustifyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.JustifyTextView);
        textColor = ta.getColor(R.styleable.JustifyTextView_text_color, Color.parseColor("#000000"));
        ta.recycle();
        setTextColor(textColor);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        Paint.FontMetrics fm = getPaint().getFontMetrics();

        float baseline = fm.descent - fm.ascent;
        float x = getPaddingLeft();
        float y = baseline;  //由于系统基于字体的底部来绘制文本，所有需要加上字体的高度。
        String txt = getText().toString();

        //文本自动换行
        String[] texts = autoSplit(txt, getPaint(), getWidth() - getPaddingLeft() - getPaddingRight());
        for (String text : texts) {
            if (TextUtils.isEmpty(text))
                return;

            canvas.drawText(text, x, y, getPaint());  //坐标以控件左上角为原点
            y += baseline + fm.leading; //添加字体行间距
        }
    }

    private String[] autoSplit(String content, Paint p, float width) {
        String[] splitByUser = content.split("\\n");
        if (splitByUser.length < 2) {
            float textWidth = p.measureText(content);
            if (textWidth <= width) {
                return new String[]{content};
            }
            return splitEveryLines(splitByUser, p, width);
        } else {
            return splitEveryLines(splitByUser, p, width);
        }
    }

    private String[] splitEveryLines(String[] splitByUser, Paint p, float width) {
        String[] lineTexts = new String[splitByUser.length * 10];
        int lineIndex = 0;
        for (String userSplitLine : splitByUser) {
            if (TextUtils.isEmpty(userSplitLine))
                return lineTexts;

            float singleLineWidth = p.measureText(userSplitLine);
            if (singleLineWidth <= width) {
                lineTexts[lineIndex] = userSplitLine;
                lineIndex++;
                continue;
            }
            int start = 0;
            int end = 1;
            while (start < userSplitLine.length()) {
                if (end <= userSplitLine.length()) {
                    if (p.measureText(userSplitLine.substring(start, end)) > width) {
                        end--;
                        lineTexts[lineIndex++] = userSplitLine.substring(start, end);
                        start = end;
                    }
                    end++;
                } else {
                    lineTexts[lineIndex++] = userSplitLine.substring(start, userSplitLine.length());
                    break;
                }
            }

        }
        return lineTexts;
    }

    @Override
    public void setTextColor(int color) {
        super.setTextColor(color);
        getPaint().setColor(color);
    }
}
