package com.blahblah.livedataplayground

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blahblah.livedataplayground.fragments.GalleryFragment
import com.blahblah.livedataplayground.viewmodel.MoviesViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var model: MoviesViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = MoviesViewModel.init(application)
        setContentView(R.layout.activity_main)
        val galleryFragment = GalleryFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, galleryFragment, "Gallery")
            .addToBackStack("Gallery")
            .commit()
    }
}
