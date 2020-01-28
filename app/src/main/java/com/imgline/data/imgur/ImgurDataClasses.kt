package com.imgline.data.imgur

import com.google.gson.annotations.SerializedName
import com.imgline.data.Post
import com.imgline.data.mimeTypeToMediaType
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

fun mapGalleryItemToPost(item: GalleryItem, origin: Class<out Any>): Post =
    if (item.isAlbum) {
         Post(item.id, item.images[0].link, item.rating, item.isAlbum, mimeTypeToMediaType(item.images[0].type), origin)
     } else {
        Post(item.id, item.link, item.rating, item.isAlbum, mimeTypeToMediaType(item.type), origin)
    }