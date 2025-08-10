package com.a200888.ailisten;

import java.text.SimpleDateFormat;

public class SoundModel {
    private String type;
    private String time;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

//    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
//    //获取当前时间
//    Date date = new Date(System.currentTimeMillis());
//time1.setText("Date获取当前日期时间"+simpleDateFormat.format(date));
}
