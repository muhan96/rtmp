package com.starnet.ftc.rtmp;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.starnet.ftc.rtmp.ffmpeg.FFmpegHandle;
import com.starnet.ftc.rtmp.ffmpeg.PushCallback;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private TextView tvCodecInfo;
    private TextView tvPushInfo;
    private final String TAG = "FFmpegHandleActivity";

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("avutil-56");
        System.loadLibrary("swresample-3");
        System.loadLibrary("avcodec-58");
        System.loadLibrary("avformat-58");
        System.loadLibrary("swscale-5");
        System.loadLibrary("avfilter-7");
        System.loadLibrary("avdevice-58");
        System.loadLibrary("postproc-55");
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initData() {
        FFmpegHandle.init(this);
        tvCodecInfo.setText(FFmpegHandle.getInstance().getAvcodecConfiguration());
        int res = FFmpegHandle.getInstance().setCallback(new PushCallback() {
            @Override
            public void videoCallback(final long pts, final long dts, final long duration, final long index) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        StringBuilder builder = new StringBuilder();
                        builder.append("pts: ").append(pts).append("\n");
                        builder.append("dts: ").append(dts).append("\n");
                        builder.append("duration: ").append(duration).append("\n");
                        builder.append("index: ").append(index).append("\n");
                        tvPushInfo.setText(builder.toString());
                    }
                });
            }
        });
        Log.d(TAG, "initData result:" + res);
    }

    private void initView() {
        tvCodecInfo = findViewById(R.id.tv_codec_info);
        tvPushInfo = findViewById(R.id.tv_push_info);
    }

    public void btnPush(View view) {
        final String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "sample.flv";
        File file = new File(path);
        Log.d(TAG, path + " " + file.exists());
        new Thread() {
            @Override
            public void run() {
                super.run();
                int result = FFmpegHandle.getInstance().pushRtmpFile(path);
                Log.d(TAG, "pushRtmpFile result:" + result);
            }
        }.start();
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
