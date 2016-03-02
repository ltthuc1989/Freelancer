package com.ltthuc.freelancer.Fragment;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;

import com.ltthuc.freelancer.CustomView.CircleProgressBar;
import com.ltthuc.freelancer.CustomView.TextureVideoView;
import com.ltthuc.freelancer.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Thuc on 3/2/2016.
 */
public class PlaceHolderFragment extends Fragment {


    @Bind(R.id.video_view)
    TextureVideoView mVideoView;
    @Bind(R.id.custom_progressBar)
    CircleProgressBar customProgressBar;

    public PlaceHolderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ButterKnife.bind(this, rootView);
      //  initCircleProgressBar();
        initVideoView();
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

//    void initCircleProgressBar(){
//
//       // customProgressBar.setColor(Constants.BLACK);
//        customProgressBar.setDuration(30000);
//        customProgressBar.setProgressWithAnimation(customProgressBar.getMax());
//
//    }
    private void initVideoView() {
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





}