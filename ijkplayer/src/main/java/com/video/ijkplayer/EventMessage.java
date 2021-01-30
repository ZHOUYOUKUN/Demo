package com.video.ijkplayer;

public class EventMessage {

    private int type;

    private int category;


    public EventMessage(int type){
        this.type = type;
    }

    public EventMessage(int type,int category){
        this.type = type;
        this.category = category;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
