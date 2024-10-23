package com.jeffmony.audio

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackParameters
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ClippingMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.MergingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.jeffmony.LibApplication
import java.io.File


object PlayerManager {

    const val TAG = "PlayerManager"

//    init {
//        System.loadLibrary("media-handle")
//    }

    val player: SimpleExoPlayer by lazy {
        initExoPlayer(LibApplication.application!!)
    }


    var fa: MultiTrackRenderersFactory? = null
    private fun initExoPlayer(context: Context): SimpleExoPlayer {
        fa = FFMPEGRenderFactory(1, context);

        //        val player = SimpleExoPlayer.Builder(context, DefaultRenderersFactory(context)).setTrackSelector(MultiTrackSelector()).build()
        val player = SimpleExoPlayer.Builder(context, fa!!).setTrackSelector(MultiTrackSelector())
            .build() //        val player = SimpleExoPlayer.Builder(context, renderersFactory).setTrackSelector(MultiTrackSelector()).build()

            .also {

            }
        player.volume = 1f

        player.repeatMode = SimpleExoPlayer.REPEAT_MODE_OFF
        player.playWhenReady = true
        player.setPlaybackParameters(PlaybackParameters(0.9f))  // 设置播放器事件监听器
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


    fun play1(context: Context, path: String) {
        val dataSourceFactory = DefaultDataSourceFactory(context, Util.getUserAgent(context, context.packageName))
        val audioSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(Uri.fromFile(File(path))))


        player.playWhenReady = true
        player.prepare(audioSource)
    }


    fun play2(context: Context, path1: String, path2: String) {
        val dataSourceFactory = DefaultDataSourceFactory(context, Util.getUserAgent(context, context.packageName))
        val audioSource1 = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(Uri.fromFile(File(path1))))
        val audioSource2 = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(Uri.fromFile(File(path2))))

        var s1 = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(Uri.fromFile(File(path1))))
        var s2 = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(Uri.fromFile(File(path2))))
//        var audioSource = MergingMediaSource(true, ClippingMediaSource(s1, 0, 9 * 1_000_000), ClippingMediaSource(s2, 0, 3 * 1_000_000))
        var audioSource = MergingMediaSource(true, audioSource1, audioSource2)
        player.playWhenReady = true
        player.setMediaSource(audioSource)
        player.prepare()
    }

    fun play3(context: Context, path1: String, path2: String, path3: String) {
        val dataSourceFactory = DefaultDataSourceFactory(context, Util.getUserAgent(context, context.packageName))
        val audioSource1 = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(Uri.fromFile(File(path1))))
        val audioSource2 = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(Uri.fromFile(File(path2))))

        var s1 = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(Uri.fromFile(File(path1))))
        var s2 = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(Uri.fromFile(File(path2))))
        var s3 = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(Uri.fromFile(File(path3))))
        var audioSource = MergingMediaSource(true, ClippingMediaSource(s1, 0, 9 * 1_000_000), ClippingMediaSource(s2, 0, 3 * 1_000_000), s3)
        player.playWhenReady = true
        player.setMediaSource(audioSource)
        player.prepare()
    }


    fun setVolume(volume: Float) {
        fa?.audioSinkList?.forEachIndexed { index, multiMediaCodecAudioRenderer ->
            if(index==1){
                multiMediaCodecAudioRenderer.setVolume(0.5f)
            }else{
                multiMediaCodecAudioRenderer.setVolume(0.01f)
            }
        }
    }


}
