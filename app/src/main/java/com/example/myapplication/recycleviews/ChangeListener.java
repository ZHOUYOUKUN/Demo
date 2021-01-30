package com.example.myapplication.recycleviews;

/**
 * @author: dukangkang
 * @date: 2020-02-06 13:02.
 * @description: todo ...
 */
public class ChangeListener {

    public enum State {
        /**
         * 展开
         */
        EXPANDED,
        /**
         * 折叠
         */
        COLLAPSED,
        /**
         * 闲置
         */
        IDLE
    }

    public void onStateChanged(State state) {

    }
}
