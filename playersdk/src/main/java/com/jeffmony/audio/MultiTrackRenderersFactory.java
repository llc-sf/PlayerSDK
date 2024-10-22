package com.jeffmony.audio;

import android.content.Context;
import android.os.Handler;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.Renderer;
import com.google.android.exoplayer2.audio.AudioProcessor;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.audio.AudioSink;
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;

import java.util.ArrayList;
import java.util.List;

public class MultiTrackRenderersFactory extends DefaultRenderersFactory {
    public int audioTrackCnt;

    private final List<MultiMediaCodecAudioRenderer> audioSinkList = new ArrayList<>();

    private final List<AudioSink> audioSinks = new ArrayList<>();

    public MultiTrackRenderersFactory(int audioTrackCnt, Context context) {
        super(context);
        this.audioTrackCnt = audioTrackCnt;
    }

    public int getAudioTrackCnt() {
        return audioTrackCnt;
    }

    public void setAudioTrackCnt(int audioTrackCnt) {
        this.audioTrackCnt = audioTrackCnt;

    }


    @Override
    protected void buildAudioRenderers(Context context, @ExtensionRendererMode int extensionRendererMode, MediaCodecSelector mediaCodecSelector, boolean enableDecoderFallback, AudioSink audioSink, Handler eventHandler, AudioRendererEventListener eventListener, ArrayList<Renderer> out) {
        audioSinkList.clear();
        for (int i = 0; i < audioTrackCnt; i++) {
//            out.add(ffmpegRenderer);
            MultiMediaCodecAudioRenderer multiMediaCodecAudioRenderer = new MultiMediaCodecAudioRenderer(i, context,
                    MediaCodecSelector.DEFAULT);
            out.add(multiMediaCodecAudioRenderer);
        }
    }
}
