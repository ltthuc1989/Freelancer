package com.ltthuc.freelancer.models;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import com.ltthuc.freelancer.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by Thuc on 3/5/2016.
 */
public class AudioPlayerController {
    public MediaPlayer mediaPlayer;
    int rawFile;
    Context context;

    public List<BaseMedia> playList;
    int[] rawFiles ;

    private MediaPlayer.OnPreparedListener mOnPreparedListener;
    public AudioPlayerController(Context context, int[] rawFiles){
          
        mediaPlayer = new MediaPlayer();
        this.context = context;
        this.rawFiles = rawFiles;
        initPlayList();


    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    void initPlayList(){
        playList = new ArrayList<>();

        for(int i=0;i<rawFiles.length;i++){
            playList.add(new BaseMedia(context,i,rawFiles[i]));
        }
    }

    public void pause(){
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }
    public void start(){
        if(mediaPlayer!=null){
            mediaPlayer.start();
        }
    }

    public void stop(){
        mediaPlayer.stop();
    }
    public void playWithPreparing(int id){
        try {

            Uri uri =Uri.parse(playList.get(id).getMediaPath());

            mediaPlayer.setDataSource(context,uri);
            if((playList.get(id).rawFile== R.raw.music))
                mediaPlayer.setLooping(true);
           // mediaPlayer.setLooping(true);
            mediaPlayer.prepare();
            mediaPlayer.start();
        }catch (IOException error){


        }

    }
    public void onResume(){
        try {
            mediaPlayer.start();


        } catch (Exception we) {
            we.printStackTrace();
        }
    }
    public void resetMusic(int id){
        try {
            if(mediaPlayer!=null){
                mediaPlayer.reset();
            }
            Uri uri =Uri.parse(playList.get(id).getMediaPath());
            mediaPlayer.setDataSource(context,uri);
         //   mediaPlayer.setLooping(true);
            mediaPlayer.prepare();
            mediaPlayer.start();
        }catch (IOException error){


        }

    }
    public void stopPlayback() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;

            AudioManager am = (AudioManager)context.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
            am.abandonAudioFocus(null);
        }
    }

    public List<BaseMedia> getPlayList() {
        return playList;
    }

    public void setPlayList(List<BaseMedia> playList) {
        this.playList = playList;
    }
}
