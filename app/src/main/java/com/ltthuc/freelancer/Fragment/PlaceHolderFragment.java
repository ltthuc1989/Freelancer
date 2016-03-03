package com.ltthuc.freelancer.Fragment;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;

import com.ltthuc.freelancer.CustomView.CircleProgressBar;
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
public class PlaceHolderFragment extends Fragment {


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

    public PlaceHolderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);
        playVideo(0);
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

    }

    private void initVideoModels() {
        videoModels_Option_1 = new ArrayList<>();
        videoModels_Option_2 = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            videoModels_Option_1.add(new VideoModel(i, 1, rawFiles[i]));
            videoModels_Option_2.add(new VideoModel(i, 2, rawFiles[i]));
        }
    }

    private void playVideo(int id) {
        mVideoView.setmCircleProgressBar(customProgressBar);
        mVideoView.setVideoPath(getVideoPath());
        mVideoView.setMediaController(new MediaController(getActivity()));
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(final MediaPlayer mp) {
                startVideoPlayback();
                startVideoAnimation();
            }
        });
    }



    private void startVideoPlayback() {
        // "forces" anti-aliasing - but increases time for taking frames - so keep it disabled
        // mVideoView.setScaleX(1.00001f);
        mVideoView.start();

    }

    private void startVideoAnimation() {
        mVideoView.animate().setDuration(mVideoView.getDuration()).start();
    }

    private String getVideoPath() {
        return "android.resource://" + getActivity().getPackageName() + "/" + R.raw.video;
    }


    @OnClick({R.id.img_prev, R.id.img_play, R.id.img_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_prev:
                if (optionMode == 1) {
                    if (steps_option_1 > 0) {
                        steps_option_1 = steps_option_1 - 1;
                    }
                } else {
                    if (steps_option_1 > 0) {
                        steps_option_2 = steps_option_2 - 1;
                    }
                }
                break;
            case R.id.img_play:
                break;
            case R.id.img_next:
                if (optionMode == 1) {
                    if (steps_option_1 < 6) {
                        steps_option_1 = steps_option_1 + 1;
                    }
                } else {
                    if (steps_option_1 < 6) {
                        steps_option_2 = steps_option_2 + 1;
                    }
                }
                break;
        }
    }
}