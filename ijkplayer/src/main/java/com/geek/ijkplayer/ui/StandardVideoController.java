package com.geek.ijkplayer.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ijkplayer.R;
import com.geek.ijkplayer.controller.GestureVideoController;
import com.geek.ijkplayer.player.IjkVideoView;
import com.geek.ijkplayer.util.L;
import com.geek.ijkplayer.util.PlayerUtils;


/**
 * 直播/点播控制器
 */

public class StandardVideoController extends GestureVideoController implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private final String TAG = StandardVideoController.class.getSimpleName();
    protected TextView mTotalTime, mCurrTime;
    protected ImageView mFullScreenButton;
    protected LinearLayout mBottomContainer, mTopContainer;
    protected SeekBar mVideoProgress;
    protected ImageView mBackButton;
    protected ImageView mLockButton;
    protected TextView mTitle;
    private boolean mIsLive;
    private boolean mIsDragging;

    private ProgressBar mBottomProgress;
    private ImageView mPlayButton;
    private ImageView mStartPlayButton;
    private ProgressBar mLoadingProgress;
    private FrameLayout mThumbContent;
    private ImageView mThumb;
    private LinearLayout mCompleteContainer;
    private TextView mSysTime;//系统当前时间
    private ImageView mBatteryLevel;//电量
    private Animation mShowAnim = AnimationUtils.loadAnimation(getContext(), R.anim.dkplayer_anim_alpha_in);
    private Animation mHideAnim = AnimationUtils.loadAnimation(getContext(), R.anim.dkplayer_anim_alpha_out);
    private BatteryReceiver mBatteryReceiver;
    protected ImageView mRefreshButton;
    private onProgressListener mOnProgressListener;
    private RelativeLayout mVideoContent;
    private LinearLayout mHolderAdContent;
    private CountDownTimer countDownTimer;
    private int time = 0;
    private TextView timeTextView;

    public void setOnProgressListener(onProgressListener onProgressListener) {
        this.mOnProgressListener = onProgressListener;
    }

    @Override
    protected void initView() {
        super.initView();
        mFullScreenButton = mControllerView.findViewById(R.id.fullscreen);
        mFullScreenButton.setOnClickListener(this);
        mBottomContainer = mControllerView.findViewById(R.id.bottom_container);
        mTopContainer = mControllerView.findViewById(R.id.top_container);
        mVideoProgress = mControllerView.findViewById(R.id.seekBar);
        mVideoProgress.setOnSeekBarChangeListener(this);
        mTotalTime = mControllerView.findViewById(R.id.total_time);
        mCurrTime = mControllerView.findViewById(R.id.curr_time);
        mBackButton = mControllerView.findViewById(R.id.back);
        mBackButton.setOnClickListener(this);
        mLockButton = mControllerView.findViewById(R.id.lock);
        mLockButton.setOnClickListener(this);
        mThumbContent = mControllerView.findViewById(R.id.thumb_content);
        mThumb = mControllerView.findViewById(R.id.thumb);
        mThumb.setOnClickListener(this);
        mThumbContent.setOnClickListener(this);
        mPlayButton = mControllerView.findViewById(R.id.iv_play);
        mPlayButton.setOnClickListener(this);

        mStartPlayButton = mControllerView.findViewById(R.id.start_play);
        mStartPlayButton.setOnClickListener(this);
        mLoadingProgress = mControllerView.findViewById(R.id.loading);
        mBottomProgress = mControllerView.findViewById(R.id.bottom_progress);
        ImageView rePlayButton = mControllerView.findViewById(R.id.iv_replay);
        rePlayButton.setOnClickListener(this);
        mCompleteContainer = mControllerView.findViewById(R.id.complete_container);

        mCompleteContainer.setOnClickListener(this);
        mTitle = mControllerView.findViewById(R.id.title);
        mSysTime = mControllerView.findViewById(R.id.sys_time);
        mBatteryLevel = mControllerView.findViewById(R.id.iv_battery);
        mBatteryReceiver = new BatteryReceiver(mBatteryLevel);
        mRefreshButton = mControllerView.findViewById(R.id.iv_refresh);
        mRefreshButton.setOnClickListener(this);
    }

    private TextView OutTtitle;

    private TextView videoTime;

    public StandardVideoController(@NonNull Context context) {
        this(context, null);
    }

    public StandardVideoController(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StandardVideoController(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dkplayer_layout_standard_controller;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.fullscreen || i == R.id.back) {
            doStartStopFullScreen();
        } else if (i == R.id.lock) {
            doLockUnlock();
        } else if (i == R.id.iv_play || i == R.id.thumb || i == R.id.start_play) {
            doPauseResume();
        } else if (i == R.id.iv_replay) {
            mMediaPlayer.retry();
        } else if (i == R.id.iv_refresh) {
            mMediaPlayer.refresh();
        }
    }

    public void setFullScreenButtonVisibility(boolean isShow) {
        if (mFullScreenButton != null && mTotalTime != null) {
            mTotalTime.setPadding(0, 0, 25, 0);
            mFullScreenButton.setVisibility(isShow ? VISIBLE : GONE);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getContext().unregisterReceiver(mBatteryReceiver);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getContext().registerReceiver(mBatteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    public void setmTitle(String title) {
        mTitle.setText(title);
    }

    public void showTitle() {
        mTitle.setVisibility(View.VISIBLE);
    }

    @Override
    public void setPlayerState(int playerState) {
        switch (playerState) {
            case IjkVideoView.PLAYER_NORMAL:
                L.e("PLAYER_NORMAL");
                if (mIsLocked) {
                    return;
                }
                setLayoutParams(new LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                mIsGestureEnabled = false;
                mFullScreenButton.setSelected(false);
                mBackButton.setVisibility(View.GONE);
                mLockButton.setVisibility(View.GONE);
                mTitle.setVisibility(View.INVISIBLE);
                mSysTime.setVisibility(View.GONE);
                mBatteryLevel.setVisibility(View.GONE);
                mTopContainer.setVisibility(View.GONE);
                break;
            case IjkVideoView.PLAYER_FULL_SCREEN:
                L.e("PLAYER_FULL_SCREEN");
                if (mIsLocked) {
                    return;
                }
                mIsGestureEnabled = true;
                mFullScreenButton.setSelected(true);
                mBackButton.setVisibility(View.VISIBLE);
                mTitle.setVisibility(View.VISIBLE);
                mSysTime.setVisibility(View.VISIBLE);
                mBatteryLevel.setVisibility(View.VISIBLE);
                if (mShowing) {
                    mLockButton.setVisibility(View.VISIBLE);
                    mTopContainer.setVisibility(View.VISIBLE);
                } else {
                    mLockButton.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void setPlayState(int playState) {
        super.setPlayState(playState);
        switch (playState) {
            case IjkVideoView.STATE_IDLE:
                hide();
                mIsLocked = false;
                mLockButton.setSelected(false);
                mMediaPlayer.setLock(false);
                mStartPlayButton.setSelected(false);
                mBottomProgress.setProgress(0);
                mBottomProgress.setSecondaryProgress(0);
                mVideoProgress.setProgress(0);
                mVideoProgress.setSecondaryProgress(0);
                mCompleteContainer.setVisibility(View.GONE);
                mBottomProgress.setVisibility(View.GONE);
                mLoadingProgress.setVisibility(View.GONE);

                mThumbContent.setVisibility(View.VISIBLE);
                mThumb.setVisibility(View.VISIBLE);

                mStartPlayButton.setVisibility(View.VISIBLE);
                if (OutTtitle != null) {
                    OutTtitle.setVisibility(View.VISIBLE);
                }

                if (videoTime != null) {
                    videoTime.setVisibility(View.VISIBLE);
                }

                break;
            case IjkVideoView.STATE_RELEASE:
                hide();
                mIsLocked = false;
                mLockButton.setSelected(false);
                mMediaPlayer.setLock(false);
                mStartPlayButton.setSelected(false);
                mBottomProgress.setProgress(0);
                mBottomProgress.setSecondaryProgress(0);
                mVideoProgress.setProgress(0);
                mVideoProgress.setSecondaryProgress(0);
                mCompleteContainer.setVisibility(View.GONE);
                mBottomProgress.setVisibility(View.GONE);
                mLoadingProgress.setVisibility(View.GONE);
                mStartPlayButton.setVisibility(View.VISIBLE);
                mThumbContent.setVisibility(View.VISIBLE);
                mThumb.setVisibility(View.VISIBLE);
                if (OutTtitle != null) {
                    OutTtitle.setVisibility(View.VISIBLE);
                }

                if (videoTime != null) {
                    videoTime.setVisibility(View.VISIBLE);
                }
                break;
            case IjkVideoView.STATE_PLAYING:
                L.e("STATE_PLAYING");
                post(mShowProgress);
                mPlayButton.setSelected(true);
                mStartPlayButton.setSelected(true);
                mLoadingProgress.setVisibility(View.GONE);
                mCompleteContainer.setVisibility(View.GONE);
                mThumbContent.setVisibility(View.GONE);
                mStartPlayButton.setVisibility(View.GONE);
                if (OutTtitle != null) {
                    OutTtitle.setVisibility(View.GONE);
                }

                if (videoTime != null) {
                    videoTime.setVisibility(View.GONE);
                }
                break;
            case IjkVideoView.STATE_PAUSED:
                L.e("STATE_PAUSED");
                mPlayButton.setSelected(false);
                mStartPlayButton.setSelected(false);
                mStartPlayButton.setVisibility(View.VISIBLE);
                if (OutTtitle != null) {
                    OutTtitle.setVisibility(View.VISIBLE);
                }

                if (videoTime != null) {
                    videoTime.setVisibility(View.GONE);
                }
                break;
            case IjkVideoView.STATE_PREPARING:
                L.e("STATE_PREPARING");
                mCompleteContainer.setVisibility(View.GONE);
                mStartPlayButton.setVisibility(View.GONE);
                mLoadingProgress.setVisibility(View.VISIBLE);
//                mThumb.setVisibility(View.VISIBLE);
                if (OutTtitle != null) {
                    OutTtitle.setVisibility(View.GONE);
                }
                break;
            case IjkVideoView.STATE_PREPARED:
                L.e("STATE_PREPARED");
                if (!mIsLive) {
                    mBottomProgress.setVisibility(View.VISIBLE);
                }
//                mLoadingProgress.setVisibility(GONE);
                mStartPlayButton.setVisibility(View.GONE);
                if (OutTtitle != null) {
                    OutTtitle.setVisibility(View.GONE);
                }
                break;
            case IjkVideoView.STATE_ERROR:
                L.e("STATE_ERROR");
                mStartPlayButton.setVisibility(View.GONE);
                mLoadingProgress.setVisibility(View.GONE);
                mThumbContent.setVisibility(View.GONE);
                mThumb.setVisibility(View.GONE);
                mBottomProgress.setVisibility(View.GONE);
                mTopContainer.setVisibility(View.GONE);
                if (OutTtitle != null) {
                    OutTtitle.setVisibility(View.GONE);
                }
                break;
            case IjkVideoView.STATE_BUFFERING:
                L.e("STATE_BUFFERING");
                mStartPlayButton.setVisibility(View.GONE);
                mLoadingProgress.setVisibility(View.VISIBLE);
                mThumb.setVisibility(View.GONE);
                if (OutTtitle != null) {
                    OutTtitle.setVisibility(View.GONE);
                }
                break;
            case IjkVideoView.STATE_BUFFERED:
                mLoadingProgress.setVisibility(View.GONE);
                mStartPlayButton.setVisibility(View.GONE);
                mThumbContent.setVisibility(View.GONE);
                L.e("STATE_BUFFERED");
                if (OutTtitle != null) {
                    OutTtitle.setVisibility(View.GONE);
                }
                break;
            case IjkVideoView.STATE_PLAYBACK_COMPLETED:
                mThumbContent.setVisibility(View.VISIBLE);
                mCompleteContainer.setVisibility(View.VISIBLE);
                L.e("STATE_PLAYBACK_COMPLETED");
                hide();
                removeCallbacks(mShowProgress);
                mStartPlayButton.setVisibility(View.GONE);
//                mThumbContent.setVisibility(View.VISIBLE);
//                mCompleteContainer.setVisibility(View.VISIBLE);  //广告播放完成
                mBottomProgress.setProgress(0);
                mBottomProgress.setSecondaryProgress(0);
                mIsLocked = false;
                mMediaPlayer.setLock(false);
                if (OutTtitle != null) {
                    OutTtitle.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
    }

    public void setVideoContent(RelativeLayout videoContent) {
        mVideoContent = videoContent;
    }

    public void setAdContent(LinearLayout adContent) {
        mHolderAdContent = adContent;
    }

    /**
     * 设置定时器
     */
    private void setCountDownTimer() {
        countDownTimer = new CountDownTimer(time * 1000 + 1050, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long t = millisUntilFinished / 1000;
                time = (int) t;
                if (timeTextView != null && t >= 1) {
                    timeTextView.setText(t - 1 + "s | 关闭广告");
                }
            }

            @Override
            public void onFinish() {
                mCompleteContainer.setVisibility(View.VISIBLE);
                if (mVideoContent != null && mHolderAdContent != null) {
                    mVideoContent.setVisibility(View.VISIBLE);
                    mHolderAdContent.setVisibility(View.GONE);
                }
            }
        };
        countDownTimer.start();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        long duration = mMediaPlayer.getDuration();
        long currentPosition = mMediaPlayer.getCurrentPosition();
        long newPosition = (duration * seekBar.getProgress()) / mVideoProgress.getMax();

        if (mOnProgressListener != null) {
            mOnProgressListener.onSeekProgress(stringForTime((int) currentPosition), stringForTime((int) newPosition), stringForTime((int) (newPosition > duration ? newPosition - duration : duration - newPosition)), newPosition > currentPosition);
        }
        mMediaPlayer.seekTo((int) newPosition);
        mIsDragging = false;
        post(mShowProgress);
        show();
    }

    protected void doLockUnlock() {
        if (mIsLocked) {
            mIsLocked = false;
            mShowing = false;
            mIsGestureEnabled = true;
            show();
            mLockButton.setSelected(false);
        } else {
            hide();
            mIsLocked = true;
            mIsGestureEnabled = false;
            mLockButton.setSelected(true);
        }
        mMediaPlayer.setLock(mIsLocked);
    }

    /**
     * 设置是否为直播视频
     */
    public void setLive() {
        mIsLive = true;
        mBottomProgress.setVisibility(View.GONE);
        mVideoProgress.setVisibility(View.INVISIBLE);
        mTotalTime.setVisibility(View.INVISIBLE);
        mCurrTime.setVisibility(View.INVISIBLE);
        mRefreshButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mIsDragging = true;
        removeCallbacks(mShowProgress);
        removeCallbacks(mFadeOut);
    }

    @Override
    public void hide() {
        if (mShowing) {
            if (mMediaPlayer.isFullScreen()) {
                mLockButton.setVisibility(View.GONE);
                if (!mIsLocked) {
                    hideAllViews();
                }
            } else {
                mBottomContainer.setVisibility(View.GONE);
                mBottomContainer.startAnimation(mHideAnim);
                mStartPlayButton.setVisibility(View.GONE);
//                mPause.setVisibility(View.GONE);
                if (OutTtitle != null) {
                    OutTtitle.setVisibility(View.GONE);
                }
            }
            if (!mIsLive && !mIsLocked) {
                mBottomProgress.setVisibility(View.VISIBLE);
                mBottomProgress.startAnimation(mShowAnim);
            }
            mShowing = false;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        if (!fromUser) {
            return;
        }

        long duration = mMediaPlayer.getDuration();
        long newPosition = (duration * progress) / mVideoProgress.getMax();
        if (mCurrTime != null) {
            mCurrTime.setText(stringForTime((int) newPosition));
        }
    }

    private void hideAllViews() {
        mTopContainer.setVisibility(View.GONE);
        mTopContainer.startAnimation(mHideAnim);
        mBottomContainer.setVisibility(View.GONE);
        mBottomContainer.startAnimation(mHideAnim);
//        mPause.setVisibility(View.GONE);
        mStartPlayButton.setVisibility(View.GONE);
        if (OutTtitle != null) {
            OutTtitle.setVisibility(View.GONE);
        }

    }

    private void show(int timeout) {
        if (mSysTime != null) {
            mSysTime.setText(getCurrentSystemTime());
        }
        if (!mShowing) {
            if (mMediaPlayer.isFullScreen()) {
                mLockButton.setVisibility(View.VISIBLE);
                if (!mIsLocked) {
                    showAllViews();
                }
            } else {
                mBottomContainer.setVisibility(View.VISIBLE);
                mBottomContainer.startAnimation(mShowAnim);
                mStartPlayButton.setVisibility(View.VISIBLE);
                if (OutTtitle != null) {
                    OutTtitle.setVisibility(View.VISIBLE);
                }
            }
            if (!mIsLocked && !mIsLive) {
                mBottomProgress.setVisibility(View.GONE);
                mBottomProgress.startAnimation(mHideAnim);
            }
            mShowing = true;
        }
        removeCallbacks(mFadeOut);
        if (timeout != 0) {
            postDelayed(mFadeOut, timeout);
        }
    }

    private void showAllViews() {
        mBottomContainer.setVisibility(View.VISIBLE);
        mBottomContainer.startAnimation(mShowAnim);
        mTopContainer.setVisibility(View.VISIBLE);
        mTopContainer.startAnimation(mShowAnim);
        mStartPlayButton.setVisibility(View.VISIBLE);
        if (OutTtitle != null) {
            OutTtitle.setVisibility(View.VISIBLE);
        }

    }

    public void SetOutTitle(TextView textView) {
        OutTtitle = textView;
    }

    public void setVideoTime(TextView textView) {

        videoTime = textView;
    }

    @Override
    protected int setProgress() {
        if (mMediaPlayer == null || mIsDragging) {
            return 0;
        }

        if (mTitle != null && TextUtils.isEmpty(mTitle.getText())) {

            mTitle.setText(mMediaPlayer.getTitle());
        }

        if (mIsLive) {
            return 0;
        }

        int position = (int) mMediaPlayer.getCurrentPosition();
        int duration = (int) mMediaPlayer.getDuration();
        if (mOnProgressListener != null) {
            mOnProgressListener.playCurrentTime(position);
        }
        if (mVideoProgress != null) {

            if (duration > 0) {
                mVideoProgress.setEnabled(true);
                int pos = (int) (position * 1.0 / duration * mVideoProgress.getMax());
                mVideoProgress.setProgress(pos);
                mBottomProgress.setProgress(pos);
            } else {
                mVideoProgress.setEnabled(false);
            }
            int percent = mMediaPlayer.getBufferedPercentage();
            if (percent >= 95) { //解决缓冲进度不能100%问题
                mVideoProgress.setSecondaryProgress(mVideoProgress.getMax());
                mBottomProgress.setSecondaryProgress(mBottomProgress.getMax());
            } else {
                mVideoProgress.setSecondaryProgress(percent * 10);
                mBottomProgress.setSecondaryProgress(percent * 10);
            }
        }

        if (mTotalTime != null) {
            mTotalTime.setText(stringForTime(duration));
        }
        if (mCurrTime != null) {
            mCurrTime.setText(stringForTime(position));
        }

        return position;
    }

    @Override
    public void show() {
        show(mDefaultTimeout);
    }


    public interface onProgressListener {
        void onSeekProgress(String priorTime, String lastTime, String skipTime, boolean isForward);

        void playCurrentTime(long time);
    }


    @Override
    protected void slideToChangePosition(float deltaX) {
        if (mIsLive) {
            mNeedSeek = false;
        } else {
            super.slideToChangePosition(deltaX);
        }
    }

    public ImageView getThumb() {
        return mThumb;
    }

    @Override
    public boolean onBackPressed() {
        if (mIsLocked) {
            show();
            return true;
        }

        Activity activity = PlayerUtils.scanForActivity(getContext());
        if (activity == null) {
            return super.onBackPressed();
        }

        if (mMediaPlayer.isFullScreen()) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            mMediaPlayer.stopFullScreen();
            return true;
        }
        return super.onBackPressed();
    }
}
