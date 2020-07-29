package com.starnet.ftc.rtmp.ffmpeg;

import android.content.Context;

/**
 * Created by ftc on 20-7-29.
 */

public class FFmpegHandle {
    private final String TAG = "FFmpegHandle";
    private static FFmpegHandle mInstance;

    public static void init(Context context) {
        mInstance = new FFmpegHandle();
    }

    public static FFmpegHandle getInstance() {
        if(mInstance == null) {
            throw new RuntimeException("FFmpegeHandle must init first");
        }
        return mInstance;
    }

    static {
        System.loadLibrary("avutil-56");
        System.loadLibrary("swresample-3");
        System.loadLibrary("avcodec-58");
        System.loadLibrary("avformat-58");
        System.loadLibrary("swscale-5");
        System.loadLibrary("avfilter-7");
        System.loadLibrary("avdevice-58");
        System.loadLibrary("postproc-55");
        System.loadLibrary("ffmpeg-handle");
    }

    public native int setCallback(PushCallback pushCallback);

    public native String getAvcodecConfiguration();

    public native int pushRtmpFile(String path);
}
