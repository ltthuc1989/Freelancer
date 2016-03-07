package com.ltthuc.freelancer.models;

import android.content.Context;

/**
 * Created by Thuc on 3/5/2016.
 */
public class BaseMedia {
    int rawFile;
    int id;
    Context context;
    public final int STATE_PLAYING = 3;
    public final int STATE_PAUSED = 4;
    public final int STATE_PLAYBACK_COMPLETED = 5;
    public int mCurrentState =0;

    public BaseMedia(Context context,int id,int rawFile){
        this.context = context;
        this.id = id;
        this.rawFile = rawFile;


    }
    public String getMediaPath() {
        return "android.resource://" + context.getPackageName() + "/" + rawFile;
    }
}
