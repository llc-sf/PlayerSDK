package com.jeffmony.audio

import android.content.Context
import com.google.android.exoplayer2.Renderer
import com.google.android.exoplayer2.audio.AudioRendererEventListener
import com.google.android.exoplayer2.audio.AudioSink
import com.google.android.exoplayer2.ext.ffmpeg.FfmpegAudioRenderer
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector


class FFMPEGRenderFactory(audioTrackCnt: Int, context: Context) :
    MultiTrackRenderersFactory(audioTrackCnt, context) {

    init {
        setExtensionRendererMode(EXTENSION_RENDERER_MODE_PREFER)
    }

    override fun buildAudioRenderers(context: Context,
                                     extensionRendererMode: Int,
                                     mediaCodecSelector: MediaCodecSelector,
                                     enableDecoderFallback: Boolean,
                                     audioSink: AudioSink,
                                     eventHandler: android.os.Handler,
                                     eventListener: AudioRendererEventListener,
                                     out: ArrayList<Renderer>) {
        super.buildAudioRenderers(context, extensionRendererMode, mediaCodecSelector, enableDecoderFallback, audioSink, eventHandler, eventListener, out)
    }

}