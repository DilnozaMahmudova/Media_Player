//package com.company.dilnoza.player
//
//import android.annotation.SuppressLint
//import android.app.Service
//import android.content.Intent
//import android.database.Cursor
//import android.media.MediaPlayer
//import android.net.Uri
//import android.os.Handler
//import android.os.IBinder
//import android.os.Looper
//import android.provider.MediaStore
//import android.view.View
//import android.widget.SeekBar
//import android.widget.Toast
//import androidx.recyclerview.widget.DefaultItemAnimator
//import com.company.dilnoza.player.model.ModelAudio
//import kotlinx.android.synthetic.main.activity_main.*
//
//
///**
// * Created by Mahmudova Dilnoza on 9/17/2021.
// * QQB
// * icebear03051999@gmail.com
// */
//class PlayerService:Service() {
//    val mediaPlayer = MediaPlayer()
//    private lateinit var listMedia: ArrayList<ModelAudio>
//    private var audioIndex=0
//    private var currentPos=-1
//    private var totalDuration=-1
//    override fun onCreate() {
//        super.onCreate()
//        setAudio()
//    }
//    override fun onBind(p0: Intent?): IBinder? =null
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        return START_STICKY
//    }
//    @SuppressLint("Range")
//    private fun getAudioFiles() {
//        listMedia = ArrayList()
//        val contentResolver = contentResolver
//        val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
//        val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)
//
//        if (cursor != null && cursor.moveToFirst()) {
//            do {
//                val title: String =
//                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
//                val artist: String =
//                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
//                val duration: String =
//                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
//                val url: String =
//                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
//                val modelAudio = ModelAudio(title, duration, artist, Uri.parse(url))
//                listMedia.add(modelAudio)
//            } while (cursor.moveToNext())
//        }
//        val adapter = AudioAdapter(this, listMedia)
//        binding.recyclerView.adapter = adapter
//
//        adapter.setOnItemClickListener(object : AudioAdapter.OnItemClickListener {
//            override fun onItemClick(pos: Int, v: View?) {
//                Toast.makeText(this, "music>>>>>$pos", Toast.LENGTH_SHORT).show()
//                playAudio(pos)
//            }
//        })
//    }
//    fun setAudio() {
//
//        binding.recyclerView.itemAnimator = DefaultItemAnimator()
//        val mediaPlayer = MediaPlayer()
//        getAudioFiles()
//
//        binding.seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
//            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {}
//            override fun onStartTrackingTouch(seekBar: SeekBar) {}
//            override fun onStopTrackingTouch(seekBar: SeekBar) {
//                currentPos = seekBar.progress
//                mediaPlayer.seekTo(currentPos as Int)
//            }
//        })
//        mediaPlayer.setOnCompletionListener {
//            audioIndex++
//            if (audioIndex < listMedia.size) {
//                playAudio(audioIndex)
//            } else {
//                audioIndex = 0
//                playAudio(audioIndex)
//            }
//        }
//        if (listMedia.isNotEmpty()) {
//            playAudio(audioIndex)
//            prevAudio()
//            nextAudio()
//            setPause()
//        }
//    }
//    fun playAudio(pos: Int) {
//        try {
//            mediaPlayer.reset()
//            //set file path
//            mediaPlayer.setDataSource(this, listMedia[pos].uri)
//            mediaPlayer.prepare()
//            mediaPlayer.start()
//            pause.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24)
//            audio_name.text = listMedia[pos].audioTitle
//            audioIndex = pos
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        setAudioProgress()
//    }
//
//    //set audio progress
//    fun setAudioProgress() {
//        //get the audio duration
//        currentPos = mediaPlayer.currentPosition
//        totalDuration = mediaPlayer.duration
//
////        //display the audio duration
////        total.setText(timerConversion(totalDuration as Long))
////        current.setText(timerConversion(currentPos as Long))
//        binding.seekbar.max = totalDuration
//        val handler = Handler(Looper.myLooper()!!)
//        val runnable: Runnable = object : Runnable {
//            override fun run() {
//                try {
//                    currentPos = mediaPlayer.getCurrentPosition()
//                    //current.setText(timerConversion(currentPos as Long))
//                    binding.seekbar.progress = currentPos as Int
//                    handler.postDelayed(this, 1000)
//                } catch (ed: IllegalStateException) {
//                    ed.printStackTrace()
//                }
//            }
//        }
//        handler.postDelayed(runnable, 1000)
//    }
//
//    //play previous audio
//    fun prevAudio() {
//        prev.setOnClickListener(View.OnClickListener {
//            if (audioIndex > 0) {
//                audioIndex--
//                playAudio(audioIndex)
//            } else {
//                audioIndex = listMedia.size - 1
//                playAudio(audioIndex)
//            }
//        })
//    }
//
//    //play next audio
//    fun nextAudio() {
//        next.setOnClickListener(View.OnClickListener {
//            if (audioIndex < listMedia.size - 1) {
//                audioIndex++
//                playAudio(audioIndex)
//            } else {
//                audioIndex = 0
//                playAudio(audioIndex)
//            }
//        })
//    }
//
//    //pause audio
//    fun setPause() {
//        pause.setOnClickListener(View.OnClickListener {
//            if (mediaPlayer.isPlaying()) {
//                mediaPlayer.pause()
//                pause.setImageResource(R.drawable.ic_baseline_play_circle_outline_24)
//            } else {
//                mediaPlayer.start()
//                pause.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24)
//            }
//        })
//    }
//
//    //time conversion
//    fun timerConversion(value: Long): String? {
//        val audioTime: String
//        val dur = value.toInt()
//        val hrs = dur / 3600000
//        val mns = dur / 60000 % 60000
//        val scs = dur % 60000 / 1000
//        audioTime = if (hrs > 0) {
//            String.format("%02d:%02d:%02d", hrs, mns, scs)
//        } else {
//            String.format("%02d:%02d", mns, scs)
//        }
//        return audioTime
//    }
//
//}