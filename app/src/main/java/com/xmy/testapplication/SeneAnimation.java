package com.xmy.testapplication;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created on 2017/6/2.
 * Author:xumengyuan
 * Description:XXX
 */

public class SeneAnimation {

    private ImageView mImageView;
    private int[] mImageRes;
    private int mGapTime;
    private Runnable mRunable;
    private boolean running = false;
    private int loadNum = 0;
    private Context context;
    public SeneAnimation(Context context, ImageView imageView, int[] mImageRes, int mGapTime) {
        this.context = context;
        this.mImageView = imageView;
        this.mImageRes = mImageRes;
        this.mGapTime = mGapTime;
    }

    public void start(){
        loadNum = 0;
        running = true;

        Runtime runtime = Runtime.getRuntime();
        long startMem = runtime.totalMemory() - runtime.freeMemory();
        Log.e("SeneAnim:start", "total:"+ runtime.totalMemory() / 1024 + " ,free:" + runtime.freeMemory()/1024 + " ,used:" + startMem / 1024);

        play();
    }

    public void stop(){
        running = false;
        mImageView.removeCallbacks(mRunable);
        mRunable = null;
    }

    private void play(){

        Runtime runtime = Runtime.getRuntime();
        long endMem = runtime.totalMemory() - runtime.freeMemory();
        Log.e("SeneAnim:end", "total:"+ runtime.totalMemory() / 1024 + " ,free:" + runtime.freeMemory()/1024 + " ,used:" + endMem / 1024);

        if (mRunable == null){
            mRunable = new Runnable() {
                @Override
                public void run() {
                    if (!running){
                        return;
                    }
                    mImageView.setImageResource(mImageRes[loadNum]);
                    if (loadNum == mImageRes.length - 1){
                        return;
                    }
                    loadNum++;
                    play();
                }
            };
        }
        if (mImageRes[loadNum] == R.drawable.shenquan30){
            mImageView.postDelayed(mRunable, 1000);
            return;
        }
        mImageView.postDelayed(mRunable, mGapTime);
    }

}
