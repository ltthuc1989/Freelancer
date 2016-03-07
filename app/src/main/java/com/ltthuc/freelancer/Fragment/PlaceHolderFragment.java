package com.ltthuc.freelancer.Fragment;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ltthuc.freelancer.CustomView.CircleProgressBar;
import com.ltthuc.freelancer.CustomView.CountDownTimerView;
import com.ltthuc.freelancer.CustomView.TextureVideoView;
import com.ltthuc.freelancer.R;
import com.ltthuc.freelancer.Utils.Constants;
import com.ltthuc.freelancer.models.AudioPlayerController;
import com.ltthuc.freelancer.models.VideoModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Thuc on 3/2/2016.
 */
public class PlaceHolderFragment extends Fragment implements CircleProgressBar.ProgressBarCallback, TextureVideoView.TextureVideoViewListener, CountDownTimerView.TimerListener {


    @Bind(R.id.video_view)
    TextureVideoView mVideoView;
    @Bind(R.id.custom_progressBar)
    CircleProgressBar customProgressBar;

    List<VideoModel> videoModels_Option_1;
    List<VideoModel> videoModels_Option_2;
    @Bind(R.id.img_prev)
    ImageView imgPrev;
    @Bind(R.id.img_play)
    ImageView imgPlay;
    @Bind(R.id.img_next)
    ImageView imgNext;
    int optionMode = 1;

    int[] rawFiles = {R.raw.video1, R.raw.video2, R.raw.video3, R.raw.video4, R.raw.video5, R.raw.video6, R.raw.video7};
    @Bind(R.id.btnOption1)
    Button btnOption1;
    @Bind(R.id.btnOption2)
    Button btnOption2;
    @Bind(R.id.txtCountDown)
    CountDownTimerView txtCountDown;
    @Bind(R.id.linNext)
    LinearLayout linNext;
    @Bind(R.id.txtNext)
    TextView txtNext;
    private boolean isPaused = false;
    //Declare a variable to hold count down timer's paused status
    private boolean isCanceled = false;
    public AudioPlayerController audioPlayerController;
    public AudioPlayerController audioPlayerControllerMusic;
    int[] rawFiles2 = {R.raw.start, R.raw.forwardback, R.raw.finish};
    int[] music = {R.raw.music};
    //Declare a variable to hold CountDownTimer remaining time
    private long timeRemaining = 0;
    long totalTime = 7 * 60000;
    boolean isPressBtnNext = false;
    boolean isPressBtnPrev = false;
    boolean isReset;
    int countStep = 0;
    int countStep_Option1 = 0;
    int countStep_Option2 = 0;
    boolean changePlayToBreak;
    boolean isFirstinit;


    public PlaceHolderFragment() {
    }

    static MediaPlayer mediaPlayer;

    private void pausePlayBack() {
        if (!isReset) {
            if (isPaused) {
                imgPlay.setBackgroundResource(R.drawable.ic_pause);
                audioPlayerControllerMusic.start();
            } else {
                audioPlayerControllerMusic.pause();
                imgPlay.setBackgroundResource(R.drawable.ic_play);
            }
            try{
                if (!mediaPlayer.isPlaying()) {
                    isCanceled = false;
                    isPaused = false;

                    txtCountDown.stopCountDown();
                    timeRemaining = timeRemaining - 2000;

                    resumeTimer(timeRemaining);

                    customProgressBar.start();
                    mediaPlayer.start();
                } else {
                    isPaused = true;
                    mVideoView.getMediaPlayer().pause();
                    customProgressBar.pause();
                }
            }catch (IllegalStateException error){
                isCanceled = false;
                isPaused = false;

                txtCountDown.stopCountDown();
                timeRemaining = timeRemaining - 2000;

                resumeTimer(timeRemaining);

                customProgressBar.start();
                mediaPlayer.start();
            }

        } else {
            onPlayBackgroundMusic();
            mVideoView.startVideoPlayback(1);
            mVideoView.startVideoAnimation();
            isReset = false;

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);
        initData();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        ButterKnife.unbind(this);

    }
   void  resume(){

       isCanceled = false;
       isPaused = false;
       if (txtCountDown.getmCountDownTimer() != null) {
           txtCountDown.stopCountDown();
       }
       if(mediaPlayer.isPlaying()) {
           mediaPlayer.start();
       }

       mVideoView.totalPlayTime = totalTime - countStep * Constants.ONE_MINUTE;
       restartTimer(mVideoView.totalPlayTime);
       // updateData();
       isPressBtnNext = true;
       customProgressBar.getObjectAnimator().end();
       mVideoView.playNext(1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mVideoView != null) {
            mVideoView.stopPlayback();
            mediaPlayer = null;
            audioPlayerControllerMusic.stopPlayback();
            audioPlayerController.stopPlayback();
            customProgressBar = null;
            txtCountDown = null;

            mVideoView = null;
        }
    }

