package com.company.dilnoza.player.model

import android.net.Uri


/**
 * Created by Mahmudova Dilnoza on 9/17/2021.
 * QQB
 * icebear03051999@gmail.com
 */
data class ModelAudio(
    val audioTitle: String,
    val audioDuration: String,
    val audioArtist: String,
    val uri: Uri
)