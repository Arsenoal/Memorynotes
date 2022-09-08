package com.bumian.memorynotes.common

import android.content.res.Resources
import android.util.TypedValue

fun Float.toDp(res: Resources): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        res.displayMetrics
    )
}