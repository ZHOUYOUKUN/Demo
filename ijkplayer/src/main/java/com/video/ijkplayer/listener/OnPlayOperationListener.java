package com.video.ijkplayer.listener;

public interface OnPlayOperationListener  {


    void startPlay();

    void pausePlay();


    void dragSeek(int type);


    void openFullScreen();

    void exitFullScreen();

    void finishPlay();
}
