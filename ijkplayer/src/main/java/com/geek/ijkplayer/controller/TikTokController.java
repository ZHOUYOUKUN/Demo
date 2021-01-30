package com.geek.ijkplayer.controller;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.example.ijkplayer.R;
import com.geek.ijkplayer.player.BaseIjkVideoView;
import com.geek.ijkplayer.player.IjkVideoView;
import com.geek.ijkplayer.util.L;

/**
 * 抖音
 */

public class TikTokController extends BaseVideoController {


    public static final int STATE_LOOP_PLAY_COMPELTE = 100;

    private ImageView thumb;

    public TikTokController(@NonNull Context context) {
        this(context, null);
    }

    public TikTokController(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TikTokController(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_tiktok_controller;
    }

    @Override
    protected void initView() {
        super.initView();
        thumb = mControllerView.findViewById(R.id.iv_thumb);
    }

    @Override
    public void setPlayState(int playState) {
        super.setPlayState(playState);
        L.d("playState---->" + playState);
        switch (playState) {
            case IjkVideoView.STATE_IDLE:
                L.e("STATE_IDLE");
                thumb.setVisibility(VISIBLE);
                break;
            case IjkVideoView.STATE_PLAYING:
                L.e("STATE_PLAYING");
                post(mShowProgress);
                thumb.setVisibility(GONE);
                break;
            case IjkVideoView.STATE_PREPARED:
                L.e("STATE_PREPARED");
                break;
        }
    }

    public ImageView getThumb() {
        return thumb;
    }

    @Override
    protected int setProgress() {
        if (mMediaPlayer == null) {
            return 0;
        }
        int position = (int) mMediaPlayer.getCurrentPosition();
        int duration = (int) mMediaPlayer.getDuration();
        int p = position / 1000;
        int d = duration / 1000;
        if (p == d && p > 0) {

            if (mMediaPlayer instanceof BaseIjkVideoView) {
                BaseIjkVideoView baseIjkVideoView = (BaseIjkVideoView) mMediaPlayer;
                baseIjkVideoView.getmOnVideoViewStateChangeListeners().onPlayStateChanged(STATE_LOOP_PLAY_COMPELTE);
            }
        }
        return position;
    }


}
