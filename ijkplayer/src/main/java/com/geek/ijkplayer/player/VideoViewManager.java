package com.geek.ijkplayer.player;


/**
 * 视频播放器管理器，需要配合addToPlayerManager()使用
 */
public class VideoViewManager {

    private IjkVideoView mPlayer;
    private boolean isVideoInfo = false; //是否是跳转到短视频详情

    private VideoViewManager() {
    }

    private static VideoViewManager sInstance;

    public static VideoViewManager instance() {
        if (sInstance == null) {
            synchronized (VideoViewManager.class) {
                if (sInstance == null) {
                    sInstance = new VideoViewManager();
                }
            }
        }
        return sInstance;
    }

    public void setCurrentVideoPlayer(IjkVideoView player) {
        mPlayer = player;
    }

    public IjkVideoView getCurrentVideoPlayer() {
        return mPlayer;
    }

    public void releaseVideoPlayer() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }


    public void pauseVideoPlayer() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
        }
    }

    public void releaseVideoPlayer(boolean isShowAd) {
        if (mPlayer != null) {
            if (!isVideoInfo || isVideoInfo && (mPlayer.getMediaPlayer() == null || !mPlayer.getMediaPlayer().isPlaying())) {
                mPlayer.release();
                mPlayer = null;
            }
        }
    }


    public void setVideoInfo(boolean isVideoInfo) {
        this.isVideoInfo = isVideoInfo;
    }

    public void stopPlayback() {
        if (mPlayer != null) {
            mPlayer.stopPlayback();
        }
    }

    public boolean onBackPressed() {
        return mPlayer != null && mPlayer.onBackPressed();
    }


    public void setVolume(boolean isVolume) {
        if (mPlayer != null) {
            mPlayer.setMute(isVolume);
        }
    }
}
