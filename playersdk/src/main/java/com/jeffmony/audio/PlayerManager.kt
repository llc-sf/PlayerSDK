package com.jeffmony.audio

import android.content.Context
import android.database.MergeCursor
import android.net.Uri
import android.os.Handler
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackParameters
import com.google.android.exoplayer2.Renderer
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.audio.AudioCapabilities
import com.google.android.exoplayer2.audio.AudioProcessor
import com.google.android.exoplayer2.audio.AudioRendererEventListener
import com.google.android.exoplayer2.audio.DefaultAudioSink
import com.google.android.exoplayer2.audio.MediaCodecAudioRenderer
import com.google.android.exoplayer2.drm.DrmSessionManager
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector
import com.google.android.exoplayer2.source.ClippingMediaSource
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.MergingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.jeffmony.LibApplication
import java.io.File


object PlayerManager {

    const val TAG = "PlayerManager"


    val player: SimpleExoPlayer by lazy {
        initExoPlayer(LibApplication.application!!)
    }




    private fun initExoPlayer(context: Context): SimpleExoPlayer {


        val player = SimpleExoPlayer.Builder(context, MultiTrackRenderersFactory(2,context)).setTrackSelector(MultiTrackSelector()).build()
//        val player = SimpleExoPlayer.Builder(context, renderersFactory).setTrackSelector(MultiTrackSelector()).build()

            .also {

            }
        player.volume = 1f

        player.repeatMode = SimpleExoPlayer.REPEAT_MODE_ALL
        player.playWhenReady = true
        player.setPlaybackParameters(PlaybackParameters(1f))  // 设置播放器事件监听器
        return player
    }


    fun releasePlayer() {
        player.stop()
    }


    private fun playByMediaSource(mediaSource: MediaSource,
                                  autoPlay: Boolean = false,
                                  repeatMode: Int = SimpleExoPlayer.REPEAT_MODE_OFF) {
        player.stop()
        player.playWhenReady = autoPlay
        player.prepare(mediaSource)
        player.repeatMode = repeatMode
    }




    fun play1(context: Context, path:String) {
        val dataSourceFactory = DefaultDataSourceFactory(context, Util.getUserAgent(context, context.packageName))
        val audioSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(Uri.fromFile(File(path))))


        player.playWhenReady = true
        player.prepare(audioSource)
    }


    fun play2(context: Context, path1:String,path2:String) {
        val dataSourceFactory = DefaultDataSourceFactory(context, Util.getUserAgent(context, context.packageName))
        val audioSource1 = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(Uri.fromFile(File(path1))))
        val audioSource2 = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(Uri.fromFile(File(path2))))

        var s1 = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(Uri.fromFile(File(path1))))
        var s2 = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(Uri.fromFile(File(path2))))
       var audioSource =  MergingMediaSource(true, ClippingMediaSource(s1,0,5*1_000_000), ClippingMediaSource(s2,0,5*1_000_000))
        player.playWhenReady = true
        player.prepare(audioSource)
    }

}
