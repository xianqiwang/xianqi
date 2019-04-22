package com.nfp.update;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import java.text.DecimalFormat;

public class CustomProgressBar extends ProgressBar {

    private android.content.Context mContext;
    private android.graphics.Paint mPaint;
    private android.graphics.PorterDuffXfermode mPorterDuffXfermode;
    private float mProgress;
    private int mState;
    private static final int STATE_DEFAULT = 101;
    private static final int STATE_DOWNLOADING = 102;
    private static final int STATE_PAUSE = 103;
    private static final int STATE_DOWNLOAD_FINISH = 104;
    private static final float TEXT_SIZE_SP = 17f;
    private static final float ICON_TEXT_SPACING_DP = 5f;

    public CustomProgressBar(android.content.Context context) {
        super(context, null, android.R.attr.progressBarStyleHorizontal);
        mContext = context;
        init();
    }

    public CustomProgressBar(Context context,AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }


    public synchronized void setState(int state) {
        mState = state;
        invalidate();
    }


    public synchronized void setProgress(float progress) {
        super.setProgress((int) progress);
        mProgress = progress;
    }

    @Override
    protected void onDraw(android.graphics.Canvas canvas) {
        super.onDraw(canvas);
        switch (mState) {
            case STATE_DEFAULT:
                drawIconAndText(canvas, STATE_DEFAULT, false);
                break;

            case STATE_DOWNLOADING:
                drawIconAndText(canvas, STATE_DOWNLOADING, false);
                break;

            case STATE_PAUSE:
                drawIconAndText(canvas, STATE_PAUSE, false);
                break;

            case STATE_DOWNLOAD_FINISH:
                drawIconAndText(canvas, STATE_DOWNLOAD_FINISH, true);
                break;

            default:
                drawIconAndText(canvas, STATE_DEFAULT, false);
                break;
        }
    }

    private void init() {
        setIndeterminate(false);
        setIndeterminateDrawable(android.support.v4.content.ContextCompat.getDrawable(mContext,
                android.R.drawable.progress_indeterminate_horizontal));
        setProgressDrawable(android.support.v4.content.ContextCompat.getDrawable(mContext,
                R.drawable.pb_shape_blue));
        setMax(100);

        mPaint = new android.graphics.Paint();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setTextAlign(Paint.Align.LEFT);
        mPaint.setTextSize(MeasureUtil.sp2px(mContext, TEXT_SIZE_SP));
        mPaint.setTypeface(android.graphics.Typeface.MONOSPACE);

        mPorterDuffXfermode = new android.graphics.PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC_IN);
    }

    private void initForState(int state) {
        switch (state) {
            case STATE_DEFAULT:
                setProgress(100);
                mPaint.setColor(android.graphics.Color.WHITE);
                break;

            case STATE_DOWNLOADING:
                mPaint.setColor(android.support.v4.content.ContextCompat.getColor(mContext, R.color.blue));
                break;

            case STATE_PAUSE:
                mPaint.setColor(android.support.v4.content.ContextCompat.getColor(mContext, R.color.blue));
                break;

            case STATE_DOWNLOAD_FINISH:
                setProgress(100);
                mPaint.setColor(android.graphics.Color.WHITE);
                break;

            default:
                setProgress(100);
                mPaint.setColor(android.graphics.Color.WHITE);
                break;
        }
    }

    private void drawIconAndText(android.graphics.Canvas canvas, int state, boolean onlyText) {
        initForState(state);

        String text = getText(state);
        android.graphics.Rect textRect = new android.graphics.Rect();
        mPaint.getTextBounds(text, 0, text.length(), textRect);

        if (onlyText) {
            // 仅绘制文字
            float textX = (getWidth() / 2) - textRect.centerX();
            float textY = (getHeight() / 2) - textRect.centerY();
            canvas.drawText(text, textX, textY, mPaint);
        } else {
            Bitmap icon = getIcon(state);
            float textX = (getWidth() / 2) -
                    getOffsetX(icon.getWidth(), textRect.centerX(), ICON_TEXT_SPACING_DP, true);
            float textY = (getHeight() / 2) - textRect.centerY();
            canvas.drawText(text, textX, textY, mPaint);
            float iconX = (getWidth() / 2) - icon.getWidth() -
                    getOffsetX(icon.getWidth(), textRect.centerX(), ICON_TEXT_SPACING_DP, false);
            float iconY = (getHeight() / 2) - icon.getHeight() / 2;
            canvas.drawBitmap(icon, iconX, iconY, mPaint);
            if (state == STATE_DEFAULT) return;
            Bitmap bufferBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            Canvas bufferCanvas = new Canvas(bufferBitmap);
            bufferCanvas.drawBitmap(icon, iconX, iconY, mPaint);
            bufferCanvas.drawText(text, textX, textY, mPaint);
            mPaint.setXfermode(mPorterDuffXfermode);
            mPaint.setColor(android.graphics.Color.WHITE);
            RectF rectF = new RectF(0, 0, getWidth() * mProgress / 100, getHeight());
            bufferCanvas.drawRect(rectF, mPaint);
            canvas.drawBitmap(bufferBitmap, 0, 0, null);
            mPaint.setXfermode(null);
            if (!icon.isRecycled()) {
                icon.isRecycled();
            }
            if (!bufferBitmap.isRecycled()) {
                bufferBitmap.recycle();
            }
        }
    }

    private Bitmap getIcon(int state) {
        android.graphics.Bitmap icon;
        switch (state) {
            case STATE_DEFAULT:
                icon = android.graphics.BitmapFactory.decodeResource(getResources(), R.drawable.pb_download);
                break;

            case STATE_DOWNLOADING:
                icon = android.graphics.BitmapFactory.decodeResource(getResources(), R.drawable.pb_pause_blue);

                break;

            case STATE_PAUSE:
                icon = android.graphics.BitmapFactory.decodeResource(getResources(), R.drawable.pb_continue_blue);
                break;

            default:
                icon = android.graphics.BitmapFactory.decodeResource(getResources(), R.drawable.pb_download);
                break;
        }
        return icon;
    }

    private String getText(int state) {
        String text;
        switch (state) {
            case STATE_DEFAULT:
                text = getResources().getString(R.string.pb_download);
                break;

            case STATE_DOWNLOADING:
                java.text.DecimalFormat decimalFormat = new java.text.DecimalFormat("#0.00");
                text = decimalFormat.format(mProgress) + "%";
                break;

            case STATE_PAUSE:
                text = getResources().getString(R.string.pb_continue);
                break;

            case STATE_DOWNLOAD_FINISH:
                text = getResources().getString(R.string.pb_open);
                break;

            default:
                text = getResources().getString(R.string.pb_download);
                break;
        }
        return text;
    }

    private float getOffsetX(float iconWidth, float textHalfWidth, float spacing, boolean isText) {
        float totalWidth = iconWidth + MeasureUtil.dip2px(mContext, spacing) + textHalfWidth * 2;
        if (isText) return totalWidth / 2 - iconWidth - spacing;
        return totalWidth / 2 - iconWidth;
    }

}
