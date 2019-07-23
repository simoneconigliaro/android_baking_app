package com.project.simoneconigliaro.bakingapp.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.project.simoneconigliaro.bakingapp.R;
import com.project.simoneconigliaro.bakingapp.models.Step;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Simone on 16/12/2017.
 */

public class StepFragment extends Fragment {

    private static final String STATE_POSITION_KEY = "state_position";
    private static final String CURRENT_POSITION_VIDEO_KEY = "current_position";
    @BindView(R.id.player_view)
    SimpleExoPlayerView mPlayerView;
    SimpleExoPlayer mExoPlayer;
    @Nullable
    @BindView(R.id.button_prev_step)
    ImageView mPrevButton;
    @Nullable
    @BindView(R.id.button_next_step)
    ImageView mNextButton;
    @Nullable
    @BindView(R.id.tv_description)
    TextView descriptionTextView;
    @Nullable
    @BindView(R.id.tv_short_description_step)
    TextView shortDescriptionTextView;
    @Nullable
    @BindView(R.id.iv_thumbnail)
    ImageView thumbnailImageView;
    ArrayList<Step> mSteps;
    int mPosition;
    long currentPositionVideo;
    MediaSource mMediaSource;
    private View.OnClickListener onClickListenerButton = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.button_prev_step:
                    mPosition--;
                    releasePlayer();
                    restoreVisibilityViews();
                    setViews(mPosition);
                    break;

                case R.id.button_next_step:
                    mPosition++;
                    releasePlayer();
                    restoreVisibilityViews();
                    setViews(mPosition);
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_step, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mPrevButton.setOnClickListener(onClickListenerButton);
        mNextButton.setOnClickListener(onClickListenerButton);


        if (getArguments() != null) {
            mSteps = getArguments().getParcelableArrayList(RecipeFragment.ARRAYLIST_STEPS_KEY);
            mPosition = getArguments().getInt(RecipeFragment.POSITION_KEY, 0);
        } else {
            Intent intentThatStartedActivity = getActivity().getIntent();
            if (intentThatStartedActivity != null) {
                mSteps = intentThatStartedActivity.getParcelableArrayListExtra(RecipeFragment.ARRAYLIST_STEPS_KEY);
                mPosition = intentThatStartedActivity.getIntExtra(RecipeFragment.POSITION_KEY, 0);
            }
        }
        if (savedInstanceState != null) {
            mPosition = savedInstanceState.getInt(STATE_POSITION_KEY);
            currentPositionVideo = savedInstanceState.getLong(CURRENT_POSITION_VIDEO_KEY);
        }
        setViews(mPosition);
    }

    private void setViews(int position) {


        if (position == 0) {
            mPrevButton.setVisibility(View.INVISIBLE);
        }
        if (position == mSteps.size() - 1) {
            mNextButton.setVisibility(View.INVISIBLE);
        }
        String description = mSteps.get(position).getDescription();
        descriptionTextView.setText(description);

        String shortDescription = mSteps.get(position).getShortDescription();
        shortDescriptionTextView.setText(shortDescription);

        String thumbnailUrl = mSteps.get(position).getThumbnailURL();
        if(TextUtils.isEmpty(thumbnailUrl)){
            thumbnailImageView.setVisibility(View.GONE);
        } else {
            Picasso.with(getContext()).load(thumbnailUrl).error(R.drawable.placeholder_error).into(thumbnailImageView);
        }

        String videoUrl = mSteps.get(position).getVideoURL();
        initializePlayer(videoUrl);
    }

    private void restoreVisibilityViews() {
        mPrevButton.setVisibility(View.VISIBLE);
        mNextButton.setVisibility(View.VISIBLE);
        mPlayerView.setVisibility(View.VISIBLE);
    }

    private void initializePlayer(String videoUrl) {
        if (!TextUtils.isEmpty(videoUrl)) {
            Uri mediaUri = Uri.parse(videoUrl);
            if (mExoPlayer == null) {
                TrackSelector trackSelector = new DefaultTrackSelector();
                LoadControl loadControl = new DefaultLoadControl();
                mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
                mPlayerView.setPlayer(mExoPlayer);
                String userAgent = Util.getUserAgent(getContext(), "Baking App");
                mMediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                        getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
                mExoPlayer.setPlayWhenReady(true);
                mExoPlayer.prepare(mMediaSource);
            }
        } else {
            mPlayerView.setVisibility(View.GONE);
        }
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_POSITION_KEY, mPosition);
        outState.putLong(CURRENT_POSITION_VIDEO_KEY, currentPositionVideo);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer != null) {
            mExoPlayer.setPlayWhenReady(false);
            currentPositionVideo = mExoPlayer.getCurrentPosition();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mExoPlayer != null) {
            mExoPlayer.setPlayWhenReady(true);
            mExoPlayer.prepare(mMediaSource);
            mExoPlayer.seekTo(currentPositionVideo);

        }
    }
}
