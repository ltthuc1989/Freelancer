package com.ltthuc.freelancer.models;

import android.app.Activity;

import com.ltthuc.freelancer.Utils.Constants;

/**
 * Created by Thuc on 3/3/2016.
 */
public class VideoModel extends BaseMedia{
    int option;
    int rawFile;
    int playTime;
    int breakTime;
    int id;




    public VideoModel(Activity context, int id, int option, int rawFile) {
         super(context,id,rawFile);
        this.option = option;

        initPlayTime();

    }

    public int getCurrentState() {
        return mCurrentState;
    }

    public void setCurrentState(int mCurrentState) {
        this.mCurrentState = mCurrentState;
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
                breakTime = Constants.BREAK_TIME_OPTION_1;
                break;
            case 2:
                if (id == 0 || id == 6) {
                    playTime = Constants.PLAY_TIME_OPTION_2;
                } else {
                    playTime = Constants.PLAY_TIME_OPTION_2 + Constants.BREAK_TIME_OPTION_2;
                }
                breakTime = Constants.BREAK_TIME_OPTION_2;
                break;
        }

    }

    public int getPlayTime() {
        return playTime;
    }



    public int getBreakTime() {
        return breakTime;
    }

}
