package com.easysteps.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.easysteps.R;
import com.easysteps.retrofit.URLs;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

public class ExoPlayerActivity extends AppCompatActivity {

    SimpleExoPlayerView exoPlayerView;
    ImageView img_link;

    // creating a variable for exoplayer
    SimpleExoPlayer exoPlayer;
    String file_type;
    private String videoURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exo_player);
        exoPlayerView = findViewById(R.id.idExoPlayerVIew);
        img_link = findViewById(R.id.img_link);

        Intent intent = getIntent();
        videoURL = intent.getStringExtra("deal_link");
        file_type = intent.getStringExtra("deal_file_type");

        if (file_type.equals("1")) {
            if (img_link.getVisibility() == View.GONE) {
                img_link.setVisibility(View.VISIBLE);
                exoPlayerView.setVisibility(View.GONE);
            }
            Glide.with(ExoPlayerActivity.this).load(URLs.IMAGE_URL + videoURL).into(img_link);
        } else {
            if (exoPlayerView.getVisibility() == View.GONE) {
                exoPlayerView.setVisibility(View.VISIBLE);
                img_link.setVisibility(View.GONE);
            }
            ProgressDialog progress = new ProgressDialog(this);
            progress.setMessage("Video is Loading...) ");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            Log.e("TAG", "onBindViewHolder: " + videoURL);
            try {
                BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
                exoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
                Uri videouri = Uri.parse(videoURL);
                DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
                ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                MediaSource mediaSource = new ExtractorMediaSource(videouri, dataSourceFactory, extractorsFactory, null, null);
                exoPlayerView.setPlayer(exoPlayer);
                exoPlayer.prepare(mediaSource);

                if (exoPlayer.isLoading()) {
                    progress.show();
                } else {
                    progress.dismiss();
                    exoPlayer.setPlayWhenReady(true);
                }

                exoPlayer.addListener(new ExoPlayer.EventListener() {


                    @Override
                    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

                    }

                    @Override
                    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

                    }

                    @Override
                    public void onLoadingChanged(boolean isLoading) {
                        if (exoPlayer.isLoading()) {
                            progress.show();
                        } else {
                            progress.dismiss();
                        }
                    }

                    @Override
                    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                        progress.dismiss();
                        exoPlayer.setPlayWhenReady(true);
                    }

                    @Override
                    public void onRepeatModeChanged(int repeatMode) {

                    }

                    @Override
                    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

                    }

                    @Override
                    public void onPlayerError(ExoPlaybackException error) {
                        progress.dismiss();
                    }

                    @Override
                    public void onPositionDiscontinuity(int reason) {

                    }


                    @Override
                    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

                    }

                    @Override
                    public void onSeekProcessed() {

                    }
                });
            } catch (Exception e) {
                Log.e("TAG", "Error : " + e.toString());
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (!file_type.equals("1")) {
            exoPlayer.setPlayWhenReady(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!file_type.equals("1")) {
            exoPlayer.setPlayWhenReady(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!file_type.equals("1")) {
            exoPlayer.stop();
            exoPlayer.release();
        }
    }
}