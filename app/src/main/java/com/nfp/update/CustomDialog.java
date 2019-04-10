package com.nfp.update;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomDialog extends Dialog {

    public CustomDialog(Context context) {
        super(context);
    }

    public CustomDialog(Context context, int theme) {
        super(context, theme);
    }

    public CustomDialog setDialogLayout(int width,int height){
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity=Gravity.CENTER;
        layoutParams.width  = width;
        layoutParams.height =height;
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);
        return this;
    }
    public static class Builder {
        private String message;
        private View contentView;
        private String positiveButtonText;
        private String negativeButtonText;
        private String singleButtonText;
        private View.OnClickListener positiveButtonClickListener;
        private View.OnClickListener negativeButtonClickListener;
        private View.OnClickListener singleButtonClickListener;
        private static int Width,Height;
        private View layout;
        private CustomDialog dialog;
        public Builder(Context context,int width,int height) {

            dialog = new CustomDialog(context, R.style.Dialog);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(R.layout.new_dialog, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            Width=width;
            Height=height;
                    }



        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText, View.OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText, View.OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setSingleButton(String singleButtonText, View.OnClickListener listener) {
            this.singleButtonText = singleButtonText;
            this.singleButtonClickListener = listener;
            return this;
        }

        public CustomDialog createSingleButtonDialog() {
            showSingleButton();
            layout.findViewById(R.id.singleButton).setOnClickListener(singleButtonClickListener);
            if (singleButtonText != null) {
                ((Button) layout.findViewById(R.id.singleButton)).setText(singleButtonText);
            } else {
                ((Button) layout.findViewById(R.id.singleButton)).setText("OK");
            }
            create();
            return dialog;
        }

        public CustomDialog createTwoButtonDialog() {
            showTwoButton();
            layout.findViewById(R.id.positiveButton).setOnClickListener(positiveButtonClickListener);
            layout.findViewById(R.id.negativeButton).setOnClickListener(negativeButtonClickListener);
            if (positiveButtonText != null) {
                ((Button) layout.findViewById(R.id.positiveButton)).setText(positiveButtonText);
            } else {
                ((Button) layout.findViewById(R.id.positiveButton)).setText("OK");
            }
            if (negativeButtonText != null) {
                ((Button) layout.findViewById(R.id.negativeButton)).setText(negativeButtonText);
            } else {
                ((Button) layout.findViewById(R.id.negativeButton)).setText("NO");
            }
            create();
            return dialog;
        }

        private void create() {
            if (message != null) {
                ((TextView) layout.findViewById(R.id.message)).setText(message);
            } else if (contentView != null) {
                ((LinearLayout) layout.findViewById(R.id.content)).removeAllViews();
                ((LinearLayout) layout.findViewById(R.id.content))
                        .addView(contentView, new ViewGroup.LayoutParams(Width/*ViewGroup.LayoutParams.MATCH_PARENT*/,Height/* ViewGroup.LayoutParams.MATCH_PARENT*/));
            }
            dialog.setContentView(layout);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(false);
        }

        public Builder setProgressBarVisible(){
            layout.findViewById(R.id.pb).setVisibility (View.VISIBLE);
            return this;
        }

        private void showTwoButton() {
            layout.findViewById(R.id.singleButtonLayout).setVisibility(View.GONE);
            layout.findViewById(R.id.twoButtonLayout).setVisibility(View.VISIBLE);
        }

        private void showThreeButton() {
            layout.findViewById(R.id.singleButtonLayout).setVisibility(View.GONE);
            layout.findViewById(R.id.twoButtonLayout).setVisibility(View.VISIBLE);
        }

        private void showSingleButton() {
            layout.findViewById(R.id.singleButtonLayout).setVisibility(View.VISIBLE);
            layout.findViewById(R.id.twoButtonLayout).setVisibility(View.GONE);
        }

    }

}