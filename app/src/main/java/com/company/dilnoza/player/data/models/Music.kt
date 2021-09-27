package com.company.dilnoza.player.data.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Music(
    var id: Long? = null,
    var artist: String? = null,
    var title: String? = null,
    var data: String? = null,
    var displayName: String? = null,
    var duration: Long? = null,
    var imageUri: Uri? = null,
) : Parcelable
