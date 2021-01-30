package com.example.myapplication.video

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.geek.ijkplayer.listener.OnVideoViewStateChangeListener
import com.geek.ijkplayer.player.PlayerConfig
import com.geek.ijkplayer.player.VideoViewManager
import com.geek.ijkplayer.ui.StandardVideoController
import kotlinx.android.synthetic.main.activity_video_layout.*

class VideoActivity : AppCompatActivity() {
    private val url = "http://weather-tools-test.oss-cn-shanghai.aliyuncs.com/tianqi/video/578c694e885c411d8b460f96731e1de833.mp4"

    companion object {
        fun startActivity(context: Context) {
            val intent = Intent(context, VideoActivity.javaClass)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_layout)
        setIjkVideoViewConfig()
        if (!video_view.isPlaying) {
            video_view.start()
        }
    }

    override fun onPause() {
        super.onPause()
        if (video_view.isPlaying) {
            video_view.pause()
        }
    }


    private fun setIjkVideoViewConfig() {
        video_view.stopPlayback()
        val controller = StandardVideoController(this)
        video_view.setPlayerConfig(PlayerConfig.Builder()
                .savingProgress()
                .build())
        video_view.setVideoController(controller)
        //播放状态监听
        video_view.clearOnVideoViewStateChangeListeners()
        video_view.addOnVideoViewStateChangeListener(object : OnVideoViewStateChangeListener {
            override fun onPlayStateChanged(playState: Int) {

            }

            override fun onPlayerStateChanged(playerState: Int) {


            }

        })


        if (!TextUtils.isEmpty(url)) {
            video_view.setUrl(url)
            video_view.title = "小牛视频"
            val ijkView = VideoViewManager.instance().currentVideoPlayer
            if (ijkView != null && ijkView.mediaPlayer != null && ijkView.mediaPlayer.isPlaying) {
                video_view.setmMediaPlayer(VideoViewManager.instance().currentVideoPlayer.mediaPlayer)
                video_view.addDisplay()
            } else {
                video_view.start()
            }
        }



        controller.setOnProgressListener(object : StandardVideoController.onProgressListener {
            override fun onSeekProgress(priorTime: String?, lastTime: String?, skipTime: String?, isForward: Boolean) {
                if (isForward) {
                    //快进
                } else {
                    //快退
                }
            }

            override fun playCurrentTime(time: Long) {}
        })
    }


}