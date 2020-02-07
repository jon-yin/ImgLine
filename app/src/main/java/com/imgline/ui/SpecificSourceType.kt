package com.imgline.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.imgline.R
import com.imgline.data.network.imgur.AbstractSource
import com.imgline.data.network.imgur.ImgurDefaultSource
import com.imgline.data.network.imgur.ImgurSearchSource


enum class SpecificSourceType(
    val concreteSource : (Map<String, String>) -> AbstractSource,
    val generalSourceType: SourceType,
    @StringRes val stringResource: Int = generalSourceType.stringResource,
    @DrawableRes val iconResource: Int = generalSourceType.iconResource
) {
    IMGUR_FRONT_PAGE(::ImgurDefaultSource,
        SourceType.IMGUR,
        R.string.imgur_default),
    IMGUR_SEARCH(::ImgurSearchSource,
        SourceType.IMGUR,
        R.string.imgur_search)
}

enum class SourceType(
    @DrawableRes val iconResource: Int,
    @StringRes val stringResource: Int
) {
    IMGUR(
        R.drawable.imgur_logo,
        R.string.imgur
    )
}

val SOURCE_TO_SPECIFIC = SpecificSourceType.values().groupBy { it.generalSourceType }


interface HasSourceOrigin {
    val origin : SpecificSourceType
}
