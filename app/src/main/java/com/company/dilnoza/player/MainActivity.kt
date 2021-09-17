@file:Suppress("DEPRECATION")

package com.company.dilnoza.player

import android.Manifest
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.company.dilnoza.player.databinding.ActivityMainBinding
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.io.File


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: SongsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
       // binding.listEdit.layoutManager=LinearLayoutManager(this)
        permission()
    }

    private fun permission() {
        Dexter.withContext(this)
            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
            displaySongs()
            Toast.makeText(this@MainActivity, "permission", Toast.LENGTH_SHORT).show()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) { /* ... */
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) { /* ... */
                }
            }).check()
    }

    private fun findSong(file: File): ArrayList<File> {
        val arrayList = ArrayList<File>()

        val files = file.listFiles()
        if(files!=null){
            for (singleFile in files) {
                if (singleFile.isDirectory && !singleFile.isHidden) {
                    arrayList.addAll(findSong(singleFile))
                } else {
                    if (singleFile.name.endsWith(".mp3") || singleFile.name.endsWith(".wav")) {
                        arrayList.add(singleFile)
                    }
                }
            }
        }

        return arrayList
    }

    private fun displaySongs() {
        val songs = findSong(Environment.getExternalStorageDirectory())
        val item = arrayOfNulls<String>(songs.size)
        for (i in 0 until songs.size) {
            item[i] = songs[i].name.toString().replace(".mp3", "").replace(".wav", "")

        }
        adapter = SongsAdapter(item)
      //  binding.listEdit.adapter = adapter
    }
}