package com.nfp.update;
import android.widget.ListView;
public class NewListView extends ListView{


    private Boolean mIsListLoopable=false;

    @android.support.annotation.RequiresApi (api = android.os.Build.VERSION_CODES.LOLLIPOP)
    public NewListView (android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super (context, attrs, defStyleAttr, defStyleRes);
    }
    public NewListView (android.content.Context context) {
        super (context);
    }
    public NewListView (android.content.Context context, android.util.AttributeSet attrs) {
        super (context, attrs);
    }

    public void setListLoopable(boolean isLoopable) {
        mIsListLoopable = isLoopable;
    }
    public boolean getListLoopable() {
        return mIsListLoopable;
    }

}
