package kg.programm.programmingapp.ui.lecture

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import kg.programm.programmingapp.R
import kg.programm.programmingapp.databinding.ActivityVideoFullBinding
import kg.programm.programmingapp.util.*

class VideoFullActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVideoFullBinding

    private var player: SimpleExoPlayer? = null
    private lateinit var listener: VideoPlayerListener

    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0

    private var link: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoFullBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            @Suppress("DEPRECATION")
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        playbackPosition = intent.getLongExtra(EXTRA_TIME_POS, 0)
        link = intent.getStringExtra(EXTRA_VIDEO_URL) ?: ""
        if (link.take(12) != "https://fire" && link.take(10) != "http://176") {
            binding.videoView.visibility = View.GONE
            binding.youtubePlayerView.visibility = View.VISIBLE
            binding.progressBar.hide()

            lifecycle.addObserver(binding.youtubePlayerView)

            binding.youtubePlayerView.addYouTubePlayerListener(object :
                AbstractYouTubePlayerListener() {

                override fun onError(
                    youTubePlayer: YouTubePlayer,
                    error: PlayerConstants.PlayerError
                ) {
                    toast("error $error")
                }

                override fun onReady(youTubePlayer: YouTubePlayer) {
                    val id = getYoutubeId(link)
                    Log.d("NURIKO", "onReady (line 72): $id")
                    youTubePlayer.cueVideo(id, 0F)
                }

            })

        } else {
            binding.videoView.visibility = View.VISIBLE
            binding.youtubePlayerView.visibility = View.GONE
            listener = VideoPlayerListener()
            initVideoPlayer()
        }

        binding.arrBack.setOnClickListener {
            onBackPressed()
        }

    }

    private fun getYoutubeId(url: String): String {
        if (url.contains("watch")) {
            var ret = ""
            var i = 32
            while (i <= 42) {
                ret += url[i]
                i++
            }
            return ret
        } else {
            var ret = ""
            var i = 17
            while (i <= 27) {
                ret += url[i]
                i++
            }
            return ret
        }
    }

    private fun initVideoPlayer() {

        if (player == null) {
            val trackSelector = DefaultTrackSelector(this)
            trackSelector.setParameters(
                trackSelector.buildUponParameters().setMaxVideoSizeSd()
            )
            player = SimpleExoPlayer.Builder(this)
                .setTrackSelector(trackSelector)
                .build()
        }
        binding.videoView.player = player

        player!!.setMediaItem(MediaItem.fromUri(link))
        player!!.playWhenReady = playWhenReady
        player!!.seekTo(currentWindow, playbackPosition)
        player!!.addListener(listener)
        player!!.prepare()

        binding.arrBack.setOnClickListener {
            onBackPressed()
        }

    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    private fun releasePlayer() {
        if (player != null) {
            playWhenReady = player!!.playWhenReady
            playbackPosition = player!!.currentPosition
            currentWindow = player!!.currentWindowIndex
            player!!.release()
            player = null
        }
    }

    inner class VideoPlayerListener : Player.Listener {

        override fun onPlaybackStateChanged(state: Int) {

            when (state) {
                ExoPlayer.STATE_BUFFERING -> {
                    binding.progressBar.show()
                }
                ExoPlayer.STATE_READY -> {
                    binding.progressBar.hide()
                }
                ExoPlayer.STATE_ENDED -> {
                    playWhenReady = false
                }
                ExoPlayer.STATE_IDLE -> {

                }
            }
        }
    }
}