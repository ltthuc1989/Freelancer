package com.ltthuc.freelancer.models;

import android.app.Activity;

import com.ltthuc.freelancer.Utils.Constants;

/**
 * Created by Thuc on 3/3/2016.
 */
public class VideoModel {
    int option;
    int rawFile;
    int playTime;
    int breakTime;
    int id;
    String videoPath;
    Activity context;

    public VideoModel(Activity context, int id, int option, int rawFile) {
        this.id = id;
        this.option = option;
        this.rawFile = rawFile;
        this.context = context;
        initPlayTime();

    }

    public String getVideoPath() {
        return "android.resource://" + context.getPackageName() + "/" + rawFile;
    }

    public int getOption() {
        return option;
    }

    public void setOption(int option) {
        this.option = option;
    }

    public int getRawFile() {
        return rawFile;
    }

    public void setRawFile(int rawFile) {
        this.rawFile = rawFile;
    }

    public void initPlayTime() {
        switch (option) {
            case 1:

                if (id == 0 || id == 6) {
                    playTime = Constants.PLAY_TIME_OPTION_1;
                } else {
                    playTime = Constants.PLAY_TIME_OPTION_1 * 2;
                }

                break;
            case 2:
                if (id == 0 || id == 6) {
                    playTime = Constants.PLAY_TIME_OPTION_2;
                } else {
                    playTime = Constants.PLAY_TIME_OPTION_2 + Constants.BREAK_TIME_OPTION_2;
                }
                break;
        }

    }

    public int getPlayTime() {
        return playTime;
    }

    public void setPlayTime(int playTime) {
        this.playTime = playTime;
    }

    public void initBreakTime() {
        switch (option) {
            case 1:
                breakTime = Constants.BREAK_TIME_OPTION_1;
                break;
            case 2:
                breakTime = Constants.BREAK_TIME_OPTION_2;
                break;
        }

    }

    public int getBreakTime() {
        return breakTime;
    }

}
