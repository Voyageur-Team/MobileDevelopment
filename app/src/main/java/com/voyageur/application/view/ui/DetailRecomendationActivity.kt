package com.voyageur.application.view.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.voyageur.application.R
import com.voyageur.application.databinding.ActivityDetailRecomendationBinding

class DetailRecomendationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailRecomendationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_recomendation)

    }
}