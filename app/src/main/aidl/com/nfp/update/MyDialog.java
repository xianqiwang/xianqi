package com.nfp.update;

class DefDialog extends android.app.Dialog {

    //    style引用style样式
    public DefDialog(android.content.Context context, int width, int height, android.view.View layout, int style) {

        super(context, style);

        setContentView(layout);

        android.view.Window window = getWindow();

        android.view.WindowManager.LayoutParams params = window.getAttributes();

        params.gravity = android.view.Gravity.CENTER;

        window.setAttributes(params);
    }


}
