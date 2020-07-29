package com.starnet.ftc.rtmp.ffmpeg;

/**
 * Created by ftc on 20-7-29.
 */

public interface PushCallback {
    public void videoCallback(long pts, long dts, long duration, long index);
}
