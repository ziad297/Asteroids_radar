package com.ziad.asteroidradar

import com.squareup.moshi.Json

data class POD(@Json(name = "media_type") val mediaType: String, val title: String,
               val url: String)