package com.nfp.update;

public class ProgressActivity extends android.app.Activity {

    private java.util.Timer timer;
    private ProgressBarWithPercent progressBarWithPercent;

    private static android.content.Context context;
    @Override
    protected void onCreate (android.os.Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.progressactivity);
        progressBarWithPercent = (ProgressBarWithPercent) findViewById(R.id.progressBarWithPercent);
        progressBarWithPercent.setOnProgressBarListener(new OnProgressBarListener(){

            @Override
            public void onProgressChange (int current, int max) {

                if (current == max) {
                    progressBarWithPercent.setProgress(0);
                }

            }
        });

        timer = new java.util.Timer ();
        timer.schedule(new java.util.TimerTask () {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        progressBarWithPercent.incrementProgressBy(1);

                    }
                });
            }
        }, 1000, 100);


    }
}
