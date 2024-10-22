package com.jeffmony.playersdk.impl;

import android.content.Context;
import android.net.Uri;
import android.view.Surface;

import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.jeffmony.playersdk.LogUtils;
import com.jeffmony.playersdk.PlayerParams;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Map;

public class ExoPlayerImpl extends PlayerImpl {

    private static final int PREPARE_NULL = 0x0;
    private static final int PREPARING_STATE = 0x1;
    private static final int PREPARED_STATE = 0x2;

    private Context mContext;
    private SimpleExoPlayer mPlayer;
    private MediaSource mMediaSource;
    private int mPrepareState = PREPARE_NULL;

    private boolean mIsInitPlayerListener = false;
    private PlayerEventListener mEventListener;
    private boolean mIsLooping = false;
    private boolean mUseOkHttp = false;

    public ExoPlayerImpl(Context context, PlayerParams params) {
        super(context, params);
        mContext = context.getApplicationContext();
        mPlayer = new SimpleExoPlayer.Builder(context).build();
        initPlayerParams(params);
    }


    private void initPlayerParams(PlayerParams params) {
        if (params == null) return;
        mUseOkHttp = params.useOkHttp();
    }

    @Override
    public void setSonicVolume(float volume) {
        PlaybackParameters parameters = new PlaybackParameters(1f);
        mPlayer.setPlaybackParameters(parameters);
    }

    @Override
    public void setDataSource(FileDescriptor fd, long offset, long length) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
    }

    @Override
    public void setDataSource(String path) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
    }

    @Override
    public void setDataSource(FileDescriptor fd) throws IOException, IllegalArgumentException, IllegalStateException {
    }

    @Override
    public void setDataSource(Context context, Uri uri) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
    }

    @Override
    public void setDataSource(Context context, Uri uri, Map<String, String> headers) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
    }

    @Override
    public void setSurface(Surface surface) {
        mPlayer.setVideoSurface(surface);
    }

    @Override
    public void prepareAsync() throws IllegalStateException {
        super.prepareAsync();
        if (!mIsInitPlayerListener) {
            initPlayerListener();
        }
        mPrepareState = PREPARING_STATE;
        mPlayer.prepare(mMediaSource);
    }

    @Override
    public void start() throws IllegalStateException {
        mPlayer.setPlayWhenReady(true);
        super.start();
    }

    @Override
    public void pause() throws IllegalStateException {
        mPlayer.setPlayWhenReady(false);
        super.pause();
    }

    @Override
    public void setSpeed(float speed) {
        PlaybackParameters parameters = new PlaybackParameters(speed);
        mPlayer.setPlaybackParameters(parameters);
    }

    @Override
    public void stop() throws IllegalStateException {
        mPlayer.stop();
        super.stop();
    }

    @Override
    public void reset() {
        mPlayer.stop();
        super.reset();
    }

    @Override
    public void release() {
    }

    @Override
    public long getCurrentPosition() {
        return mPlayer.getCurrentPosition();
    }

    @Override
    public long getDuration() {
        return mPlayer.getDuration();
    }

    @Override
    public boolean isPlaying() {
        return mPlayer.isPlaying();
    }

    @Override
    public void seekTo(long msec) throws IllegalStateException {
        mPlayer.seekTo(msec);
        super.seekTo(msec);
    }

    @Override
    public void setLooping(boolean isLooping) {
        if (isLooping) {
            if (!mIsLooping) {
                mPlayer.setRepeatMode(Player.REPEAT_MODE_ONE);
            }
            mIsLooping = true;
        } else {
            mPlayer.setRepeatMode(Player.REPEAT_MODE_OFF);
            mIsLooping = false;
        }
    }

    @Override
    public boolean isLooping() {
        return mIsLooping;
    }

    private void initPlayerListener() {
    }



    private class PlayerEventListener implements Player.Listener {


        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            LogUtils.d("onPlayerStateChanged playWhenReady="+playWhenReady+", playbackState="+playbackState);
            switch(playbackState) {
                case Player.STATE_BUFFERING:
                    break;
                case Player.STATE_IDLE:
                    break;
                case Player.STATE_READY:
                    if (mPrepareState == PREPARING_STATE) {
                        notifyOnPrepared();
                        mPrepareState = PREPARED_STATE;
                    }
                    break;
                case Player.STATE_ENDED:
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onIsPlayingChanged(boolean isPlaying) {
            LogUtils.d("onIsPlayingChanged isPlaying="+isPlaying);
        }
    }


}