    private void swichOption(int mode) {


        if (mode == 1) {
            btnOption1.setBackground(getResources().getDrawable(R.drawable.background_button_selected));
            btnOption2.setBackground(getResources().getDrawable(R.drawable.background_button_none));
            btnOption1.setTextColor(getResources().getColor(R.color.colorPrimary));
            btnOption2.setTextColor(Color.WHITE);
        } else {
            btnOption2.setBackground(getResources().getDrawable(R.drawable.background_button_selected));
            btnOption1.setBackground(getResources().getDrawable(R.drawable.background_button_none));
            btnOption2.setTextColor(getResources().getColor(R.color.colorPrimary));
            btnOption1.setTextColor(Color.WHITE);
        }

    }

    private void initData() {
        audioPlayerController = new AudioPlayerController(getActivity(), rawFiles2);
        audioPlayerControllerMusic = new AudioPlayerController(getActivity(), music);
        audioPlayerController.playWithPreparing(0);

        initVideoModels();
        txtCountDown.setOnTimerListener(this);
        swichOption(optionMode);
        initVideoView();

    }


    private void restartTimer(long millison) {
        txtCountDown.setTime(millison);
        txtCountDown.startCountDown();


    }

    private void resumeTimer(long timeRemaining) {
        restartTimer(timeRemaining);
        txtCountDown.startCountDown();
    }


