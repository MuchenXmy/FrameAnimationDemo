package com.xmy.testapplication;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity implements MediaPlayer.OnPreparedListener{
    //动画资源文件
    int[] srcId = { R.drawable.shenquan00, R.drawable.shenquan01, R.drawable.shenquan02, R.drawable.shenquan03, R.drawable.shenquan04,
                    R.drawable.shenquan05, R.drawable.shenquan06, R.drawable.shenquan07, R.drawable.shenquan08, R.drawable.shenquan09,
                    R.drawable.shenquan10, R.drawable.shenquan11, R.drawable.shenquan12, R.drawable.shenquan13, R.drawable.shenquan14,
                    R.drawable.shenquan15, R.drawable.shenquan16, R.drawable.shenquan17, R.drawable.shenquan18, R.drawable.shenquan19,
                    R.drawable.shenquan20, R.drawable.shenquan21, R.drawable.shenquan22, R.drawable.shenquan23, R.drawable.shenquan24,
                    R.drawable.shenquan25, R.drawable.shenquan26, R.drawable.shenquan27, R.drawable.shenquan28, R.drawable.shenquan29,
                    R.drawable.shenquan30, R.drawable.shenquan31, R.drawable.shenquan32, R.drawable.shenquan33, R.drawable.shenquan34,
                    R.drawable.shenquan35};

    private FrameAnimation frameView;

    private SeneAnimation seneAnim;
    private ImageView img;
    private ImageView img2;

    private SurfaceView surfaceView;
    private MediaPlayer player;
    private Display currDisplay;
    private int vWidth,vHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initFrameAnim();
        initSeneAnim();
        initAnimDrawable();
        initMediaView();
    }

    /**
     * 初始化 方式一 自定义SurfaceView
     */
    private void initFrameAnim() {
        frameView = (FrameAnimation) findViewById(R.id.ani_view);
        frameView.setBitmapResoursID(srcId);
        frameView.setGapTime(50);
        //设置监听事件
        frameView.setOnFrameFinisedListener(new FrameAnimation.OnFrameFinishedListener() {

            @Override
            public void onStop() {
                doLog("FrameView");
            }

            @Override
            public void onStart() {
                doLog("FrameView");
            }
        });
    }

    /**
     * 初始化 方式二
     */
    private void initSeneAnim() {
        img = (ImageView) findViewById(R.id.img);
        seneAnim = new SeneAnimation(this, img, srcId, 50);
    }

    /**
     * 初始化 方式三
     */
    private void initAnimDrawable() {
        img2 = (ImageView) findViewById(R.id.img2);
    }

    /**
     * 初始化 方式四
     */
    private void initMediaView() {
        surfaceView = (SurfaceView) findViewById(R.id.mediaView);
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Log.e("MediaPlayer", "surfaceCreated");
                // 当SurfaceView中的Surface被创建的时候被调用
                //在这里我们指定MediaPlayer在当前的Surface中进行播放
                player.setDisplay(holder);

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });

        //然后，我们取得当前Display对象
        currDisplay = this.getWindowManager().getDefaultDisplay();
    }


    @OnClick(R.id.btn1)
    public void doClick(View view){
        frameView.setVisibility(View.VISIBLE);
        img.setVisibility(View.GONE);
        seneAnim.stop();
        img2.setVisibility(View.GONE);
        MyAnimationDrawable.isStop = true;
        surfaceView.setVisibility(View.GONE);

        frameView.start();
    }

    @OnClick(R.id.btn2)
    public void doClick2(View view){
        frameView.setVisibility(View.GONE);
        img.setVisibility(View.VISIBLE);
        seneAnim.stop();
        img2.setVisibility(View.GONE);
        MyAnimationDrawable.isStop = true;
        surfaceView.setVisibility(View.GONE);

        seneAnim.start();
    }

    @OnClick(R.id.btn3)
    public void doClick3(View view){
        frameView.setVisibility(View.GONE);
        img.setVisibility(View.GONE);
        seneAnim.stop();
        img2.setVisibility(View.VISIBLE);
        surfaceView.setVisibility(View.GONE);

        MyAnimationDrawable.animateRawManuallyFromXML(R.drawable.anim_list, img2, new Runnable() {
            @Override
            public void run() {
                doLog("MyAnimationDrawable");
            }
        }, new Runnable() {
            @Override
            public void run() {
                doLog("MyAnimationDrawable");
            }
        });
    }

    @OnClick(R.id.btn4)
    public void doClick4(View view){
        frameView.setVisibility(View.GONE);
        img.setVisibility(View.GONE);
        seneAnim.stop();
        img2.setVisibility(View.GONE);
        MyAnimationDrawable.isStop = true;
        surfaceView.setVisibility(View.VISIBLE);

        player = MediaPlayer.create(this, R.raw.min_shenquan); //读取资源文件中视频
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
    }

    /**
     * 打印内存使用情况
     * @param TAG
     */
    private void doLog(String TAG){
        Runtime runtime = Runtime.getRuntime();
        long endMem = runtime.totalMemory() - runtime.freeMemory();
        Log.e(TAG,  "total:"+ runtime.totalMemory() / 1024 + " ,free:" + runtime.freeMemory()/1024 + " ,used:" + endMem / 1024);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        // 当prepare完成后，该方法触发，在这里我们播放视频
        Log.e("MediaPlayer", "onPrepared");
        //首先取得video的宽和高
        vWidth = player.getVideoWidth();
        vHeight = player.getVideoHeight();

        if(vWidth > currDisplay.getWidth() || vHeight > currDisplay.getHeight()){
            //如果video的宽或者高超出了当前屏幕的大小，则要进行缩放
            float wRatio = (float)vWidth/(float)currDisplay.getWidth();
            float hRatio = (float)vHeight/(float)currDisplay.getHeight();

            //选择大的一个进行缩放
            float ratio = Math.max(wRatio, hRatio);

            vWidth = (int)Math.ceil((float)vWidth/ratio);
            vHeight = (int)Math.ceil((float)vHeight/ratio);

            //设置surfaceView的布局参数
            surfaceView.setLayoutParams(new LinearLayout.LayoutParams(vWidth, vHeight));
        }
        player.start();
    }
}
