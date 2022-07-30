package com.example.calculadora

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class ButtonItem(
    val itemID:Int,
    @StringRes val symbolText: Int?,
    @DrawableRes val iconDrawable: Int?,
    val colorId:Int,
    val onClick: (() -> Unit)?
    )