    private void initVideoView() {
        if (!isReset) {
            mVideoView.setCircleProgressBar(customProgressBar);

            mVideoView.setTextureListener(this);
        }
        if (optionMode == 1) {
            mVideoView.setPlayList(videoModels_Option_1);
            mVideoView.setVideoPath(videoModels_Option_1.get(0).getMediaPath());
        } else {
            mVideoView.setPlayList(videoModels_Option_2);
            mVideoView.setVideoPath(videoModels_Option_2.get(0).getMediaPath());
        }


        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(final MediaPlayer mp) {
                if (isReset) {
                    txtCountDown.setTime(Constants.COUNT_DOWN_TIME);
                    txtCountDown.startCountDown();
                }
                mp.setLooping(true);
                onPlayBackgroundMusic();
                mediaPlayer = mVideoView.getMediaPlayer();

                mVideoView.startVideoPlayback(1);
                mVideoView.startVideoAnimation();
                isReset = false;
                isFirstinit = true;
            }
        });


    }


    private void initVideoModels() {
        videoModels_Option_1 = new ArrayList<>();
        videoModels_Option_2 = new ArrayList<>();
        for (int i = 0; i < Constants.MEDIAPLAYER_SIZE; i++) {
            videoModels_Option_1.add(new VideoModel(getActivity(), i, 1, rawFiles[i]));
            videoModels_Option_2.add(new VideoModel(getActivity(), i, 2, rawFiles[i]));
        }
    }

    private Drawable getThumbnail(String path) {
        Drawable drawable;
        Bitmap bitmap;
        try {
            bitmap = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Images.Thumbnails.MINI_KIND);
            drawable = new BitmapDrawable(getResources(), bitmap);
        } catch (Exception e) {
            return null;
        }
        return drawable;
    }

    @OnClick({R.id.img_prev, R.id.img_play, R.id.img_next, R.id.btnOption1, R.id.btnOption2})
    public void onClick(View view) {
        MediaPlayer mediaPlayer = mVideoView.getMediaPlayer();
        switch (view.getId()) {

            case R.id.img_prev:
                if (countStep > 0) {
                    countStep = countStep - 1;
                }

                linNext.setVisibility(View.GONE);
                audioPlayerController.resetMusic(1);
                isCanceled = false;
                isPaused = false;
                if (txtCountDown.getmCountDownTimer() != null) {
                    txtCountDown.stopCountDown();
                }

                mVideoView.totalPlayTime = (Constants.MEDIAPLAYER_SIZE - countStep) * Constants.ONE_MINUTE;
//
                restartTimer(mVideoView.totalPlayTime);

                // updateData();
                isPressBtnPrev = true;
                customProgressBar.getObjectAnimator().end();
                mVideoView.playPrevious(1);

                break;
            case R.id.img_play:
                pausePlayBack();
                break;


            case R.id.img_next:
                if (countStep < Constants.MEDIAPLAYER_SIZE) {
                    countStep = countStep + 1;
                }
                linNext.setVisibility(View.GONE);
                if (countStep == Constants.MEDIAPLAYER_SIZE)
                    break;
                audioPlayerController.resetMusic(1);
                isCanceled = false;
                isPaused = false;
                if (txtCountDown.getmCountDownTimer() != null) {
                    txtCountDown.stopCountDown();
                }


//                if (optionMode == 1) {
//                    mVideoView.totalPlayTime = mVideoView.totalPlayTime - Constants.PLAY_TIME_OPTION_1;
//                } else {
//                    mVideoView.totalPlayTime = mVideoView.totalPlayTime - Constants.PLAY_TIME_OPTION_2;
//                }
                mVideoView.totalPlayTime = totalTime - countStep * Constants.ONE_MINUTE;
                restartTimer(mVideoView.totalPlayTime);
                // updateData();
                isPressBtnNext = true;
                customProgressBar.getObjectAnimator().end();
                mVideoView.playNext(1);
                //onProgressCompleted();
                break;
            case R.id.btnOption1:
                release();
                optionMode = 1;
                swichOption(optionMode);
                txtCountDown.stopCountDown();
                initVideoView();

                break;
            case R.id.btnOption2:
                optionMode = 2;
                release();
                txtCountDown.stopCountDown();
                swichOption(optionMode);
                initVideoView();
                break;
        }
    }

    @Override
    public void finishProgress() {
        //do something

    }

    @Override
    public void onPlayCompleted() {
        //linNext.setVisibility(View.VISIBLE);
        // mVideoView.playNext();

    }

    @Override
    public void onProgressCompleted() {
        //  Log.d("borainProgressCompled",String.valueOf(customProgressBar.getCurrentProgress()));
        if (!isPaused && !isReset & !isPressBtnNext & !isPressBtnPrev) {
            updateData();

        }
        if (isPressBtnNext) {
            isPressBtnNext = false;
        }
        if (isPressBtnPrev) {
            isPressBtnPrev = false;
        }

    }

    void release() {
        linNext.setVisibility(View.GONE);
        imgPlay.setBackgroundResource(R.drawable.ic_pause);
        audioPlayerControllerMusic.stop();
        audioPlayerController.stop();
        isReset = true;

        isPaused = false;
        //Declare a variable to hold count down timer's paused status
        isCanceled = false;
        mVideoView.totalPlayTime = Constants.COUNT_DOWN_TIME;
        //Declare a variable to hold CountDownTimer remaining time
        timeRemaining = 0;
        totalTime = 7 * 60000;
        isPressBtnNext = false;
        isPressBtnPrev = false;
        customProgressBar.getObjectAnimator().end();
        mVideoView.setCurrentPositionMedia(0);

        // initData();


    }

    @Override
    public void onDelaySecondForPlayNext(Boolean b) {
        //  isPaused = b;
    }

    int i = 0;

    void updateData() {
        isPaused = false;
        isCanceled = false;
        int currentMedia = 0;
        try {
            currentMedia = mVideoView.getCurrentPositionMedia();

        } catch (NullPointerException error) {

        }
        if (optionMode == 1) {
            mVideoView.totalPlayTime = mVideoView.totalPlayTime - Constants.PLAY_TIME_OPTION_1;
            if (mVideoView.totalPlayTime != Constants.BREAK_TIME_OPTION_1) {
                if (isVisibleNEXT(mVideoView.totalPlayTime)) {
                    linNext.setVisibility(View.VISIBLE);
                    videoModels_Option_1.get(currentMedia).setCurrentState(Constants.STATE_PLAYBACK_COMPLETED);
                    countStep = countStep + 1;
                    mVideoView.playNext(1);
                } else {
                    if (isPressBtnNext && !isPressBtnPrev) {
                        mVideoView.playNext(1);
                        isPressBtnNext = false;
                    } else if (!isPressBtnNext && !isPressBtnPrev) {
                        mVideoView.resetCircleProgressBar(currentMedia, 1);
                    } else if (isPressBtnPrev && !isPressBtnNext) {
                        mVideoView.playPrevious(1);
                    }

                    linNext.setVisibility(View.GONE);

                }
            } else {
                linNext.setVisibility(View.GONE);
                mVideoView.resetCircleProgressBar(currentMedia, 1);
                mVideoView.getMediaPlayer().seekTo(20000);
                mVideoView.getMediaPlayer().stop();
            }
        } else {
            if (!changePlayToBreak) {
                mVideoView.totalPlayTime = mVideoView.totalPlayTime - Constants.PLAY_TIME_OPTION_2;
            } else {
                mVideoView.totalPlayTime = mVideoView.totalPlayTime - Constants.BREAK_TIME_OPTION_2;
                changePlayToBreak = false;
            }
            if (mVideoView.totalPlayTime != Constants.BREAK_TIME_OPTION_2) {
                if (isVisibleNEXT2(mVideoView.totalPlayTime)) {
                    linNext.setVisibility(View.VISIBLE);
                    videoModels_Option_2.get(currentMedia).setCurrentState(Constants.STATE_PLAYBACK_COMPLETED);
                    changePlayToBreak = true;
                    countStep = countStep + 1;
                    mVideoView.playNext(2);
                } else {
                    if (isPressBtnNext && !isPressBtnPrev) {
                        mVideoView.playNext(1);
                        isPressBtnNext = false;
                    } else if (!isPressBtnNext && !isPressBtnPrev) {
                        mVideoView.resetCircleProgressBar(currentMedia, 1);
                    } else if (isPressBtnPrev && !isPressBtnNext) {
                        mVideoView.playPrevious(1);
                    }

                    linNext.setVisibility(View.GONE);

                }
            } else {
                linNext.setVisibility(View.GONE);
                mVideoView.resetCircleProgressBar(currentMedia, 2);
                mVideoView.getMediaPlayer().seekTo(20000);
                mVideoView.getMediaPlayer().stop();
            }
        }

    }

    boolean isVisibleNEXT(Long duration) {
        Integer i = duration.intValue();
        boolean isVisible = false;
        switch (i) {
            case 390000:
                isVisible = true;
                break;
            case 330000:
                isVisible = true;
                break;
            case 270000:
                isVisible = true;
                break;
            case 210000:
                isVisible = true;
                break;
            case 150000:
                isVisible = true;
                break;
            case 90000:
                isVisible = true;
                break;


        }
        return isVisible;

    }

    boolean isVisibleNEXT2(Long duration) {
        Integer i = duration.intValue();
        boolean isVisible = false;
        switch (i) {
            case 375000:
                isVisible = true;
                break;
            case 315000:
                isVisible = true;
                break;
            case 255000:
                isVisible = true;
                break;
            case 195000:
                isVisible = true;
                break;
            case 135000:
                isVisible = true;
                break;
            case 75000:
                isVisible = true;
                break;


        }
        return isVisible;

    }

    @Override
    public void onPlayBackgroundMusic() {
        try {
            if (!isReset) {
                audioPlayerControllerMusic.playWithPreparing(0);
            } else {
                audioPlayerControllerMusic.resetMusic(0);
            }
        } catch (IllegalStateException error) {
            audioPlayerControllerMusic.resetMusic(0);
        }

    }

    @Override
    public void onTick(long millisUntilFinished) {
        //   Log.d("CurrentTick", String.valueOf(millisUntilFinished / 1000));
        // linNext.setVisibility(View.GONE);
        if (isPaused || isCanceled) {
            //If the user request to cancel or paused the
            //CountDownTimer we will cancel the current instance
            Log.d("timeMillion", String.valueOf((millisUntilFinished / 1000) % 60));
            Log.d("timeRemaining", String.valueOf((timeRemaining / 1000) % 60));
            txtCountDown.getmCountDownTimer().cancel();

        } else {
            //Display the remaining seconds to app interface
            //1 second = 1000 milliseconds
            //  txtCountDown.setText("" + millisUntilFinished / 1000);
            //Put count down timer remaining time in a variable

            timeRemaining = millisUntilFinished;
            Log.d("CurrentTimeRemaining", String.valueOf((timeRemaining / 1000) % 60));
        }

    }

    @Override
    public void onFinish() {
        audioPlayerController.resetMusic(2);
        release();
    }

    @Override
    public void onPause() {
        super.onPause();


        try {
            pausePlayBack();
        } catch (Exception we) {
            we.printStackTrace();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        try {
            if (isFirstinit) {
                resume();
            }

        } catch (Exception we) {
            we.printStackTrace();
        }
    }
}