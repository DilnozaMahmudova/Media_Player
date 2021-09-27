package com.company.dilnoza.player.data.local

import android.content.Context

class LocalStorage private constructor(context: Context) {
    companion object {
        @Volatile
        lateinit var instance: LocalStorage
            private set

        fun init(context: Context) {
            instance =
                LocalStorage(
                    context
                )
        }
    }

    private val pref = context.getSharedPreferences("LocalStorage", Context.MODE_PRIVATE)

    var isPlaying: Boolean by BooleanPreference(pref, false)
    var lastPlayedData: String by StringPreference(pref, "")

    fun clear() {
        pref.edit().clear().apply()
    }
}