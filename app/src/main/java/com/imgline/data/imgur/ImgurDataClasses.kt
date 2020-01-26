package com.imgline.data.imgur

import com.google.gson.annotations.SerializedName
import java.util.*

data class GalleryItem(
    val id: String,
    val title: String,
    val description: String?,
    @SerializedName("datetime")
    val date: Date,
    @SerializedName("points")
    val rating: Int,
    @SerializedName("is_album")
    val isAlbum: Boolean,
    val type: String,
    val images: List<ImgurImage>,
    val link: String
)

data class ImgurImage(
    val id: String,
    val description: String?,
    val title: String,
    @SerializedName("datetime")
    val date: Date,
    val type: String,
    val link: String
)
