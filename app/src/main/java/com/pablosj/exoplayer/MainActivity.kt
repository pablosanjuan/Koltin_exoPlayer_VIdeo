package com.pablosj.exoplayer

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), Player.EventListener {

    lateinit var simpleExoPlayer: SimpleExoPlayer
    private var playBackPositionn = 0L
    private val dashUrl = "http://rdmedia.bbc.co.uk/dash/ondemand/bbb/2/client_manifest-separate_init.mpd"
    private val bandWidthMeter by lazy {
        DefaultBandwidthMeter()
    }
    private val adaptiveTrackSelectionFactory by lazy {
        AdaptiveTrackSelection.Factory(bandWidthMeter)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        initializeExoPLayer()
    }

    override fun onStop() {
        super.onStop()
        releaseExoPLayer()
    }

    private fun initializeExoPLayer(){
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(
            DefaultRenderersFactory(this),
            DefaultTrackSelector(adaptiveTrackSelectionFactory),
            DefaultLoadControl()
        )
        prepareExoPLayer()
        ep_simple_exo_player.player = simpleExoPlayer
        simpleExoPlayer.seekTo(playBackPositionn)
        simpleExoPlayer.playWhenReady = true
        simpleExoPlayer.addListener(this)
    }
    private fun releaseExoPLayer(){
        playBackPositionn = simpleExoPlayer.currentPosition
        simpleExoPlayer.release()
    }

    private fun buidMediaSource(uri: Uri): MediaSource {
        val dataSourceFactory = DefaultHttpDataSourceFactory("ua", bandWidthMeter)
        val dashChunckSourceFactory = DefaultDashChunkSource.Factory(dataSourceFactory)
        return DashMediaSource(uri, dataSourceFactory, dashChunckSourceFactory, null, null)
    }

    private fun prepareExoPLayer(){
        val uri = Uri.parse(dashUrl)
        val mediaSource = buidMediaSource(uri)
        simpleExoPlayer.prepare(mediaSource)
    }

    override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {
    }

    override fun onTracksChanged(trackGroups: TrackGroupArray, trackSelections: TrackSelectionArray) {
    }

    override fun onPlayerError(error: ExoPlaybackException) {
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        if (playbackState == Player.STATE_BUFFERING)
            pb_progress_bar.visibility = View.VISIBLE
        else if (playbackState == Player.STATE_READY)
            pb_progress_bar.visibility == View.INVISIBLE
    }

    override fun onLoadingChanged(isLoading: Boolean) {
    }

    override fun onPositionDiscontinuity() {
    }

    override fun onRepeatModeChanged(repeatMode: Int) {
    }

    override fun onTimelineChanged(timeline: Timeline?, manifest: Any?) {
    }
}