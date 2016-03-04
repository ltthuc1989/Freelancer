package com.ltthuc.freelancer.Fragment;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;

import com.ltthuc.freelancer.CustomView.CircleProgressBar;
import com.ltthuc.freelancer.CustomView.CountDownTimerView;
import com.ltthuc.freelancer.CustomView.TextureVideoView;
import com.ltthuc.freelancer.R;
import com.ltthuc.freelancer.models.VideoModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Thuc on 3/2/2016.
 */
public class PlaceHolderFragment extends Fragment implements CircleProgressBar.ProgressBarCallback {


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
    int steps_option_1 = 0;
    int steps_option_2 = 0;
    int optionMode = 1;

    int[] rawFiles = {R.raw.video1, R.raw.video2, R.raw.video3, R.raw.video4, R.raw.video5, R.raw.video6, R.raw.video7};
    @Bind(R.id.btnOption1)
    Button btnOption1;
    @Bind(R.id.btnOption2)
    Button btnOption2;
    @Bind(R.id.txtCountDown)
    CountDownTimerView txtCountDown;
    private boolean isPaused = false;
    //Declare a variable to hold count down timer's paused status
    private boolean isCanceled = false;

    //Declare a variable to hold CountDownTimer remaining time
    private long timeRemaining = 0;
    public PlaceHolderFragment() {
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mVideoView != null) {
            mVideoView.stopPlayback();
            mVideoView = null;
        }
    }

    private void initData() {
        initVideoModels();
        initProgressBar();
        initVideoView();

    }
    private void initProgressBar(){
//        VideoModel videoModel = videoModels_Option_1.get(0);
//        int duration = videoModel.getPlayTime()+videoModel.getBreakTime();
//        customProgressBar.setTxtCountDown(txtCountDown);
//        customProgressBar.setDuration(duration);
    }
    private void initCoundownTimer(){
       // txtCountDown.setTime(Constants.COUNT_DOWN_TIME);
        txtCountDown.setOnTimerListener(new CountDownTimerView.TimerListener() {
            @Override
            public void onTick(long millisUntilFinished) {
                if(isPaused || isCanceled)
                {
                    //If the user request to cancel or paused the
                    //CountDownTimer we will cancel the current instance
                    txtCountDown.getmCountDownTimer().cancel();
                }
                else {
                    //Display the remaining seconds to app interface
                    //1 second = 1000 milliseconds
                    txtCountDown.setText("" + millisUntilFinished / 1000);
                    //Put count down timer remaining time in a variable
                    timeRemaining = millisUntilFinished;
                }
            }

            @Override
            public void onFinish() {
                txtCountDown.setText("Time up!");
            }
        });
    }

    private void initVideoView() {

        mVideoView.setmCircleProgressBar(customProgressBar);
        mVideoView.setPlayList(videoModels_Option_1);
        mVideoView.setVideoPath(videoModels_Option_1.get(0).getVideoPath());
        mVideoView.setMediaController(new MediaController(getActivity()));
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(final MediaPlayer mp) {
                mp.setLooping(true);
                mVideoView.startVideoPlayback();
                mVideoView.startVideoAnimation();
            }
        });

    }


    private void initVideoModels() {
        videoModels_Option_1 = new ArrayList<>();
        videoModels_Option_2 = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
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

    @OnClick({R.id.img_prev, R.id.img_play, R.id.img_next})
    public void onClick(View view) {
        MediaPlayer mediaPlayer = mVideoView.getMediaPlayer();
        switch (view.getId()) {

            case R.id.img_prev:
                mVideoView.playPrevious();
                break;
            case R.id.img_play:
                if (mediaPlayer.isPlaying()) {
                    isPaused = true;
                    mVideoView.getMediaPlayer().pause();
                } else if (!mediaPlayer.isPlaying() && mediaPlayer != null) {
                   isCanceled = false;
                    mediaPlayer.start();
                }
                break;
            case R.id.img_next:
                mVideoView.playNext();
                break;
        }
    }



    @Override
    public void finishProgress() {
        //do something

    }

}