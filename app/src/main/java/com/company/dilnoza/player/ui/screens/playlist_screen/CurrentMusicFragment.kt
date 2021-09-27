package com.company.dilnoza.player.ui.screens.playlist_screen

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.company.dilnoza.player.R
import com.company.dilnoza.player.data.local.LocalStorage
import com.company.dilnoza.player.databinding.PageMusicLayoutBinding
import com.company.dilnoza.player.playback.MusicService
import com.company.dilnoza.player.util.extensions.loadImage
import com.company.dilnoza.player.data.models.Music
import com.sablab.android_simple_music_player.data.models.enums.ServiceCommand
import com.company.dilnoza.player.util.Constants
import com.sablab.android_simple_music_player.util.extensions.getAudioInfo
import com.sablab.android_simple_music_player.util.timberErrorLog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


/**
 * Created by Mahmudova Dilnoza on 27-Sep-21.
 * icebear03051999@gmail.com
 */
@AndroidEntryPoint
class CurrentMusicFragment : Fragment(R.layout.page_music_layout) {

    private val binding: PageMusicLayoutBinding by viewBinding(PageMusicLayoutBinding::bind)
    private val args: CurrentMusicFragmentArgs by navArgs()
    private lateinit var playingData:Music

    @Inject
    lateinit var storage: LocalStorage


    private val clickReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val command = intent.extras?.getSerializable(Constants.COMMAND_DATA) as? ServiceCommand
            timberErrorLog(command?.name.toString())
            binding.apply {
                when (command) {
                    ServiceCommand.PLAY -> {
                        btnPlayPause.setImageResource(R.drawable.ic_pause)
                    }
                    ServiceCommand.PAUSE -> {
                        btnPlayPause.setImageResource(R.drawable.ic_play)
                    }
                    else -> {
                    }
                }
            }
        }
    }
    private val notificationClickReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val command = intent.extras?.getSerializable(Constants.COMMAND_DATA) as? ServiceCommand
            timberErrorLog(command?.name.toString())
            binding.apply {
                when (command) {
                    ServiceCommand.PREV -> {
                        prevClicked()
                    }
                    ServiceCommand.NEXT -> {
                        nextClicked()
                    }
                    else -> {
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadViews()
        playingData=args.music
        startMusicService(playingData, ServiceCommand.PLAY_NEW)
        binding.btnPlayPause.setImageResource(R.drawable.ic_pause)

        storage.lastPlayedData = playingData.data ?: ""
        loadPlayingData(playingData)

    }

    private fun loadViews() {
        requireContext().registerReceiver(clickReceiver, IntentFilter(Constants.ACTION_PLAYER))
        requireContext().registerReceiver(
            notificationClickReceiver,
            IntentFilter(Constants.NOTIFICATION_ACTION_PLAYER)
        )

        binding.apply {


            btnNext.setOnClickListener {
                val intent = Intent(Constants.NOTIFICATION_ACTION_PLAYER)
                intent.putExtra(Constants.COMMAND_DATA, ServiceCommand.NEXT)
                requireContext().sendBroadcast(intent)
            }

            btnPrev.setOnClickListener {
                val intent = Intent(Constants.NOTIFICATION_ACTION_PLAYER)
                intent.putExtra(Constants.COMMAND_DATA, ServiceCommand.PREV)
                requireContext().sendBroadcast(intent)
            }

            btnPlayPause.setOnClickListener {
                if (storage.isPlaying) {
                    startMusicService(playingData, serviceCommand = ServiceCommand.PAUSE)
                    btnPlayPause.setImageResource(R.drawable.ic_play)
                } else {
                    startMusicService(playingData, serviceCommand = ServiceCommand.PLAY)
                    btnPlayPause.setImageResource(R.drawable.ic_pause)
                }
            }

            /**
             * Its used to make author name and title textview s scrollable(horizontally)
             */
            textName.isSelected = true
            textAuthorName.isSelected = true

            if (storage.isPlaying) {
                btnPlayPause.setImageResource(R.drawable.ic_pause)
            } else {
                btnPlayPause.setImageResource(R.drawable.ic_play)
            }

            if (storage.lastPlayedData.isNotEmpty()) {
            }
        }
    }

    private fun prevClicked() {
        binding.apply {
            try {
                val pos = Constants.allMusics.indexOf(playingData?.data)

                var t = pos - 1
                if (t <= 0)
                    t = 0

                if (t < Constants.allMusics.size) {
                    Constants.allMusics.toList()[t].let { it1 ->
                        playingData = requireContext().getAudioInfo(it1)!!
                    }
                }
//                startMusicService(playingData, serviceCommand = ServiceCommand.PLAY)
                btnPlayPause.setImageResource(R.drawable.ic_pause)
                playingData.let {
                    storage.lastPlayedData = it.data ?: ""
                    loadPlayingData(it)
                }
            } catch (e: Exception) {
                timberErrorLog(e.message.toString())
            }
        }
    }

    private fun nextClicked() {
        binding.apply {
            try {
                val pos = Constants.allMusics.indexOf(playingData.data)

                val t = pos + 1
                if (t < Constants.allMusics.size) {
                    Constants.allMusics.toList()[t].let { it1 ->
                        playingData = requireContext().getAudioInfo(it1)!!
                    }
                }
//                startMusicService(playingData, serviceCommand = ServiceCommand.PLAY)
                btnPlayPause.setImageResource(R.drawable.ic_pause)
                playingData.let {
                    storage.lastPlayedData = it.data ?: ""
                    loadPlayingData(it)
                }
            } catch (e: Exception) {
                timberErrorLog(e.message.toString())
            }
        }
    }

    private fun loadPlayingData(it: Music) {
        binding.apply {
            textName.text = it.title
            textAuthorName.text = it.artist
            it.imageUri.let { it1 -> image.loadImage(it1) }
            seekbar.setOnSeekBarChangeListener( object :SeekBar.OnSeekBarChangeListener{


                override fun onProgressChanged(seekBar:SeekBar, progress:Int, fromUser:Boolean) {

                }

              override fun onStartTrackingTouch(seekBar: SeekBar) {

                }

             override fun onStopTrackingTouch(seekBar: SeekBar) {
//                    current_pos = seekBar.progress;
//                    mediaPlayer.seekTo((int) current_pos);
                }
            });
        }
    }

    private fun startMusicService(data: Music? = null, serviceCommand: ServiceCommand) {
        val intent = Intent(context, MusicService::class.java)
        intent.putExtra(Constants.COMMAND_DATA, serviceCommand)
        intent.putExtra(Constants.MUSIC_DATA, data)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            requireContext().startForegroundService(intent)
        } else {
            requireContext().startService(intent)
        }
    }

    override fun onDestroyView() {

        requireContext().unregisterReceiver(clickReceiver)
        requireContext().unregisterReceiver(notificationClickReceiver)
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!storage.isPlaying) {
            requireContext().stopService(Intent(context, MusicService::class.java))
        }
    }
}