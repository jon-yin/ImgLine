package com.imgline.data.models

import com.imgline.ui.SpecificSourceType

data class Source(val name: String, val origin: SpecificSourceType, val args: Map<String, String>)