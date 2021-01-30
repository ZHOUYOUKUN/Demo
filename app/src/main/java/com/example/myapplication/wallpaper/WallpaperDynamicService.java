package com.example.myapplication.wallpaper;

import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.service.wallpaper.WallpaperService;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;


import java.io.File;
import java.io.IOException;

/**
 * @description 动态壁纸服务
 * @time 2020/11/24 10:55
 */
public class WallpaperDynamicService extends WallpaperService {
    private static final String TAG =WallpaperDynamicService.class.getName();
    private static String sVideoPath;

    /**
     * 设置静音
     *
     * @param context
     */
    public static void setVoiceSilence(Context context) {
        Intent intent = new Intent(Constant.VIDEO_PARAMS_CONTROL_ACTION);
        intent.putExtra(Constant.ACTION, Constant.ACTION_VOICE_SILENCE);
        context.sendBroadcast(intent);
    }

    /**
     * 设置有声音
     *
     * @param context
     */
    public static void setVoiceNormal(Context context) {
        Intent intent = new Intent(Constant.VIDEO_PARAMS_CONTROL_ACTION);
        intent.putExtra(Constant.ACTION, Constant.ACTION_VOICE_NORMAL);
        context.sendBroadcast(intent);
    }

    public static void changeVideo(Context context) {
        Intent intent = new Intent(Constant.VIDEO_PARAMS_CONTROL_ACTION);
        intent.putExtra(Constant.ACTION, Constant.ACTION_CHANGE_VIDEO);
        context.sendBroadcast(intent);
    }

    /**
     * 设置壁纸
     *
     * @param context
     */
    public static void setToWallPaper(Context context, String videoPath) {
        sVideoPath = videoPath;
        Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
        intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, new ComponentName(context,WallpaperDynamicService.class));
        context.startActivity(intent);
        changeVideo(context);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand:" + sVideoPath);
        try {
            replay();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate:" + sVideoPath);
    }

    private void replay() throws IOException {
        if (wallpaperEngine != null) {
            wallpaperEngine.refreshSource();
        }
    }

    VideoWallpaperEngine wallpaperEngine;

    @Override
    public Engine onCreateEngine() {
        Log.d(TAG, "onCreateEngine:" + sVideoPath);
        wallpaperEngine = new VideoWallpaperEngine();
        return wallpaperEngine;
    }

    class VideoWallpaperEngine extends Engine {
        private MediaPlayer mMediaPlayer;
        private BroadcastReceiver mVideoVoiceControlReceiver;

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            IntentFilter intentFilter = new IntentFilter(Constant.VIDEO_PARAMS_CONTROL_ACTION);
            mVideoVoiceControlReceiver = new VideoVoiceControlReceiver();
            registerReceiver(mVideoVoiceControlReceiver, intentFilter);
        }

        @Override
        public void onDestroy() {
            unregisterReceiver(mVideoVoiceControlReceiver);
            super.onDestroy();
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            if (mMediaPlayer == null) {
                return;
            }
            if (visible) {
                mMediaPlayer.start();
            } else {
                mMediaPlayer.pause();
            }
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            File file=new File(sVideoPath);
            if(file.exists()){
                Log.d(TAG, "video path exists");

            }
            if (TextUtils.isEmpty(sVideoPath)) {
                Log.d(TAG, "video path should not be null");
            } else {
                Log.d(TAG, "w:h");
                mMediaPlayer = new MediaPlayer();
                mMediaPlayer.setSurface(holder.getSurface());
                try {
                    mMediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                        @Override
                        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                            Log.d(TAG, "video:w" + width + ", h:" + height);
                        }
                    });
                    mMediaPlayer.setDataSource(sVideoPath);
                    mMediaPlayer.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
                    mMediaPlayer.setLooping(true);
                    mMediaPlayer.setVolume(0f, 0f);
                    mMediaPlayer.prepare();
                    mMediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            if (mMediaPlayer != null) {
                mMediaPlayer.release();
                mMediaPlayer = null;
            }
        }

        public void refreshSource() {
            Log.d(TAG, "refreshSource:" + sVideoPath);
            File file=new File(sVideoPath);
            if(!file.isFile()){
                Log.d(TAG, "video path should not be not isFile");
                return;
            }
            if (TextUtils.isEmpty(sVideoPath) || mMediaPlayer == null) {
                Log.d(TAG, "video path should not be null");
            } else {
                try {
                    if (mMediaPlayer.isPlaying()) {
                        mMediaPlayer.stop();
                    }
                    mMediaPlayer.reset();
                    mMediaPlayer.setDataSource(sVideoPath);
                    mMediaPlayer.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
                    mMediaPlayer.setLooping(true);
                    mMediaPlayer.setVolume(0f, 0f);
                    mMediaPlayer.prepare();
                    mMediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        class VideoVoiceControlReceiver extends BroadcastReceiver {
            @Override
            public void onReceive(Context context, Intent intent) {
                int action = intent.getIntExtra(Constant.ACTION, -1);
                switch (action) {
//                    case Constant.ACTION_VOICE_NORMAL:
//                        mMediaPlayer.setVolume(1.0f, 1.0f);
//                        break;
//                    case Constant.ACTION_VOICE_SILENCE:
//                        mMediaPlayer.setVolume(0, 0);
//                        break;
                    case Constant.ACTION_CHANGE_VIDEO:
                        refreshSource();
                        break;
                }
            }
        }
    }
}
