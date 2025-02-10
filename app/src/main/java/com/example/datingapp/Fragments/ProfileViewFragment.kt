package com.example.datingapp.Fragments

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.datingapp.R

class ProfileViewFragment @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val profileImage: ImageView
    private val profileName: TextView
    private val profileAge: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.profile_block, this, true)
        profileImage = findViewById(R.id.profileImage)
        profileName = findViewById(R.id.profileName)
        profileAge = findViewById(R.id.profileAge)
    }

    fun setProfileData(name: String, age: Int, imageRes: Bitmap) {
        profileName.text = name
        profileAge.text = "$age anos"
        profileImage.setImageBitmap(imageRes)
    }
}
