package com.bumian.memorynotes.presentation.common

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import com.bumian.memorynotes.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class MapIconView(
    private val mContext: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0): FrameLayout(mContext, attrs, defStyle) {

    private lateinit var mapIcon: AppCompatImageView

    init { initView() }

    private fun initView() {
        mapIcon = inflate(mContext, R.layout.layout_map_icon, this).findViewById(R.id.noteImage)
    }

    fun loadIcon(iconUri: Uri) {
        Glide.with(mContext)
            .load(iconUri)
            .placeholder(R.drawable.test)
            .into(mapIcon)
    }
}