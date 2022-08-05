package com.example.mobile_postagram

import android.util.Log


class DicebearService {
    companion object {
        private val SPRITE_TYPE = listOf(
            "male",
            "female"
        )
        fun getRandomAvatarURL(): String {
            val url = "https://avatars.dicebear.com/api/adventurer-neutral/${System.currentTimeMillis().toInt()}.jpg"
            Log.d("Dicebear",url)
            return url
        }
    }
}

